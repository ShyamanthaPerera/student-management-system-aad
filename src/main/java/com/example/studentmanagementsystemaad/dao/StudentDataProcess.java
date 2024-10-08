package com.example.studentmanagementsystemaad.dao;

import com.example.studentmanagementsystemaad.dao.StudentData;
import com.example.studentmanagementsystemaad.dto.StudentDTO;

import java.sql.Connection;
import java.sql.SQLException;

public final class StudentDataProcess implements StudentData {

    static String SAVE_STUDENT = "INSERT INTO students (id, name, email, city, level) VALUES(?,?,?,?,?)";
    static String GET_STUDENT = "SELECT * FROM students WHERE id=?";
    static String UPDATE_STUDENT = "UPDATE students SET name = ?, city = ?, email = ?, level = ? WHERE id = ?";
    static String DELETE_STUDENT = "DELETE FROM students WHERE id=?";

    @Override
    public StudentDTO getStudent(String studentId, Connection connection) throws SQLException {
        var studentDTO = new StudentDTO();
        try {
            var ps = connection.prepareStatement(GET_STUDENT);
            ps.setString(1, studentId);
            var resultSet = ps.executeQuery();
            while (resultSet.next()) {
                studentDTO.setId(resultSet.getString("id"));
                studentDTO.setName(resultSet.getString("name"));
                studentDTO.setCity(resultSet.getString("city"));
                studentDTO.setEmail(resultSet.getString("email"));
                studentDTO.setLevel(resultSet.getString("level"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return studentDTO;
    }

    @Override
    public boolean saveStudent(StudentDTO studentDTO, Connection connection) {
        try {
            var ps = connection.prepareStatement(SAVE_STUDENT);
            ps.setString(1, studentDTO.getId());
            ps.setString(2, studentDTO.getName());
            ps.setString(3, studentDTO.getEmail());
            ps.setString(4, studentDTO.getCity());
            ps.setString(5, studentDTO.getLevel());
            return ps.executeUpdate() != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public boolean deleteStudent(String studentId, Connection connection) {
        try {
            var ps = connection.prepareStatement(DELETE_STUDENT);
            ps.setString(1, studentId);
            return ps.executeUpdate() != 0;
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public boolean updateStudent(String studentId, StudentDTO updatedStudent, Connection connection) {
        try {
            var ps = connection.prepareStatement(UPDATE_STUDENT);
            ps.setString(1, updatedStudent.getName());
            ps.setString(2, updatedStudent.getCity());
            ps.setString(3, updatedStudent.getEmail());
            ps.setString(4, updatedStudent.getLevel());
            ps.setString(5, studentId);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
