package model;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;
import java.io.Serializable;


public class TrivialDataRecord implements Serializable, PortableObject {
    
    private String cityName;
    
    public TrivialDataRecord(){
        super();
    }

    public TrivialDataRecord(String cityName) {
        this.cityName = cityName;
        System.out.println(">>>>>Serializer initialized. Done.");
    }
    

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeString(0, cityName);
        
        System.out.println(">>>>>Record.writeExternal. Done.");
    }
    
    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        cityName = pofReader.readString(0);
        //cityName = "WARSAW";
        
        System.out.println(">>>>>Record.readExternal. Done.");
    }


}
