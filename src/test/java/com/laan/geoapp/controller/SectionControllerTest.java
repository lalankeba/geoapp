package com.laan.geoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.request.SectionUpdateRequest;
import com.laan.geoapp.util.PathUtil;
import com.laan.geoapp.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class SectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void initAfter() {
        testUtils.deleteAllSectionsAfterRunningJobs();
    }

    @Test
    void createSection() throws Exception {
        SectionAddRequest sectionAddRequest = testUtils.makeSectionAddRequest(1, 2);

        this.mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post(PathUtil.SECTIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(sectionAddRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(containsString(sectionAddRequest.getName())))
                .andExpect(jsonPath("$.geologicalClasses").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(fieldWithPath("name").description("Name to be saved for the section. Should be unique throughout the system."))
                                        .and(subsectionWithPath("geologicalClasses").description("Geological class details"))
                                        .and(fieldWithPath("geologicalClasses[].name").description("Name of the geological class"))
                                        .and(fieldWithPath("geologicalClasses[].code").description("Code of the geological class")),
                                responseFields(
                                        fieldWithPath("name").description("Saved name of the section"))
                                        .and(subsectionWithPath("geologicalClasses").description("Saved geological class details"))
                                        .and(fieldWithPath("geologicalClasses[].name").description("Saved name of the geological class"))
                                        .and(fieldWithPath("geologicalClasses[].code").description("Saved code of the geological class"))
                        )
                );
    }

    @Test
    void getSection() throws Exception {
        SectionAddRequest sectionAddRequest = testUtils.makeSectionAddRequest(3, 1);
        testUtils.createSection(sectionAddRequest);

        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get(PathUtil.SECTIONS + PathUtil.NAME_PLACEHOLDER, sectionAddRequest.getName())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(containsString(sectionAddRequest.getName())))
                .andExpect(jsonPath("$.geologicalClasses").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("name").description("Name of the section"))
                        )
                );
    }

    @Test
    void getSections() throws Exception {
        SectionAddRequest sectionAddRequest1 = testUtils.makeSectionAddRequest(4, 2);
        SectionAddRequest sectionAddRequest2 = testUtils.makeSectionAddRequest(5, 3);
        testUtils.createSection(sectionAddRequest1);
        testUtils.createSection(sectionAddRequest2);

        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get(PathUtil.SECTIONS)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(1))))
                .andExpect(jsonPath("$.[*].name").exists())
                .andExpect(jsonPath("$.[*].geologicalClasses").exists())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    void updateSection() throws Exception {
        SectionAddRequest sectionAddRequest = testUtils.makeSectionAddRequest(6, 2);
        testUtils.createSection(sectionAddRequest);

        SectionUpdateRequest sectionUpdateRequest = testUtils.makeSectionUpdateRequest(4, 1);

        this.mockMvc.perform(
                    RestDocumentationRequestBuilders
                            .put(PathUtil.SECTIONS + PathUtil.NAME_PLACEHOLDER, sectionAddRequest.getName())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(sectionUpdateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(containsString(sectionUpdateRequest.getName())))
                .andExpect(jsonPath("$.geologicalClasses").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("name").description("Name of the existing section")),
                                requestFields(fieldWithPath("name").description("Name to be updated for the section. Should be unique throughout the system."))
                                        .and(subsectionWithPath("geologicalClasses").description("Geological class details"))
                                        .and(fieldWithPath("geologicalClasses[].name").description("Name to be updated of the geological class"))
                                        .and(fieldWithPath("geologicalClasses[].code").description("Code to be updated of the geological class")),
                                responseFields(
                                        fieldWithPath("name").description("Updated name of the section"))
                                        .and(subsectionWithPath("geologicalClasses").description("Updated geological class details"))
                                        .and(fieldWithPath("geologicalClasses[].name").description("Updated Name of the geological class"))
                                        .and(fieldWithPath("geologicalClasses[].code").description("Updated code of the geological class"))
                        )
                );
    }

    @Test
    void deleteSection() throws Exception {
        SectionAddRequest sectionAddRequest = testUtils.makeSectionAddRequest(7, 2);
        testUtils.createSection(sectionAddRequest);

        this.mockMvc.perform(
                RestDocumentationRequestBuilders
                        .delete(PathUtil.SECTIONS + PathUtil.NAME_PLACEHOLDER, sectionAddRequest.getName())
                )
                .andExpect(status().isNoContent())
                .andDo(
                        document("{method-name}",
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("name").description("Name of the section that needs to be deleted"))
                        )
                );
    }

    @Test
    void getSectionsByGeoCode() throws Exception {
        SectionAddRequest sectionAddRequest = testUtils.makeSectionAddRequest(8, 3);
        testUtils.createSection(sectionAddRequest);

        this.mockMvc.perform(
                        RestDocumentationRequestBuilders
                                .get(PathUtil.SECTIONS + PathUtil.BY_CODE + "?code=" +"GC83")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(equalTo(1))))
                .andExpect(jsonPath("$.[*].name").exists())
                .andExpect(jsonPath("$.[*].geologicalClasses").exists())
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(parameterWithName("code").description("Geological class code"))
                        )
                );
    }

}
