package com.example.demo2.service.tutorialService;

import com.example.demo2.model.Tutorial;
import com.example.demo2.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
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
    public List<Tutorial> findTutorialBySearch(String description, Long id) {
//        Long parId = Long.parseLong(id);
        String sql = "Select e from Tutorial e where 1 = 1";
        StringBuilder sb = new StringBuilder(sql);
        try {
            if (id != null) {
                sb.append(" and e.id = :id");
            }
            if ((description != null) &&(!description.isEmpty())) {
                sb.append(" and e.description = :description");
            }
            Query query = entityManager.createQuery(sb.toString(), Tutorial.class);
            if (id != null) {
                query.setParameter("id", id);
            }
            if ((description != null) &&(!description.isEmpty())) {
                query.setParameter("description", description);
            }

            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
