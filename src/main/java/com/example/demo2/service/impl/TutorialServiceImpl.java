package com.example.demo2.service.impl;

import com.example.demo2.model.Tutorial;
import com.example.demo2.repository.TutorialRepository;
import com.example.demo2.service.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class TutorialServiceImpl implements TutorialService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TutorialRepository tutorialRepository;

    @Override
    public Page<Tutorial> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.tutorialRepository.findAll(pageable);
    }

    @Override
    public List<Tutorial> findTutorialBySearch(String keyword) {

        return tutorialRepository.findTutorialBySearch(keyword);
    }
}
