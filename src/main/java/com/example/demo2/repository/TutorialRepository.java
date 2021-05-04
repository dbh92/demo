package com.example.demo2.repository;

import com.example.demo2.model.AppUser;
import com.example.demo2.model.Tutorial;
import com.example.demo2.model.Tutorial;
import lombok.Builder;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
public interface TutorialRepository extends JpaRepository<Tutorial,Long> {
    List<Tutorial> findByPublished(boolean published);
    List<Tutorial> findByTitleContaining(String title);
    Tutorial findByDescription(String description);
}
