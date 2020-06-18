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

        //creates an array of all possible genres, had to check for these
        String genresIndex [] = {"Film-Noir","Action","Adventure","Horror","Romance","War","Western",
                "Documentary","Sci-Fi","Drama","Thriller","(no genres listed)","Crime","Fantasy","Animation",
                "IMAX","Comedy","Mystery","Children","Musical"};

        //makes an empty array with 20 different spots for lists. Each spot has a list for each type of genre
        ArrayList <Integer> genresList[] = new ArrayList[20];

        //The integer based array for the movie ratings that the user of the program gives
        int[] newRatings = new int[20]; 
        for(int i = 0; i<20; i++) genresList[i] = new ArrayList();

        /*takes the movie information from the movies.csv file, and sorts them
        create a new buffer reader*/
        BufferedReader reader = new BufferedReader(new FileReader("movies.csv"));

        // an array to contain all of the movies from the file
        Movie [] movie = new Movie[200000];
        reader.readLine();
        String movieLine = reader.readLine();
        String movieFields[];
        int movieId;
        //reads the information from the csv file and organizes it
        while(movieLine!=null)
        {
            //Takes the line of the csv file and splits it
            movieFields = movieLine.split(",");
            movieId = Integer.parseInt(movieFields[0]);
            //It turns out that if there is a comma in the title it splits it, so this accounts for it
            String middle = movieFields[1];
            //Takes all the split parts of the title and puts it back together
            for(int i = 2; i<movieFields.length-1; i++){
                middle = middle + "," + movieFields[i];
            }

            //sorts all of the movies by genres by crossreferencing it with the list genresIndex
            //Initilized in here because the size of genres varies and we would have to check how long it needs to be, and when there is an empty spot
            String [] genres = movieFields[movieFields.length-1].split("\\|");
            for(int i = 0; i<genres.length; i++)
            {
                for(int j = 0; j<20; j++)
                {
                    if(genres[i].equals(genresIndex[j]))
                    {
                        //Adds the movie to a genre its part of
                        genresList[j].add(movieId);
                        break;
                    }
                }

            }
            //Creates an object movie using the information gathered
            movie[movieId] = new Movie(movieId, middle, genres);
            //Reads the next line of the csv file
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
        //Stops when there is no more users to check
        while (ratings != null) 
        {
            //Splits the data into userId, movieId, rating and timestamp. Timestamp is useless
            ratingsFields = ratings.split(",");
            //Once the userId changes it means that all the data has been collected for one user and an object of type user can be made
            if (userId != Integer.parseInt(ratingsFields[0]))
            {
                userId = Integer.parseInt(ratingsFields[0]);
                //We just made a constructor with the userId. The movieId and rating is added seperately so there's no need for it in the constructor
                user[userId] = new User(userId);
            }
            //Adds the user's rating of a movie to the total ratings of a movie, so we can keep track of what's the most popular movie to ask about
            movie[Integer.parseInt(ratingsFields[1])].addRating(Double.parseDouble(ratingsFields[2]));
            //Add's the movieId to the user class so we know what movie they watched
            user[userId].addMovieId(Integer.parseInt(ratingsFields[1]));
            //Adds the rating for the particular movie
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

        String recordAns = ("no");

        //ask the user if they want their responses recorded into the "database" (csv file)
        System.out.println("\nWould you like your responses to be recorded? (Type yes or no)");
        recordAns = in.next();

        //ask the user to rate the movies given to them
        System.out.println("Please rate these 20 movies from 0.5 (hate it) to +5 (love it), if you didn't watch it type 0 \n");
        //creates an object of type user, Andrew
        User Andrew = new User((int)10e9+7);
        //Will make sure there is no overlap in movies asked by storing the movies we've already asked about
        ArrayList<Integer> check = new ArrayList();
        int number = 1;
        double rating = .1;

        //Create a writer instance to write data to csv file
        FileWriter writer = new FileWriter("ratings.csv",true);
        double max = 0; int index = 0;
        System.out.println("How many multiples of 20 movies do you want to rate?");
        int multipleCounter = 0;
        //This forces the user to choose a multiple of 20 between 1 and 10
        while(multipleCounter <= 0 || multipleCounter > 10)
        {
            System.out.println("Pick a number from 1 to 10.");
            multipleCounter = in.nextInt();
        }
        //for loop to check the genresList 
        for(int i = 0; i<(20*multipleCounter); i++)
        {
            //in the following loops, modulus (%) will force it to go through the genre list spots again since it only goes up to twenty
            for(int j = 0; j<genresList[i%20].size(); j++)
            {
                movieId = genresList[i%20].get(j);
                /*if the movieID is not contained and the total rating is greater than the max,
                change the max as the total rating*/
                if(!check.contains(movieId) && movie[movieId].totalRating() >= max)
                {
                    max = movie[movieId].totalRating();
                    index = genresList[i%20].get(j);
                }
            }
            //Stores the we asked about movie so we don't ask about it again
            check.add(index);
            System.out.println(number + "\t" + movie[index].getTitle());
            //keeps asking the user to rate the movie until they give a valid input
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
            //It the rating is 0 it means they did not watch the movie and it doesn't matter
            if(rating != 0){
                Andrew.addMovieId(index);
                Andrew.addRating(rating);
            }
            //Resets rating, max, and index
            rating = 0.1;
            max= 0;
            index = 0;
        }

        //finishes the writing process
        writer.close();
        int totalMovieCount;
        //Makes a high different score so it would be changed for sure
        double userScore = 1000000;
        int compatibleUser = 0;

        //gives the users the recommendations
        //This for loop goes through the list of users so we can check each one        
        for (int i = 0; i<user.length; i++)
        {
            //Makes sure we are not checking an empty spot for the movies we asked about
            if (user[i] != null)
            {
                //Goes through the ratings of user i.
                for (int j = 0; j < user[i].returnRatings().size(); j++)
                {
                    //Goes through the ratings of the user we asked to rate movies
                    for (int h = 0; h < Andrew.returnRatings().size(); h++)
                    //Makes sure we are comparing the same movie
                        if (Andrew.returnMovieId().get(h) == user[i].returnMovieId().get(j))
                        {
                            user[i].addDifference(Andrew.returnRatings().get(h) - user[i].returnRatings().get(j));
                            //prevents going through the Andrew list multiple times if movie id is the same
                            break;
                        }
                }
                totalMovieCount = user[i].returnRatings().size() + Andrew.returnRatings().size() - user[i].getCommonMovie();
                //We get the average difference per user and multiply it by the percentage of movies that are not in common. 
                //A low average difference score is good and so is a low percentage of movies that are not in common
                user[i].giveDifScore(user[i].returnDifference() / user[i].getCommonMovie() * (1-(user[i].getCommonMovie() / (totalMovieCount-user[i].getCommonMovie()))));
                //Get's the lowest differnce score
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
