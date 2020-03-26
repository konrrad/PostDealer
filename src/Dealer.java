import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Dealer {


    private final HttpClient client;
    private final Gson gson;
    private List<Post> listOfPosts;
    private ArrayList<User> listOfUsers;
    //I don't assume that userIDs are consecutive nor sorted(despite they are, but the set is small, so one can check it
    // If the set was bigger checking these properties by eye would take too long),
    // so I create a mapping:
    // userId->Position in listOfUsers to properly match posts for them
    private Map<Integer, Integer> idPositionMap = new HashMap<>();
    //for performance,especially in getClosestLivingUsers()
    private Map<Integer, Integer> positionIdMap=new HashMap<>();
    private Map<Integer, User> idUserMap=new HashMap<>();

    public Dealer() {
        client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        gson = new Gson();
    }

    public List<Post> getListOfPosts() {
        return new ArrayList<>(listOfPosts);
    }

    public ArrayList<User> getListOfUsers() {
        return new ArrayList<>(listOfUsers);
    }

    public void joinPostsAndUsers() throws IOException, InterruptedException {
        getPosts();
        getUsers();
        joinPostsWithUsers();
    }

    private void joinPostsWithUsers() {
        listOfPosts.forEach(post -> {
            if (this.idPositionMap.containsKey(post.userId)) {
                this.listOfUsers.get(this.idPositionMap.get(post.userId)).addPost(post);
            }
        });
    }

    //using sync requests, because there is really nothing I can do without that data
    private void getUsers() throws IOException, InterruptedException {
        HttpRequest getUsersRequest = HttpRequest.newBuilder(URI.create("https://jsonplaceholder.typicode.com/users")).
                timeout(Duration.ofSeconds(30)).build();
        HttpResponse<String> response = client.send(getUsersRequest, HttpResponse.BodyHandlers.ofString());
        createListOfUsers(response.body());
    }

    private void createListOfUsers(String usersJSON) {
        this.listOfUsers = gson.fromJson(usersJSON, new TypeToken<ArrayList<User>>() {
        }.getType());
        IntStream.range(0, listOfUsers.size()).forEach(position -> {
            User usr=this.listOfUsers.get(position);
            usr.initializeList();
            final int id=usr.id;
            this.idPositionMap.put(id, position);
            this.positionIdMap.put(position,id);
            this.idUserMap.put(id,usr);
        });
    }

    private void getPosts() throws IOException, InterruptedException {
        HttpRequest getPostsRequest = HttpRequest.newBuilder(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .timeout(Duration.ofSeconds(30)).build();
        HttpResponse<String> response = client.send(getPostsRequest, HttpResponse.BodyHandlers.ofString());
        createListOfPosts(response.body());

    }

    private void createListOfPosts(String s) {
        this.listOfPosts = gson.fromJson(s, new TypeToken<List<Post>>() {
        }.getType());
    }

    //method that creates a list of Strings in form: "user_name napisał(a) count postów"
    public List<String> getUserCountList() {
        return listOfUsers.stream().map(User::getUserCountString).collect(Collectors.toList());
    }

    public List<String> getNotUniqueTitles(List<String> titlesArg)
    {
        Set<String> resultSet = new HashSet<>();
        Set<String> setOfTitles = new HashSet<>();
        for(String title: titlesArg)
        {
            if(setOfTitles.contains(title))
            {
                resultSet.add(title);
            }
            else
            {
                setOfTitles.add(title);
            }
        }
        return new LinkedList<>(resultSet);
    }

    public List<String> getNotUniqueTitlesFromData() {
        return getNotUniqueTitles(this.getPostsTitles());

    }

    private List<String> getPostsTitles() {
        return this.listOfPosts.stream().map(Post::getTitle).collect(Collectors.toList());
    }

    public List<Pair<User,User>> getClosestLivingUsers()
    {
        int numOfUsers=this.listOfUsers.size();
        //2d array to avoid counting distances many times
        double[][] distances =new double[numOfUsers][numOfUsers];
        List<Pair<User,User>> result=new ArrayList<>();
        for(int i=0;i<numOfUsers;i++)
        {
            distances[i][i]=0;
            for(int j=i+1;j<numOfUsers;j++)
            {   //since getDistanceInKilometers return the shortest path
                distances[i][j]=GeoCoordinates.getDistanceInKilometers(this.listOfUsers.get(i).address.geo,this.listOfUsers.get(j).address.geo);
                distances[j][i]=distances[i][j];
            }
        }
        for(int i=0;i<numOfUsers;i++)
        {
            double current_min=Double.POSITIVE_INFINITY;
            int candidate_position=0;
            for(int j=0;j<numOfUsers;j++)
            {
                if(i!=j&&distances[i][j]<current_min)
                {
                    current_min=distances[i][j];
                    candidate_position=j;
                }
            }
            User usr1=this.idUserMap.get(this.positionIdMap.get(i));
            User usr2=this.idUserMap.get(this.positionIdMap.get(candidate_position));
            result.add(new Pair<>(usr1,usr2));
        }
        return result;

    }
}
