package com.example.studentmanagementsystemaad.dao;

import com.example.studentmanagementsystemaad.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public interface StudentData {
    StudentDTO getStudent(String studentId, Connection connection) throws SQLException;
    boolean saveStudent(StudentDTO studentDTO, Connection connection);
    boolean deleteStudent(String studentId,Connection connection);
    boolean updateStudent(String studentId,StudentDTO student,Connection connection);
}
