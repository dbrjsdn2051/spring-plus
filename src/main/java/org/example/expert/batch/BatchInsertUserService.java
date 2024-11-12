package org.example.expert.batch;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchInsertUserService {

    private final JdbcBatchInsertRepository jdbcBatchInsertRepository;
    private final Faker faker = new Faker();

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Transactional
    public void insertUser(int dataSize) {
        log.info("배치 작업 시작");
        long startTime = System.currentTimeMillis();

        HashSet<String> uniqueNickname = new HashSet<>();
        HashSet<String> uniqueEmail = new HashSet<>();

        List<User> queryList = new ArrayList<>();

        for (int i = 1; i <= dataSize; i++) {
            String nickname = "userA";
            String email = "test@example.com";

            while (!uniqueNickname.add(nickname)) {
                nickname = faker.name().username();
            }

            while (!uniqueEmail.add(email)) {
                email = faker.internet().emailAddress();
            }

            queryList.add(User.builder()
                    .email(email)
                    .password("1234")
                    .userRole(UserRole.USER)
                    .nickname(nickname)
                    .build());

            if (i > 0 && i % batchSize == 0) {
                jdbcBatchInsertRepository.saveAllUsers(queryList);
                queryList.clear();
            }
        }

        if (!queryList.isEmpty()) {
            jdbcBatchInsertRepository.saveAllUsers(queryList);
            queryList.clear();
        }

        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        log.info("Batch Insert Execute Time = {}", executeTime);
    }
}
