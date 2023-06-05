package sdi.servicedesk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.Equipment;
import sdi.servicedesk.utils.QueryBuilder;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class EquipmentDAO {

    private final SessionFactory sessionFactory;
    private final QueryBuilder queryBuilder;

    @Autowired
    public EquipmentDAO(SessionFactory sessionFactory, QueryBuilder queryBuilder) {
        this.sessionFactory = sessionFactory;
        this.queryBuilder = queryBuilder;
    }

    public void save(Equipment equipment) {
        Session session = sessionFactory.getCurrentSession();
        session.save(equipment);
    }

    public List<Equipment> findAll(int page, int resultPerPage, String orderBy, Map<String, Object> searchParams, Map<String, Object> filterParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT e FROM Equipment e LEFT JOIN FETCH e.equipmentClass");
        this.queryBuilder.buildQuery(queryBuilder, searchParams, orderBy, filterParams);

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(queryBuilder.toString(), Equipment.class)
                .setFirstResult(calculateOffset(page, resultPerPage))
                .setMaxResults(resultPerPage)
                .getResultList();
    }

    public List<Object[]> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT e.id, e.name FROM Equipment e WHERE e.name = :nameParam")
                .setParameter("nameParam", name)
                .list();


    }

    public List<Object[]> findBySerial(String serial) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT e.id, e.serial FROM Equipment e WHERE e.serial = :serialParam")
                .setParameter("serialParam", serial)
                .list();
    }

    public Equipment findOneById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Equipment.class, id);
    }



    public void update(int id, Equipment equipment) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE Equipment e SET e.name = :nameParam, e.serial = :serialParam, e.equipmentClass = :equipClassParam WHERE e.id = :idParam")
                .setParameter("nameParam", equipment.getName())
                .setParameter("serialParam", equipment.getSerial())
                .setParameter("equipClassParam", equipment.getEquipmentClass())
                .setParameter("idParam", id)
                .executeUpdate();
    }

    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE Equipment e SET e.inUse = 0 where e.id = :idParam")
                .setParameter("idParam", id)
                .executeUpdate();
    }

    public int rowsCount(Map<String, Object> searchParams, Map<String, Object> filterParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM Equipment e");
        this.queryBuilder.buildQuery(queryBuilder, searchParams, null, filterParams);

        Session session = sessionFactory.getCurrentSession();
        return Integer.parseInt(session.createQuery(queryBuilder.toString()).getSingleResult().toString());
    }

    public int calculateOffset(int page, int resultPerPage) {
        return resultPerPage * (page - 1);
    }
}
