package PoolTest;

import utils.DBConnection;

import java.sql.Connection;

public class PoolThreadTest {

    public static void main(String[] args) {

        Runnable task = () -> {
            try {
                Connection conn = DBConnection.getConnection();
                System.out.println(
                        Thread.currentThread().getName() + " got connection"
                );
                Thread.sleep(2000);
                conn.close();
                System.out.println(
                        Thread.currentThread().getName() + " released connection"
                );
            } catch (Exception e) {
                System.out.println(
                        Thread.currentThread().getName() + " failed: " + e.getMessage()
                );
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(task, "Thread-" + i).start();
        }
    }
}
