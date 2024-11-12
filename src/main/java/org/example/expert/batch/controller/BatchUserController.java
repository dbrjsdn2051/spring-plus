package org.example.expert.batch.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.batch.dto.BatchRequestDto;
import org.example.expert.batch.dto.BatchResponseDto;
import org.example.expert.batch.service.BatchUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BatchUserController {
    private final BatchUserService batchUserService;

    @GetMapping("/batch/user")
    public ResponseEntity<BatchResponseDto> find(@RequestBody BatchRequestDto requestDto) {
        BatchResponseDto responseDto = batchUserService.findUser(requestDto.getNickname());
        return ResponseEntity.ok(responseDto);
    }
}
