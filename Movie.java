/**
 * Write a description of class Movie here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.*;
public class Movie
{
    //creates variables for objects of type Movie
    private int movieId;
    //creates a string for titles and genres of movies
    public String title, genres[];
    //creates a fillable array of blank size
    private ArrayList<Double> movieRating = new ArrayList();
    public double averageRating = 0, totalRating = 0;
    public Movie (int m, String t,String [] g)
    {
        movieId = m;
        title = t;
        genres = g;
    }
    //methods of object Movie
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
    
}