import java.util.*;
import java.io.*;
/**
 * This program uses data from a file with information on movies and a file of movies ratings of past users
 * The program asks a user to rate 20 movies, then it compares their ratings with those of other users.
 * First it compares what movies the users rated, finding how many movies they have in common with other users.
 * Then it calculates the difference between the users' ratings on each common movie.
 * ...
 * Using that information it recommends a movie to them
 */
public class Main
{
    public static void main (String[] args) throws IOException
    {
        //user manual: tells the user what the purpose
        System.out.println("Welcome to the Netflix Movie Recommender. \nThere are a lot of movies out there and you may not know which to choose.");
        System.out.println("This program was created to give recommendations based on a list of several hundred previous ratings from users like you.");
        System.out.println("Using basic commands such as entering an integer from +0.5 (hate) to +5 (love) \nor 0 if you've never seen, you can rate a list of movies that you've watched.");
        System.out.println("Your ratings will be compared to those of other users and based off of a similarity score, some movies will be recomended to you.");

        //creates a scanner to get input
        Scanner in = new Scanner(System.in);

        //creates an array of all possible genres
        String genresIndex [] = {"Film-Noir","Action","Adventure","Horror","Romance","War","Western",
                "Documentary","Sci-Fi","Drama","Thriller","(no genres listed)","Crime","Fantasy","Animation",
                "IMAX","Comedy","Mystery","Children","Musical"};

        //makes an empty array with 20 different spots for the ratings
        ArrayList <Integer> genresList[] = new ArrayList[20];

        //The integer based array for the movie ratings that the user of the program gives
        int[] newRatings = new int[20];
        int zeroCounter = 0;
        for(int i = 0; i<20; i++) genresList[i] = new ArrayList();

        /*takes the movie information from the movies.csv file, and sorts them
        create a new buffer reader*/
        BufferedReader reader = new BufferedReader(new FileReader("movies.csv"));
        
        // an array to contain all of the movies from the file
        Movie [] movie = new Movie[200000];
        reader.readLine();
        String movieLine = reader.readLine();
        String movieFields[];
       
        //uses information from the file to create a new movie object
        while(movieLine!=null)
            {
            //This alters the csv file. 
            movieFields = movieLine.split(",");
            int movieId = Integer.parseInt(movieFields[0]);
            String middle = movieFields[1];
            for(int i = 2; i<movieFields.length-1; i++){
                middle = middle + "," + movieFields[i];
            }
            
            //sorts all of the movies by genres by crossreferencing it with the list genresIndex
            String [] genres = movieFields[movieFields.length-1].split("\\|");
            for(int i = 0; i<genres.length; i++)
            {
                for(int j = 0; j<20; j++)
                {
                    if(genres[i].equals(genresIndex[j]))
                    {
                        genresList[j].add(movieId);
                        break;
                    }
                }

            }
            movie[movieId] = new Movie(movieId, middle, genres);
            movieLine = reader.readLine();
        }
        reader.close();

        //takes the ratings from the ratings.csv file and stores it
        reader = new BufferedReader(new FileReader("ratings.csv"));
        reader.readLine();
        String ratings = reader.readLine();
        String[] ratingsFields;
        //creates an array with new users
        User [] user = new User[750];
        
        //give them an initial point 
        int userId = 0;
        int openUserSpot = 0;
       
        //this reads all the data to make it useable for us
        //makes sure the data is not empty so that we can read the data and store
        while (ratings != null) 
        {
            //we are splitting the data with a commma to store
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
        //searches for an open spot in the user list, when it finds one it stops and records the spot
        for(int i = 1; i<user.length; i++)
        {
            if(user[i] == null)
            {
                openUserSpot = i;
                break;
            }
        }       
        
        //creates a new list to record 
        int [] record = new int[20];
        for(int i = 0; i<20; i++)
        {
            double max = 0; int index = 0;
            //this is the program that gets the data for the user to rate the movies
            for(int j = 0; j<genresList[i].size(); j++)
            {
                if(movie[genresList[i].get(j)].totalRating>=max)
                {
                    max = movie[genresList[i].get(j)].totalRating;
                    index = genresList[i].get(j);
                }
            }
            record[i] = index;
        }
        String recordAns = ("no");
        
        //ask the user if they want their responses recorded into the "database" (csv file)
        System.out.println("\nWould you like your responses to be recorded? (Type yes or no)");
        recordAns = in.next();

        //ask the user to rate the movies given to them
        System.out.println("Please rate these 20 movies from 0.5 (hate it) to +5 (love it), if you didn't watch it type 0 \n");
        //creates an object of type user, Andrew
        User Andrew = new User((int)10e9+7);
        ArrayList<Integer> check = new ArrayList();
        int number = 1;
        double rating = .1;

        //Create a writer instance to write data to csv file
        FileWriter writer = new FileWriter("ratings.csv",true);

        //for loop to check the genresList 
        for(int i = 0; i<20; i++)
        {
            double max = 0; int index = 0;
            for(int j = 0; j<genresList[i].size(); j++)
            {
                int movieId = genresList[i].get(j);
                /*if the movieID is not contained and the total rating is greater than the max,
                change the max as the total rating*/
                if(!check.contains(movieId) && movie[movieId].totalRating() >= max)
                {
                    max = movie[movieId].totalRating();
                    index = genresList[i].get(j);
                }
            }
            check.add(index);
            System.out.println(number + "\t" + movie[index].getTitle());
           
            //keeps asking the user to rate the movie until they give a valad input
            while(rating % .5 != 0 || 0> rating || rating >5)
            {
                System.out.println("Please rate this movie from 0-5, with 0 meaning you did not watch the movie. Your rating should be a multiple of .5");
                rating = in.nextDouble();
            }
            
            //records the user's ratings if they agreed to
            if (rating > 0 && recordAns.equalsIgnoreCase("yes"))
            {
                writer.append(String.valueOf(openUserSpot) + ","); writer.append(String.valueOf(index) + ","); writer.append(String.valueOf(rating)); writer.append(",101010101\n");
            }
           
            if(rating != 0){
                Andrew.addMovieId(index);
                Andrew.addRating(rating);
            }
            rating = 0.1;
        }
       
        //finishes the writing process
        writer.close();
        int totalMovieCount;
        double userScore = 1000000;
        int compatibleUser = 0;
        
        //gives the users the recommendations
        for (int i = 0; i<user.length; i++)
        {
            //make use that part of the array is not empty
            if (user[i] != null)
            {
                for (int j = 0; j < user[i].returnRatings().size(); j++)
                {
                    for (int h = 0; h < Andrew.returnRatings().size(); h++)
                    //prevents going through the Andrew list multiple times if movie id is the same
                        if (Andrew.returnMovieId().get(h) == user[i].returnMovieId().get(j))
                        {
                            user[i].addDifference(Andrew.returnRatings().get(h) - user[i].returnRatings().get(j));
                            break;
                        }
                }
                totalMovieCount = user[i].returnRatings().size() + Andrew.returnRatings().size() - user[i].getCommonMovie();
                /*we do differnece/common so that as difference becomes similar, the similarity becomes larger
                * the reason why we do 1-common/(total-common) is to put common/(total-common) into the same direction as difference/common
                if we do 1-(common/(total/common)), as the values of this gets smaller, similarity becomes larger. This is the same direction as difference/common*/  
                user[i].giveDifScore(user[i].returnDifference() / user[i].getCommonMovie() * (1-(user[i].getCommonMovie() / (totalMovieCount-user[i].getCommonMovie()))));
                
                if (user[i].getDifScore() < userScore)
                {
                    compatibleUser = i;
                    userScore = user[i].getDifScore();
                }
            }
        }
        
        //prints out the user number the current user is most common with and all the movies they rated highly
        System.out.println("You are most similar with User " + compatibleUser + ". The movies reccomended for you are:");
        for (int i = 0; i<user[compatibleUser].returnRatings().size(); i++)
        {
            if (user[compatibleUser].returnRatings().get(i) >= 4)
            {
                System.out.println(movie[user[compatibleUser].returnMovieId().get(i)].getTitle());
            }
        }
    }
}
