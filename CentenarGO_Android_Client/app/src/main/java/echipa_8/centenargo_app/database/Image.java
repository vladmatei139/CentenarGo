package echipa_8.centenargo_app.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

import java.util.Objects;

@Entity
public class Image {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "local_path")
    private String localPath;

    @ColumnInfo(name = "title")
    private String title;

    public Image(int id, String localPath, String title) {
        this.id = id;
        this.localPath = localPath;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return id == image.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
