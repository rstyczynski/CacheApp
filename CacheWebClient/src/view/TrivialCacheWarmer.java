package view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TrivialCacheWarmer extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType(CONTENT_TYPE);
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head><title>Cache warmer</title></head>");
            out.println("<body>");
            out.println("<p>Filling the cache.</p>");
            
            new model.TrivialCacheWarmer().fillCacheWithSampleData();
            
            out.println("<p>Done.</p>");
            
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    
    
}
