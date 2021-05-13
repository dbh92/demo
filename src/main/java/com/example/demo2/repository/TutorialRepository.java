package com.example.demo2.repository;


import com.example.demo2.model.Tutorial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, Long> {

    @Query("SELECT t FROM Tutorial t WHERE CONCAT(t.id, t.title, t.description, t.published) LIKE %?1%")
    List<Tutorial> findTutorialBySearch(String keyword);

}
