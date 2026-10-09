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
import java.util.Set;

/** POST /api/complaints/status  (params: id, status) */
@WebServlet(urlPatterns = "/api/complaints/status")
public class UpdateStatusServlet extends HttpServlet {

    private static final Set<String> ALLOWED = Set.of("PENDING", "IN_PROGRESS", "RESOLVED", "REJECTED");

    @Autowired
    private ComplaintDao dao;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String status = req.getParameter("status");
            if (status == null || !ALLOWED.contains(status)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                mapper.writeValue(resp.getWriter(), Map.of("error", "Invalid status"));
                return;
            }
            boolean ok = dao.updateStatus(id, status);
            if (!ok) resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            mapper.writeValue(resp.getWriter(), Map.of("updated", ok));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Invalid id"));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
