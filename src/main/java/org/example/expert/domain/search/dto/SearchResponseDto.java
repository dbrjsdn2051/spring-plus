package org.example.expert.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchResponseDto {

    private String title;
    private long managerCount;
    private long commentCount;
}
