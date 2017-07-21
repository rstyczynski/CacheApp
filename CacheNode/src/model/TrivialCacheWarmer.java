package model;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

public class TrivialCacheWarmer {
    public TrivialCacheWarmer() {
        super();
    }
    
    
    public void fillCacheWithSampleData(){
        NamedCache nc= CacheFactory.getCache("trivialCache");
        nc.put("1",new TrivialRecord("Arsenal"));
        nc.put("2",new TrivialRecord("Aston Villa"));
        nc.put("3",new TrivialRecord("Burnley"));
        nc.put("4",new TrivialRecord("Chelsea"));
        nc.put("5",new TrivialRecord("Crystal Palace"));
        nc.put("6",new TrivialRecord("Everton"));
        nc.put("7",new TrivialRecord("Hull City"));
        nc.put("8",new TrivialRecord("Leicester City"));
        nc.put("9",new TrivialRecord("Liverpool"));
    }  
}
