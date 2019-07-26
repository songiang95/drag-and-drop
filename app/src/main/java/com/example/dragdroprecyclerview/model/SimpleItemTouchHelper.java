package com.example.dragdroprecyclerview.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dragdroprecyclerview.Adapter;

public class SimpleItemTouchHelper extends ItemTouchHelper.Callback {

    private Adapter mAdapter;
    private int mFromPos;
    private int mToPos;
    private RecyclerView.ViewHolder oldTarget;

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public SimpleItemTouchHelper(Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        if (oldTarget == null) {
            oldTarget = target;
        }
        if (oldTarget != target) {
            oldTarget.itemView.setAlpha(1f);
            oldTarget = target;
            oldTarget.itemView.setAlpha(0.5f);
        }

        if (mAdapter != null) {
            if (!(target instanceof Adapter.FolderViewHolder)) {
                mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mFromPos = viewHolder.getAdapterPosition();
                mToPos = target.getAdapterPosition();
                return true;
            } else {
                mFromPos = viewHolder.getAdapterPosition();
                mToPos = target.getAdapterPosition();
                Folder folder = ((Folder) mAdapter.getItem(mToPos));
                if (!(((Folder) mAdapter.getItem(mToPos)).isExpanded())) {
                    mAdapter.expandFolder(folder, mToPos);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        oldTarget.itemView.setAlpha(1f);
        BookmarkItem fromItem = mAdapter.getItem(mFromPos);
        BookmarkItem toItem = mAdapter.getItem(mToPos);

        if (mFromPos != mToPos && fromItem instanceof File && toItem instanceof Folder) {
            if (!(((File) fromItem).isBelongToFolder())) {
                //case 1. move file từ ngoài vào folder
                mAdapter.onItemMoved(mFromPos, mToPos, 1);
                Log.d("abba", "di chuyển từ ngoài vào folder");
            } else if (((File) fromItem).isBelongToFolder() && !(((Folder) toItem).isContain(fromItem))) {
                //case 2. move file từ folder vào folder
                mAdapter.onItemMoved(mFromPos, mToPos, 2);
                Log.d("abba", "di chuyển từ folder vào folder");
            }
        } else if (mFromPos != mToPos && fromItem instanceof File && toItem instanceof File) {
            //case 3. move file từ trong folder ra ngoài.
            if (((File) fromItem).isBelongToFolder() && !((File) toItem).isBelongToFolder()) {
                mAdapter.onItemMoved(mFromPos, mToPos, 3);
                Log.d("abba", "di chuyển từ folder ra ngoài");
            } else if (((File) fromItem).isBelongToFolder() && ((File) toItem).isBelongToFolder()) {
                //check 2 file có cùng chung folder hay không
                if (!((File) fromItem).getContainerFolder().isContain(toItem)) {
                    //case 4: di chuyển từ folder vào folder
                    mAdapter.onItemMoved(mFromPos, mToPos, 4);
                    Log.d("abba", "di chuyển từ folder vào folder 2");
                }
            } else if (!((File) fromItem).isBelongToFolder() && ((File) toItem).isBelongToFolder()) {
                //case 5: move file từ ngoài vào folder đang mở
                mAdapter.onItemMoved(mFromPos, mToPos, 5);
                Log.d("abba", " move file từ ngoài vào folder đang mở");
            }
        }
    }
}
