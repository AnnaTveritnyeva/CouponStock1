package db_utils;

import com.mysql.cj.protocol.Resultset;
import utils.DateUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.Map;

public class DB_Utils {
    private static Connection connection;

    /**
     * runs query by prepared query (String)
     *
     * @param query String
     */
    public static void runQuery(String query) {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException | InterruptedException err) {
            System.out.println(err.getMessage());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * runs query with keys by prepared query (string) and map with params
     *
     * @param query  String
     * @param params Map of Integer and Object
     */
    public static void runQuery(String query, Map<Integer, Object> params) {
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            params.forEach((key, value) -> {
                try {
                    if (value instanceof Integer) {
                        statement.setInt(key, (Integer) value);
                    } else if (value instanceof String) {
                        statement.setString(key, String.valueOf(value));
                    } else if (value instanceof Date) {
                        statement.setDate(key, (Date) value);
                    } else if (value instanceof Double) {
                        statement.setDouble(key, (Double) value);
                    } else if (value instanceof Boolean) {
                        statement.setBoolean(key, (Boolean) value);
                    } else if (value instanceof Float) {
                        statement.setFloat(key, (Float) value);
                    } else if (value instanceof LocalDate) {
                        statement.setDate(key, DateUtils.localDateToSqlDate((LocalDate) value));
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            });
            statement.execute();
        } catch (SQLException | InterruptedException err) {
            System.out.println(err.getMessage());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
    }

    /**
     * runs query for getting results (ResultSet) by prepared query (String)
     *
     * @param query String
     * @return ResultSet
     */
    public static Resultset runQueryForResult(String query) {
        Resultset resultset = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            resultset = (Resultset) statement.executeQuery();
        } catch (SQLException | InterruptedException err) {
            System.out.println(err.getMessage());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);

        }
        return resultset;
    }

    /**
     * runs query with keys for getting results (ResultSet) by prepared query (String)
     * and map with params
     *
     * @param query  String
     * @param params Map of Integer and Object
     * @return ResultSet
     */
    public static Resultset runQueryForResult(String query, Map<Integer, Object> params) {
        Resultset resultset = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            params.forEach((key, value) -> {
                try {
                    if (value instanceof Integer) {
                        statement.setInt(key, (Integer) value);
                    } else if (value instanceof String) {
                        statement.setString(key, String.valueOf(value));
                    } else if (value instanceof Date) {
                        statement.setDate(key, (Date) value);
                    } else if (value instanceof Double) {
                        statement.setDouble(key, (Double) value);
                    } else if (value instanceof Boolean) {
                        statement.setBoolean(key, (Boolean) value);
                    } else if (value instanceof Float) {
                        statement.setFloat(key, (Float) value);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            });
            resultset = (Resultset) statement.executeQuery();
        } catch (SQLException | InterruptedException err) {
            System.out.println(err.getMessage());
        } finally {
            ConnectionPool.getInstance().restoreConnection(connection);
        }
        return resultset;
    }

    /**
     * checks if exists by result set
     *
     * @param resultSet ResultSet
     * @return boolean
     */
    public static boolean checkExistence(ResultSet resultSet) {
        boolean exists = false;

        try {
            //makes "exists" return the same result as the query in DB
            while (resultSet.next()) {
                exists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return exists;
    }
}
