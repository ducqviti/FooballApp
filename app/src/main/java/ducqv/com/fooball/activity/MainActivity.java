package ducqv.com.fooball.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import ducqv.com.fooball.R;
import ducqv.com.fooball.action.Listener;
import ducqv.com.fooball.adapter.AdapterGroup;
import ducqv.com.fooball.db.DatabaseHelper;
import ducqv.com.fooball.object.Group;
import ducqv.com.fooball.object.Item;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper dbHelper;
    private AdapterGroup madapterGroup;
    private RecyclerView mRecyclerViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                finish();
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
                    Toast.makeText(MainActivity.this,
                            "Bạn không được để trống",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                Group group = new Group();
                group.setNameGroup(addTitle.getText().toString());
                Log.d("addgroup", "onClick: " + addTitle.getText().toString());
                dbHelper.AddGroup(group);
                getInfoGroup();
            }
        });
        builder.show();
    }
    public boolean isValidAdd(String add) {
        return add.isEmpty();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
