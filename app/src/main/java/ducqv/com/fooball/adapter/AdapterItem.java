package ducqv.com.fooball.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ducqv.com.fooball.R;
import ducqv.com.fooball.activity.ItemActivity;
import ducqv.com.fooball.db.DatabaseHelper;
import ducqv.com.fooball.object.Comment;

/**
 * Created by TOSHIBA on 7/11/2017.
 */

public class AdapterItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Comment> listData = new ArrayList<>();
    private ItemActivity activity;
    private DatabaseHelper dbHelper;
    private int idComment;

    public AdapterItem(List<Comment> listData, ItemActivity activity, DatabaseHelper dbHelper) {
        this.listData = listData;
        this.activity = activity;
        this.dbHelper = dbHelper;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.layout_comment, parent, false);
        return new RecyclerViewHolder(itemView);

    }

    public void updateList(List<Comment> data) {
        listData = data;
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
            implements AdapterView.OnItemClickListener {
        public ImageView mimg_Avarta, mimg_Menu_coment;
        public TextView mtv_Hienthi_coment, mtv_Name_coment;
        public ImageView mimhienthi;
        public LinearLayout mlayout_cmt;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mlayout_cmt = (LinearLayout) itemView.findViewById(R.id.layout_cmt);
            mimg_Avarta = (ImageView) itemView.findViewById(R.id.img_Avarta);
            mimg_Menu_coment = (ImageView) itemView.findViewById(R.id.img_Menu_comment);
            mtv_Hienthi_coment = (TextView) itemView.findViewById(R.id.tv_Hienthi_coment);
            mtv_Name_coment = (TextView) itemView.findViewById(R.id.tv_Name_coment);
            mimhienthi = (ImageView) itemView.findViewById(R.id.imhienthi);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((RecyclerViewHolder) holder).mtv_Hienthi_coment.setText(listData.get(position).getNhanxet());
        ((RecyclerViewHolder) holder).mimhienthi
                .setImageBitmap(BitmapFactory.decodeFile(listData.get(position).getUrlImage()));

        ((RecyclerViewHolder) holder).mimg_Menu_coment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(((RecyclerViewHolder) holder).mimg_Menu_coment, listData.get(position));
            }
        });
        ((RecyclerViewHolder) holder).mimhienthi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_image);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                ImageView imageView = (ImageView) dialog.findViewById(R.id.imageview);
                imageView.setImageBitmap(BitmapFactory.decodeFile(listData.get(position).getUrlImage()));
                dialog.getWindow().setLayout(LinearLayoutCompat
                        .LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                dialog.show();
            }
        });
        ((RecyclerViewHolder) holder).mimhienthi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showMenuImage(((RecyclerViewHolder) holder).mimg_Menu_coment, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private void showPopupMenu(ImageView img, final Comment comment) {
        PopupMenu popupMenu = new PopupMenu(activity, img);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_cmt, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem CmtMenu) {
                switch (CmtMenu.getItemId()) {
                    case R.id.editcmt:
                        activity.showdialogEditComment(comment);
                        break;
                    case R.id.deletecmt:
                        showdialogDelete(comment);
                        break;
                    case R.id.donecmt:
                        dbHelper.close();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void showMenuImage(ImageView img, final int position) {
        PopupMenu popupMenu = new PopupMenu(activity, img);
        popupMenu.getMenuInflater().inflate(R.menu.menu_edit_image, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem CmtMenu) {
                switch (CmtMenu.getItemId()) {
                    case R.id.mn_chupanh:
                        setIdComment(listData.get(position).getIdComment());
                        activity.showMayAnh(activity.SHOW_EDIT_CAMERA);
                        Log.d("showMayAnh", "onMenuItemClick: " + activity.SHOW_EDIT_CAMERA);
                        break;
                    case R.id.mn_layanh:
                        activity.clickLong(listData.get(position));
                        Log.d("clickLong", "onLongClick: " + listData.get(position).toString());
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    void showdialogDelete(final Comment comment) {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                activity).create();
        alertDialog.setTitle("Xoá");
        alertDialog.setMessage(" Bạn chắc chắn muốn xoá ?");
        alertDialog.setCancelable(false);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.show();
                        dbHelper.deleteCmt(comment.getIdComment());
                        activity.getComment();
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }

}
