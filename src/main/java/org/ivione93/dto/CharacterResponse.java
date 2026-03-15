package org.ivione93.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CharacterResponse(
      String id,
      String name,
      String status,
      String species,
      String gender,
      Origin origin,
      Location location,
      String image
) {
  public record Origin(String name, String url) {}
  public record Location(String name, String url) {}
}
