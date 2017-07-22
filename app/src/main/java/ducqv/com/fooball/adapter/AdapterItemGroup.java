package ducqv.com.fooball.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ducqv.com.fooball.R;
import ducqv.com.fooball.action.Listener;
import ducqv.com.fooball.activity.ItemActivity;
import ducqv.com.fooball.activity.MainActivity;
import ducqv.com.fooball.db.DatabaseHelper;
import ducqv.com.fooball.object.Group;
import ducqv.com.fooball.object.Item;

/**
 * Created by TOSHIBA on 7/8/2017.
 */

public class AdapterItemGroup extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<Item> listData = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private Context context;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private int idGroup;
    Listener.OnUpdateListener onUpdateListener;
    private RecyclerView mRecyvlerDialog;
    private DialogAdapter dialogAdapter;

    public AdapterItemGroup(List<Item> listData, Context context) {
        this.listData = listData;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void updateItemGroup(List<Item> data, int idGroup) {
        listData = data;
        this.idGroup = idGroup;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View layoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_layout, parent, false);
            return new FooterViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM) {

            View layoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_item, parent, false);
            return new ItemViewHolder(layoutView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.d("huycii", "listData : " + listData.size());
        Log.d("huykoicvoidvjdghuib", "onBindViewHolder");
        if (holder instanceof FooterViewHolder) {
            Log.d("huykoicvoidvjdghuib", "FooterViewHolder");

            ((FooterViewHolder) holder).mTvAddItem.setText("Thêm Thẻ");
            ((FooterViewHolder) holder).mTvAddItem.setOnClickListener(this);
        } else if (holder instanceof ItemViewHolder) {
            Log.d("huykoicvoidvjdghuib", "ItemViewHolder");
            //xác định vi tri item can lay
            final Item item = listData.get(position);
            ((ItemViewHolder) holder).mTv_info_Item.setText(item.getNameItem());
            ((ItemViewHolder) holder).mTv_info_Item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ItemActivity.class);
                    intent.putExtra("item", item);
                    context.startActivity(intent);
                }
            });
            ((ItemViewHolder) holder).mbtn_Menu_Item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(((ItemViewHolder) holder).mbtn_Menu_Item, listData.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position))
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == listData.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_item:
                showDialogAddITem(idGroup);
                break;
        }
    }

    private void showDialogAddITem(final int idGroup) {
        Log.d("ducqvsfdfs", "ducqv idGroup :" + idGroup);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialoglayout = inflater.inflate(R.layout.layout_add, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //set dialog vao View
        builder.setView(dialoglayout);
        builder.setTitle(context.getString(R.string.add_item));

        final EditText additem = (EditText) dialoglayout.findViewById(R.id.tvAdd);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isValidAdd(additem.getText().toString())) {
                    Toast.makeText(context, "Bạn không thể để tên thẻ trống", Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                Item item = new Item();
                item.setNameItem(additem.getText().toString());
                item.setIdGroupItem(idGroup);
                Log.d("setOntitleClick1111222", "setOntitleClick: " + idGroup);
                dbHelper.AddItem(item);
                listData = dbHelper.getListItem(idGroup);
                notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private boolean isValidAdd(String add) {
        return !add.isEmpty();
    }

    private void showPopupMenu(final ImageView mbtn_Menu_Item, final Item item) {
        mbtn_Menu_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, mbtn_Menu_Item);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_item, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem itemMenu) {
                        switch (itemMenu.getItemId()) {
                            case R.id.editItem:
                                showDialogEditItem(item);
                                break;
                            case R.id.deleteItem:
                                showDialogDeleteItem(item);
                                break;
                            case R.id.moveItem:
                                showDialogMoveItem(item);
                                break;
                            case R.id.doneItem:
                                dbHelper.close();
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

    void showDialogEditItem(final Item item) {
        LayoutInflater inflaterEdit = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialoglayout = inflaterEdit.inflate(R.layout.layout_add, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialoglayout);
        builder.setTitle("Sửa Tên Thẻ");
        final EditText edititem = (EditText) dialoglayout.findViewById(R.id.tvAdd);
        //lay ten item
        edititem.setText(item.getNameItem().toString());
        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isValidAdd(edititem.getText().toString())) {
                    Toast.makeText(context, "Không thể để tên nhóm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                //set title moi lay tu additem
                item.setNameItem(edititem.getText().toString());
                Log.d("setNameItem", "onClick: "+edititem.getText().toString());
                notifyDataSetChanged();
                dbHelper.updateItem(item);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    void showDialogDeleteItem(final Item item) {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setTitle("Xoá");
        alertDialog.setMessage("Bạn muốn xoá " + item.getNameItem().toString()
                + " khỏi danh sách ?");
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.show();
                        dbHelper.deleteItem(item);
                        Log.d("dbHelperdeleteGroup", "onClick: " + item);
                        listData = dbHelper.getListItem(idGroup);
                        notifyDataSetChanged();
                        ((MainActivity) context).getInfoGroup();
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

    void showDialogMoveItem(final Item item) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_title);
        dialog.setTitle("Chuyển");
        mRecyvlerDialog = (RecyclerView) dialog.findViewById(R.id.RecyclerDialog);
        mRecyvlerDialog.setLayoutManager(new LinearLayoutManager(context));
        dialogAdapter = new DialogAdapter(dbHelper.getAllGroup());
        mRecyvlerDialog.setAdapter(dialogAdapter);
        dialogAdapter.setOntitleClickListen(new DialogAdapter.OntitleClick() {
            @Override
            public void setOntitleClick(int idTitle) {
                Log.d("setOntitleClick1111", "setOntitleClick: " + idTitle);
                // thay ko

                item.setIdGroupItem(idTitle);
                dbHelper.UpdateIdGroup(item);
                onUpdateListener.onUpdate();
                notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public void setOnUpdateListener(Listener.OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }
}
