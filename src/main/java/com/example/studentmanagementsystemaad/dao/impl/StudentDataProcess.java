package com.example.studentmanagementsystemaad.dao.impl;

import java.sql.Connection;

public class StudentDataProcess {

    static String SAVE_STUDENT = "INSERT INTO students (id, name, email, city, level) VALUES(?,?,?,?,?)";
    static String GET_STUDENT = "SELECT * FROM students WHERE id=?";
    static String UPDATE_STUDENT = "UPDATE student SET name = ?, city = ?, email = ?, level = ? WHERE id = ?";
    static String DELETE_STUDENT = "DELETE FROM student WHERE id=?";
}
