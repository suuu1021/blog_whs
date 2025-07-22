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

    @Query("SELECT b FROM Board b JOIN FETCH b.user u WHERE b.id = :id ")
    Optional<Board> findByIdJoinUser(@Param("id") Long id);

    }

