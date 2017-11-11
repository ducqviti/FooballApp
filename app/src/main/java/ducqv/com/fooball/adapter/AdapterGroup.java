package ducqv.com.fooball.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ducqv.com.fooball.R;
import ducqv.com.fooball.action.Listener;
import ducqv.com.fooball.activity.MainActivity;
import ducqv.com.fooball.db.DatabaseHelper;
import ducqv.com.fooball.object.Group;
import ducqv.com.fooball.object.Item;

/**
 * Created by TOSHIBA on 7/8/2017.
 */

public class AdapterGroup extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Group> listData = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private Context context;
    private ArrayList<Item> itemArrayList;
    Listener.OnUpdateListener onUpdateListener;

    public AdapterGroup(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void setList(List<Group> data) {
        listData.clear();
        listData.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnUpdateListener(Listener.OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View groupView = inflater.inflate(R.layout.layout_group, parent, false);
        return new AdapterGroup.RecyclerViewHolder(groupView);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
            implements AdapterView.OnItemClickListener {
        private TextView mtvGroup;
        private ImageView mbtnMenuGroup;
        private RecyclerView mrecycle_Item_Group;
        AdapterItemGroup madapterItemGroup;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mbtnMenuGroup = (ImageView) itemView.findViewById(R.id.btnMenuGroup);
            mtvGroup = (TextView) itemView.findViewById(R.id.tvGroup);
            itemArrayList = new ArrayList<>();
            mrecycle_Item_Group = (RecyclerView) itemView.findViewById(R.id.recycle_item);
            mrecycle_Item_Group.setLayoutManager(new GridLayoutManager(context, 1));
            madapterItemGroup = new AdapterItemGroup(itemArrayList, context);
            mrecycle_Item_Group.setHasFixedSize(true);
            mrecycle_Item_Group.setAdapter(madapterItemGroup);
            madapterItemGroup.setOnUpdateListener(onUpdateListener);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }

        private void updateItemGroup(int position) {
            madapterItemGroup.updateItemGroup(dbHelper.getListItem(listData.get(position)
                    .getIdGroup()), listData.get(position).getIdGroup());
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((RecyclerViewHolder) holder).mtvGroup.setText(listData.get(position).getNameGroup());
        Log.d("mtvGroup", "onBindViewHolder: " + listData.get(position).getNameGroup());

        ((RecyclerViewHolder) holder).mbtnMenuGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(((RecyclerViewHolder) holder).mbtnMenuGroup, listData.get(position));
            }
        });
        ((RecyclerViewHolder) holder).updateItemGroup(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    private void showPopupMenu(final ImageView mbtnMenuGroup, final Group group) {
        mbtnMenuGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, mbtnMenuGroup);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_group, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem groupMenu) {
                        switch (groupMenu.getItemId()) {
                            case R.id.editGroup:
                                showDialogEditGroup(group);
                                break;
                            case R.id.deleteGroup:
                                showDialogDeleteGroup(group);
                                break;
                            case R.id.doneGroup:
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

    void showDialogEditGroup(final Group group) {
        LayoutInflater inflaterEdit = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialoglayout = inflaterEdit.inflate(R.layout.layout_add, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialoglayout);
        builder.setTitle("Sửa Tên Nhom");
        builder.setCancelable(false);
        final EditText edititem = (EditText) dialoglayout.findViewById(R.id.tvAdd);
        //lay ten group
        edititem.setText(group.getNameGroup().toString());
        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isValidAdd(edititem.getText().toString())) {
                    Toast.makeText(context, "Không thể thay tên nhóm trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                //set title moi lay tu additem
                group.setNameGroup(edititem.getText().toString());
                // trong doi tuong group nay no da co id roi nen minh ko phai getid cho no nua
                dbHelper.updateGroup(group);
                ((MainActivity) context).getInfoGroup();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    void showDialogDeleteGroup(final Group group) {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setTitle("Xoá");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Bạn muốn xoá " + group.getNameGroup().toString()
                + " khỏi danh sách ?");
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.show();
                        dbHelper.deleteGroup(group);
                        Log.d("dbHelper.deleteGroup", "onClick: " + group);
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

    private boolean isValidAdd(String add) {
        return add.isEmpty();
    }
}
