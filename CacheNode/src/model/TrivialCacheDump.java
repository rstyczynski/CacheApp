package model;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class TrivialCacheDump {
    public TrivialCacheDump() {
        super();
    }
    
    
    public List<TrivialDataRecord> getDataFromCache(){
        
        
          NamedCache cache = CacheFactory.getCache("trivialCache");
          
          List<TrivialDataRecord> records = new ArrayList<TrivialDataRecord>(20);

          Iterator<Map.Entry<String, TrivialDataRecord>> it;
          it = cache.entrySet().iterator();

          while(it.hasNext()){
             records.add(it.next().getValue());
          }
          
          return records;
      }
    
}
