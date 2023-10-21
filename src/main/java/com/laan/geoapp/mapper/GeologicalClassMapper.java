package com.laan.geoapp.mapper;

import com.laan.geoapp.dto.request.GeologicalClassAddRequest;
import com.laan.geoapp.entity.GeologicalClassEntity;
import com.laan.geoapp.entity.SectionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GeologicalClassMapper {

    GeologicalClassMapper INSTANCE = Mappers.getMapper( GeologicalClassMapper.class );

    @Mapping(target = "name", source = "addRequest.name")
    GeologicalClassEntity mapAddRequestToEntity(GeologicalClassAddRequest addRequest, SectionEntity sectionEntity);

    default List<GeologicalClassEntity> mapAddRequestsToEntities(List<GeologicalClassAddRequest> addRequests, SectionEntity sectionEntity) {
        return addRequests.stream()
                .map(addRequest -> mapAddRequestToEntity(addRequest, sectionEntity))
                .toList();
    }

    @Mapping(target = "sectionEntity", ignore = true)
    GeologicalClassEntity mapAddRequestToEntity(GeologicalClassAddRequest addRequest);

}
