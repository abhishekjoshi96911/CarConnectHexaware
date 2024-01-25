package util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import exception.InvalidInputException;

public class Dateutil {
	 String inputDate ;
     String expectedFormat = "yyyy-MM-dd";
     String expectedYearFormat="yyyy";
    
    public Dateutil(String inputDate) {
		super();
		this.inputDate = inputDate;
	}



    public  boolean validateDateFormat() throws InvalidInputException {
        SimpleDateFormat sdf = new SimpleDateFormat(expectedFormat);
        sdf.setLenient(false); // Disable lenient parsing to enforce strict validation

        try {
            Date parsedDate = sdf.parse(inputDate);
            if (!inputDate.equals(sdf.format(parsedDate))) {
               return false;
            }
            else {
            	return true;
            }
        
        }catch(ParseException e) {
        	return false;
    }
}
    public  boolean validateYearFormat() throws InvalidInputException {
        SimpleDateFormat sdf = new SimpleDateFormat(expectedYearFormat);
        sdf.setLenient(false); // Disable lenient parsing to enforce strict validation

        try {
            Date parsedDate = sdf.parse(inputDate);
            if (!inputDate.equals(sdf.format(parsedDate))) {
               return false;
            }
            else {
            	return true;
            }
        
        }catch(ParseException e) {
        	return false;
    }
}

    
}


