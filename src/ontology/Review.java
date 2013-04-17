/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology;

/**
 *
 * @author Desktop
 */
public class Review {
    
    private String critic;
    private String date;
    private String original_score;
    private String freshness;
    private String publication;
    private String quote;

    public Review(String critic, String date, String original_score, String freshness, String publication, String quote) {
        this.critic = critic;
        this.date = date;
        this.original_score = original_score;
        this.freshness = freshness;
        this.publication = publication;
        this.quote = quote;
    }

    public String getCritic() {
        return critic;
    }

    public String getDate() {
        return date;
    }

    public String getOriginal_score() {
        return original_score;
    }

    public String getFreshness() {
        return freshness;
    }

    public String getPublication() {
        return publication;
    }

    public String getQuote() {
        return quote;
    }
    
    
    
}
