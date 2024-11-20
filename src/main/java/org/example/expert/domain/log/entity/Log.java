package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "logs")
@EntityListeners(AuditingEntityListener.class)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LogStatus logStatus;

    private String message;

    private String className;
    private String methodName;

    @CreatedDate
    private LocalDateTime createAt;

    public Log(Long id, LogStatus logStatus, String message, String className, String methodName, LocalDateTime createAt) {
        this.id = id;
        this.logStatus = logStatus;
        this.message = message;
        this.className = className;
        this.methodName = methodName;
        this.createAt = createAt;
    }

    public Log(LogStatus logStatus, String message, String className, String methodName) {
        this.logStatus = logStatus;
        this.message = message;
        this.className = className;
        this.methodName = methodName;
    }
}
