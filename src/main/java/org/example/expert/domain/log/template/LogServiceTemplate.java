package org.example.expert.domain.log.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.log.service.LogService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogServiceTemplate {

    private final LogService logService;

    public <T> T execute(TransactionCallback<T> callback) {
        try {
            T result = callback.execute();
            logService.saveLog(true, "Success");
            return result;
        } catch (Exception e) {
            log.info("Error = {}", e.getMessage());
            logService.saveLog(false, e.getMessage());
            throw new IllegalArgumentException();
        }
    }
}
