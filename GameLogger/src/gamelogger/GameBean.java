/**
 * Game Bean is a java Bean for table and whole project. Game Bean make working with attributes easier.
 * The bean contains id, name, date, duration attributes
 * duration should formatted for reading easily, because the duration saved as minutes, for larger values, the formatter converts it to hours and minutes.
 */
package gamelogger;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author hawdi
 */
public class GameBean {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty date;
    private SimpleStringProperty duration;
    public static Map<String, Integer> formattedValues = new HashMap<>();
    
    public GameBean(int id, String name, int duration, String date){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleStringProperty(formattedDuration(duration));
        this.date = new SimpleStringProperty(date);
    }
    
    public GameBean(String name, int duration){
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleStringProperty(String.valueOf(duration));
    }
    
    /**
     * 
     * @param id is id of the record
     * @param name of the record
     */
    public GameBean(int id, String name){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }
    /**
     * 
     * @return Name of the bean
     */
    public String getName(){
        return name.get();
    }
    /**
     * 
     * @return Duration of the bean, a formatted string
     */
    public String getDuration(){
        return duration.get();
    }
    /**
     * 
     * @return Integer value of Duration, without format
     */
    public int getIntegerDuration(){
        return Integer.parseInt(duration.get());
    }
    /**
     * 
     * @return Date of the bean saved in database
     */
    public String getDate(){
        return date.get();
    }
    /**
     * 
     * @return ID of the bean
     */
    public Integer getId(){
        return id.get();
    }
    /**
     * 
     * @param duration as a parameter to format it to formatted String, change minutes to hours. e.g. 150 minutes -> <b>2 hours & 30 minutes</b>
     * @return 
     */
    private String formattedDuration(int duration){
        String formattedDuration;
        if(duration <= 0)
            formattedDuration = "0";
        else if(duration < 60)
            formattedDuration = duration + " minutes";
        else{
            int result = duration / 60;
            int remains = duration % 60;
            if(remains != 0)
                formattedDuration = result + " hour(s) & " + remains + " minutes";
            else
                formattedDuration = result + " hour(s)";
        }
        formattedValues.put(formattedDuration, duration);
        return formattedDuration;
    }
}
