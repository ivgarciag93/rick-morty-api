package org.ivione93.boundary;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.ivione93.dto.CharacterResponse;
import org.ivione93.dto.CharactersResponse;
import org.ivione93.dto.ErrorResponse;
import org.ivione93.services.RickMortyService;

@ApplicationScoped
@Path("/v1/rickmorty")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(
    name = "Rick & Morty",
    description = "Operaciones de consulta para personajes del universo de Rick and Morty"
)
public class RickMortyApi {

  @Inject RickMortyService rickMortyService;

  @GET
  @Path("/characters")
  @RunOnVirtualThread
  @Operation(
      summary = "Obtener listado de personajes",
      description = "Recupera la lista paginada de personajes de Rick and Morty obtenidos desde la API remota."
  )
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Listado de personajes obtenido exitosamente",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = CharactersResponse.class)
          )
      ),
      @APIResponse(
          responseCode = "500",
          description = "Error interno del servidor al consultar la API remota",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  public CharactersResponse getCharacters() {
    Log.info("Call to getCharacters");
    return rickMortyService.getCharacters();
  }

  @GET
  @Path("/characters/{characterId}")
  @RunOnVirtualThread
  @Operation(
      summary = "Obtener personaje por ID",
      description = "Busca un personaje por su identificador numérico en la base de datos local; si no existe, lo consulta en la API externa y lo persiste."
  )
  @APIResponses(value = {
      @APIResponse(
          responseCode = "200",
          description = "Personaje encontrado exitosamente",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = CharacterResponse.class)
          )
      ),
      @APIResponse(
          responseCode = "500",
          description = "Error interno del servidor al recuperar o sincronizar el personaje",
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  public CharacterResponse getCharacter(
      @Parameter(
          description = "Identificador único del personaje",
          required = true,
          example = "1"
      )
      @PathParam("characterId") int characterId) {
    Log.infof("Call to getCharacter by id %d", characterId);
    return rickMortyService.getCharacter(characterId);
  }

}
