package model;

import com.tangosol.io.pof.PortableObject;

import java.io.Serializable;


public class SubElementRecord extends GeneralRecord implements Serializable, PortableObject {

    private static final long serialVersionUID = 0xc877ca71L;
    private String valueSub;
    


    public SubElementRecord(String _value) {
        super(_value);
        
        this.valueSub = _value;
        System.out.println(">>>>>SubelementRecord.Serializer initialized. Done.");

    }
    

    public void setValueSub(String _value) {
        this.valueSub = _value;
    }

    public String getValueSub() {
        return valueSub;
    }

    @Override
    public String toString() {
        return valueSub;
    }



}
