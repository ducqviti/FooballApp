package ducqv.com.fooball.adapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import java.security.acl.Group;
import java.util.List;
import ducqv.com.fooball.R;
import ducqv.com.fooball.activity.MainActivity;
import ducqv.com.fooball.db.DatabaseHelper;

/**
 * Created by TOSHIBA on 7/2/2017.
 */

public class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ducqv.com.fooball.object.Group> listData;
    OntitleClick onTitleClick;
    Group group;
    MainActivity mainActivity;
    DatabaseHelper dbHelper;

    public DialogAdapter(List<ducqv.com.fooball.object.Group> arrayGroup) {
        this.listData = arrayGroup;

    }

    public void updateList(List<ducqv.com.fooball.object.Group> data) {
        listData = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.move_title, parent, false);
        return new DialogAdapter.RecyclerViewHolder(itemView);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
            implements AdapterView.OnItemClickListener {
        public TextView mdialogTitle;
        public ImageView mbtn_Menu_Item;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mdialogTitle = (TextView) itemView.findViewById(R.id.dialogTitle);
            mbtn_Menu_Item = (ImageView) itemView.findViewById(R.id.btn_Menu_Item);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }
    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, final int position) {
        ((DialogAdapter.RecyclerViewHolder) holder)
                .mdialogTitle.setText(listData.get(position).getNameGroup());
        Log.d("ducqvsdfsdf", "onBindViewHolder: " + listData.get(position).getNameGroup());

        ((DialogAdapter.RecyclerViewHolder) holder)
                .mdialogTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTitleClick.setOntitleClick(listData.get(position).getIdGroup());
                Log.d("setOntitle", "setOntitleClick: " + listData.get(position).getIdGroup());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public interface OntitleClick {
        void setOntitleClick(int idTitle);
    }

    public void setOntitleClickListen(OntitleClick onTitleClick) {
        this.onTitleClick = onTitleClick;
    }
}
