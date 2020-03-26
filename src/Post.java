public class Post {
    public final int userId;
    public final int id;
    public final String title;
    public final String body;

    public Post(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }
}
