package org.example.expert.domain.todo.repository;

import org.assertj.core.api.Assertions;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("할일 목록 검색 시 날짜 정보를 넣었을때 정상 출력")
    void getTodoListWithWeatherSuccessTest() {
        // given
        User user = new User("test@example.com", "password1!", UserRole.USER, "test");
        userRepository.save(user);
        todoRepository.saveAll(List.of(
                new Todo("titleA", "contentA", "sunny", user),
                new Todo("titleB", "contentB", "rainy", user)
        ));

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Todo> todoList = todoRepository.findAllByOrderByModifiedAtDesc(pageable, "sunny");

        // then
        assertThat(todoList).hasSize(1);
        assertThat(todoList).extracting("weather").containsExactly("sunny");
    }

    @Test
    @DisplayName("할일 목록 검색 시 날짜 정보를 넣지 않았을 때 전체 출력")
    void getTodoListWithNothingSuccessTest(){
        // given
        User user = new User("test@example.com", "password1!", UserRole.USER, "test");
        userRepository.save(user);
        todoRepository.saveAll(List.of(
                new Todo("titleA", "contentA", "sunny", user),
                new Todo("titleB", "contentB", "rainy", user)
        ));

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Todo> todoList = todoRepository.findAllByOrderByModifiedAtDesc(pageable, null);

        // then
        assertThat(todoList).hasSize(2);
        assertThat(todoList).extracting("weather").containsExactly("sunny", "rainy");

    }
}