package org.ivione93.dto.rickmortyapi;

import java.time.Instant;
import java.util.List;

public class ApiCharacterResponse {
  public String id;
  public String name;
  public String status;
  public String species;
  public String type;
  public String gender;
  public Origin origin;
  public Location location;
  public String image;
  public List<String> episode;
  public String url;
  public Instant created;

  public static class Origin {
    public String name;
    public String url;
  }

  public static class Location {
    public String name;
    public String url;
  }

}

