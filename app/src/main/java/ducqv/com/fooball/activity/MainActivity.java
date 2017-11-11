package ducqv.com.fooball.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import ducqv.com.fooball.R;
import ducqv.com.fooball.action.Listener;
import ducqv.com.fooball.adapter.AdapterGroup;
import ducqv.com.fooball.db.DatabaseHelper;
import ducqv.com.fooball.object.Group;
import ducqv.com.fooball.unility.PreferenceHelper;
import ducqv.com.fooball.welcome.Welcome;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper dbHelper;
    private AdapterGroup madapterGroup;
    private RecyclerView mRecyclerViewGroup;
    private PreferenceHelper mPreferenceHelper;
    ImageView mimg_Menu_Main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferenceHelper = new PreferenceHelper(this);
        init();
        getInfoGroup();
    }

    public void getInfoGroup() {
        madapterGroup.setList(dbHelper.getAllGroup());
    }

    private void init() {
        FloatingActionButton maddGroup = (FloatingActionButton) findViewById(R.id.add_group);
        maddGroup.setOnClickListener(this);
        ImageView mbtnBack = (ImageView) findViewById(R.id.btnBack);
        mbtnBack.setOnClickListener(this);

        TextView mtvNameItem = (TextView) findViewById(R.id.tvNameItem);
//        mtvNameItem.setText(mPreferenceHelper.getUserName());
        String name = getIntent().getStringExtra("userName");
        mtvNameItem.setText(name);

        mimg_Menu_Main = (ImageView) findViewById(R.id.img_Menu_Main);
        mimg_Menu_Main.setOnClickListener(this);
        dbHelper = new DatabaseHelper(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);

        mRecyclerViewGroup = (RecyclerView) findViewById(R.id.recycle_group);
        mRecyclerViewGroup.setLayoutManager(layoutManager);
        madapterGroup = new AdapterGroup(this);
        mRecyclerViewGroup.setAdapter(madapterGroup);
        madapterGroup.setOnUpdateListener(new Listener.OnUpdateListener() {
            @Override
            public void onUpdate() {
                getInfoGroup();
            }
        });
        getInfoGroup();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_group:
                addGroup();
                break;
            case R.id.btnBack:
                AlertDialog();
                break;
            case R.id.img_Menu_Main:
                showMenuMain(mimg_Menu_Main);
                break;
        }
    }

    public void addGroup() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialoglayout = inflater.inflate(R.layout.layout_add, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set dialog vao View
        builder.setView(dialoglayout);
        //title cua dialog
        builder.setTitle("Thêm Nhóm");
        final EditText addTitle = (EditText) dialoglayout.findViewById(R.id.tvAdd);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isValidAdd(addTitle.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Bạn không được để trống", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                Group group = new Group();
                group.setNameGroup(addTitle.getText().toString());
                dbHelper.AddGroup(group);
                getInfoGroup();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();
        System.exit(0);
    }

    void AlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setTitle("Ban chắc chắn muốn thoát khỏi chương trình ?");
        builder
                .setMessage("Bấm vào Yes để thoát")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showMenuMain(final ImageView mimg_Menu_Main) {
        mimg_Menu_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, mimg_Menu_Main);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem groupMenu) {
                        switch (groupMenu.getItemId()) {
                            case R.id.action_settings:
                                break;
                            case R.id.action_logout:
                                mPreferenceHelper.isLogin(false);
                                LoginManager.getInstance().logOut();
//                                AccessToken.setCurrentAccessToken(null);
                                Intent intent = new Intent(MainActivity.this, Welcome.class);
                                startActivity(intent);
                                finish();
                                break;
                        }
                        return false;
                    }
                });
                // Hiển thị menu
                popupMenu.show();
            }
        });
    }

    public boolean isValidAdd(String add) {
        return add.isEmpty();
    }

}
