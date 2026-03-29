package org.ivione93.services.providers;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.ivione93.dto.rickmortyapi.ApiCharacterResponse;
import org.ivione93.dto.rickmortyapi.ApiCharactersResponse;
import org.ivione93.services.dataservices.RickMortyDataService;

@ApplicationScoped
public class RickMortyProvider {

  @RestClient RickMortyDataService rickMortyDataService;

  @CacheResult(cacheName = "character-cache")
  public ApiCharacterResponse getCharacter(final int characterId) {
    return rickMortyDataService.getCharacter(characterId);
  }

  public ApiCharactersResponse getCharacters() {
    return rickMortyDataService.getCharacters();
  }

  @CacheInvalidateAll(cacheName = "character-cache")
  public static void invalidateCache() {}

}
