package sdi.servicedesk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.UserRole;

import java.util.List;

@Component
@Transactional
public class UserRoleDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRoleDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<UserRole> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT r FROM UserRole r", UserRole.class).getResultList();
    }
}
