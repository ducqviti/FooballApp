package ducqv.com.fooball.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import ducqv.com.fooball.R;
import ducqv.com.fooball.activity.MainActivity;
import ducqv.com.fooball.db.DatabaseHelper;
import ducqv.com.fooball.object.Dangky;

public class Welcome extends BaseActivity implements View.OnClickListener {

    private DatabaseHelper dbHelper;
    private Dangky dangky;
    private List<Dangky> listDangky;
    private EditText edtTenDangNhap, edtMatkhau;
    private Button btnDangnhap, btnDangky;
    private TextView tvQuenmatkhau;
    private CheckBox cbHienThi, cbLuu;
    private LoginButton btnFacebook;
    private SignInButton btnGoogle;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private static final String TAG = Welcome.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        callbackManager = CallbackManager.Factory.create();
        dbHelper = new DatabaseHelper(this);
        listDangky = dbHelper.getAllDangky();
        edtTenDangNhap = (EditText) findViewById(R.id.edtTenDangNhap);
        edtMatkhau = (EditText) findViewById(R.id.edtMatkhau);
        cbHienThi = (CheckBox) findViewById(R.id.cbHienThi);
        cbLuu = (CheckBox) findViewById(R.id.cbLuu);
        tvQuenmatkhau = (TextView) findViewById(R.id.tvQuenmatkhau);
        btnDangnhap = (Button) findViewById(R.id.btnDangnhap);
        btnDangky = (Button) findViewById(R.id.btnDangky);
        btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
        btnGoogle = (SignInButton) findViewById(R.id.btnGoogle);
        btnDangnhap.setOnClickListener(this);
        btnDangky.setOnClickListener(this);
        cbHienThi.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);

        String userName = mPreferenceHelper.getUserName();
        String passWord = mPreferenceHelper.getUsePass();
        edtTenDangNhap.setText(userName);
        edtMatkhau.setText(passWord);
        cbLuu.setChecked(mPreferenceHelper.isLuu());
        if (mPreferenceHelper.isLogin())
            btnDangnhap.performClick();

        keyHash();
        loginFacebook();
        checkFacebook();
    }


    // kiem tra ten trong databse
    private boolean checkNameDataBase(String name, String pass) {
        if (listDangky.size() > 0) {
            for (int i = 0; i < listDangky.size(); i++) {
                if (name.endsWith(listDangky.get(i).getTendangnhap())
                        && pass.endsWith(listDangky.get(i).getMatkhau())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDangnhap:
                Dangnhap();
                break;
            case R.id.btnDangky:
                Dangky();
                break;
            case R.id.tvQuenmatkhau:
                QuenmatKhau();
                break;
            case R.id.cbHienThi:
                CheckBoxHienThi();
                break;
            case R.id.btnGoogle:
                signInGoogle();
                break;
        }
    }

    void Dangky() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void Dangnhap() {
        String name = edtTenDangNhap.getText().toString();
        String pass = edtMatkhau.getText().toString();
        if (!checkNameDataBase(name, pass)) {
            Toast.makeText(this, "Bạn nhập sai Tên Đăng Nhập hoặc Mật Khẩu", Toast.LENGTH_LONG).show();
            return;
        }
        //Chuyen sang form Sub activity
        Intent in = new Intent(this, MainActivity.class);
        //chuyen du lieu sang form con
        in.putExtra("keyname", name);
        in.putExtra("keypass", pass);
        //bat dau khoi chay activitygit
        startActivity(in);
        // luu vao preferen de lau sau dang nhap ko phai nhap pass va use lan nua
        mPreferenceHelper.setUserName(name);
        mPreferenceHelper.setUsePass(pass);
        mPreferenceHelper.isLuu(cbLuu.isChecked());
        mPreferenceHelper.isLogin(true);
        finish();
    }

    void QuenmatKhau() {

    }

    void CheckBoxHienThi() {
        edtMatkhau.setInputType(129);
        if (cbHienThi.isChecked())
            edtMatkhau.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    //check face xem co con dang nhap hay k
    public void checkFacebook() {
        AccessToken token;
        token = AccessToken.getCurrentAccessToken();
        Log.d("ducqvdp", "token : " + token);
        if (token != null) {
            JSONObject json_object = new JSONObject();
            String name = json_object.optString("name");
            String email = json_object.optString("email");
            Intent intent = new Intent(Welcome.this, MainActivity.class);
            intent.putExtra("userName", name);
            intent.putExtra("useremail", email);
            startActivity(intent);
            finish();
        }
    }

    void keyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "ducqv.com.fooball",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    void loginFacebook() {
        btnFacebook.setReadPermissions("email");
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetailsFace(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("ducqvdp", "onCancel: ");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("ducqvdp", "onError: ");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("ducqvdp", "onActivityResult: " + callbackManager.onActivityResult(requestCode, resultCode, data));
        // Nhập kết quả trả về khi đăng nhập
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    protected void getUserDetailsFace(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            //lays name
                            JSONObject json_object,
                            GraphResponse response) {
                        Log.d("ducqvdp", "json_object : " + json_object);
                        String name = json_object.optString("name");
                        Log.d("ducqvdp", "name : " + name);
                        Intent intent = new Intent(Welcome.this, MainActivity.class);
                        intent.putExtra("userName", name);
                        startActivity(intent);
                        finish();
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    private void signInGoogle() {

        GoogleSignInOptions google = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
//   .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, google)
                .build();

        // Customizing G+ button
        btnGoogle.setSize(SignInButton.SIZE_STANDARD);
        btnGoogle.setScopes(google.getScopeArray());

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Intent i = new Intent(Welcome.this, MainActivity.class);
        startActivity(i);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            //Cập nhật giao diện khi đăng nhập thành công
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            Log.e(TAG, "Name: " + personName + ", email: " + email);

        }
    }
}
