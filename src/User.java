import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {
    public final int id;
    public final String name;
    public final String username;
    public final String email;
    public final Address address;
    public final String phone;
    public final String website;
    public final Company company;
    private List<Post> posts;

    public User(int id, String name, String username, String email, Address address, String phone, String website, Company company) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.company = company;

    }
    public void addPost(Post post)
    {
        this.posts.add(post);
    }
    public void initializeList()
    {
        posts=new LinkedList<>();
    }
    public int getNumberOfPosts()
    {
        return this.posts.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    public List<Post> getPosts() {
        return new ArrayList<>(this.posts);
    }

    public String getUserCountString()
    {
        return String.format("%s napisal(a) %d postow", this.name, this.getNumberOfPosts());
    }
}
