package com.generic.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility methods for JDBC database interaction.
 * <p>Provides connection management, query execution, and result set navigation helpers.
 */
@Slf4j
public final class DataBaseUtils {

    private DataBaseUtils(){
    }

    private static Connection con;
    private static Statement stm;
    private static ResultSet rs;
    private static ResultSetMetaData rsmd;

    /** Opens DB connection with given credentials. */
    public static void createConnection(String url, String user, String pass){
        try{
            con = DriverManager.getConnection(url, user, pass);
        }catch (SQLException e){
            log.warn("Connection failed.{}", e.getMessage());
        }
    }

    /** Opens DB connection using EnvConfig properties. */
    public static void createConnection(){
        String url = "";
        String user = "";
        String pass = "";
        createConnection(url, user, pass);
    }

    /** Executes SQL query and stores ResultSet/metadata. */
    public static ResultSet runQuery(String sql){
        try {
            stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
            rsmd = rs.getMetaData();
        }catch(SQLException e){
            log.warn("Query failed.{}", e.getMessage());
        }
        return rs;
    }

    /** Closes result set, statement, and connection if open. */
    public static void destroy(){
        try{
            if(rs != null) rs.close();
            if(stm != null) stm.close();
            if(con != null) con.close();
        }catch (SQLException e){
            log.warn("ERROR OCCURRED WHILE CLOSING RESOURCES {}", e.getMessage());
        }
    }

    /** Resets cursor to before first row. */
    private static void resetCursor(){
        try{
            rs.beforeFirst();
        }catch(SQLException e){
            log.warn(e.getMessage());
        }
    }

    /** @return total number of rows in current result set. */
    public static int getRowCount(){
        int count = 0;
        try{
            rs.last();
            count = rs.getRow();
        } catch(SQLException e){
            log.warn(e.getMessage());
        } finally {
            resetCursor();
        }
        return count;
    }

    /** @return number of columns in current result set. */
    public static int getColumnCount(){
        int count = 0;
        try{
            count = rsmd.getColumnCount();
        }catch(SQLException e){
            log.warn(e.getMessage());
        }
        return count;
    }

    /** @return column names as list. */
    public static List<String> getColumnNamesAsList(){
        List<String> list = new ArrayList<>();
        try{
            for (int i = 1; i < getColumnCount(); i++) {
                list.add(rsmd.getColumnName(i));
            }
        }catch (SQLException e){
            log.warn(e.getMessage());
        }
        return list;
    }

    /** @return all values from given row as list. */
    public static List<String> getRowAsList(int rowNum){
        List<String> list = new ArrayList<>();
        try{
            rs.absolute(rowNum);
            for (int i = 1; i <= getColumnCount(); i++) {
                list.add(rs.getString(i));
            }
        }catch(SQLException e){
            log.warn(e.getMessage());
        } finally {
            resetCursor();
        }
        return list;
    }

    /** @return value from given row/column. */
    public static String getCellValue(int rowNum, int columnNum){
        String cellValue = "";
        try{
            rs.absolute(rowNum);
            cellValue = rs.getString(columnNum);
        } catch(SQLException e){
            log.warn(e.getMessage());
        } finally{
            resetCursor();
        }
        return cellValue;
    }

    /** @return first cell value (row 1, col 1). */
    public static String getFirstCell(){
        return getCellValue(1, 1);
    }

    /** @return all values from given column as list. */
    public static List<String> getColumnAsList(int colNum){
        List<String> list = new ArrayList<>();
        try{
            rs.beforeFirst();
            while(rs.next()){
                list.add(rs.getString(colNum));
            }
        } catch(SQLException e){
            log.warn(e.getMessage());
        }
        return list;
    }

    /** Prints all rows and columns of current result set. */
    public static void printAllTable(){
        resetCursor();
        try{
            while(rs.next()){
                for (int i = 1; i < getColumnCount(); i++) {
                    System.out.printf("%-25s", rs.getString(i));
                }
                System.out.println();
            }
        }catch(SQLException e){
            log.warn(e.getMessage());
        }
    }

    /** @return entire result set as list of maps (column→value). */
    public static List<Map<String, Object>> getAllDataAsMap(){
        List<Map<String, Object>> list = new ArrayList<>();
        resetCursor();
        try {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i < getColumnCount(); i++) {
                    map.put(rsmd.getColumnName(i), rs.getString(i));
                }
                list.add(map);
            }
        }catch(SQLException e){
            log.warn(e.getMessage());
        } finally {
            resetCursor();
        }
        return list;
    }
}
