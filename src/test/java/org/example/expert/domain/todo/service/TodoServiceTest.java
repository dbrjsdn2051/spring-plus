package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.config.security.AuthUser;
import org.example.expert.domain.log.template.LogServiceTemplate;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private WeatherClient weatherClient;
    @Mock
    private LogServiceTemplate logServiceTemplate;

    @InjectMocks
    private TodoService todoService;


    @Test
    @DisplayName("Todo 저장시 담당 매니저도 정상 등록")
    void TodoSaveManagerSuccessTest() {
        // given
        User userA = User.builder().id(1L).email("test@example.com").nickname("userA").userRole(UserRole.USER).build();
        AuthUser authUser = new AuthUser(userA);
        TodoSaveRequest reqDto = new TodoSaveRequest("titleA", "test");

        when(weatherClient.getTodayWeather()).thenReturn("sunny");
        Todo savedTodo = Todo.builder().id(1L).title("titleA").contents("test").weather("sunny").user(userA).build();
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // when
        TodoSaveResponse respDto = todoService.saveTodo(authUser, reqDto);

        // then
        assertThat(respDto).isNotNull();
        assertThat(respDto.getTitle()).isEqualTo(reqDto.getTitle());
        assertThat(respDto.getContents()).isEqualTo(reqDto.getContents());
        assertThat(respDto.getWeather()).isEqualTo("sunny");
        assertThat(respDto.getUser().getEmail()).isEqualTo(userA.getEmail());

        verify(weatherClient, times(1)).getTodayWeather();
        verify(todoRepository, times(1)).save(any());
        verify(logServiceTemplate, times(1)).execute(any());
    }


    @DisplayName("Todo 저장후 담당 매니저 저장 실패")
    void TodoSaveManagerFailTest() {
        // given
        User userA = User.builder().id(1L).email("test@example.com").nickname("userA").userRole(UserRole.USER).build();
        AuthUser authUser = new AuthUser(userA);
        TodoSaveRequest reqDto = new TodoSaveRequest("titleA", "test");

        when(weatherClient.getTodayWeather()).thenReturn("sunny");
        Todo savedTodo = Todo.builder().id(1L).title("titleA").contents("test").weather("sunny").user(userA).build();
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        doThrow(new Exception())
                .when(logServiceTemplate.execute(() -> managerRepository.save(new Manager(userA, savedTodo))));

        // when & then
        assertThatThrownBy(() -> todoService.saveTodo(authUser, reqDto))
                .isInstanceOf(Exception.class);

        verify(weatherClient, times(1)).getTodayWeather();
        verify(todoRepository, times(1)).save(any());
        verify(logServiceTemplate, times(1)).execute(any());
    }

}