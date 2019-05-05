
package gamelogger;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author hawdi
 */
public class GameBean {
    private SimpleStringProperty name;
    private SimpleStringProperty date;
    private SimpleStringProperty duration;
    public static Map<String, Integer> formattedValues = new HashMap<>();
    
    public GameBean(String name, int duration, String date){
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleStringProperty(formattedDuration(duration));
        this.date = new SimpleStringProperty(date);
    }
    
    public String getName(){
        return name.get();
    }
    
    public String getDuration(){
        return duration.get();
    }
    
    public String getDate(){
        return date.get();
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
