package com.laan.geoapp.controller;

import com.laan.geoapp.dto.request.SectionAddRequest;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
@Slf4j
public class ExportControllerTest {

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
    public void exportFile() throws Exception {
        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get(PathUtil.EXPORT)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("id").description("Id of the export job"))
                                        .and(fieldWithPath("result").description("Result of the export job"))
                                        .and(fieldWithPath("detail").description("More information of the export job"))
                        ));
    }

    @Test
    public void getExportJobStatus() throws Exception {
        JobResponse jobResponse = testUtils.exportFile();
        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get(PathUtil.EXPORT + PathUtil.ID_PLACEHOLDER, jobResponse.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("result").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the export job")),
                                responseFields(
                                        fieldWithPath("id").description("Id of the export job"))
                                        .and(fieldWithPath("result").description("Result of the export job"))
                                        .and(fieldWithPath("detail").description("More information of the export job"))
                        ));
    }

    @Test
    public void getExportFile() throws Exception {
        SectionAddRequest sectionAddRequest1 = testUtils.makeSectionAddRequest(31, 2);
        testUtils.createSection(sectionAddRequest1);
        SectionAddRequest sectionAddRequest2 = testUtils.makeSectionAddRequest(32, 3);
        testUtils.createSection(sectionAddRequest2);

        JobResponse jobResponse = testUtils.exportFileAndWait();
        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get(PathUtil.EXPORT + PathUtil.ID_PLACEHOLDER + PathUtil.FILE, jobResponse.getId())
                )
                .andExpect(status().isOk())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the export job"))
                        ));
    }

}
