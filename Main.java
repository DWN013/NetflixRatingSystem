import java.util.Scanner;
import java.util.*;
import java.io.*;
/**
 * Write a description of class Main here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Main
{
    public static void main (String[] args) throws IOException
    {
        //creates a list of all possible genres
        String genresIndex [] = {"Film-Noir","Action","Adventure","Horror","Romance","War","Western",
                "Documentary","Sci-Fi","Drama","Thriller","(no genres listed)","Crime","Fantasy","Animation",
                "IMAX","Comedy","Mystery","Children","Musical"};
        ArrayList <Integer> genresList[] = new ArrayList[20];
        //The integer based array for the movie ratings that the user of the program gives
        int[] newRatings = new int[20];
        int zeroCounter = 0;
        for(int i = 0;i<20;i++) genresList[i] = new ArrayList();
        
        BufferedReader reader = new BufferedReader(new FileReader("movies.csv"));
        Movie [] movie = new Movie[200000];
        reader.readLine();
        String movieLine = reader.readLine();
        String movieFields[];

        while(movieLine!=null)
        {
            movieFields = movieLine.split(",");
            int movieId = Integer.parseInt(movieFields[0]);
            String middle = movieFields[1];
            for(int i = 2;i<movieFields.length-1;i++){
                middle = middle+","+movieFields[i];
            }
            String [] genres = movieFields[movieFields.length-1].split("\\|");
            for(int i = 0;i<genres.length;i++){
                for(int j = 0;j<20;j++){
                    if(genres[i].equals(genresIndex[j])){
                        genresList[j].add(movieId);
                        break;
                    }
                }

            }
            movie[movieId] = new Movie(movieId, middle, genres);
            movieLine = reader.readLine();
        }
        reader.close();

        reader = new BufferedReader(new FileReader("ratings.csv"));
        reader.readLine();
        String ratings = reader.readLine();
        String[] ratingsFields;
        User [] user = new User[750];
        int userId = 0;
        while (ratings != null) 
        {
            ratingsFields = ratings.split(",");
            if (userId != Integer.parseInt(ratingsFields[0]))
            {
                userId = Integer.parseInt(ratingsFields[0]);
                user[userId] = new User(userId);

            }
            movie[Integer.parseInt(ratingsFields[1])].addRating(Double.parseDouble(ratingsFields[2]));
            user[userId].addMovieId(Integer.parseInt(ratingsFields[1]));
            user[userId].addRating(Double.parseDouble(ratingsFields[2]));
            ratings = reader.readLine();
        }
        reader.close();

        int [] record = new int[20];
        for(int i = 0;i<20;i++){
            double max = 0; int index = 0;
            for(int j = 0;j<genresList[i].size();j++){
                if(movie[genresList[i].get(j)].totalRating>=max){
                    max = movie[genresList[i].get(j)].totalRating;
                    index = genresList[i].get(j);
                }
            }
            record[i] = index;
            System.out.println(movie[index].title);
        }
        
        
        System.out.println("Please rate these movies on a scale from 0 - 5 (Increments of .5. 0 Meaning you have not seen the movie, 5 meaning I like it alot, .5 meaning you disliked it heavily.)");
        for(int i  = 0;i<20;i++) {
            System.out.println("Input Rating for Movie" + i);
            newRatings[i] = Integer.parseInt(input.next());
            if (newRatings[i] == 0)
            {
                zeroCounter++;
            }
        }
    }
}
