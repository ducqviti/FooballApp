package ducqv.com.fooball.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ducqv on 7/23/2017.
 */

public class Dangky implements Parcelable {
    private int id;
    private String tendangnhap;
    private String email;
    private String matkhau;
    private String nhaplaimatkhau;

    public Dangky() {
    }

    public Dangky(String tendangnhap, String email, String matkhau, String nhaplaimatkhau) {
        this.tendangnhap = tendangnhap;
        this.email = email;
        this.matkhau = matkhau;
        this.nhaplaimatkhau = nhaplaimatkhau;
    }

    protected Dangky(Parcel in) {
        id = in.readInt();
        tendangnhap = in.readString();
        email = in.readString();
        matkhau = in.readString();
        nhaplaimatkhau = in.readString();
    }

    public static final Creator<Dangky> CREATOR = new Creator<Dangky>() {
        @Override
        public Dangky createFromParcel(Parcel in) {
            return new Dangky(in);
        }

        @Override
        public Dangky[] newArray(int size) {
            return new Dangky[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTendangnhap() {
        return tendangnhap;
    }

    public void setTendangnhap(String ten) {
        this.tendangnhap = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getNhaplaimatkhau() {
        return nhaplaimatkhau;
    }

    public void setNhaplaimatkhau(String nhaplaimatkhau) {
        this.nhaplaimatkhau = nhaplaimatkhau;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(tendangnhap);
        dest.writeString(email);
        dest.writeString(matkhau);
        dest.writeString(nhaplaimatkhau);
    }
}
