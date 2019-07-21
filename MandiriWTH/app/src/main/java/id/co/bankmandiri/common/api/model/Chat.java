package id.co.bankmandiri.common.api.model;

public class Chat {

    // visible to user
    public String name;
    public long date;
    public boolean me;
    public String content;

    // not visible to user
    public long id;
    public Object image;
    public String click_receiver;
    public String click_sender;
    public String click_convers;
    public int status_read;

    public Chat(boolean me, String name, long date, String content) {
        super();
        this.me = me;
        this.content = content;
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public Chat setName(String name) {
        this.name = name;
        return this;
    }

    public long getDate() {
        return date;
    }

    public Chat setDate(long date) {
        this.date = date;
        return this;
    }

    public boolean isMe() {
        return me;
    }

    public Chat setMe(boolean me) {
        this.me = me;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Chat setContent(String content) {
        this.content = content;
        return this;
    }

    public long getId() {
        return id;
    }

    public Chat setId(long id) {
        this.id = id;
        return this;
    }

    public Object getImage() {
        return image;
    }

    public Chat setImage(Object image) {
        this.image = image;
        return this;
    }

    public String getClick_receiver() {
        return click_receiver;
    }

    public Chat setClick_receiver(String click_receiver) {
        this.click_receiver = click_receiver;
        return this;
    }

    public String getClick_sender() {
        return click_sender;
    }

    public Chat setClick_sender(String click_sender) {
        this.click_sender = click_sender;
        return this;
    }

    public String getClick_convers() {
        return click_convers;
    }

    public Chat setClick_convers(String click_convers) {
        this.click_convers = click_convers;
        return this;
    }

    public int getStatus_read() {
        return status_read;
    }

    public Chat setStatus_read(int status_read) {
        this.status_read = status_read;
        return this;
    }
}