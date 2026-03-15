package org.ivione93.services;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ivione93.dto.rickmortyapi.CharacterResponse;
import org.ivione93.dto.rickmortyapi.CharactersResponse;
import org.ivione93.services.async.RickMortyAsynCallService;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class RickMortyService {

  @Inject
  RickMortyAsynCallService rickMortyAsynCallService;

  public CharacterResponse getCharacter(final int characterId) {
    CompletableFuture<CharacterResponse> characterResponse = rickMortyAsynCallService.getCharacter(characterId);

    try {
      return characterResponse.join();
    } catch (final Exception ex) {
      Log.errorf(ex, "Error while obtaining character info");
      throw new RuntimeException(ex);
    }
  }

  public CharactersResponse getCharacters() {
    CompletableFuture<CharactersResponse> charactersResponse = rickMortyAsynCallService.getCharacters();

    try {
      return charactersResponse.join();
    } catch (final Exception ex) {
      Log.errorf(ex, "Error while obtaining characters info");
      throw new RuntimeException(ex);
    }
  }
}
