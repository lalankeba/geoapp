package com.laan.geoapp.mapper;

import com.laan.geoapp.dto.request.GeologicalClassAddRequest;
import com.laan.geoapp.entity.GeologicalClassEntity;
import com.laan.geoapp.entity.SectionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GeologicalClassMapper {

    GeologicalClassMapper INSTANCE = Mappers.getMapper( GeologicalClassMapper.class );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "addRequest.name")
    @Mapping(target = "sectionEntity", source = "existingSectionEntity")
    GeologicalClassEntity mapAddRequestToEntity(GeologicalClassAddRequest addRequest, SectionEntity existingSectionEntity);

    default List<GeologicalClassEntity> mapAddRequestsToEntities(List<GeologicalClassAddRequest> addRequests, SectionEntity existingSectionEntity) {
        return addRequests.stream()
                .map(addRequest -> mapAddRequestToEntity(addRequest, existingSectionEntity))
                .collect(Collectors.toList());
    }
}