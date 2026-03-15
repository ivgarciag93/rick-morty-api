package org.ivione93.services.providers;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.ivione93.dto.rickmortyapi.CharacterResponse;
import org.ivione93.dto.rickmortyapi.CharactersResponse;
import org.ivione93.services.dataservices.RickMortyDataService;

@ApplicationScoped
public class RickMortyProvider {

  @RestClient RickMortyDataService rickMortyDataService;

  public CharacterResponse getCharacter(final int characterId) {
    return rickMortyDataService.getCharacter(characterId);
  }

  public CharactersResponse getCharacters() {
    return rickMortyDataService.getCharacters();
  }

}
