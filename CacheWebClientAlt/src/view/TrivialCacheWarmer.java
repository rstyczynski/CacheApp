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
        cache.put("1A", new TrivialRecord("1A - Raz - One"));
        cache.put("2A", new TrivialRecord("2A - Dwa - Two"));
        cache.put("3A", new TrivialRecord("3A - Trzy - Three"));
        cache.put("4A", new TrivialRecord("4A - Cztery - Four"));
        cache.put("5A", new TrivialRecord("5A - Pięć - Five"));
        cache.put("6A", new TrivialRecord("6A - Sześć - Six"));
        cache.put("7A", new TrivialRecord("7A - Siedem - Seven"));

        out.println("<p>Done.</p>");

        out.println("</body>");
        out.println("</html>");
        out.close();
    }


}
