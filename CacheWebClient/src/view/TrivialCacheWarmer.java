package view;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.TrivialRecord;

public class TrivialCacheWarmer extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        NamedCache cache = CacheFactory.getCache("trivialCache");
        cache.put("0", new TrivialRecord("0 - Zero - Zero"));
    }

    
    @SuppressWarnings("unchecked")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Cache warmer</title></head>");
        out.println("<body>");
        out.println("<p>Filling the cache</p>");

        NamedCache cache = CacheFactory.getCache("trivialCache");
        cache.put("1", new TrivialRecord("1 - Raz - One"));
        cache.put("2", new TrivialRecord("2 - Dwa - Two"));
        cache.put("3", new TrivialRecord("3 - Trzy - Three"));
        cache.put("4", new TrivialRecord("4 - Cztery - Four"));
        cache.put("5", new TrivialRecord("5 - Pięć - Five"));
        cache.put("6", new TrivialRecord("6 - Sześć - Six"));
        cache.put("7", new TrivialRecord("7 - Siedem - Seven"));

        out.println("<p>Done.</p>");

        out.println("</body>");
        out.println("</html>");
        out.close();
    }


}
