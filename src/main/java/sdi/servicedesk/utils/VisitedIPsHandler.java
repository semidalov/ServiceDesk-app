package sdi.servicedesk.utils;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import sdi.servicedesk.controllers.TasksController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class VisitedIPsHandler {
    private final List<String> IPs = new ArrayList<>();
    private final ArrayBlockingQueue<String> queue;
    ExecutorService executor;
    private static final Logger LOGGER = Logger.getLogger(TasksController.class);

    {
        String ip1 = "0:0:0:0:0:0:0:1";
        String ip2 = "192.168.1.10";
        String ip3 = "192.168.0.72";
        String ip4 = "192.168.0.71";
        String ip5 = "192.168.0.70";
        String ip6 = "192.168.1.1";

        IPs.add(ip1);
        IPs.add(ip2);
        IPs.add(ip3);
        IPs.add(ip4);
        IPs.add(ip5);
        IPs.add(ip6);
    }

    public VisitedIPsHandler() {
        queue = new ArrayBlockingQueue<>(1);
        executor = Executors.newSingleThreadExecutor();
    }

    public void handleIpAddress(String ip) {
        try {
            queue.put(ip);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executor.submit(new Handler());
    }


    class Handler implements Runnable {

        @Override
        public void run() {

            String ip = null;
            try {
                ip = queue.take();
            } catch (InterruptedException e) {
                LOGGER.warn(e);
                throw new RuntimeException(e);
            }

            if (checkExcludedAddresses(ip)) {

                try {
                    String url = "jdbc:postgresql://192.168.1.90:5432/servicedesk_service";
                    String username = "postgres";
                    String password = "root";
                    Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();

                    try (Connection conn = DriverManager.getConnection(url, username, password)) {

                        PreparedStatement statement = conn.prepareStatement("INSERT INTO visited_ips (ip, timestamp) VALUES (?, ?)");
                        statement.setString(1, ip);
                        statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        statement.executeUpdate();

                        System.out.println(statement);

                    }
                } catch (Exception ex) {
                    LOGGER.warn(ex);
                }
            }
        }

        private boolean checkExcludedAddresses(String ip) {

            for (String s : IPs) {
                if (s.equals(ip)) return false;
            }
            return true;
        }
    }
}
