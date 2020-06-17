import java.util.*;
public class User 
{
    //creates variables for the id of a user and number of movies in common
    private int userId, moviesInCommon;

    //creates variables for the difference scores and difference
    private double difScore, difference;
   
    //creates a fillable array of blank size for the id of movies a user rates
    private ArrayList<Integer> movieId = new ArrayList();

    //creates a fillable array of blank size for the ratings a user gives
    private ArrayList<Double> ratings = new ArrayList();

    //the constructor for objects of type User
    public User (int u)
    {
        userId = u;
    }

    //methods of object User
    //add a movie to the list of movies
    public void addMovieId(int m){
        movieId.add(m);
    }

    //add to difference and number of movies in common
    public void addDifference(double dif)
    {
        difference += Math.abs(dif);
        moviesInCommon ++;
    }

    //return the difference
    public double returnDifference()
    {
        return difference;
    }
    
    //return ratings
    public ArrayList<Double> returnRatings()
    {
        return ratings;
    }

    //return movie id
    public ArrayList<Integer> returnMovieId()
    {
        return movieId;
    }
    
    //add a rating to the list of ratings
    public void addRating(double r){
        ratings.add(r);
    }

    //calculate the difference score
    public void giveDifScore (double score)
    {
        difScore = score;   
    }

    //get the number of movies in common
    public int getCommonMovie()
    {
        return moviesInCommon;
    }
    
    //get the difference score
    public double getDifScore()
    {
        return difScore;
    }
}
