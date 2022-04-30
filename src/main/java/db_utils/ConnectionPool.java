package db_utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

public class ConnectionPool {
    //attributes:

    private static final int NUM_OF_CONNECTIONS = DB_Manager.MAX_CONNECTION; //max number of the connections that we can get
    private static ConnectionPool instance = null;
    private final Stack<Connection> connections = new Stack<>(); // Stack of the connections

    //methods:

    /**
     * using openAllConnections method
     *
     * @throws SQLException if went wrong
     */
    private ConnectionPool() throws SQLException {
        openAllConnections();
    }

    /**
     * Opens 10 connection in advanced
     *
     * @throws SQLException if went wrong
     */
    private void openAllConnections() throws SQLException {
        for (int index = 0; index < NUM_OF_CONNECTIONS; index += 1) {
            Connection connection = DriverManager.getConnection(DB_Manager.URL, DB_Manager.USER_NAME, DB_Manager.USER_PASS);
            connections.push(connection);
        }
    }

    /**
     * returns connection
     * double check
     *
     * @return instance of ConnectionPool
     */
    public static ConnectionPool getInstance() {
        //checks if the Connection pool still null before locking the critical code
        if (instance == null) {
            //creates the connection pool
            synchronized (ConnectionPool.class) {
                //checks again if the ConnectionPool is still null before creating the code
                if (instance == null) {
                    try {
                        instance = new ConnectionPool();
                    } catch (SQLException err) {
                        System.out.println(err.getMessage());
                    }
                }
            }
        }
        return instance;
    }

    /**
     * gets connection when one is free
     *
     * @return Connection
     * @throws InterruptedException if it was interrupted
     */
    public Connection getConnection() throws InterruptedException {
        synchronized (connections) {
            if (connections.isEmpty()) {
                //waits until we will get a connection back
                connections.wait();
            }
            return connections.pop();
        }
    }

    /**
     * returns connection after it been used
     *
     * @param connection Connection
     */
    public void restoreConnection(Connection connection) {
        synchronized (connections) {
            connections.push(connection);
            //notifies that we got back a connection from the user
            connections.notify();
        }
    }

    /**
     * Closes all connections
     *
     * @throws InterruptedException if it has interrupted
     */
    public void closeAllConnection() throws InterruptedException {
        synchronized (connections) {
            while (connections.size() < NUM_OF_CONNECTIONS) {
                connections.wait();
            }
            connections.removeAllElements();
        }
    }
}
