package com.example.complaint.servlet;

import com.example.complaint.dao.ComplaintDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

/** POST /api/complaints/delete  (param: id) */
@WebServlet(urlPatterns = "/api/complaints/delete")
public class DeleteComplaintServlet extends HttpServlet {

    @Autowired
    private ComplaintDao dao;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            boolean ok = dao.delete(id);
            if (!ok) resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            mapper.writeValue(resp.getWriter(), Map.of("deleted", ok));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Invalid id"));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
