package sdi.servicedesk.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.EquipmentClass;
import sdi.servicedesk.utils.QueryBuilder;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class EquipmentClassDAO {

    private static final Logger LOGGER = Logger.getLogger(EquipmentClass.class);
    private final SessionFactory sessionFactory;
    private final QueryBuilder queryBuilder;

    @Autowired
    public EquipmentClassDAO(SessionFactory sessionFactory, QueryBuilder queryBuilder) {
        this.sessionFactory = sessionFactory;
        this.queryBuilder = queryBuilder;
    }

    public List<EquipmentClass> findAll(int page, int resultPerPage, String orderBy, Map<String, Object> searchParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT c FROM EquipmentClass c");
        this.queryBuilder.buildQuery(queryBuilder, searchParams, orderBy, null);

        Session session = sessionFactory.getCurrentSession();
        List<EquipmentClass> classes = session.createQuery(queryBuilder.toString(), EquipmentClass.class)
                .setFirstResult(calculateOffset(page, resultPerPage))
                .setMaxResults(resultPerPage)
                .getResultList();
        return classes;
    }

    public List<EquipmentClass> findAll() {
        Session session = sessionFactory.getCurrentSession();
        List<EquipmentClass> classes = session.createQuery("SELECT c FROM EquipmentClass c", EquipmentClass.class)
                .getResultList();
        LOGGER.info(classes);
        return classes;
    }

    public EquipmentClass findOnaById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT e FROM EquipmentClass e WHERE e.id = :idPAram", EquipmentClass.class)
                .setParameter("idPAram", id)
                .getSingleResult();
    }

    public void save(EquipmentClass equipmentClass) {
        Session session = sessionFactory.getCurrentSession();
        session.save(equipmentClass);
    }

    public void update(EquipmentClass equipmentClass) {
        Session session = sessionFactory.getCurrentSession();
        session.update(equipmentClass);
    }

    public int rowsCount() {
        Session session = sessionFactory.getCurrentSession();
        return Integer.parseInt(session.createQuery("SELECT COUNT(*) FROM EquipmentClass ").getSingleResult().toString());
    }

    public int calculateOffset(int page, int resultPerPage) {
        return resultPerPage * (page - 1);
    }
}
