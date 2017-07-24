package logic;

import com.tangosol.util.MapEvent;
import com.tangosol.util.MapListener;

import javax.annotation.PostConstruct;


public class TrivialListener implements MapListener {

    @Override
    public void entryDeleted(MapEvent mapEvent) {
        System.out.println(">>>>>TrivialListener. Delete:" + mapEvent);
    }

    @Override
    public void entryInserted(MapEvent mapEvent) {
        System.out.println(">>>>>TrivialListener. Insert:" + mapEvent);
    }

    @Override
    public void entryUpdated(MapEvent mapEvent) {
        System.out.println(">>>>>TrivialListener. Update:" + mapEvent);
    }


    @PostConstruct
    public void postConstruct() {
       System.out.println(">>>>>TrivialListener. Initialized by postConstruct.");
    }
    
    public void init() {
       System.out.println(">>>>>TrivialListener. Initialized by Spring.");
    }
    
}