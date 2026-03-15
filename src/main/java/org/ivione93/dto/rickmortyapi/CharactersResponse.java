package org.ivione93.dto.rickmortyapi;

import java.util.List;

public class CharactersResponse {

  public Info info;
  public List<CharacterResponse> results;

  public static class Info {
    public int count;
    public int pages;
    public String next;
    public String prev;
  }
}
