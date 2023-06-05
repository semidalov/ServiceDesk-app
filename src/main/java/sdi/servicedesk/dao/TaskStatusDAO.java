package sdi.servicedesk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.TaskStatus;

import java.util.List;

@Component
@Transactional
public class TaskStatusDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public TaskStatusDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<TaskStatus> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT ts FROM TaskStatus ts", TaskStatus.class).getResultList();
    }
}
