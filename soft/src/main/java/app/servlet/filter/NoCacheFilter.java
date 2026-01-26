package app.servlet.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoCacheFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof HttpServletResponse resp) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setDateHeader("Expires", 0);
        }
        chain.doFilter(request, response);
    }
}
