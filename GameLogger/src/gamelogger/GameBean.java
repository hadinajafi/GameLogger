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
    
    public String getName(){
        return name.get();
    }
    
    public String getDuration(){
        return duration.get();
    }
    
    public int getIntegerDuration(){
        return Integer.parseInt(duration.get());
    }
    
    public String getDate(){
        return date.get();
    }
    
    public Integer getId(){
        return id.get();
    }
    
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
