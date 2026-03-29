package org.ivione93.services;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.ivione93.dto.rickmortyapi.ApiCharacterResponse;
import org.ivione93.entity.Character;

import java.util.Optional;

@ApplicationScoped
public class CharactersService {

  @Inject Event<Character> characterEvent;

  public Optional<Character> findById(int characterId) {
    return Optional.ofNullable(Character.findById(characterId));
  }

  @Transactional
  public void saveCharacter(ApiCharacterResponse characterResponse) {
    Character stored = Character.findById(characterResponse.id);

    if (stored == null) {
      Character character = new Character();
      character.id = characterResponse.id;
      character.name = characterResponse.name;
      character.status = characterResponse.status;
      character.specie = characterResponse.species;
      character.gender = characterResponse.gender;
      character.image = characterResponse.image;
      character.persist();
      characterEvent.fire(character);
    }
  }

  public void notifyPersist(@Observes(during = TransactionPhase.AFTER_SUCCESS) Character character) {
    Log.debugf("Character %s saved successfully", character.id);
  }

}
