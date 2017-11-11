package ducqv.com.fooball.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TOSHIBA on 7/9/2017.
 */

public class Item implements Parcelable {

    private int idItem;
    private String  nameItem;
    private int idGroupItem;
    private String motaItem;
    private String nhanxetItem;

    public Item() {
    }

    public Item(int idItem, String nameItem, int idGroupItem) {
        this.idItem = idItem;
        this.nameItem = nameItem;
        this.idGroupItem = idGroupItem;
    }

    protected Item(Parcel in) {
        idItem = in.readInt();
        nameItem = in.readString();
        idGroupItem = in.readInt();
        motaItem = in.readString();
        nhanxetItem = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public int getIdGroupItem() {
        return idGroupItem;
    }

    public void setIdGroupItem(int idGroupItem) {
        this.idGroupItem = idGroupItem;
    }

    public String getMotaItem() {
        return motaItem;
    }

    public void setMotaItem(String motaItem) {
        this.motaItem = motaItem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idItem);
        dest.writeString(nameItem);
        dest.writeInt(idGroupItem);
        dest.writeString(motaItem);
        dest.writeString(nhanxetItem);
    }
    @Override
    public String toString() {
        return "TitleItem :" + getNameItem() + " : "
                + "IdGroupItem : " + getIdGroupItem() + " : "
                + "idItem :" + getIdItem();
    }
}
