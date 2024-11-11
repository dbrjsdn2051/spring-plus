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

    private boolean isSuccess;

    private String message;

    @CreatedDate
    private LocalDateTime createAt;

    @Builder
    public Log(Long id, boolean isSuccess, String message, LocalDateTime createAt) {
        this.id = id;
        this.isSuccess = isSuccess;
        this.message = message;
        this.createAt = createAt;
    }

    public Log(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
