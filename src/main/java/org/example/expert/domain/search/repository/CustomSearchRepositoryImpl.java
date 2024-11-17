package org.example.expert.domain.search.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.search.dto.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;

@Repository
@RequiredArgsConstructor
public class CustomSearchRepositoryImpl implements CustomSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<SearchResponseDto> search(Pageable pageable, String titleCond, String nicknameCond, LocalDate startDate, LocalDate endDate) {
        List<SearchResponseDto> results = jpaQueryFactory
                .select(
                        Projections.constructor(
                                SearchResponseDto.class,
                                todo.title,
                                manager.countDistinct(),
                                comment.countDistinct()
                        ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(
                        titleEq(titleCond),
                        nicknameEq(nicknameCond),
                        searchDate(startDate, endDate)
                )
                .groupBy(todo.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(todo.createdAt.desc())
                .fetch();

        JPAQuery<Long> totalCount = jpaQueryFactory
                .select(todo.id.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .where(
                        titleEq(titleCond),
                        nicknameEq(nicknameCond),
                        searchDate(startDate, endDate)
                );

        return PageableExecutionUtils.getPage(results, pageable, totalCount::fetchOne);
    }

    private BooleanExpression titleEq(String titleCond) {
        return titleCond != null ? todo.title.contains(titleCond) : null;
    }

    private BooleanExpression nicknameEq(String nicknameCond) {
        return nicknameCond != null ? todo.managers.any().user.nickname.contains(nicknameCond) : null;
    }

    private BooleanExpression searchDate(LocalDate startDate, LocalDate endDate) {
        BooleanExpression isStartDate =
                startDate != null ? todo.createdAt.goe(LocalDateTime.of(startDate, LocalTime.MIN)) : null;
        BooleanExpression isEndDate =
                endDate != null ? todo.createdAt.loe(LocalDateTime.of(endDate, LocalTime.MAX).withNano(0)) : null;
        return Expressions.allOf(isStartDate, isEndDate);
    }
}
