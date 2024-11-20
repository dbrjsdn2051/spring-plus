package org.example.expert.domain.search.repository;

import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.search.dto.SearchResponseDto;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TestConfig;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CustomSearchRepositoryImpl.class, TestConfig.class})
class CustomSearchRepositoryImplTest {

    @Autowired
    private CustomSearchRepository customSearchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void init() {
        User userA = User.builder()
                .email("test1@example.com")
                .userRole(UserRole.USER)
                .nickname("userA")
                .build();

        User userB = User.builder()
                .email("test2@example.com")
                .userRole(UserRole.USER)
                .nickname("userB")
                .build();


        userRepository.saveAll(List.of(userA, userB));

        Todo todoA = new Todo("titleA", "content1", "sunny", userA);
        Todo todoB = new Todo("titleB", "content1", "sunny", userA);
        Todo todoC = new Todo("titleC", "content1", "sunny", userA);
        Todo todoD = new Todo("titleD", "content1", "sunny", userA);
        Todo todoE = new Todo("titleE", "content1", "sunny", userB);
        Todo todoF = new Todo("titleF", "content1", "sunny", userB);

        todoRepository.saveAll(List.of(
                todoA, todoB, todoC, todoD, todoE, todoF
        ));

        commentRepository.saveAll(List.of(
                new Comment("GoodA", userA, todoA),
                new Comment("GoodB", userB, todoA),
                new Comment("GoodC", userA, todoA),
                new Comment("GoodD", userB, todoA),
                new Comment("GoodE", userA, todoA),
                new Comment("GoodF", userB, todoA),
                new Comment("GoodG", userA, todoB),
                new Comment("GoodH", userB, todoB),
                new Comment("GoodJ", userA, todoB)
        ));
    }

    @Test
    @DisplayName("일치하는 검색 결과 정상 출력")
    void searchSuccessTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SearchResponseDto> results
                = customSearchRepository.search(pageable, "title", "user", null, null);

        assertThat(results).hasSize(6);
        assertThat(results).extracting("managerCount").containsExactly(1L, 1L, 1L, 1L, 1L, 1L);
        assertThat(results).extracting("commentCount").containsExactly(6L, 3L, 0L, 0L, 0L, 0L);
    }

    @Test
    @DisplayName("검색 조건을 넣지 않았을 떄 정상 동작")
    void searchAllSuccessTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<SearchResponseDto> results
                = customSearchRepository.search(pageRequest, null, null, null, null);

        assertThat(results).hasSize(6);
        for (SearchResponseDto result : results) {
            System.out.println("result.toString() = " + result.getTitle());
            System.out.println("result.getCommentCount() = " + result.getCommentCount());
            System.out.println("result = " + result.getManagerCount());
        }
    }
}