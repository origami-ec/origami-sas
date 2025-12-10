package org.origami.docs.dto;
public class QRCodeRequest {

    private String text;
    private String title;
    private String footer;
    private int width = 300;
    private int height = 300 ;

    public QRCodeRequest() {
    }

    public QRCodeRequest(String text) {
        this.text = text;
    }

    public QRCodeRequest(String text, String title, String footer) {
        this.text = text;
        this.title = title;
        this.footer = footer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
