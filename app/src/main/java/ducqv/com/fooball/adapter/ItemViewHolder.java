package ducqv.com.fooball.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ducqv.com.fooball.R;


/**
 * Created by systena on 7/7/2017.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView mTv_info_Item;
    public ImageView mbtn_Menu_Item;
    public ItemViewHolder(View itemView) {
        super(itemView);
        mTv_info_Item = (TextView)itemView.findViewById(R.id.tv_info_item);
        mbtn_Menu_Item= (ImageView) itemView.findViewById(R.id.btn_Menu_Item);
    }



}
