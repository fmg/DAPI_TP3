/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology;

import com.hp.hpl.jena.ontology.Individual;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Desktop
 */
public class OntologyFiller {

    
    private final String dataSetFileName;
    private myOntology myOnt;

    public OntologyFiller(String dataSetFileName, String inFileName, String outFileName) throws Exception {
        this.dataSetFileName = dataSetFileName;
        myOnt = myOntology.getObjectInstance(inFileName, outFileName);
    }
    
    
    public void populateOntology(){
        
        File fXmlFile = new File(dataSetFileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder;
        org.w3c.dom.Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
        } catch (Exception ex) {
             Logger.getLogger(OntologyFiller.class.getName()).log(Level.SEVERE, null, ex);
             return;
        }
        
        //optional, but recommended
	doc.getDocumentElement().normalize();
        
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        
        NodeList nList = doc.getElementsByTagName("movie");
 
	System.out.println("Number of films:" +  nList.getLength());
        
        
        for(int i = 0; i < nList.getLength(); i++){
            Element eElement = (Element)nList.item(i);
            addMovieToOntology(eElement);
        }     
            
        myOnt.writeOntologyToFile();
    }
    
    
    private void addMovieToOntology(Element movie){
        
        //id
        long id = Long.parseLong(movie.getElementsByTagName("id").item(0).getTextContent());

        //title
        String title = movie.getElementsByTagName("title").item(0).getTextContent();
        
        //year
        int year = Integer.parseInt(movie.getElementsByTagName("year").item(0).getTextContent());

        //genres
        ArrayList<String> genresArray = new ArrayList<String>();
        NodeList genres = ((Element) movie.getElementsByTagName("genres").item(0)).getElementsByTagName("genre");
        for (int i = 0; i < genres.getLength(); i++) {
            String genre = genres.item(i).getTextContent();
            genresArray.add(genre);
        }

        //mpaa_rating
        String mpaaRating = movie.getElementsByTagName("mpaa_rating").item(0).getTextContent();

        //runtime
        String runtime = movie.getElementsByTagName("runtime").item(0).getTextContent();
        
        //critics_consensus
        String critics_consensus = movie.getElementsByTagName("critics_consensus").item(0).getTextContent(); 
        
        //ratings
        Element ratings = ((Element) movie.getElementsByTagName("ratings").item(0));
        String critics_rating = ratings.getElementsByTagName("critics_rating").item(0).getTextContent();
        int critics_score = Integer.parseInt(ratings.getElementsByTagName("critics_score").item(0).getTextContent());
        String audience_rating = ratings.getElementsByTagName("audience_rating").item(0).getTextContent();
        int audience_score = Integer.parseInt(ratings.getElementsByTagName("audience_score").item(0).getTextContent());       

        //synopsis
        String synopsis = movie.getElementsByTagName("synopsis").item(0).getTextContent();

        //studio
        String studio = movie.getElementsByTagName("studio").item(0).getTextContent();
        
        
        //initializes the individual with all the standard information
        Individual movieIndvidual = myOnt.createMovieIndividual(id, title, year, genresArray, mpaaRating, runtime, critics_consensus, critics_rating, critics_score, audience_rating, audience_score, synopsis, studio);
        
        
        
        //abridged_cast
        Cast movieCast = new Cast();
        NodeList cast = ((Element) movie.getElementsByTagName("abridged_cast").item(0)).getElementsByTagName("cast");
        for (int i = 0; i < cast.getLength(); i++) {
            Element actor = (Element) cast.item(i);
            long actor_id = Long.parseLong(actor.getElementsByTagName("id").item(0).getTextContent());
            String actor_name = actor.getElementsByTagName("name").item(0).getTextContent();

            ArrayList<String> charactersArray = new ArrayList<String>();
            NodeList characters = ((Element) actor.getElementsByTagName("characters").item(0)).getElementsByTagName("character");
            for (int k = 0; k < characters.getLength(); k++) {

                String movie_character = characters.item(k).getTextContent();
                charactersArray.add(movie_character);


            }
            movieCast.addActor(actor_name, actor_id, charactersArray);
        }
        myOnt.addCastToMovieIndividual(movieIndvidual, movieCast, title, year);
        
        
        //abridged_directors
        ArrayList<String> directorArray = new ArrayList<String>();
        NodeList directors = ((Element) movie.getElementsByTagName("abridged_directors").item(0)).getElementsByTagName("director");
        for (int i = 0; i < directors.getLength(); i++) {

            String movie_director = directors.item(i).getTextContent();
            if (!movie_director.equals("-")) {
                directorArray.add(movie_director);
            }
        }
        myOnt.addDirectorsToMovieIndividual(movieIndvidual, directorArray);



        //reviews
        ArrayList<Review> reviewArray = new ArrayList<Review>();
        NodeList reviews = ((Element) movie.getElementsByTagName("reviews").item(0)).getElementsByTagName("review");
        for (int i = 0; i < reviews.getLength(); i++) {

            Element review = (Element) reviews.item(i);
            //critic
            String rev_critic = review.getElementsByTagName("critic").item(0).getTextContent();
            String rev_date = review.getElementsByTagName("date").item(0).getTextContent();
            String rev_original_score = review.getElementsByTagName("original_score").item(0).getTextContent();
            String rev_freshness = review.getElementsByTagName("freshness").item(0).getTextContent();
            String rev_publication = review.getElementsByTagName("publication").item(0).getTextContent();
            String rev_quote = review.getElementsByTagName("quote").item(0).getTextContent();

            reviewArray.add(new Review(rev_critic, rev_date, rev_original_score, rev_freshness, rev_publication, rev_quote));
        }
        myOnt.addReviewsToMovieIndividual(movieIndvidual, reviewArray, title, year);
        
    }
    
    
}
