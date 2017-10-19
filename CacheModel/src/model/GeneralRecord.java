package model;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class GeneralRecord  implements Serializable, PortableObject {
    
    private static final long serialVersionUID = 0xc877ca70L;
    private String value;
    


    public GeneralRecord(String _value) {
        this.value = _value;
        System.out.println(">>>>>GeneralRecord.Serializer initialized. Done.");
    }
    

    public void setValue(String _value) {
        this.value = _value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeString(0, value);
        
        System.out.println(">>>>>GeneralRecord.writeExternal. Done.");
    }
    
    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        value = pofReader.readString(0);
        
        System.out.println(">>>>>GeneralRecord.readExternal. Done.");
    }

    @SuppressWarnings("unchecked")
    public static <T> T clone(final T source) {
        if (source == null) {
            return null;
        }
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(source);

            final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            final ObjectInputStream ois = new ObjectInputStream(bais);
            final T result = (T)ois.readObject();
            return result;
        } catch (final IOException e) {
            System.out.println("Error cloning object [{}]. " + source != null ? source.getClass().getSimpleName() : "null" + e);
            return null;
        } catch (final ClassNotFoundException e) {
            System.out.println("Error cloning object [{}]. " + source != null ? source.getClass().getSimpleName() : "null" + e);
            return null;
        } catch (final Exception e) {
            System.out.println("Error cloning object [{}]. " + source != null ? source.getClass().getSimpleName() : "null" + e);
            return null;
        }
    }


    }

