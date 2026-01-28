package controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.time.LocalDate;

import app.repository.OptionRepository;
import app.service.OptionLoader;
import app.domain.Option;

public class ShiftSettingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/setting.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int generateDays = Integer.parseInt(req.getParameter("generateDays"));
        int maxMonth = Integer.parseInt(req.getParameter("maxWorkMonth"));
        int maxDay = Integer.parseInt(req.getParameter("maxWorkDay"));
        int newcomer = Integer.parseInt(req.getParameter("newcomerMinutes"));
        int senior = Integer.parseInt(req.getParameter("seniorRequired"));
        LocalDate firstDate = LocalDate.parse(req.getParameter("firstDate"));

        OptionLoader loader = new OptionLoader(new OptionRepository());
        Option current = loader.load(); // 既存行取得

        Option updated = new Option(
                maxMonth,
                maxDay,
                newcomer,
                senior,
                generateDays,
                firstDate
        );

        loader.save(updated);

        System.out.println("条件設定DB保存完了");

        resp.sendRedirect("confirm.jsp");
    }
}
