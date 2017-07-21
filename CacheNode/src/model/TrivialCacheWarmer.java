package model;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class TrivialCacheWarmer {
    public TrivialCacheWarmer() {
        super();
    }
    
    
    public void fillCacheWithSampleData(){
        NamedCache nc= CacheFactory.getCache("trivialCache");
        nc.put("1",new TrivialDataRecord("Arsenal"));
        nc.put("2",new TrivialDataRecord("Aston Villa"));
        nc.put("3",new TrivialDataRecord("Burnley"));
        nc.put("4",new TrivialDataRecord("Chelsea"));
        nc.put("5",new TrivialDataRecord("Crystal Palace"));
        nc.put("6",new TrivialDataRecord("Everton"));
        nc.put("7",new TrivialDataRecord("Hull City"));
        nc.put("8",new TrivialDataRecord("Leicester City"));
        nc.put("9",new TrivialDataRecord("Liverpool"));
    }  
}
