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

/**
 * GET /api/complaints            -> all complaints
 * GET /api/complaints?status=X   -> filtered by status
 * GET /api/complaints?id=5       -> a single complaint
 */
@WebServlet(urlPatterns = "/api/complaints")
public class ListComplaintsServlet extends HttpServlet {

    @Autowired
    private ComplaintDao dao;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            String idParam = req.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                var c = dao.findById(Integer.parseInt(idParam.trim()));
                if (c == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    mapper.writeValue(resp.getWriter(), Map.of("error", "Complaint not found"));
                } else {
                    mapper.writeValue(resp.getWriter(), c);
                }
                return;
            }
            mapper.writeValue(resp.getWriter(), dao.findAll(req.getParameter("status")));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Invalid id"));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
