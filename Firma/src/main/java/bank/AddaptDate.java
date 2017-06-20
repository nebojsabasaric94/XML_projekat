package bank;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;



public class AddaptDate extends XmlAdapter<String, Date> {

    public Date unmarshal(String value) {
    	java.util.Date utilDate = MyDatatypeConverter.parseDate(value);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
    	
        return sqlDate;
    }

    public String marshal(Date value) {
        return (MyDatatypeConverter.printDate(value));
    }
}
