package com.sp.web.book.board.movie.controller;

import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Log
public class JDBCTests {

    static {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }


    public void testConnection() throws SQLException {
        try (Connection con =
                DriverManager.getConnection(
                        " jdbc:mysql://127.0.0.1:3306/testDB",
                        "root",
                        "4855sksms"
                )){
            System.out.println(con);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
