package app.db;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//@WebServlet("/workers")
public class WorkerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h2>Worker List</h2>");

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM worker")) {

            out.println("<table border='1'>");
            out.println("<tr><th>WorkerID</th><th>Authority</th><th>Monthly</th><th>Total</th></tr>");

            while (rs.next()) {
                String workerID = rs.getString("workerID");
                boolean authority = rs.getBoolean("has_authority");
                int monthly = rs.getInt("monthly_work_minutes");
                int total = rs.getInt("total_work_minutes");

                out.println("<tr>");
                out.println("<td>" + workerID + "</td>");
                out.println("<td>" + authority + "</td>");
                out.println("<td>" + monthly + "</td>");
                out.println("<td>" + total + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");

        } catch (SQLException e) {
            out.println("<p style='color:red;'>DB接続エラー: " + e.getMessage() + "</p>");
        }

        out.println("</body></html>");
    }
}