package app.retvens.rown.ApiRequest;

public class ImageNotificationDataClass {
    private String title;
    private String body;
    private String mediaUrl;

    public ImageNotificationDataClass(String title, String body, String mediaUrl) {
        this.title = title;
        this.body = body;
        this.mediaUrl = mediaUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}

