package org.ivione93.services;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ivione93.dto.CharacterResponse;
import org.ivione93.dto.CharactersResponse;
import org.ivione93.dto.rickmortyapi.ApiCharacterResponse;
import org.ivione93.dto.rickmortyapi.ApiCharactersResponse;
import org.ivione93.services.async.RickMortyAsyncCallService;
import org.ivione93.services.converters.RickMortyConverter;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class RickMortyService {

  @Inject
  RickMortyAsyncCallService rickMortyAsynCallService;

  @Inject
  RickMortyConverter rickMortyConverter;

  public CharactersResponse getCharacters() {
    CompletableFuture<ApiCharactersResponse> charactersResponse = rickMortyAsynCallService.getCharacters();

    try {
      return rickMortyConverter.mapToCharacters(charactersResponse.join());
    } catch (final Exception ex) {
      Log.errorf(ex, "Error while obtaining characters info");
      throw new RuntimeException(ex);
    }
  }

  public CharacterResponse getCharacter(final int characterId) {
    CompletableFuture<ApiCharacterResponse> characterResponse = rickMortyAsynCallService.getCharacter(characterId);

    try {
      return rickMortyConverter.mapToCharacter(characterResponse.join());
    } catch (final Exception ex) {
      Log.errorf(ex, "Error while obtaining character info");
      throw new RuntimeException(ex);
    }
  }
}
