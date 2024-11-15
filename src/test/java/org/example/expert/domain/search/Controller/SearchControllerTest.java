package org.example.expert.domain.search.Controller;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.security.SecurityConfig;
import org.example.expert.domain.search.dto.SearchResponseDto;
import org.example.expert.domain.search.service.SearchService;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JwtUtil.class, SecurityConfig.class})
@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
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
                        .param("endDate", "2024-12-12")
                        .header("Authorization", generateJwtToken())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("titleA"))
                .andExpect(jsonPath("$.content[0].managerCount").value(5))
                .andExpect(jsonPath("$.content[0].commentCount").value(10));
    }

    @Test
    @DisplayName("토큰 정보를 누락했을 때 에러 발생")
    void searchUnAuthorizationMissingFailTest() throws Exception {
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
                        .param("endDate", "2024-12-12")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("검색 조건을 넣지 않아도 정상 출력")
    void searchNoParameterSuccessTest() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<SearchResponseDto> pages
                = new PageImpl<>(List.of(new SearchResponseDto("titleA", 5L, 10L)), pageable, 1);

        when(searchService.searchTodos(eq(pageable), any(), any(), any(), any())).thenReturn(pages);

        // when & then
        mockMvc.perform(get("/search/todos")
                        .header("Authorization", generateJwtToken())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("titleA"))
                .andExpect(jsonPath("$.content[0].managerCount").value(5))
                .andExpect(jsonPath("$.content[0].commentCount").value(10));
    }

    private String generateJwtToken() {
        return jwtUtil.createToken(1L, "test@example.com", UserRole.USER, "userA");
    }
}