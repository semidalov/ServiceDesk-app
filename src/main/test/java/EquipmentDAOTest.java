import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;

import java.util.List;

import org.hibernate.*;
import org.hibernate.query.*;
import org.hibernate.query.Query;
import org.junit.jupiter.api.*;
import sdi.servicedesk.models.*;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;

public class EquipmentDAOTest {

    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    public static void setup() {
        sessionFactory = HibernateUtil.getSessionFactory();
        System.out.println("SessionFactory created");
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) sessionFactory.close();
        System.out.println("SessionFactory destroyed");
    }

    @AfterEach
    public void closeSession() {
        if (session != null) session.close();
        System.out.println("Session closed\n");
    }

    @Test
    @Transactional
    public void tryToSave() {
        Session session = sessionFactory.openSession();
        Transaction txn = session.beginTransaction();
        Query query = session.createQuery("UPDATE User u SET u.username = :userParam WHERE u.id = :idParam")
                .setParameter("userParam", "asd")
                .setParameter("idParam", 2);
        query.executeUpdate();

        session.createQuery("UPDATE Employee e SET e.user.id = 22 WHERE e.id = 7")
                .executeUpdate();

        txn.commit();
    }
}
