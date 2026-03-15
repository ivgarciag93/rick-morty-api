package org.ivione93.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CharactersResponse (
    Info info,
    List<CharacterResponse> results
){
  public record Info(
      int count,
      int pages,
      String next,
      String prev) {}
}
