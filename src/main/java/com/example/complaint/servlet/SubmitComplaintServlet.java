package com.example.complaint.servlet;

import com.example.complaint.dao.ComplaintDao;
import com.example.complaint.model.Complaint;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

/** POST /api/complaints/submit  - creates a new complaint. */
@WebServlet(urlPatterns = "/api/complaints/submit")
public class SubmitComplaintServlet extends HttpServlet {

    @Autowired
    private ComplaintDao dao;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String name = trim(req.getParameter("name"));
        String email = trim(req.getParameter("email"));
        String category = trim(req.getParameter("category"));
        String subject = trim(req.getParameter("subject"));
        String description = trim(req.getParameter("description"));

        if (name.isEmpty() || email.isEmpty() || category.isEmpty()
                || subject.isEmpty() || description.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of("error", "All fields are required"));
            return;
        }

        try {
            Complaint c = new Complaint();
            c.setName(name);
            c.setEmail(email);
            c.setCategory(category);
            c.setSubject(subject);
            c.setDescription(description);
            int id = dao.save(c);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(resp.getWriter(), Map.of("id", id, "message", "Complaint submitted"));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
