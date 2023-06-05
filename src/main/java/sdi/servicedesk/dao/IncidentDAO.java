package sdi.servicedesk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.Incident;
import sdi.servicedesk.utils.QueryBuilder;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class IncidentDAO {

    private final SessionFactory sessionFactory;
    private final QueryBuilder queryBuilder;

    @Autowired
    public IncidentDAO(SessionFactory sessionFactory, QueryBuilder queryBuilder) {
        this.sessionFactory = sessionFactory;
        this.queryBuilder = queryBuilder;
    }

    public void save(Incident incident) {
        Session session = sessionFactory.getCurrentSession();
        session.save(incident);
    }

    public Incident findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Incident.class, id);
    }

    public List<Incident> findByEquipmentId(int page, int resultPerPage, int equipmentId, String orderBy, Map<String, Object> searchParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT i FROM Incident i WHERE i.equipmentClass.id = " +
                "(SELECT e.equipmentClass.id FROM Equipment e WHERE e.id = :paramId)");
        for (var entry : searchParams.entrySet()) {
            this.queryBuilder.buildWhereBySearch(queryBuilder, entry.getKey(), entry.getValue(), true);
        }
        this.queryBuilder.buildOrderBy(queryBuilder, orderBy);

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(queryBuilder.toString(), Incident.class)
                .setFirstResult(calculateOffset(page, resultPerPage))
                .setMaxResults(resultPerPage)
                .setParameter("paramId", equipmentId)
                .getResultList();
    }

    public void update(int id, Incident incident) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE Incident i SET i.title = :titleParam, i.equipmentClass = :classParam, i.priority = :priorityParam WHERE i.id = :idParam")
                .setParameter("titleParam", incident.getTitle())
                .setParameter("classParam", incident.getEquipmentClass())
                .setParameter("priorityParam", incident.getPriority())
                .setParameter("idParam", id)
                .executeUpdate();
    }

    public List<Incident> findAll(int page, int resultsPerPage) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT i FROM Incident i LEFT JOIN FETCH i.equipmentClass LEFT JOIN FETCH i.priority", Incident.class)
                .setFirstResult(calculateOffset(page, resultsPerPage))
                .setMaxResults(resultsPerPage)
                .getResultList();
    }

    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("DELETE Incident e WHERE e.id = :idParam")
                .setParameter("idParam", id)
                .executeUpdate();
    }

    public int rowsCount(int equipmentId, Map<String, Object> searchParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM Incident i WHERE i.equipmentClass.id = (SELECT e.equipmentClass.id FROM Equipment e WHERE e.id = :idParam)");
        if (searchParams != null)
            for (var entry : searchParams.entrySet()) {
                this.queryBuilder.buildWhereBySearch(queryBuilder, entry.getKey(), entry.getValue(), true);
            }

        Session session = sessionFactory.getCurrentSession();
        return Integer.parseInt(session.createQuery(queryBuilder.toString())
                .setParameter("idParam", equipmentId)
                .getSingleResult().toString());
    }

    public String checkParam(String param) {
        if (param == null)
            return "%%";
        return param;
    }

    public int calculateOffset(int page, int resultPerPage) {
        return resultPerPage * (page - 1);
    }
}
