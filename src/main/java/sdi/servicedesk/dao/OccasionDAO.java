package sdi.servicedesk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.Occasion;
import sdi.servicedesk.utils.QueryBuilder;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class OccasionDAO {

    private final SessionFactory sessionFactory;
    private final QueryBuilder queryBuilder;

    @Autowired
    public OccasionDAO(SessionFactory sessionFactory, QueryBuilder queryBuilder) {
        this.sessionFactory = sessionFactory;
        this.queryBuilder = queryBuilder;
    }

    public List<Occasion> findOccasions(int page, int resultsPerPage, Map<String, Object> searchParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Occasion o");
        this.queryBuilder.buildQuery(queryBuilder, searchParams, null, null);

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(queryBuilder.toString(), Occasion.class)
                .setFirstResult(calculateOffset(page, resultsPerPage))
                .setMaxResults(resultsPerPage)
                .getResultList();
    }

    public int rowsCount(Map<String, Object> searchParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM Occasion o");
        this.queryBuilder.buildQuery(queryBuilder, searchParams, null, null);

        Session session = sessionFactory.getCurrentSession();
        return Integer.parseInt(session.createQuery(queryBuilder.toString()).getSingleResult().toString());
    }

    public int calculateOffset(int page, int resultPerPage) {
        return resultPerPage * (page - 1);
    }
}
