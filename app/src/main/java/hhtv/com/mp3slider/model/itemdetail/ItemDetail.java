package hhtv.com.mp3slider.model.itemdetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nienb on 12/4/16.
 */
public class ItemDetail implements Serializable{
    private Integer status;
    private Integer id;
    private String audio;
    private List<Data> data = new ArrayList<Data>();

    public ItemDetail() {
    }

    public ItemDetail(Integer status, Integer id, String audio, List<Data> data) {
        this.status = status;
        this.id = id;
        this.audio = audio;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ItemDetail{" +
                "status=" + status +
                ", id=" + id +
                ", audio='" + audio + '\'' +
                ", data=" + data +
                '}';
    }
}
