/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Desktop
 */
public class myOntology {
    
    public final static String ONTOLOGY_IRI = "http://www.semanticweb.org/desktop/ontologies/2013/3/movieontology.owl";
    public final static String ONTOLOGY_NS = ONTOLOGY_IRI + "#";
    
    //Ontology classes
    public final static String ONT_CLASS_MOVIE = "Movie";
    public final static String ONT_CLASS_CAST = "Cast";
    public final static String ONT_CLASS_GENRE = "Genre";
    public final static String ONT_CLASS_AUDIENCE_RATING = "Audience_Rating";
    public final static String ONT_CLASS_CRITIC_RATING = "Critic_Rating";
    public final static String ONT_CLASS_STUDIO = "Studio";
    public final static String ONT_CLASS_ACTOR = "Actor";
    public final static String ONT_CLASS_DIRECTOR = "Director";
    public final static String ONT_CLASS_REVIEW = "Review";
    public final static String ONT_CLASS_CRITIC = "Critic";
    public final static String ONT_CLASS_PUBLICATION = "Publication";
    
    
    
       
    //Ontology obtect properties 
    public final static String ONT_OBP_GENRE = "hasGenre";
    public final static String ONT_OBP_CRITIC_RATING = "hasCriticRating";
    public final static String ONT_OBP_AUDIENCE_RATING = "hasAudienceRating";
    public final static String ONT_OBP_STUDIO = "hasStudio";
    public final static String ONT_OBP_CAST = "hasCast";
    public final static String ONT_OBP_ACTOR = "hasActor";
    public final static String ONT_OBP_DIRECTOR = "hasDirector";
    public final static String ONT_OBP_REVIEW = "hasReview";
    public final static String ONT_OBP_PUBLICATION = "hasPublication";
    public final static String ONT_OBP_CRITIC = "hasCritic";

    
    
    //Ontology datatype properties
    public final static String ONT_DTP_MOVIE_ID = "movieID";
    public final static String ONT_DTP_TITLE = "title";
    public final static String ONT_DTP_YEAR = "year";
    public final static String ONT_DTP_MPAA_RATING = "mpaaRating";
    public final static String ONT_DTP_RUNTIME = "runtime";
    public final static String ONT_DTP_CRITIC_CONSENSUS = "criticConsensus";
    public final static String ONT_DTP_SYNOPSIS = "synopsis";
    public final static String ONT_DTP_GENRE_NAME = "genreName";
    public final static String ONT_DTP_STUDIO_NAME = "studioName";

    public final static String ONT_DTP_RATING_NAME = "ratingName";
    public final static String ONT_DTP_AUDIENCE_SCORE = "audienceScore";
    public final static String ONT_DTP_CRITIC_SCORE = "criticScore";

    public final static String ONT_DTP_PERSON_NAME = "personName";
    public final static String ONT_DTP_ACTOR_ID = "actorID";
    public final static String ONT_DTP_CHARACTER_NAME = "characterName";
    
    public final static String ONT_DTP_DATE = "date";
    public final static String ONT_DTP_REVIEW_SCORE = "reviewScore";
    public final static String ONT_DTP_QUOTE = "quote";
    public final static String ONT_DTP_PUBLICATION_NAME = "publicationName";
    
    
    
    
    
    private final String ontologyInFileName;
    private final String ontologyOutFileName;
    
    
    private OntModel myOntModel;
    
    
    private static myOntology myOnt;

    private myOntology(String inFileName, String outFileName) {
        ontologyInFileName = inFileName;
        ontologyOutFileName = outFileName;
        
        myOntModel = ModelFactory.createOntologyModel();
        InputStream is = FileManager.get().open(inFileName);
        
        myOntModel.read(is, null);
        
    }
    
    
    /**
    * Cloning is not supported due to the object being singleton
    */
    @Override
   public Object clone() throws CloneNotSupportedException {
           throw new CloneNotSupportedException();
   }
    
    
    public synchronized static myOntology getObjectInstance(String inFileName, String outFileName) throws Exception {
        if (myOnt == null) {
                myOnt = new myOntology(inFileName, outFileName);
        }
        return myOnt;
    }
    
    
    
    public void writeOntologyToFile(){
        try {
            myOntModel.write(new FileOutputStream(new File(ontologyOutFileName)));
        } catch (Exception ex) {
            Logger.getLogger(myOntology.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public Individual createMovieIndividual(long movieID, String title, int year, ArrayList<String> genres, 
            String mpaaRating, String runtime, String critics_consensus,
            String critics_rating, int critics_score, String audience_rating, int audience_score,
            String synopsis, String studio){

        
        OntClass movieClass = myOntModel.getOntClass(ONTOLOGY_NS + ONT_CLASS_MOVIE);
        Individual movieIndv = movieClass.createIndividual(ONTOLOGY_NS + normalizeString(title) + "_(" + year +")");//same title, can be uses, but not in the same year
        
        //id
        movieIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_MOVIE_ID), movieID);
        
        //title
        movieIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_TITLE), title);
        
        //year
        movieIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_YEAR), new Integer(year));      
        
        //genres
        for(String genre:genres){
            Individual genreIndv = myOntModel.getIndividual(ONTOLOGY_NS + normalizeString(genre));
            if(genreIndv == null){
                genreIndv = createIndividualFromClass(ONT_CLASS_GENRE, ONTOLOGY_NS + normalizeString(genre));
                genreIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_GENRE_NAME), genre);
            }   
            movieIndv.addProperty(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_GENRE), genreIndv);
        }    
        
        //runtime
        if (!runtime.equals("-")) {
            movieIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_RUNTIME), new Integer(Integer.parseInt(runtime)));
        }
        
        //mpaRating
        movieIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_MPAA_RATING), mpaaRating);
        
        //synopsis
        movieIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_SYNOPSIS), synopsis);
                
        
        
        //critic score
        movieIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_CRITIC_SCORE), new Integer(critics_score));
        
        //critic rating
        //System.out.println(critics_rating + " ->" + title);
        Individual crIndv = myOntModel.getIndividual(ONTOLOGY_NS + normalizeString(critics_rating));
            if(crIndv == null){
                crIndv = createIndividualFromClass(ONT_CLASS_CRITIC_RATING, ONTOLOGY_NS + normalizeString(critics_rating));
                crIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_RATING_NAME), critics_rating);
            }   
        movieIndv.addProperty(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_CRITIC_RATING), crIndv);
        
        
        
        //audience score
        movieIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_AUDIENCE_SCORE), new Integer(audience_score));
        
        //audience rating
        Individual arIndv = myOntModel.getIndividual(ONTOLOGY_NS + normalizeString(audience_rating));
            if(arIndv == null){
                arIndv = createIndividualFromClass(ONT_CLASS_AUDIENCE_RATING, ONTOLOGY_NS + normalizeString(audience_rating));
                arIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_RATING_NAME), audience_rating);
            }   
        movieIndv.setPropertyValue(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_AUDIENCE_RATING), arIndv);        
        
        
        
        //critic_consensus
        movieIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_CRITIC_CONSENSUS), critics_consensus);
        
        //studio
        Individual studioIndv = myOntModel.getIndividual(ONTOLOGY_NS + normalizeString(studio));
            if(studioIndv == null){
                studioIndv = createIndividualFromClass(ONT_CLASS_STUDIO, ONTOLOGY_NS + normalizeString(studio));
                studioIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_STUDIO_NAME), studio);
            }   
        movieIndv.setPropertyValue(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_STUDIO), studioIndv);    
        
        
        return movieIndv;
        
    }
    
    
    public void addCastToMovieIndividual(Individual movieIndv, Cast cst, String movieTitle, int year){
        
        OntClass castClass = myOntModel.getOntClass(ONTOLOGY_NS + ONT_CLASS_CAST);
        Individual castIndv = castClass.createIndividual(ONTOLOGY_NS + "cast_" + normalizeString(movieTitle) + "_(" + year +")");//same title, can be uses, but not in the same year
        
        
        for(Actor a:cst.getAbrigedCast()){
            
            Individual actorIndv = myOntModel.getIndividual(ONTOLOGY_NS + normalizeString(a.getName()));
            if(actorIndv == null){
                actorIndv = createIndividualFromClass(ONT_CLASS_ACTOR, ONTOLOGY_NS + normalizeString(a.getName()));
                actorIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_PERSON_NAME), a.getName());         
            }
            
            for(String chr: a.getCharacters()){
                String tmp =  chr + " " + "("+ movieTitle + " ("+ year+ ")" + ")";
                actorIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS+ ONT_DTP_CHARACTER_NAME), 
                        tmp);
            }
            
            castIndv.addProperty(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_ACTOR), actorIndv);
            
        }
        
        movieIndv.setPropertyValue(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_CAST), castIndv);
        
    }
    
    
    public void addDirectorsToMovieIndividual(Individual movieIndv, ArrayList<String> directors){
        
        for(String director:directors){
                        
            Individual dirIndv = myOntModel.getIndividual(ONTOLOGY_NS + normalizeString(director));
            if(dirIndv == null){
                dirIndv = createIndividualFromClass(ONT_CLASS_DIRECTOR, ONTOLOGY_NS + normalizeString(director));
                dirIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_PERSON_NAME), director);         
            }
            
            movieIndv.addProperty(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_DIRECTOR), dirIndv);
            
        }
        
    }
    
    
    
    public void addReviewsToMovieIndividual(Individual movieIndv, ArrayList<Review> reviews, String movieTitle, int year){

        
        for(Review rev: reviews){
        
            OntClass revClass = myOntModel.getOntClass(ONTOLOGY_NS + ONT_CLASS_REVIEW);
            Individual revIndv = revClass.createIndividual(ONTOLOGY_NS + "review_"+ normalizeString(rev.getPublication())
                    + "_("+normalizeString(movieTitle) + "_(" + year +"))");//same title, can be uses, but not in the same year


            //critic
            if(!normalizeString(rev.getCritic()).equals("")){
                Individual criticIndv = myOntModel.getIndividual(ONTOLOGY_NS + normalizeString(rev.getCritic()));
                if(criticIndv == null){
                    criticIndv = createIndividualFromClass(ONT_CLASS_CRITIC, ONTOLOGY_NS + normalizeString(rev.getCritic()));
                    criticIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_PERSON_NAME), rev.getCritic());         
                }
                revIndv.addProperty(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_CRITIC), criticIndv);
            }
            
            //date
            revIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_DATE), rev.getDate());
            
            //original score
            revIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_REVIEW_SCORE), rev.getOriginal_score());
            
            //freshness
            if(!normalizeString(rev.getFreshness()).equals("")){
                Individual ratingIndv = myOntModel.getIndividual(ONTOLOGY_NS + normalizeString(rev.getFreshness()));
                if(ratingIndv == null){
                    ratingIndv = createIndividualFromClass(ONT_CLASS_CRITIC_RATING, ONTOLOGY_NS + normalizeString(rev.getFreshness()));
                    ratingIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_RATING_NAME), rev.getFreshness());         
                }
                revIndv.addProperty(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_CRITIC_RATING), ratingIndv);
            }
            
            
            //publication
            Individual publIndv = myOntModel.getIndividual(ONTOLOGY_NS + normalizeString(rev.getPublication()));
            if(publIndv == null){
                publIndv = createIndividualFromClass(ONT_CLASS_PUBLICATION, ONTOLOGY_NS + normalizeString(rev.getPublication()));
                publIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_PERSON_NAME), rev.getPublication());         
            }
            revIndv.addProperty(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_PUBLICATION), publIndv);

            
            //quote
            revIndv.addLiteral(myOntModel.getDatatypeProperty(ONTOLOGY_NS + ONT_DTP_QUOTE), rev.getQuote());

        
            movieIndv.addProperty(myOntModel.getProperty(ONTOLOGY_NS + ONT_OBP_REVIEW), revIndv);
        }
        
    }
    
    
    private String normalizeString(String str){
        
        return str.toLowerCase().replaceAll("\n", "").replaceAll(" ", "_").replaceAll("&", "and").replaceAll("\"", "");
        
    }
    
    
    
    private Individual createIndividualFromClass(String className, String indvName){
        OntClass clss = myOntModel.getOntClass(ONTOLOGY_NS + className);
        Individual indv = clss.createIndividual(indvName);
        
        return indv;
    }
    
    
}
