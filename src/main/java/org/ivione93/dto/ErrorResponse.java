package org.ivione93.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  public String error;
  public int code;

  public ErrorResponse() {}

  public ErrorResponse(String error, int code) {
    this.error = error;
    this.code = code;
  }
}
