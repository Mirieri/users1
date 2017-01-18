package ke.co.eaglesafari.items;


public class GcmItem {
    String type,content;
    UserItem user;

    public UserItem getUser() {
        return user;
    }

    public GcmItem setUser(UserItem user) {
        this.user = user;
        return  this;
    }

    public String getType() {
        return type;
    }

    public GcmItem setType(String type) {
        this.type = type;
        return this;
    }

    public String getContent() {
        return content;
    }

    public GcmItem setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "GcmItem{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                '}';
    }
}
