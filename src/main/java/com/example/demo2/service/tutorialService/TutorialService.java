package com.example.demo2.service.tutorialService;

import com.example.demo2.model.Tutorial;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TutorialService {
    Page<Tutorial> findPaginated(int pageNo, int pageSize, String sortField, String sortDir);

    List<Tutorial> findTutorialBySearch(String description, Long id);
}

