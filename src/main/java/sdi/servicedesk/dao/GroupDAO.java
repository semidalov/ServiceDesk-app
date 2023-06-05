package sdi.servicedesk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.Group;

import java.util.List;

@Component
@Transactional
public class GroupDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public GroupDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Group> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT g FROM Group g", Group.class).getResultList();
    }
}
