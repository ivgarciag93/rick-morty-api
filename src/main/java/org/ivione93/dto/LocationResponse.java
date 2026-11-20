package org.ivione93.dto;

import java.util.List;

public record LocationResponse(
    Integer id,
    String name,
    String type,
    String dimension,
    List<String> residents,
    String url,
    String created) {}
