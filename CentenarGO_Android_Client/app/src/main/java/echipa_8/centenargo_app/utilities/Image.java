package echipa_8.centenargo_app.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Image implements Parcelable {

    private Integer id;
    private String title;
    private String path;
    private String username;

    public Image(Integer id, String title, String path, String username) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(path);
        dest.writeString(username);
    }

    private Image(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.path = in.readString();
        this.username = in.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
