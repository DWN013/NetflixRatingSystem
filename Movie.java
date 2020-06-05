import java.util.*;
public class Movie
{
    //creates variables for the id of a movie
    private int movieId;
    
    //creates strings for titles and genres of a movie
    public String title, genres[];
    
    //creates a fillable array of blank size for ratings
    private ArrayList<Double> movieRating = new ArrayList();
    
    //creates variables for the average and total rating
    public double averageRating = 0, totalRating = 0;
    
    //the constructor for objects of type Movie
    public Movie (int m, String t,String [] g)
    {
        movieId = m;
        title = t;
        genres = g;
    }
    
    //methods of object Movie
    //adds a rating to the list of ratings on the movie
    public void addRating(double r){
        movieRating.add(r);
        totalRating+=r;
        averageRating = totalRating/movieRating.size();
    }
    
    //gets average rating for a movie
    public double getAverageRating(){
        return averageRating;
    }
    
    //returns the title for a movie
    public String getTitle(){
        return title;
    }
    
    //returns the total rating for a movie
    public double totalRating(){
        return totalRating;
    }

}
