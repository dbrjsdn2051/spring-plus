package org.example.expert.batch;

import com.github.javafaker.Faker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class BatchInsertUserService {

    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    private final Faker faker = new Faker();

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Transactional
    public void insertUser(int dataSize) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        HashSet<String> uniqueNickname = new HashSet<>();
        HashSet<String> uniqueEmail = new HashSet<>();

        try {
            for (int i = 0; i < dataSize; i++) {
                String nickname = "userA";
                String email = "test@example.com";

                while (!uniqueNickname.add(nickname)) {
                    nickname = faker.name().username();
                }

                while (!uniqueEmail.add(email)) {
                    email = faker.internet().emailAddress();
                }

                User user = User.builder()
                        .email(email)
                        .password("1234")
                        .nickname(nickname)
                        .userRole(UserRole.USER)
                        .build();

                em.persist(user);

                if (i > 0 && i % batchSize == 0) {
                    em.flush();
                    em.clear();
                }
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("batch 비정상 중단");
        } finally {
            em.close();
        }
    }
}
