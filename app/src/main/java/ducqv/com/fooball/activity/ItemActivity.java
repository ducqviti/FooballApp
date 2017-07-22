package ducqv.com.fooball.activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ducqv.com.fooball.R;
import ducqv.com.fooball.action.Listener;
import ducqv.com.fooball.adapter.AdapterItem;
import ducqv.com.fooball.db.DatabaseHelper;
import ducqv.com.fooball.object.Comment;
import ducqv.com.fooball.object.Item;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHelper dbHelper;
    private Item objItem;
    private Comment objComment;
    private AdapterItem adapterItem;
    private List<Item> listData = new ArrayList<Item>();
    private ImageView mbtnBack, mimg_clean, mimg_Menu, mimg_Check, mbtn_Cmt, mim_camera, mimg_Anhchup, mget_image;
    private TextView mtvNameItemCm;
    private EditText mtv_mota, mtv_Nhanxet;
    private Button mbtnSavemt;
    private RecyclerView mRecyclerViewCmt;
    public static final int SHOW_CAMERA = 1;
    public static final int SHOW_EDIT_CAMERA = 2;
    public static final int LOAD_IMAGE_CAPTURE = 3;
    public static final int EDIT_IMAGE_CAPTURE = 4;

    private String nameImage;// khoi tao bien de gan tên của ảnh
    private Comment objCommentLongClick;
    private Boolean checkCap = false;
    private ImageView editImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        dbHelper = new DatabaseHelper(this);
        //chuyen obj
        objItem = getIntent().getParcelableExtra("item");

        LinearLayoutManager layoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);
        listData = new ArrayList<>();
        mRecyclerViewCmt = (RecyclerView) findViewById(R.id.RecyvlerView_Cmt);
        mRecyclerViewCmt.setLayoutManager(layoutManager);
        Log.d("layoutManager", "onCreate: " + layoutManager);
        adapterItem = new AdapterItem(dbHelper.getListCommentItem(objItem.getIdItem()), this, dbHelper);
        Log.d("AdapterItem", "onCreate: " + objItem.getIdItem());
        mRecyclerViewCmt.setAdapter(adapterItem);
        Log.d("adapterItem", "onCreate: " + adapterItem);
        mRecyclerViewCmt.setNestedScrollingEnabled(false);

        mbtnBack = (ImageView) findViewById(R.id.btnBack);
        mimg_clean = (ImageView) findViewById(R.id.img_clean);
        mimg_Menu = (ImageView) findViewById(R.id.img_Menu);
        mimg_Check = (ImageView) findViewById(R.id.img_Check);
        mim_camera = (ImageView) findViewById(R.id.im_camera);
        mimg_Anhchup = (ImageView) findViewById(R.id.img_Anhchup);
        mget_image = (ImageView) findViewById(R.id.get_image);

        mbtnBack.setOnClickListener(this);
        mimg_clean.setOnClickListener(this);
        mimg_Menu.setOnClickListener(this);
        mimg_Check.setOnClickListener(this);
        mim_camera.setOnClickListener(this);
        mget_image.setOnClickListener(this);

        mtvNameItemCm = (TextView) findViewById(R.id.tvNameItemCm);
        mtvNameItemCm.setText(objItem.getNameItem());

        Log.d("mtvNameItemCm", "onCreate: " + (objItem.getNameItem()));

        mtv_mota = (EditText) findViewById(R.id.tv_mota);
        mbtnSavemt = (Button) findViewById(R.id.btn_Save_mt);
        mbtnSavemt.setOnClickListener(this);

        mtv_Nhanxet = (EditText) findViewById(R.id.tv_Nhanxet);
        mbtn_Cmt = (ImageView) findViewById(R.id.btn_Cmt);
        mbtn_Cmt.setOnClickListener(this);

        getMota();
        getComment();
    }

    public void updateList(List<Item> data) {
        listData = data;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btn_Save_mt:
                ClickSavemota();
                getMota();
                break;
            case R.id.btn_Cmt:
                if (checkCap) {
                    ClickSaveCmt(SHOW_CAMERA);
                    checkCap = false;

                } else
                    ClickSaveCmt(LOAD_IMAGE_CAPTURE);
                getComment();
                break;
            case R.id.im_camera:
                showMayAnh(SHOW_CAMERA);
                checkCap = true;
                break;
            case R.id.get_image:
                getImage();
                break;
            case R.id.editImage:
                showMayAnh(SHOW_EDIT_CAMERA);
                break;
        }
    }

    // get mota tu databsae theo id item
    void getMota() {
        mtv_mota.setText(dbHelper.getMota(objItem.getIdItem()));
    }

    public void getComment() {
        List<Comment> listComment = dbHelper.getListCommentItem(objItem.getIdItem());
        adapterItem.updateList(listComment);
        adapterItem.notifyDataSetChanged();
        Log.d("getListCommentItem", "ducqv: " + objItem.getIdItem());
    }

    void ClickSavemota() {
        objItem.setMotaItem(mtv_mota.getText().toString());
        dbHelper.AddMota(objItem);
        Log.d("Addmota", "onClick: " + objItem);

        if (mRecyclerViewCmt != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mRecyclerViewCmt.getWindowToken(), 0);
        }
    }

    void ClickSaveCmt(int click) {
        if (isValidNhanxet(mtv_Nhanxet.getText().toString().trim())) {
            mtv_Nhanxet.setError("Bạn chưa nhập nội dung");
            return;
        }
        //update vao database
        Comment comment = new Comment();
        comment.setIdItem(objItem.getIdItem());

        switch (click) {
            case SHOW_CAMERA:
                comment.setUrlImage(nameImage);
                break;
            case LOAD_IMAGE_CAPTURE:
                comment.setUrlImage(nameImage);
                break;
        }
        comment.setNhanxet(mtv_Nhanxet.getText().toString());
        dbHelper.AddComment(comment);
        mtv_Nhanxet.setText("");
        mimg_Anhchup.setImageBitmap(null);
        nameImage = "";

        //lam ẩn ban phim khi ban enter
        if (mRecyclerViewCmt != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mRecyclerViewCmt.getWindowToken(), 0);
        }

    }

    public void showMayAnh(int type) {

        // Mở camera mặc định
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Tiến hành gọi Capture Image intent
        startActivityForResult(intent, type);
    }

    public void getImage() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, LOAD_IMAGE_CAPTURE);
    }

    public void clickLong(Comment comment) {
        objCommentLongClick = comment;
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EDIT_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("huykoivio", "rq : " + requestCode);
        switch (requestCode) {
            case SHOW_CAMERA:
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                saveToInternalStorage(bitmap);
                mimg_Anhchup.setImageBitmap(bitmap);
                break;

            case SHOW_EDIT_CAMERA:
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                saveToInternalStorage(bm);

                editImage.setImageBitmap(bm);

                break;
        }
        if (requestCode == LOAD_IMAGE_CAPTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            nameImage = cursor.getString(columnIndex);
            Log.d("ducqv1234", "onActivityResult: " + cursor.getString(columnIndex));
            cursor.close();
            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mimg_Anhchup.setImageBitmap(bmp);
        }
        if (requestCode == EDIT_IMAGE_CAPTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            nameImage = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.imhienthi);
            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bmp);
            //lay obj Comment
            objCommentLongClick.setUrlImage(nameImage);
            dbHelper.updateNhanxet(objCommentLongClick);
            getComment();
        }
    }

    //chup anh
    public String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
// gan ten anh tại thoi diem minh luu
        File mypath = new File(directory, String.valueOf(System.currentTimeMillis()) + ".jpg");
        nameImage = mypath.getPath();
        Log.d("trantp", "saveToInternalStorage: " + nameImage);
// luu duong dan vao database
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    public void showdialogEditComment(final Comment comment) {
        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialoglayout = inflater.inflate(R.layout.layout_edit, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialoglayout);
        builder.setTitle("Sửa Nhận Xét");
        final EditText additem = (EditText) dialoglayout.findViewById(R.id.tvEdit);
        //lay title tu
        additem.setText(comment.getNhanxet());
        Log.d("Ducdp", "getNhanxet: "+ comment.getNhanxet());
        editImage = (ImageView) dialoglayout.findViewById(R.id.editImage);

        editImage.setImageBitmap(BitmapFactory.decodeFile(comment.getUrlImage()));
        Log.d("Ducdp", "showdialogEditComment: "+BitmapFactory.decodeFile(comment.getUrlImage()));

        editImage.setOnClickListener(this);
        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isValidNhanxet(additem.getText().toString())) {
                    Toast.makeText(ItemActivity.this, "Không có nội dung comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                comment.setUrlImage(nameImage);
                comment.setNhanxet(additem.getText().toString());
                dbHelper.updateNhanxet(comment);
                Log.d("trantp", "onClick: " + dbHelper.updateNhanxet(comment));
                adapterItem.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private boolean isValidNhanxet(String nhanxet) {
        return nhanxet.isEmpty();
    }

}
