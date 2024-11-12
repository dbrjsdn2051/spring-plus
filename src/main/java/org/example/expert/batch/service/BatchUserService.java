package org.example.expert.batch.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.batch.dto.BatchResponseDto;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchUserService {

    private final UserRepository userRepository;

    public BatchResponseDto findUser(String nickname) {
        User findUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        return new BatchResponseDto(findUser);

    }
}
