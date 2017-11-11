package ducqv.com.fooball.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import ducqv.com.fooball.R;
import ducqv.com.fooball.db.DatabaseHelper;
import ducqv.com.fooball.object.Dangky;

public class Register extends BaseActivity implements View.OnClickListener {
    private EditText medit_Name_Register, medit_Email_Register, medit_Nhappass, medit_Nhaplaipass;
    private Button mbtCancel, mbtRegister;
    private DatabaseHelper db;
    private List<Dangky> listDangky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        listDangky = db.getAllDangky();

        medit_Name_Register = (EditText) findViewById(R.id.edit_Name_Register);
        medit_Email_Register = (EditText) findViewById(R.id.edit_Email_Register);
        medit_Nhappass = (EditText) findViewById(R.id.edit_Nhappass);
        medit_Nhaplaipass = (EditText) findViewById(R.id.edit_Nhaplaipass);
        mbtCancel = (Button) findViewById(R.id.btCancel);
        mbtRegister = (Button) findViewById(R.id.btRegister);

        mbtCancel.setOnClickListener(this);
        mbtRegister.setOnClickListener(this);
    }

    private boolean checkName(String name) {
        if (listDangky.size() > 0) {
            for (int i = 0; i < listDangky.size(); i++) {
                if (name.endsWith(listDangky.get(i).getTendangnhap())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCancel:
                finish();
                break;
            case R.id.btRegister:
                if (checkName(medit_Name_Register.getText().toString())) {
                    if ((medit_Name_Register.getText().toString()).isEmpty()) {
                        Toast.makeText(this, "Tên tên đăng nhâp trống", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (medit_Nhappass.getText().toString().equals(medit_Nhaplaipass.getText().toString())) {
                        mPreferenceHelper.setUserName(medit_Name_Register.getText().toString());
                        mPreferenceHelper.setUsePass(medit_Nhappass.getText().toString());
                        db.addDangky(new Dangky(
                                medit_Name_Register.getText().toString()
                                , medit_Email_Register.getText().toString()
                                , medit_Nhappass.getText().toString()
                                , medit_Nhaplaipass.getText().toString()
                        ));
                        Intent in = new Intent(this, Welcome.class);
                        startActivity(in);
                        //clear text trong editext khi chuyen activity
                        medit_Name_Register.getText().clear();
                        medit_Email_Register.getText().clear();
                        medit_Nhappass.getText().clear();
                        medit_Nhaplaipass.getText().clear();

                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Mật khẩu không trùng nhau", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
