package ducqv.com.fooball.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ducqv.com.fooball.object.Comment;
import ducqv.com.fooball.object.Dangky;
import ducqv.com.fooball.object.Group;
import ducqv.com.fooball.object.Item;

/**
 * Created by TOSHIBA on 7/7/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "foobal";
    private static final int DATABASE_VERSION = 1;
    //table
    private static final String TABLE_NAME = "table_foobal";
    private static final String KEY_ID = "id_foobal";
    private static final String KEY_NAME = "name_foobal";
    //dang ky
    private static final String TABLE_DANGKY = "table_dangky";
    private static final String KEY_ID_DANGKY = "id_dangky";
    private static final String KEY_TEN_DANGKY = "name_dangky";
    private static final String KEY_EMAIL = "name_email";
    private static final String KEY_MATKHAU = "name_matkhau";
    private static final String KEY_NHAPLAIMATKHAU = "name_nhaplaimatkhau";
    //Group
    private static final String TABLE_GROUP = "table_group";
    private static final String KEY_ID_GROUP = "id_group";
    private static final String KEY_NAME_GROUP = "name_group";
    //Item
    private static final String TABLE_ITEM = "table_Item";
    private static final String KEY_ID_ITEM = "id_item";
    public static final String KEY_ID_GROUP_ITEM = "id_group_item";
    private static final String KEY_NAME_ITEM = "name_item";
    //Comment
    private static final String TABLE_COMMENT = "table_comment";
    private static final String KEY_ID_COMMENT = "id_item";
    public static final String KEY_ID_ITEM_COMMENT = "id_group_item_comment";
    private static final String KEY_MOTA = "name_mota";
    private static final String KEY_NHANXET = "name_nhanxet";
    private static final String KEY_IMAGE = "name_image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";

        String CREATE_DANGKY_TABLE = "CREATE TABLE " + TABLE_DANGKY + "("
                + KEY_ID_DANGKY + " INTEGER PRIMARY KEY,"
                + KEY_TEN_DANGKY + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_MATKHAU + " TEXT,"
                + KEY_NHAPLAIMATKHAU + " TEXT" + ")";

        String CREATE_GROUP_TABLE = "CREATE TABLE " + TABLE_GROUP + "("
                + KEY_ID_GROUP + " INTEGER PRIMARY KEY,"
                + KEY_NAME_GROUP + " TEXT" + ")";

        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
                + KEY_ID_ITEM + " INTEGER PRIMARY KEY,"
                + KEY_NAME_ITEM + " TEXT,"
                + KEY_ID_GROUP_ITEM + " INTEGER" + ")";

        String CREATE_COMMENT_TABLE = "CREATE TABLE " + TABLE_COMMENT + "("
                + KEY_ID_COMMENT + " INTEGER PRIMARY KEY,"
                + KEY_ID_ITEM_COMMENT + " INTEGER,"
                + KEY_MOTA + " TEXT,"
                + KEY_NHANXET + " TEXT,"
                + KEY_IMAGE + " TEXT" + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_DANGKY_TABLE);
        db.execSQL(CREATE_GROUP_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_COMMENT_TABLE);

        Log.d("CREATE_DANGKY_TABLE", "onCreate: " + CREATE_DANGKY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IP EXITS" + TABLE_NAME);
        db.execSQL("DROP TABLE IP EXITS" + TABLE_DANGKY);
        db.execSQL("DROP TABLE IP EXITS" + TABLE_GROUP);
        db.execSQL("DROP TABLE IP EXITS" + TABLE_ITEM);
        db.execSQL("DROP TABLE IP EXITS" + TABLE_COMMENT);
        onCreate(db);
    }

    public void addDangky(Dangky dangky) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEN_DANGKY, dangky.getTendangnhap());
        values.put(KEY_EMAIL, dangky.getEmail());
        values.put(KEY_MATKHAU, dangky.getMatkhau());
        values.put(KEY_NHAPLAIMATKHAU, dangky.getNhaplaimatkhau());
        db.insert(TABLE_DANGKY, null, values);
        db.close();
    }

    //thêm nhóm
    public void AddGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_GROUP, group.getNameGroup());
        Log.d("group.getNameGroup()", "AddGroup: " + group.getNameGroup());
        db.insert(TABLE_GROUP, null, values);
        db.close();
    }

    public void AddItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_ITEM, item.getNameItem());
        values.put(KEY_ID_GROUP_ITEM, item.getIdGroupItem());
        db.insert(TABLE_ITEM, null, values);
        Log.d("ducqv", "ducqv :" + values);
        db.close();
    }

    public void AddMota(Item itemmota) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_ITEM_COMMENT, itemmota.getIdItem());
        values.put(KEY_MOTA, itemmota.getMotaItem());
        db.insert(TABLE_COMMENT, null, values);
        db.close();
    }

    public void AddComment(Comment itemcomment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_ITEM_COMMENT, itemcomment.getIdItem());
        values.put(KEY_NHANXET, itemcomment.getNhanxet());
        values.put(KEY_IMAGE, itemcomment.getUrlImage());
        db.insert(TABLE_COMMENT, null, values);
        db.close();
    }

    public int updateGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_GROUP, group.getIdGroup());
        values.put(KEY_NAME_GROUP, group.getNameGroup());
        return db.update(TABLE_GROUP, values, KEY_ID_GROUP + "=?",
                new String[]{String.valueOf(group.getIdGroup())});
    }

    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_GROUP_ITEM, item.getIdGroupItem());
        values.put(KEY_ID_ITEM, item.getIdItem());
        values.put(KEY_NAME_ITEM, item.getNameItem());
        return db.update(TABLE_ITEM, values, KEY_ID_ITEM + "=?",
                new String[]{String.valueOf(item.getIdItem())});
    }

    public void UpdateIdGroup(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_GROUP_ITEM, item.getIdGroupItem());
        values.put(KEY_NAME_ITEM, item.getNameItem());
        db.update(TABLE_ITEM, values, KEY_ID_ITEM + "=" + item.getIdItem(), null);
    }

    public int updateNhanxet(Comment itemNhanxet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NHANXET, itemNhanxet.getNhanxet());
        values.put(KEY_IMAGE, itemNhanxet.getUrlImage());
        values.put(KEY_ID_COMMENT, itemNhanxet.getIdComment());
        Log.d("saveDb", "ducqv" + values);
        return db.update(TABLE_COMMENT, values, KEY_ID_COMMENT + " = ?",
                new String[]{String.valueOf(itemNhanxet.getIdComment())});

    }

    public void deleteGroup(Group idGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEM, KEY_ID_GROUP_ITEM + "=?", new String[]{String.valueOf(idGroup.getIdGroup())});
        db.delete(TABLE_GROUP, KEY_ID_GROUP + "=?", new String[]{String.valueOf(idGroup.getIdGroup())});
        Log.d("KEY_TITLE_GROUP", "deleteGroup: " + idGroup);
        db.close();
    }

    public void deleteItem(Item idItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMMENT, KEY_ID_ITEM_COMMENT + "=?", new String[]{String.valueOf(idItem.getIdItem())});
        db.delete(TABLE_ITEM, KEY_ID_ITEM + "=?", new String[]{String.valueOf(idItem.getIdItem())});
        db.close();

    }

    public void deleteCmt(int idComment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMMENT, KEY_ID_COMMENT + "=?", new String[]{String.valueOf(idComment)});
        db.close();
    }

    public List<Dangky> getAllDangky() {
        List<Dangky> contactList = new ArrayList<Dangky>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DANGKY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Dangky dangky = new Dangky();
                dangky.setId(Integer.parseInt(cursor.getString(0)));
                dangky.setTendangnhap(cursor.getString(1));
                dangky.setEmail(cursor.getString(2));
                dangky.setMatkhau(cursor.getString(3));
                dangky.setNhaplaimatkhau(cursor.getString(4));
                contactList.add(dangky);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    //Hàm getAllContacts() sẽ trả về 1 List gồm tất cả các nhom trong bảngra
    public List<Group> getAllGroup() {
        List<Group> groupList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("rawQuery", "getAllGroup: " + selectQuery);
        if (cursor.moveToFirst()) {
            do {
                Group group = new Group(cursor.getInt(0), cursor.getString(1));
                groupList.add(group);
            } while (cursor.moveToNext());
        }
        return groupList;
    }

    //lây ID cua từng Group
    public List<Item> getListItem(int idGroup) {
        List<Item> itemList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEM + " WHERE "
                + KEY_ID_GROUP_ITEM + "=" + idGroup + "";
        //"Select * from group_table_trello where id_group_item='idGroup'"
        Log.d("Ducqv", "query: " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("selectQueryrawQuery", "getListItem: " + selectQuery);
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(cursor.getInt(0)
                        , cursor.getString(1)
                        , cursor.getInt(2));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        return itemList;

    }

    // truyen id item vao de lay mota ra
    public String getMota(int idItem) {
        String mota = "";
        String selectQuery = "SELECT  * FROM " + TABLE_COMMENT + " WHERE "
                + KEY_ID_ITEM_COMMENT + "=" + idItem + "";
        Log.d("Ducqv", "query: " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                mota = cursor.getString(2);
            } while (cursor.moveToNext());
        }
        Log.d("iTEMQU", "ITEM :" + mota);
        return mota;
    }

    // get list nhan xet
    public List<Comment> getListCommentItem(int idItem) {
        Log.d("idIteme", "id : " + idItem);
        List<Comment> itemList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COMMENT + " WHERE "
                + KEY_ID_ITEM_COMMENT + "=" + idItem + "";
        Log.d("Ducqv", "query: " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Comment item = new Comment(cursor.getInt(0)
                        , cursor.getInt(1)
                        , cursor.getString(2)
                        , cursor.getString(3)
                        , cursor.getString(4));
                Log.d("iTEMQURE", "ITEM :" + item.toString());
                if (item.getNhanxet() != null) {
                    itemList.add(item);
                }
            } while (cursor.moveToNext());
        }
        Log.d("Ducqvcmt", "query: " + itemList);
        // return item list
        return itemList;
    }

}
