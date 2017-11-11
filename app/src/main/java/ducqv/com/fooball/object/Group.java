package ducqv.com.fooball.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TOSHIBA on 7/8/2017.
 */

public class Group implements Parcelable {
    private int idGroup;
    private String nameGroup;
    private String moveTitle;

    public Group() {

    }

    public Group(int idGroup, String nameGroup) {
        this.idGroup = idGroup;
        this.nameGroup = nameGroup;
    }

    protected Group(Parcel in) {
        idGroup = in.readInt();
        nameGroup = in.readString();
        moveTitle = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public int getIdGroup() {
        return idGroup;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idGroup);
        dest.writeString(nameGroup);
        dest.writeString(moveTitle);
    }
}
