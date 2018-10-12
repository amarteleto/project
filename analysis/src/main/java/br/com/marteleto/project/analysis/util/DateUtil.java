package  br.com.marteleto.project.analysis.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateUtil  implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static final transient Logger log = Logger.getLogger(DateUtil.class.getName());
	
	public static String convertDateToString(String format, Date date){
        String result = null;
        try{
        	SimpleDateFormat df = new SimpleDateFormat(format, ResourceUtil.getLocale());
        	df.setLenient(false);
        	result = df.format(date);
        } catch (Exception ex) {
        	log.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }
    
    public static Date convertStringToDate(String format, String date){
    	Date result = null;
        try{
        	SimpleDateFormat df = new SimpleDateFormat(format, ResourceUtil.getLocale());
        	df.setLenient(false);
        	result = df.parse(date);
        } catch (Exception ex) {
        	log.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }
    
    public static Date formatDate(String format, Date date){
    	Date result = null;
        try{
        	SimpleDateFormat df = new SimpleDateFormat(format, ResourceUtil.getLocale());
        	df.setLenient(false);
        	result = df.parse(df.format(date));
        } catch (Exception ex) {
        	log.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }
    
    public static Date currentDate(){
    	Date result = null;
        try{
        	result = Calendar.getInstance(ResourceUtil.getLocale()).getTime();
        } catch (Exception ex) {
        	log.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return result;
    }
    
    public static Long dateDiff(Date initialDate, Date finalDate) {
    	Long result = null;
    	try {
	        Calendar c1 = Calendar.getInstance(ResourceUtil.getLocale());  
	        c1.setTime(initialDate);
	        Calendar c2 = Calendar.getInstance(ResourceUtil.getLocale());  
	        if(finalDate!=null){
	        	c2.setTime(finalDate);	
	        }
	        result = (c2.getTimeInMillis() - c1.getTimeInMillis());
    	 } catch (Exception ex) {
    		 log.log(Level.SEVERE, ex.getMessage(), ex);
         }
    	return result;
    }
}