/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology;

import java.util.ArrayList;

/**
 *
 * @author Desktop
 */
public class Actor {
    
    private String name;
    private long id;
    private ArrayList<String> characters;

    public Actor(String name, long id, ArrayList<String> characters) {
        this.name = name;
        this.characters = characters;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public ArrayList<String> getCharacters() {
        return characters;
    }
    
    
    
}
