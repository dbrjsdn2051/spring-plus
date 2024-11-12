package org.example.expert.batch.dto;

import lombok.Getter;
import org.example.expert.domain.user.entity.User;

@Getter
public class BatchResponseDto {

    private Long id;
    private String email;
    private String nickname;

    public BatchResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
