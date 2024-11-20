package org.example.expert.domain.log.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.log.entity.LogStatus;
import org.example.expert.domain.log.service.LogService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogServiceTemplate {

    private final LogService logService;

    public <T> T execute(TransactionCallback<T> callback, String className, String methodName) {
        try {
            T result = callback.execute();
            logService.saveLog(LogStatus.SUCCESS, LogStatus.SUCCESS.toString(), className, methodName);
            return result;
        } catch (Exception e) {
            log.info("Error = {}", e.getMessage());
            logService.saveLog(LogStatus.FAIL, e.getMessage(), className, methodName);
            throw new IllegalArgumentException("로직 실패", e);
        }
    }
}
