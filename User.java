import java.util.*;
//
public class User {
    private int userId;
    private ArrayList<Integer> movieId = new ArrayList();
    private ArrayList<Double> ratings = new ArrayList();
    public User (int u)
    {
        userId = u;
    }
    public void addMovieId(int m){
        movieId.add(m);
    }
    public void addRating(double r){
        ratings.add(r);
    }
}