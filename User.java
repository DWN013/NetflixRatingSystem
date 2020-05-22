import java.util.*;
public class User {
    //creates a variable for the id of a user
    private int userId;
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
    //add a rating to the list of ratings
    public void addRating(double r){
        ratings.add(r);
    }
}