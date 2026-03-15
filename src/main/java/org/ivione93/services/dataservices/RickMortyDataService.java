package org.ivione93.services.dataservices;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.ivione93.dto.rickmortyapi.CharacterResponse;
import org.ivione93.dto.rickmortyapi.CharactersResponse;

@Path("/api")
@RegisterRestClient(configKey = "rickmorty")
@RegisterForReflection
public interface RickMortyDataService {

  @GET
  @Path("/character/{characterId}")
  CharacterResponse getCharacter(@PathParam("characterId") int characterId);

  @GET
  @Path("/character")
  CharactersResponse getCharacters();

}
