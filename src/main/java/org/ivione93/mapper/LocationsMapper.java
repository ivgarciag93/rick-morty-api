package org.ivione93.mapper;

import org.ivione93.dto.LocationsResponse;
import org.ivione93.dto.rickmortyapi.ApiLocationsResponse;
import org.mapstruct.Mapper;

@Mapper
public interface LocationsMapper {

  LocationsResponse mapToResponse(ApiLocationsResponse apiResponse);
}
