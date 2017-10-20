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
    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        CacheFactory.ensureCluster();
        NamedCache cache = CacheFactory.getCache("trivialCache");
	TrivialRecord record = new TrivialRecord("0A - Zero A - Zero A");
	try {	
		System.out.println(">>>>>Put" + record);
        	cache.put("0A", record) ;
	} catch (Exception e) {
		System.out.println(">>>>>Put (retry)" + record);
		System.out.println(">>>>>Put retry reason:" + e);
		cache.put("0A", record);
	} 
        
        cache.release();
	
    }


    @SuppressWarnings("unchecked")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Cache reader</title></head>");
        out.println("<body>");
        out.println("<p/>");

        CacheFactory.ensureCluster();
        NamedCache cache = CacheFactory.getCache("trivialCache");

        Iterator<Map.Entry<String, TrivialRecord>> it;
        it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Object value;
            Map.Entry entry = it.next();
            try {       
                System.out.println(">>>>>Get");
                value = entry.getValue();
            } catch (Exception e) {
                System.out.println(">>>>>Get (retry)");
                System.out.println(">>>>>Get retry reason:" + e);
                value = entry.getValue();
            } 

            out.println(value + "->" + value.getClass().getClassLoader() );
            out.println("</br>");
        }

	out.println("<h1>Cloned</h1>");
        it = cache.entrySet().iterator();
        while (it.hasNext()) {
            TrivialRecord value = TrivialRecord.clone(it.next().getValue());

            out.println(value + "->" + value.getClass().getClassLoader() );
            out.println("</br>");
        }


        cache.release();

        out.println("</body>");
        out.println("</html>");
        out.close();
    }


}
