package sdi.servicedesk.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.User;
import sdi.servicedesk.utils.QueryBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Transactional
public class UserDAO {
    private final SessionFactory sessionFactory;
    private final QueryBuilder queryBuilder;

    @Autowired
    public UserDAO(SessionFactory sessionFactory, QueryBuilder queryBuilder) {
        this.sessionFactory = sessionFactory;
        this.queryBuilder = queryBuilder;
    }

    public Optional<User> findAccountByName(String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT u FROM User u  LEFT JOIN FETCH u.role LEFT JOIN FETCH u.employee WHERE  u.username = :username", User.class)
                .setParameter("username", username)
                .stream().findAny();
    }

    public List<Object[]> findByName (String username) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT u.username, u.id FROM User u WHERE u.username = :usernameParam")
                .setParameter("usernameParam", username)
                .list();
    }

    public void saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        session.save(user.getEmployee());
    }

    public List<User> findUsers(int page, int resultsPerPage, Map<String, Object> searchParams, String orderBy,
                                Map<String, Object> filterParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT u FROM User u LEFT JOIN FETCH u.employee AS e LEFT JOIN FETCH e.group");
        this.queryBuilder.buildQuery(queryBuilder, searchParams, orderBy, filterParams);

        System.out.println(queryBuilder);

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(queryBuilder.toString(), User.class)
                .setFirstResult(calculateOffset(page, resultsPerPage))
                .setMaxResults(resultsPerPage)
                .getResultList();
    }

    public List<Employee> findExecutors(int page, int resultsPerPage, Map<String, Object> searchParams, String orderBy) {
        StringBuilder queryBuilder = new StringBuilder("SELECT e FROM Employee e LEFT JOIN FETCH e.group");

        this.queryBuilder.buildQuery(queryBuilder, searchParams, orderBy, null);

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(queryBuilder.toString(), Employee.class)
                .setFirstResult(calculateOffset(page, resultsPerPage))
                .setMaxResults(resultsPerPage)
                .getResultList();
    }

    public User findUserByEmployeeId(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT u FROM User u WHERE u.employee.id = :idParam", User.class)
                .setParameter("idParam", id)
                .stream().findAny().orElse(null);
    }
    public void updatePasswordByUserId(String password, int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE User  u SET u.password = :passParam WHERE u.id = :idPAram")
                .setParameter("passParam", password)
                .setParameter("idPAram", id)
                .executeUpdate();
    }

    public void blockUser(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE User u SET u.locked = 1 WHERE u.id = :idParam")
                .setParameter("idParam", id)
                .executeUpdate();
    }

    public void unblockUser(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE User u SET u.locked = 0 WHERE u.id = :idParam")
                .setParameter("idParam", id)
                .executeUpdate();
    }

    public void update(User user, int id) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE User u SET u.username = :userParam, u.role = :roleParam WHERE u.id = :idParam")
                .setParameter("userParam", user.getUsername())
                .setParameter("roleParam", user.getRole())
                .setParameter("idParam", id)
                .executeUpdate();

        session.createQuery("UPDATE Employee e SET e.lastName = :lastNameParam, e.firstName = :firstNameParam, e.patronymic = :patronymicParam," +
                "e.group = :groupParam, e.workPhone = :phoneParam WHERE e.user.id = :idParam")
                .setParameter("lastNameParam", user.getEmployee().getLastName())
                .setParameter("firstNameParam", user.getEmployee().getFirstName())
                .setParameter("patronymicParam", user.getEmployee().getPatronymic())
                .setParameter("groupParam", user.getEmployee().getGroup())
                .setParameter("phoneParam", user.getEmployee().getWorkPhone())
                .setParameter("idParam", id)
                .executeUpdate();
    }

    public User findUserById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.role LEFT JOIN FETCH u.employee as e LEFT JOIN FETCH e.group WHERE u.id = :paramID", User.class)
                .setParameter("paramID", id)
                .getSingleResult();
    }

    public void updateUserLastLogin(LocalDateTime localDateTime, User user) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery("UPDATE User u SET u.lastLogin = :lastLoginParam WHERE u.id = :idPAram")
                .setParameter("lastLoginParam", localDateTime)
                .setParameter("idPAram", user.getId())
                .executeUpdate();
    }

    public int employeesRowsCount(Map<String, Object> searchParams, Map<String, Object> filterParams) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM Employee e");
        this.queryBuilder.buildQuery(queryBuilder, searchParams, null, filterParams);

        Session session = sessionFactory.getCurrentSession();
        return Integer.parseInt(session.createQuery(queryBuilder.toString()).getSingleResult().toString());
    }

    public int calculateOffset(int page, int resultPerPage) {
        return resultPerPage * (page - 1);
    }
}
