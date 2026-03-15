package org.ivione93.boundary;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.ivione93.dto.rickmortyapi.CharacterResponse;
import org.ivione93.dto.rickmortyapi.CharactersResponse;
import org.ivione93.services.RickMortyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class RickMortyApiTest {

    @InjectMock
    RickMortyService rickMortyService;

    @Test
    public void testGetCharacters() {
        CharactersResponse mockResponse = new CharactersResponse();
        mockResponse.results = Collections.emptyList();
        
        Mockito.when(rickMortyService.getCharacters()).thenReturn(mockResponse);

        given()
          .when().get("/v1/rickmorty/character")
          .then()
             .statusCode(200);
    }

    @Test
    public void testGetCharacter() {
        CharacterResponse mockResponse = new CharacterResponse();
        mockResponse.id = "1";
        mockResponse.name = "Rick Sanchez";
        
        Mockito.when(rickMortyService.getCharacter(1)).thenReturn(mockResponse);

        given()
          .pathParam("characterId", 1)
          .when().get("/v1/rickmorty/character/{characterId}")
          .then()
             .statusCode(200)
             .body("id", is("1"))
             .body("name", is("Rick Sanchez"));
    }
}
