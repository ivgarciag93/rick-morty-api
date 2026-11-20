package org.ivione93.dto.rickmortyapi;

import java.util.List;

public record ApiLocationResponse(
    Integer id,
    String name,
    String type,
    String dimension,
    List<String> residents,
    String url,
    String created) {}
