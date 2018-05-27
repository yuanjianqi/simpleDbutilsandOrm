package com.yuan.utils;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.sql.*;

/**
 * All Rights Reserved, Designed By Iliya Kaslana
 *
 * @author Iliya Kaslana
 * @version 1.0
 * @date 2018/5/26 23:25
 * @copyright ©2018
 */
public class Dbutils {

    public Dbutils() {
    }

    public static void close(Connection connection) throws SQLException {
        if (connection != null){
            connection.close();
        }

    }

    public static void close(Statement statement) throws SQLException{
        if (statement != null){
            statement.close();
        }
    }

    public static void close(ResultSet resultSet) throws SQLException{
        if (resultSet != null){
            resultSet.close();
        }
    }

    public static void close(Connection connection, Statement statement) throws SQLException{
        close(connection);
        close(statement);
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) throws SQLException{
        close(connection);
        close(statement);
        close(resultSet);
    }

    public static void closeSilently(Connection connection) {
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void closeSilently(Statement statement){
        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeSilently(ResultSet resultSet) {
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeSilently(Connection connection, Statement statement) {
        closeSilently(connection);
        closeSilently(statement);
    }

    public static void closeSilently(Connection connection, Statement statement, ResultSet resultSet) {
        closeSilently(connection);
        closeSilently(statement);
        closeSilently(resultSet);
    }



    public static boolean loadDriverClass(String driverClass){
        return loadDriverClass(Dbutils.class.getClassLoader(), driverClass);
    }

    /**
     * 加载数据库的驱动程序
     * @param classLoader
     * @param driverClass
     * @return 代表驱动是否加载成功
     */
    private static boolean loadDriverClass(ClassLoader classLoader, String driverClass) {
        try {
            Class<?> currentClass = classLoader.loadClass(driverClass);
            //判断一下driverClass是不是驱动程序，即Driver的实现类
            if (!Driver.class.isAssignableFrom(currentClass)){
                return false;
            }
            Constructor<Driver> constructor = (Constructor<Driver>) currentClass.getConstructor();

            //判断一下构造器的状态
            boolean isConstructorAccessible = constructor.isAccessible();
            if (!isConstructorAccessible){
                constructor.setAccessible(true);

            }

            try {
                Driver driver = constructor.newInstance();
                DriverManager.registerDriver(driver);
            } finally {
                constructor.setAccessible(isConstructorAccessible);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    /**
     * 打印SQLException的堆栈信息
     * @param exception
     */
    public static void printStrackTrace(Exception exception){
        printStrackTrace(exception, new PrintWriter(System.err));
    }

    private static void printStrackTrace(Exception exception, PrintWriter printWriter) {
        if (exception != null){
            printWriter.println("This is the Exception StackTrace");
            exception.printStackTrace(printWriter);
        }

    }

    public static void printWarnnings(Connection connection){
        printWarnnings(connection, new PrintWriter(System.err));
    }

    private static void printWarnnings(Connection connection, PrintWriter printWriter) {
        if (connection != null){
            try {
                printStrackTrace(connection.getWarnings(), printWriter);
            } catch (SQLException e) {
                printStrackTrace(e, printWriter);
            }
        }

    }

    /**
     * 以下是事务处理的工具方法
     */
    public static void beginTransaction(Connection connection) throws SQLException,NullPointerException {
        if (connection != null){
            connection.setAutoCommit(false);
        }else{
            throw new NullPointerException("Connection Pointer is null");
        }
    }

    public static void commitAndCloseSilently(Connection connection){
        if (connection != null){
            try {
                connection.commit();
            } catch (SQLException e) {
                printStrackTrace(e);
            }finally {
                closeSilently(connection);
            }
        }
    }

    public static void rollbackAndCloseSilently(Connection connection){
        if (connection != null){
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                closeSilently(connection);
            }
        }
    }



}
