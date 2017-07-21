package model;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;
import java.io.Serializable;


@SuppressWarnings("oracle.jdeveloper.java.serialversionuid-field-missing")
public class TrivialRecord implements Serializable, PortableObject {
    
    private String Liczba;
    
    public TrivialRecord(){
        super();
        System.out.println(">>>>>TrivialRecord serializer initialized. Done.");
    }

    public TrivialRecord(String cityName) {
        this.Liczba = cityName;
        System.out.println(">>>>>TrivialRecord serializer initialized. Done.");
    }
    

    public void setCityName(String cityName) {
        this.Liczba = cityName;
    }

    public String getCityName() {
        return Liczba;
    }

    @Override
    public String toString() {
        return Liczba;
    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeString(0, Liczba);
        
        System.out.println(">>>>>TrivialRecord.writeExternal. Done.");
    }
    
    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        Liczba = pofReader.readString(0);
        
        System.out.println(">>>>>TrivialRecord.readExternal. Done.");
    }


}
