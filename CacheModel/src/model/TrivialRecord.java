package model;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.*;


public class TrivialRecord implements Serializable, PortableObject {
    
    private static final long serialVersionUID = 0xc877ca7fL;
    private String value;
    
    public TrivialRecord(){
        super();
    }

    public TrivialRecord(String _value) {
        this.value = _value;
        System.out.println(">>>>>Serializer initialized. Done.");
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
        
        System.out.println(">>>>>Record.writeExternal. Done.");
    }
    
    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        value = pofReader.readString(0);
        
        System.out.println(">>>>>Record.readExternal. Done.");
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
