package org.example.expert.domain.search.Controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.search.dto.SearchResponseDto;
import org.example.expert.domain.search.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search/todos")
    public ResponseEntity<Page<SearchResponseDto>> getTodos(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "nickname", required = false) String nickname,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ) {
        Page<SearchResponseDto> searchResponseDtos = searchService.searchTodos(pageable, title, nickname, startDate, endDate);
        return ResponseEntity.ok(searchResponseDtos);
    }
}
