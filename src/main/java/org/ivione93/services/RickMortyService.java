package org.ivione93.services;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ivione93.dto.rickmortyapi.ApiCharacterResponse;
import org.ivione93.dto.rickmortyapi.ApiCharactersResponse;
import org.ivione93.services.async.RickMortyAsynCallService;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class RickMortyService {

  @Inject
  RickMortyAsynCallService rickMortyAsynCallService;

  public ApiCharacterResponse getCharacter(final int characterId) {
    CompletableFuture<ApiCharacterResponse> characterResponse = rickMortyAsynCallService.getCharacter(characterId);

    try {
      return characterResponse.join();
    } catch (final Exception ex) {
      Log.errorf(ex, "Error while obtaining character info");
      throw new RuntimeException(ex);
    }
  }

  public ApiCharactersResponse getCharacters() {
    CompletableFuture<ApiCharactersResponse> charactersResponse = rickMortyAsynCallService.getCharacters();

    try {
      return charactersResponse.join();
    } catch (final Exception ex) {
      Log.errorf(ex, "Error while obtaining characters info");
      throw new RuntimeException(ex);
    }
  }
}
