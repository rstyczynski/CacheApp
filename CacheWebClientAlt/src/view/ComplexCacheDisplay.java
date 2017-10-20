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

import model.ComplexRecord;

public class ComplexCacheDisplay extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        CacheFactory.ensureCluster();
        NamedCache cache = CacheFactory.getCache("complexCache");
	ComplexRecord record = new ComplexRecord("0A - Zero - Zero");
	try {	
		System.out.println(">>>>>ComplexCacheDisplay.Put" + record);
        	cache.put("0A", record) ;
	} catch (Exception e) {
		System.out.println(">>>>>ComplexCacheDisplay.Put (retry)" + record);
		System.out.println(">>>>>ComplexCacheDisplay.Put retry reason:" + e);
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
        NamedCache cache = CacheFactory.getCache("complexCache");

        Iterator<Map.Entry<String, ComplexRecord>> it;
        it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Object value;
            Map.Entry entry = it.next();
            try {       
                System.out.println(">>>>>ComplexCacheDisplay.Get");
                value = entry.getValue();
            } catch (Exception e) {
                System.out.println(">>>>>ComplexCacheDisplay.Get (retry)");
                System.out.println(">>>>>ComplexCacheDisplay.Get retry reason:" + e);
                value = entry.getValue();
            } 
            
            out.println(value + "->" + value.getClass().getClassLoader() );
            out.println("</br>");
        }

	out.println("<h1>Cloned</h1>");
        it = cache.entrySet().iterator();
        while (it.hasNext()) {
            ComplexRecord value = ComplexRecord.clone(it.next().getValue());

            out.println(value + "->" + value.getClass().getClassLoader() );
            out.println("</br>");
        }
        
        cache.release();

        out.println("</body>");
        out.println("</html>");
        out.close();
    }


}

