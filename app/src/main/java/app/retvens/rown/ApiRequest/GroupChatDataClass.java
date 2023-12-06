package app.retvens.rown.ApiRequest;

public class GroupChatDataClass {
    private String title;
    private String body;
    private String group_id;
    private String sender_user_id;

    public GroupChatDataClass(String title, String body, String group_id, String sender_user_id) {
        this.title = title;
        this.body = body;
        this.group_id = group_id;
        this.sender_user_id = sender_user_id;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getGroup_id() {
        return group_id;
    }

    public String getSender_user_id() {
        return sender_user_id;
    }

    // Setter methods (if needed)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public void setSender_user_id(String sender_user_id) {
        this.sender_user_id = sender_user_id;
    }


}

