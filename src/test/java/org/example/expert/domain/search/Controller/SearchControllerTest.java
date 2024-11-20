package org.example.expert.domain.search.Controller;

import org.example.expert.domain.search.dto.SearchResponseDto;
import org.example.expert.domain.search.service.SearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;


    @Test
    @WithMockUser
    @DisplayName("검색 결과 정상 출력 ")
    void searchSuccessTest() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<SearchResponseDto> pages
                = new PageImpl<>(List.of(new SearchResponseDto("titleA", 5L, 10L)), pageable, 1);

        when(searchService.searchTodos(eq(pageable), anyString(), anyString(), any(), any())).thenReturn(pages);

        // when & then
        mockMvc.perform(get("/search/todos")
                        .param("title", "titleA")
                        .param("nickname", "userA")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("titleA"))
                .andExpect(jsonPath("$.content[0].managerCount").value(5))
                .andExpect(jsonPath("$.content[0].commentCount").value(10));
    }

    @Test
    @WithMockUser
    @DisplayName("검색 조건을 넣지 않아도 정상 출력")
    void searchNoParameterSuccessTest() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<SearchResponseDto> pages
                = new PageImpl<>(List.of(new SearchResponseDto("titleA", 5L, 10L)), pageable, 1);

        when(searchService.searchTodos(eq(pageable), any(), any(), any(), any())).thenReturn(pages);

        // when & then
        mockMvc.perform(get("/search/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("titleA"))
                .andExpect(jsonPath("$.content[0].managerCount").value(5))
                .andExpect(jsonPath("$.content[0].commentCount").value(10));
    }

}