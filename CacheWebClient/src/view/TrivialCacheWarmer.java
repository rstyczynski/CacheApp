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
        out.println("<p>Filling the cache.</p>");

        NamedCache cache = CacheFactory.getCache("trivialCache");
        cache.put("1", new TrivialRecord("Raz"));
        cache.put("2", new TrivialRecord("Dwa"));
        cache.put("3", new TrivialRecord("Trzy"));
        cache.put("4", new TrivialRecord("Cztery"));
        cache.put("5", new TrivialRecord("Pięć"));
        cache.put("6", new TrivialRecord("Sześć"));
        cache.put("7", new TrivialRecord("Siedem"));

        out.println("<p>Done.</p>");

        out.println("</body>");
        out.println("</html>");
        out.close();
    }


}
