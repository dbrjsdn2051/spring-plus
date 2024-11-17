package org.example.expert.domain.log.template;

import org.example.expert.domain.log.entity.LogStatus;
import org.example.expert.domain.log.service.LogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTemplateTest {

    @Mock
    private LogService logService;

    @InjectMocks
    private LogServiceTemplate logServiceTemplate;

    @Mock
    private TransactionCallback<String> callback;

    @Test
    @DisplayName("로그 저장 성공")
    void executeSuccessTest() throws Exception {
        // given
        String successResult = "Success Result";
        when(callback.execute()).thenReturn(successResult);

        // when
        String execute = logServiceTemplate.execute(callback, this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName());

        // then
        assertThat(execute).isNotNull();
        assertThat(execute).isEqualTo(successResult);

        verify(logService, times(1)).saveLog(eq(LogStatus.SUCCESS), eq(LogStatus.SUCCESS.toString()), anyString(), anyString());
    }

    @Test
    @DisplayName("Execute가 실패할 경우 로그 저장 성공")
    void executeFailTest() throws Exception {
        // given
        when(callback.execute()).thenThrow(new RuntimeException("Save Fail"));

        // when & then
        assertThatThrownBy(() ->
                logServiceTemplate.execute(callback,
                        this.getClass().getName(),
                        Thread.currentThread().getStackTrace()[1].getMethodName()
                ))
                .isInstanceOf(RuntimeException.class);

        verify(logService, times(1)).saveLog(eq(LogStatus.FAIL), eq("Save Fail"), anyString(), anyString());
    }

}