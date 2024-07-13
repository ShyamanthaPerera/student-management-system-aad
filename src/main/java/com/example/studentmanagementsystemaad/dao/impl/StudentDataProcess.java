package com.example.studentmanagementsystemaad.dao.impl;

import com.example.studentmanagementsystemaad.dao.StudentData;
import com.example.studentmanagementsystemaad.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public class StudentDataProcess implements StudentData {

    static String SAVE_STUDENT = "INSERT INTO students (id, name, email, city, level) VALUES(?,?,?,?,?)";
    static String GET_STUDENT = "SELECT * FROM students WHERE id=?";
    static String UPDATE_STUDENT = "UPDATE student SET name = ?, city = ?, email = ?, level = ? WHERE id = ?";
    static String DELETE_STUDENT = "DELETE FROM student WHERE id=?";

    @Override
    public StudentDTO getStudent(String studentId, Connection connection) throws SQLException {
        return null;
    }

    @Override
    public String saveStudent(StudentDTO studentDTO, Connection connection) {
        return null;
    }

    @Override
    public boolean deleteStudent(String studentId, Connection connection) {
        return false;
    }

    @Override
    public boolean updateStudent(String studentId, StudentDTO student, Connection connection) {
        return false;
    }
}
