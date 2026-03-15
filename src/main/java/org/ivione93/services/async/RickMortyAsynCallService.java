package org.ivione93.services.async;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.ivione93.dto.rickmortyapi.CharacterResponse;
import org.ivione93.dto.rickmortyapi.CharactersResponse;
import org.ivione93.services.providers.RickMortyProvider;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class RickMortyAsynCallService extends AsyncCallService {

  @Inject
  RickMortyProvider rickMortyService;

  public CompletableFuture<CharacterResponse> getCharacter(final int characterId) {
    return managedExecutor
        .supplyAsync(() -> rickMortyService.getCharacter(characterId))
        .orTimeout(timeoutMilliseconds, TimeUnit.MILLISECONDS)
        .exceptionally(ex -> {
          Log.errorf(ex, "Error obtaining character info in Rick and Morty API");
          throw toCompletionException(ex);
        });
  }

  public CompletableFuture<CharactersResponse> getCharacters() {
    return managedExecutor
        .supplyAsync(() -> rickMortyService.getCharacters())
        .orTimeout(timeoutMilliseconds, TimeUnit.MILLISECONDS)
        .exceptionally(ex -> {
          Log.errorf(ex, "Error obtaining characters info in Rick and Morty API");
          throw toCompletionException(ex);
        });
  }

}
