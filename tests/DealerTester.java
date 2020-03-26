import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DealerTester {

    Dealer dealer=new Dealer();
    @Before
    public void join() throws IOException, InterruptedException {
        dealer.joinPostsAndUsers();
    }

    @Test
    public void joinPostsAndUsersTest() {

        assertNotNull(dealer.getListOfPosts());
        assertNotNull(dealer.getListOfUsers());
        assertEquals(dealer.getListOfPosts().size(),100);
        assertEquals(dealer.getListOfUsers().size(),10);
        List<User> shouldBeOne=dealer.getListOfUsers().stream().filter(user1 -> user1.id==3).collect(Collectors.toList());
        assertEquals(shouldBeOne.size(),1);
        User usr=shouldBeOne.get(0);
        assertEquals(usr.name,"Clementine Bauch");
        assertEquals(usr.address.zipcode,"59590-4157");
        assertEquals(usr.address.geo.lat,-68.6102,0.05);
        assertEquals(usr.company.name,"Romaguera-Jacobson");

        assertEquals(usr.getNumberOfPosts(),10);
        List<Post> onePostOfId22=usr.getPosts().stream().filter(post->post.id==22).collect(Collectors.toList());
        assertEquals(onePostOfId22.size(),1);
        Post post=onePostOfId22.get(0);
        assertEquals(post.title,"dolor sint quo a velit explicabo quia nam");
    }
    @Test
    public void getNotUniqueTitles()
    {
        List<String> notUnique=dealer.getNotUniqueTitles(new ArrayList<String>(Arrays.asList("Title1","Title2","Title1")));
        assertEquals(notUnique.size(),1);
        assertEquals(notUnique.get(0),"Title1");
    }

    @Test
    public void getClosestLivingUsers()
    {
        List<Pair<User,User>> usersPairs=dealer.getClosestLivingUsers();
        List<Pair<User,User>> shouldBeOneUser=usersPairs.stream().filter(usersPair->usersPair.getKey().id==4).collect(Collectors.toList());
        assertEquals(shouldBeOneUser.size(),1);
        Pair<User,User> pair=shouldBeOneUser.get(0);
        assertEquals(pair.getValue().id,9);

    }

}
