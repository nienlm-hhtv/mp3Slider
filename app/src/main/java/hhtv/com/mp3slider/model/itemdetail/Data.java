package hhtv.com.mp3slider.model.itemdetail;

/**
 * Created by nienb on 12/4/16.
 */
public class Data {
    private Integer position;
    private String image;
    private Integer start;
    private Integer end;

    public Data() {
    }

    public Data(Integer position, String image, Integer start, Integer end) {
        this.position = position;
        this.image = image;
        this.start = start;
        this.end = end;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }
}
