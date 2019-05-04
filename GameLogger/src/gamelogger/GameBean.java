
package gamelogger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author hawdi
 */
public class GameBean {
    private SimpleStringProperty name;
    private SimpleStringProperty date;
    private SimpleIntegerProperty duration;
    
    public GameBean(String name, Integer duration, String date){
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleIntegerProperty(duration);
        this.date = new SimpleStringProperty(date);
    }
    
    public String getName(){
        return name.get();
    }
    
    public Integer getDuration(){
        return duration.get();
    }
    
    public String getDate(){
        return date.get();
    }
}
