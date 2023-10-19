package com.laan.geoapp.controller;

import com.laan.geoapp.dto.response.JobResponse;
import com.laan.geoapp.util.PathUtil;
import com.laan.geoapp.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Slf4j
public class ImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @AfterEach
    void initAfter() {
        testUtils.deleteAllSectionsAfterRunningJobs();
        testUtils.deleteAllJobs();
    }

    @Test
    public void importFile() throws Exception {
        MockMultipartFile multipartFile = testUtils.getMultipartFile("geo-data.xls");

        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .multipart(PathUtil.IMPORT).file(multipartFile)
                                .with(httpBasic(testUtils.getBasicUsername(), testUtils.getBasicPassword()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.result").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("id").description("Id of the import job"))
                                        .and(fieldWithPath("result").description("Result of the import job"))
                                        .and(fieldWithPath("detail").description("More information of the import job"))
                        ));
    }

    @Test
    public void getImportJobStatus() throws Exception {
        MockMultipartFile multipartFile = testUtils.getMultipartFile("geo-data-2.xls");
        JobResponse jobResponse = testUtils.importFile(multipartFile);

        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get(PathUtil.IMPORT + PathUtil.ID_PLACEHOLDER, jobResponse.getId())
                                .with(httpBasic(testUtils.getBasicUsername(), testUtils.getBasicPassword()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(containsString(jobResponse.getId())))
                .andExpect(jsonPath("$.result").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the import job")),
                                responseFields(
                                        fieldWithPath("id").description("Id of the import job"))
                                        .and(fieldWithPath("result").description("Result of the import job"))
                                        .and(fieldWithPath("detail").description("More information of the import job"))
                        )
                );
    }

}
