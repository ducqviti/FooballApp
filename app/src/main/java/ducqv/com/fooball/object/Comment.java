package ducqv.com.fooball.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mrtran on 7/6/17.
 */

public class Comment implements Parcelable {
    private int idComment;
    private int idItem;
    private String nhanxet;
    private String mota;
    private String urlImage;
// doi tuong nay co nhung gi
    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Comment() {

    }

    public Comment(int idComment, int idItem,String mota, String nhanxet,String urlImage) {
        this.idComment = idComment;
        this.idItem = idItem;
        this.nhanxet = nhanxet;
        this.mota = mota;
        this.urlImage = urlImage;
    }

    protected Comment(Parcel in) {
        idComment = in.readInt();
        idItem = in.readInt();
        nhanxet = in.readString();
        mota = in.readString();
        urlImage=in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public void setNhanxet(String nhanxet) {
        this.nhanxet = nhanxet;
    }

    public int getIdComment() {
        return idComment;
    }

    public int getIdItem() {
        return idItem;
    }

    public String getNhanxet() {
        return nhanxet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idComment);
        dest.writeInt(idItem);
        dest.writeString(nhanxet);
        dest.writeString(mota);
        dest.writeString(urlImage);


    }
}
