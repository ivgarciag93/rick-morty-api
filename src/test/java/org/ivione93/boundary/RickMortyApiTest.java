package org.ivione93.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.ivione93.dto.rickmortyapi.ApiCharacterResponse;
import org.ivione93.dto.rickmortyapi.ApiCharactersResponse;
import org.ivione93.services.RickMortyService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@QuarkusTest
class RickMortyApiTest {

  private static final String GET_CHARACTERS_URI = "/v1/rickmorty/character";
  private static final String GET_CHARACTER_URI = "/v1/rickmorty/character/{characterId}";

  @InjectSpy
  RickMortyService rickMortyService;

  @Test
  void testGetCharacters() {
    ApiCharactersResponse mockResponse = createCharactersResponse();

    when(rickMortyService.getCharacters()).thenReturn(mockResponse);

    given()
        .when().get(GET_CHARACTERS_URI)
        .then()
        .statusCode(200)
        .body("results.size()", is(2));
  }

  @Test
  void testGetCharacters_Failure() {
    when(rickMortyService.getCharacters()).thenThrow(new RuntimeException("Error"));

    given()
        .when().get(GET_CHARACTERS_URI)
        .then()
        .statusCode(500);
  }

  @Test
  void testGetCharacter() {
    ApiCharacterResponse mockRick = createCharacter("1", "Rick Sanchez");

    when(rickMortyService.getCharacter(1)).thenReturn(mockRick);

    given()
        .pathParam("characterId", 1)
        .when().get(GET_CHARACTER_URI)
        .then()
        .statusCode(200)
        .body("id", is("1"))
        .body("name", is("Rick Sanchez"));
  }

  @Test
  void testGetCharacter_Failure() {
    when(rickMortyService.getCharacter(1)).thenThrow(new RuntimeException("Error"));

    given()
        .pathParam("characterId", 1)
        .when().get(GET_CHARACTER_URI)
        .then()
        .statusCode(500);
  }

  private static ApiCharactersResponse createCharactersResponse() {
    ApiCharacterResponse mockRick = createCharacter("1", "Rick Sanchez");
    ApiCharacterResponse mockMorty = createCharacter("2", "Morty Smith");
    ApiCharactersResponse.Info mockInfo = createInfo();
    ApiCharactersResponse mockResponse = new ApiCharactersResponse();
    mockResponse.results = List.of(mockRick, mockMorty);
    mockResponse.info = mockInfo;
    return mockResponse;
  }

  private static ApiCharacterResponse createCharacter(final String id, final String name) {
    ApiCharacterResponse mockRick = new ApiCharacterResponse();
    mockRick.id = id;
    mockRick.name = name;
    return mockRick;
  }

  private static ApiCharactersResponse.Info createInfo() {
    ApiCharactersResponse.Info mockInfo = new ApiCharactersResponse.Info();
    mockInfo.count = 2;
    mockInfo.pages = 1;
    mockInfo.next = "https://rickandmortyapi.com/api/character/";
    mockInfo.prev = null;
    return mockInfo;
  }
}
