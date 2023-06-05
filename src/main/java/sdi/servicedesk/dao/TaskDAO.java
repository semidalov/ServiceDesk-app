package sdi.servicedesk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.Occasion;
import sdi.servicedesk.models.Task;
import sdi.servicedesk.models.TaskStatus;
import sdi.servicedesk.utils.QueryBuilder;

import java.util.List;
import java.util.Map;

@Component
@Transactional
public class TaskDAO {
    private final SessionFactory sessionFactory;
    private final QueryBuilder queryBuilder;

    @Autowired
    public TaskDAO(SessionFactory sessionFactory, QueryBuilder queryBuilder) {
        this.sessionFactory = sessionFactory;
        this.queryBuilder = queryBuilder;
    }


    public List<Task> findTasks(int page, int resultsPerPage, Map<String, Object> searchParams, String orderBy, Map<String, Object> filterParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT t FROM Task t LEFT JOIN FETCH t.creator LEFT JOIN FETCH t.executor LEFT JOIN FETCH t.incident");
        
        this.queryBuilder.buildQuery(queryBuilder, searchParams, orderBy, filterParams);
        System.out.println(queryBuilder);

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(queryBuilder.toString(), Task.class)
                .setFirstResult(calculateOffset(page, resultsPerPage))
                .setMaxResults(resultsPerPage)
                .getResultList();
    }

    public void save(Task task) {
        Session session = sessionFactory.getCurrentSession();
        session.save(task);
    }
    public void update(int id, Task task) {
        System.out.println(task.getOccasion().getId());
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE Task t SET t.title = :titleParam," +
                        " t.comment = :commentParam, t.occasion = :occasionParam," +
                        " t.description = :descriptionParam," +
                        "t.equipment = :equipmentParam, t.incident = :incidentParam, t.taskStatus = :taskStatusParam, t.executor = :executorParam" +
                        " WHERE id = :idParam")
                .setParameter("titleParam", task.getTitle())
                .setParameter("commentParam", task.getComment())
                .setParameter("occasionParam", task.getOccasion())
                .setParameter("descriptionParam", task.getDescription())
                .setParameter("equipmentParam", task.getEquipment())
                .setParameter("incidentParam", task.getIncident())
                .setParameter("executorParam", task.getExecutor())
                .setParameter("taskStatusParam", task.getTaskStatus())
                .setParameter("idParam", id)
                .executeUpdate();
    }

    public void closeTask(int taskId, TaskStatus status) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE Task t SET t.taskStatus = :taskStatusPAram WHERE t.id = :idParam")
                .setParameter("idParam", taskId)
                .setParameter("taskStatusPAram", status)
                .executeUpdate();
    }

    public Task findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECt t FROM Task t " +
                        "LEFT JOIN FETCH t.creator " +
                        "LEFT JOIN FETCH t.executor " +
                        "LEFT JOIN FETCH t.occasion " +
                        "LEFT JOIN FETCH t.equipment " +
                        "LEFT JOIN FETCH t.incident " +
                        "WHERE t.id = :idPAram", Task.class)
                .setParameter("idPAram", id)
                .getSingleResult();
    }

    public void setExecutor(int id, Employee executor) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE Task t SET t.executor = :executorParam, t.taskStatus.id = 2 WHERE t.id = :idParam")
                .setParameter("executorParam", executor)
                .setParameter("idParam", id)
                .executeUpdate();
    }

    public int rowsCount(Map<String, Object> searchParams, Map<String, Object> filterParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM Task t");

        this.queryBuilder.buildQuery(queryBuilder, searchParams, null, filterParams);

        Session session = sessionFactory.getCurrentSession();
        return Integer.parseInt(session.createQuery(queryBuilder.toString()).getSingleResult().toString());
    }

    public int calculateOffset(int page, int resultPerPage) {
        return resultPerPage * (page - 1);
    }
}
