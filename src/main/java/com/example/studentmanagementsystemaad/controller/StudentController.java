package com.example.studentmanagementsystemaad.controller;

import com.example.studentmanagementsystemaad.dto.StudentDTO;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(urlPatterns = "/student")
public class StudentController extends HttpServlet {

    Connection connection;

    static String SAVE_STUDENT = "INSERT INTO students (id, name, email, city, level) VALUES(?,?,?,?,?)";
    static String GET_STUDENT = "SELECT * FROM students WHERE id=?";
    static String UPDATE_STUDENT = "UPDATE students SET name = ?, city = ?, email = ?, level = ? WHERE id = ?";
    static String DELETE_STUDENT = "DELETE FROM students WHERE id=?";

    @Override
    public void init() throws ServletException {
        try {
            var driverClass = getServletContext().getInitParameter("driver-class");
            var dbUrl = getServletContext().getInitParameter("dbURL");
            var userName = getServletContext().getInitParameter("dbUsername");
            var password = getServletContext().getInitParameter("dbPassword");
            Class.forName(driverClass);
            System.out.println(dbUrl+userName+password);
            this.connection = DriverManager.getConnection(dbUrl, userName, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var studentDTO = new StudentDTO();
        var studentId = req.getParameter("id");

        try (var writer = resp.getWriter()){
            var ps = connection.prepareStatement(GET_STUDENT);
            ps.setString(1, studentId);
            var resultSet = ps.executeQuery();
            while (resultSet.next()){
                studentDTO.setId(resultSet.getString("id"));
                studentDTO.setName(resultSet.getString("name"));
                studentDTO.setCity(resultSet.getString("city"));
                studentDTO.setEmail(resultSet.getString("email"));
                studentDTO.setLevel(resultSet.getString("level"));
            }
            System.out.println(studentDTO);
            resp.setContentType("application/json");
            var jsonb = JsonbBuilder.create();
            jsonb.toJson(studentDTO,writer);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getContentType().toLowerCase().startsWith("application/json")||req.getContentType() == null){
            //send error
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        /*----Jsonb----*/
        String id = UUID.randomUUID().toString();
        Jsonb jsonb = JsonbBuilder.create();
        StudentDTO studentDTO = jsonb.fromJson(req.getReader(), StudentDTO.class);
        studentDTO.setId(id);
        System.out.println(studentDTO);

        try {
            var ps = connection.prepareStatement(SAVE_STUDENT);
            ps.setString(1, studentDTO.getId());
            ps.setString(2, studentDTO.getName());
            ps.setString(3, studentDTO.getEmail());
            ps.setString(4, studentDTO.getCity());
            ps.setString(5, studentDTO.getLevel());
            if(ps.executeUpdate() != 0 ){
                resp.getWriter().write("Student Saved");
            } else {
                resp.getWriter().write("Student Not Saved");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(!req.getContentType().toLowerCase().startsWith("application/json")|| req.getContentType() == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        try (var writer = resp.getWriter()){
            var ps = this.connection.prepareStatement(UPDATE_STUDENT);
            var studentID = req.getParameter("id");
            Jsonb jsonb = JsonbBuilder.create();
            var updatedStudent = jsonb.fromJson(req.getReader(), StudentDTO.class);
            ps.setString(1, updatedStudent.getName());
            ps.setString(2, updatedStudent.getCity());
            ps.setString(3, updatedStudent.getEmail());
            ps.setString(4, updatedStudent.getLevel());
            ps.setString(5, studentID);
            if(ps.executeUpdate() != 0){
                System.out.println("Updated");
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else {
                writer.write("Update Failed");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var stuId = req.getParameter("id");
        try (var writer = resp.getWriter()){
            var ps = this.connection.prepareStatement(DELETE_STUDENT);
            ps.setString(1, stuId);
            if(ps.executeUpdate() != 0){
                System.out.println("Deleted");
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else {
                writer.write("Delete Failed");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }
}
