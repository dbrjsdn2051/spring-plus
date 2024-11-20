package org.example.expert.batch;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.User;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcBatchInsertRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAllUsers(List<User> queryList) {
        jdbcTemplate.batchUpdate("insert into users(email, password, nickname, user_role) values (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        User user = queryList.get(i);
                        ps.setString(1, user.getEmail());
                        ps.setString(2, user.getPassword());
                        ps.setString(3, user.getNickname());
                        ps.setString(4, user.getUserRole().toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return queryList.size();
                    }
                });
    }
}
