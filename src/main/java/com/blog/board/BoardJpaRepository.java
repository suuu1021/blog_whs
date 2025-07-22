package com.blog.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b JOIN FETCH b.user u ORDER BY b.id DESC ")
    Page<Board> findAllJoinUser(Pageable pageable);

        @Query("SELECT DISTINCT b FROM Board b " + "JOIN FETCH b.user u " +
                "LEFT JOIN FETCH b.replies r " + "LEFT JOIN FETCH r.user ru " +
                "WHERE b.id = :id")
        Optional<Board> findByIdWithDetails(@Param("id") Long id);
    }

