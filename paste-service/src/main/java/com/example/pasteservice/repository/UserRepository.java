package com.example.pasteservice.repository;

import com.example.pasteservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    @Modifying
    @Query(value = """
        delete from users where id = ?1
    """, nativeQuery = true)
    void delete(Long id);

    List<User> findAll();

    boolean existsByUsername(String username);

}
