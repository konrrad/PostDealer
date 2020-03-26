import javafx.util.Pair;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Dealer d=new Dealer();
        try
        {
            d.joinPostsAndUsers();
            System.out.println("User count lists:");
            d.getUserCountList().forEach(System.out::println);
            System.out.println("\nNot Unique posts:");
            d.getNotUniqueTitlesFromData().forEach(System.out::println);
            List<Pair<User,User>> closestUsers=d.getClosestLivingUsers();
            System.out.println("\nClosest living users:");
            closestUsers.forEach(pair->System.out.println(String.format("User: %s\nUser: %s\n",pair.getKey().name,pair.getValue().name)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
