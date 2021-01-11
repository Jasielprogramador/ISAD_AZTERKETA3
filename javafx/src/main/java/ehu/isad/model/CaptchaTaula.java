package ehu.isad.model;


import javafx.scene.image.Image;

public class CaptchaTaula {
    private Integer id;
    private String path;
    private Integer content;
    private String date;
    private Image irudia;

    public CaptchaTaula(Integer id, String path, Integer content, String date, Image irudia) {
        this.id = id;
        this.path = path;
        this.content = content;
        this.date = date;
        this.irudia = irudia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getContent() {
        return content;
    }

    public void setContent(Integer content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Image getIrudia() {
        return irudia;
    }

    public void setIrudia(Image irudia) {
        this.irudia = irudia;
    }
}
        