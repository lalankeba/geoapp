package com.laan.geoapp.mapper;

import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.request.SectionUpdateRequest;
import com.laan.geoapp.dto.response.SectionResponse;
import com.laan.geoapp.entity.SectionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectionMapper {

    SectionMapper INSTANCE = Mappers.getMapper( SectionMapper.class );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "geologicalClassEntities", ignore = true)
    SectionEntity mapAddRequestToEntity(SectionAddRequest addRequest);

    @Mapping(target = "geologicalClasses", source = "geologicalClassEntities")
    SectionResponse mapEntityToResponse(SectionEntity sectionEntity);

    List<SectionResponse> mapEntitiesToResponses(List<SectionEntity> sectionEntities);

    @Mapping(target = "id", source = "sectionId")
    @Mapping(target = "geologicalClassEntities", ignore = true)
    SectionEntity mapUpdateRequestToEntity(SectionUpdateRequest updateRequest, Long sectionId);

}
