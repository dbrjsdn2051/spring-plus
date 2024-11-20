package org.example.expert.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.search.dto.SearchResponseDto;
import org.example.expert.domain.search.repository.CustomSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final CustomSearchRepository customSearchRepository;

    public Page<SearchResponseDto> searchTodos(Pageable pageable, String title, String nickname, LocalDate startDate, LocalDate endDate) {
        return customSearchRepository.search(pageable, title, nickname, startDate, endDate);
    }
}
