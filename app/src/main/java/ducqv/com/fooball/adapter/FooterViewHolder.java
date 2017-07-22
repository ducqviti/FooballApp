package ducqv.com.fooball.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ducqv.com.fooball.R;


/**
 * Created by systena on 7/7/2017.
 */

public class FooterViewHolder extends RecyclerView.ViewHolder {
    protected TextView mTvAddItem;

    public FooterViewHolder(View itemView) {
        super(itemView);
        mTvAddItem = (TextView) itemView.findViewById(R.id.tv_add_item);
    }
}
