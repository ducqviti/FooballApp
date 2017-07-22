package ducqv.com.fooball.action;

import android.view.View;


/**
 * Created by Administrator on 4/12/2016.
 */
public class Listener {

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface onEditCameraClickListenner {
        public void onCameraClick(View view, int position);
    }

    public interface OnUpdateListener {
        void onUpdate();
    }

}