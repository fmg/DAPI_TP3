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
public class Cast {
    
    private ArrayList<Actor> abrigedCast;

    public Cast() {
        abrigedCast = new ArrayList<Actor>();
    }
    
    public void addActor(String name, long id, ArrayList<String> characters){
        abrigedCast.add(new Actor(name, id, characters));
    }

    public ArrayList<Actor> getAbrigedCast() {
        return abrigedCast;
    }
    
    
    
    
}
