package app.servlet.owner;

import app.dao.RequestDao;
import app.dao.RequestDao.RequestRow;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/owner/help")
public class OwnerHelpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestDao dao = new RequestDao();

            // requestテーブルの一覧（= 承認待ち一覧）
            List<RequestRow> list = dao.listAll();

            // ★JSPが見てる属性名は rows なので、ここを rows に合わせる
            req.setAttribute("rows", list);

            req.getRequestDispatcher("/WEB-INF/jsp/owner/help_list.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
