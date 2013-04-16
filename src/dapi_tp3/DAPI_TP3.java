/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dapi_tp3;

import java.util.logging.Level;
import java.util.logging.Logger;
import ontology.OntologyFiller;

/**
 *
 * @author Desktop
 */
public class DAPI_TP3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new OntologyFiller("movies50.xml", "movieOntRDF.owl", "populated_movieontology.owl").populateOntology();
        } catch (Exception ex) {
            Logger.getLogger(DAPI_TP3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
