package view;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.TrivialDataRecord;

public class TrivialCacheDisplay extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        
        
        List <TrivialDataRecord> teams= new model.TrivialCacheDump().getDataFromCache();
        
        out.println("<html>");
        out.println("<head><title>Cache reader</title></head>");
        out.println("<body>");
        out.println("<p/>");
        out.println("<table>");

        for(int i=0; i<teams.size();i++){
            out.println("<tr><td>");
            out.println(i+1);
            out.println("</td>");
            out.println("<td>");
            out.println(teams.get(i));
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
