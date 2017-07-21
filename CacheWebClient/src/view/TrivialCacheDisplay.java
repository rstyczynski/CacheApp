package view;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.TrivialRecord;

public class TrivialCacheDisplay extends HttpServlet {
    @SuppressWarnings("compatibility:3309922168865781288")
    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }


    @SuppressWarnings("unchecked")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Cache reader</title></head>");
        out.println("<body>");
        out.println("<p/>");

        NamedCache cache = CacheFactory.getCache("trivialCache");

        Iterator<Map.Entry<String, TrivialRecord>> it;
        it = cache.entrySet().iterator();
        while (it.hasNext()) {
            out.println(it.next().getValue());
            out.println("</br>");
        }

        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
