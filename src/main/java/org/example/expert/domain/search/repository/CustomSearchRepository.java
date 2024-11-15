package org.example.expert.domain.search.repository;

import org.example.expert.domain.search.dto.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

public interface CustomSearchRepository {

    Page<SearchResponseDto> search(Pageable pageable, String titleCond, String nicknameCond, LocalDate startDate, LocalDate endDate);
}
