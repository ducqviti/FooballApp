package ducqv.com.fooball.welcome;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import ducqv.com.fooball.db.DatabaseHelper;
import ducqv.com.fooball.unility.PreferenceHelper;

/**
 * Created by systena on 4/17/2017.
 */
public abstract class BaseActivity extends FragmentActivity {
    public PreferenceHelper mPreferenceHelper;
    public DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceHelper = new PreferenceHelper(this);
        db = new DatabaseHelper(this);
    }
}
