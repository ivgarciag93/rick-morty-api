package org.ivione93.boundary;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.ivione93.dto.CharacterResponse;
import org.ivione93.dto.CharactersResponse;
import org.ivione93.services.RickMortyService;

@ApplicationScoped
@Path("/v1/rickmorty")
public class RickMortyApi {

  @Inject RickMortyService rickMortyService;

  @GET
  @Path("/characters")
  public CharactersResponse getCharacters() {
    Log.info("Call to getCharacters");
    return rickMortyService.getCharacters();
  }

  @GET
  @Path("/characters/{characterId}")
  public CharacterResponse getCharacter(@PathParam("characterId") int characterId) {
    Log.infof("Call to getCharacter by id %d", characterId);
    return rickMortyService.getCharacter(characterId);
  }

}
