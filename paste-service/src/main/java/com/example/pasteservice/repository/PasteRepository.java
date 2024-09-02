package com.example.pasteservice.repository;

import com.example.pasteservice.entity.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long> {

    @Query(value = """
        select p.id, p.link, p.text, p.user_id from pastes p
        where p.user_id = ?1
    """, nativeQuery = true)
    List<Paste> findAllByUserId(Long userId);

}
