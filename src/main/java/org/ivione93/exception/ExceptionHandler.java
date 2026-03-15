package org.ivione93.exception;

import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.ivione93.dto.ErrorResponse;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(final Exception ex) {
    Log.error("Managing generic exception...", ex);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
      .entity(new ErrorResponse(ex.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()))
      .build();
  }

}
