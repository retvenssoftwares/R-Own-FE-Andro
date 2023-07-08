package app.retvens.rown.ApiRequest;

public class ChatDataClass {
    private String title;
    private String body;
    private String user_id;

    public ChatDataClass(String title, String body,String user_id) {
        this.title = title;
        this.body = body;
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getUser_id(){return  user_id;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUser_id(String user_id){this.user_id = user_id;}
}