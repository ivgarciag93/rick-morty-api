package org.ivione93.services.converters;

import jakarta.enterprise.context.ApplicationScoped;
import org.ivione93.dto.CharacterResponse;
import org.ivione93.dto.CharactersResponse;
import org.ivione93.dto.rickmortyapi.ApiCharacterResponse;
import org.ivione93.dto.rickmortyapi.ApiCharactersResponse;

@ApplicationScoped
public class RickMortyConverter {

  public CharactersResponse mapToCharacters(final ApiCharactersResponse apiCharactersResponse) {
    return new CharactersResponse(
        new CharactersResponse.Info(
            apiCharactersResponse.info.count,
            apiCharactersResponse.info.pages,
            apiCharactersResponse.info.next,
            apiCharactersResponse.info.prev
        ),
        apiCharactersResponse.results.stream().map(this::mapToCharacter).toList());
  }

  public CharacterResponse mapToCharacter(final ApiCharacterResponse apiCharacterResponse) {
    return new CharacterResponse(
        apiCharacterResponse.id,
        apiCharacterResponse.name,
        apiCharacterResponse.status,
        apiCharacterResponse.species,
        apiCharacterResponse.gender,
        new CharacterResponse.Origin(
          apiCharacterResponse.origin != null && apiCharacterResponse.origin.name != null
              ? apiCharacterResponse.origin.name
              : "unknown",
          apiCharacterResponse.origin != null && apiCharacterResponse.origin.url != null
              ? apiCharacterResponse.origin.url
              : "unknown"
        ),
        new CharacterResponse.Location(
          apiCharacterResponse.location != null && apiCharacterResponse.location.name != null
              ? apiCharacterResponse.location.name
              : "unknown",
          apiCharacterResponse.location != null && apiCharacterResponse.location.url != null
              ? apiCharacterResponse.location.url
              : "unknown"
        ),
        apiCharacterResponse.image
    );
  }

}
