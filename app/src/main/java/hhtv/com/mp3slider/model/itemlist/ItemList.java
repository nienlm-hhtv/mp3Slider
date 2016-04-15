package hhtv.com.mp3slider.model.itemlist;

/**
 * Created by nienb on 12/4/16.
 */
public class ItemList {
    private Integer id;
    private String name;
    private String description;
    private Integer status;
    private String image;
    private String duration;

    public ItemList() {
    }

    public ItemList(Integer id, String name, String description, Integer status, String image, String duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.image = image;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
