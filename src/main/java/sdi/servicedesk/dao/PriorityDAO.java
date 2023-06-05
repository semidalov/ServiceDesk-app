package sdi.servicedesk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.Priority;

import java.util.List;

@Component
@Transactional
public class PriorityDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public PriorityDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Priority> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT p FROM Priority p", Priority.class).getResultList();
    }
}
