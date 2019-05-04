/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private SimpleIntegerProperty id;
    
    public GameBean(Integer id, String name, Integer duration, String date){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleIntegerProperty(duration);
        this.date = new SimpleStringProperty(date);
    }
    
    public Integer getId(){
        return id.get();
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
