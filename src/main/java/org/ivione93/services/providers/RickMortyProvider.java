package org.ivione93.services.providers;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.ivione93.dto.rickmortyapi.ApiCharacterResponse;
import org.ivione93.dto.rickmortyapi.ApiCharactersResponse;
import org.ivione93.services.dataservices.RickMortyDataService;

@ApplicationScoped
public class RickMortyProvider {

  @RestClient RickMortyDataService rickMortyDataService;

  public ApiCharacterResponse getCharacter(final int characterId) {
    return rickMortyDataService.getCharacter(characterId);
  }

  public ApiCharactersResponse getCharacters() {
    return rickMortyDataService.getCharacters();
  }

}
