package org.hforh;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Question implements Serializable {
    private String id;
    private String url;
    private String title;
    private String datetime;
	
    public Question() {
    }
    
    public Question(String id, String url, String title, String datetime) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.datetime = datetime;
    }
    public String getTitle() {
        return title;
    }
    public String getDateTime() {
        return datetime;
    }
    public String getUrl() {
        return url;
    }
    public String getId() {
        return id;
    }    
/*    public void read(Scanner in) {
        this.text = in.nextLine();
        int i = 0;
        boolean done = false;
        while (!done) {
            if (!in.hasNextLine())
                done = true;
            else {
                String line = in.nextLine();
                if (line.length() == 0)
                    done = true;
                else {
                    if (line.startsWith("*")) {
                        correctChoice = i;
                        line = line.substring(1);
                    }
                    choices.add(line);
                    i++;
                }
            }
        }
    }
    public Question(String text, List<String> choices, int correctChoice) {
        this.text = text;
        this.choices = new ArrayList<String>(choices);
        this.correctChoice=correctChoice;
     }

    public List<String> getChoices() {
        return Collections.unmodifiableList(choices);
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect(int choice) {
        
        return choice == correctChoice;
        
    }*/
/*    public String getResult() {
        return userans;
    }
    
    public void setResult(boolean choice) {
        if( choice == true)
            userans = "right";
        else
            userans = "wrong";
               
    }*/
    

}
