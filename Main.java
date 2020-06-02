import java.util.*;
import java.io.*;
/**
 * This program uses data from a file with information on movies and a file of movies ratings of past users
 * The program asks a user to rate 20 movies then it compires their ratings with those of other users by...
 * Using that information it recomends a movie to them
 */
public class Main
{
    public static void main (String[] args) throws IOException
    {
        //explain what the program will do
        System.out.println("Welcome to the Netflix Movie Recommender. \nThere are a lot of movies out there and you may not know which to choose.");
        System.out.println("This program was created to give recommendations based on a list of several hundred previous ratings from users like you.");
        System.out.println("By using basic commands such as inputting an integer from 0.5 (hate it) to +5 (love it) or 0 if you never watched, you can rate a list of movies that you've watched.");
        System.out.println("Your ratings will be compared to those of other users and based off of a similarity score, a movie will be recomended to you.");

        //create a scanner to get input from the user
        Scanner in = new Scanner(System.in);

        //creates a list of all possible genres
        String genresIndex [] = {"Film-Noir","Action","Adventure","Horror","Romance","War","Western",
                "Documentary","Sci-Fi","Drama","Thriller","(no genres listed)","Crime","Fantasy","Animation",
                "IMAX","Comedy","Mystery","Children","Musical"};
        ArrayList <Integer> genresList[] = new ArrayList[20];

        //The integer based array for the movie ratings that the user of the program gives
        int[] newRatings = new int[20];
        int zeroCounter = 0;
        for(int i = 0;i<20;i++) genresList[i] = new ArrayList();

        //takes the ratings from the movies.csv file for the  
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

        //ask the user to rate the movies given to them
        System.out.println("Please rate these 20 movies from 0.5 (hate it) to +5 (love it), if you didn't watch it type 0 \n");
        User Andrew = new User((int)10e9+7);
        ArrayList<Integer> check = new ArrayList();
        int number = 1;
        for(int i = 0;i<20;i++){

            double max = 0; int index = 0;
            for(int j = 0;j<genresList[i].size();j++){
                int movieId = genresList[i].get(j);
                if(!check.contains(movieId)&&movie[movieId].totalRating()>=max){
                    max = movie[movieId].totalRating();
                    index = genresList[i].get(j);

                }
            }
            check.add(index);
            System.out.println(number+"\t"+movie[index].getTitle());
            double rating = in.nextDouble();
            if(rating>=0&&rating<=5){
                Andrew.addMovieId(index);
                Andrew.addRating(max);
            }

        }
        int totalMovieCount;
        double userScore = 1000000;
        int compatibleUser = 0;
        for (int i = 0; i<user.length; i++)
        {
            if (user[i] != null){
                for (int j = 0; j < user[i].returnRatings().size(); j++)
                {
                    for (int h = 0; h < Andrew.returnRatings().size(); h++)
                    //prevents going through the Andrew list multiple times if movie id is same
                        if (Andrew.returnMovieId().get(h) == user[i].returnMovieId().get(j))
                        {
                                user[i].addDifference(Andrew.returnRatings().get(h) - user[i].returnRatings().get(j));
                                break;
                        }
                }
                totalMovieCount = user[i].returnRatings().size() + Andrew.returnRatings().size();
                user[i].giveDifScore(user[i].returnDifference()/user[i].getCommonMovie()*(1-(user[i].getCommonMovie()/(totalMovieCount-user[i].getCommonMovie()))));
                if (user[i].getDifScore() < userScore)
                {
                    compatibleUser = i;
                    userScore = user[i].getDifScore();
                }
            }
        }
        System.out.println(compatibleUser);
    }
}
