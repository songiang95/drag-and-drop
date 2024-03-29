package com.example.dragdroprecyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dragdroprecyclerview.model.BookmarkItem;
import com.example.dragdroprecyclerview.model.File;
import com.example.dragdroprecyclerview.model.Folder;

import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_FOLDER = 0;
    private final int TYPE_FILE = 1;
    private Context mContext;
    private List<BookmarkItem> mList;

    public Adapter(Context mContext, List<BookmarkItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    //My method

    public boolean onItemMove(int fromPos, int toPos) {
        if (fromPos < toPos) {
            for (int i = fromPos; i < toPos; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPos; i > toPos; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(fromPos, toPos);
        return true;
    }

    public boolean onItemMoved(int fromPos, int toPos, int moveCase) {
        File fromFile = (File) mList.get(fromPos);
        Folder toFolder;
        File temp;
        int folderPosition;
        switch (moveCase) {
            case 1://move file từ ngoài vào folder
                toFolder = (Folder) mList.get(toPos);
                if (!toFolder.isExpanded()) {
                    toFolder.addFile(fromFile, 0);
                    fromFile.setBelongToFolder(true);
                    mList.remove(fromFile);
                    notifyItemRemoved(fromPos);
                } else {
                    temp = new File(fromFile);
                    mList.add(toPos, temp);
                    mList.remove(fromFile);
                    notifyItemMoved(fromPos, toPos);
                }

                return true;
            case 2://move file từ folder sang folder khi folder đang đóng
                temp = new File(fromFile);
                temp.getContainerFolder().removeFile(fromFile);
                toFolder = (Folder) mList.get(toPos);
                if (!toFolder.isExpanded()) {
                    toFolder.addFile(fromFile);
                    mList.remove(fromFile);
                    notifyItemRemoved(fromPos);
                } else {
                    mList.add(toPos, temp);
                    mList.remove(fromFile);
                    notifyItemMoved(fromPos, toPos);
                }

                return true;

            case 3://move file từ folder ra ngoài
                fromFile.setBelongToFolder(false);
                File temp2 = new File(fromFile);
                temp2.getContainerFolder().removeFile(fromFile);
                return true;

            case 4://move file từ folder sang folder khi folder đang mở
                File temp3 = new File(fromFile);
                temp3.getContainerFolder().removeFile(fromFile);
                temp3 = ((File) mList.get(toPos));
                toFolder = temp3.getContainerFolder();
                folderPosition = mList.indexOf(toFolder);
                toFolder.addFile(fromFile, toPos - folderPosition - 1);
                Log.d("abba", "to position : " + toPos);
                Log.d("abba", "folder position : " + folderPosition);
                return true;
            case 5:// move file từ ngoài vào folder đang mở
                toFolder = ((File) mList.get(toPos)).getContainerFolder();
                folderPosition = mList.indexOf(toFolder);
                toFolder.addFile(fromFile, toPos - folderPosition - 1);
                Log.d("abba", "to position : " + toPos);
                Log.d("abba", "folder position : " + folderPosition);
            default:
                return false;
        }

    }

    public BookmarkItem getItem(int position) {
        return mList.get(position);
    }

    public void expandFolder(Folder folder, int position) {
        if (folder.getNumberOfFile() != 0) {
            folder.setExpanded(true);
            mList.addAll(position + 1, folder.getAllFile());
            notifyItemRangeInserted(position + 1, folder.getNumberOfFile());
        }
    }

    public void collapseFolder(Folder folder, int position) {
        if (folder.getNumberOfFile() != 0) {
            folder.setExpanded(false);
            mList.removeAll(folder.getAllFile());
            notifyItemRangeRemoved(position + 1, folder.getNumberOfFile());
        }
    }

    //Implementation

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_FOLDER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_folder, parent, false);
            return new FolderViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_file, parent, false);
            return new FileViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FolderViewHolder) {
            ((FolderViewHolder) holder).tvFolderName.setText(mList.get(position).getName());
        } else {
            ((FileViewHolder) holder).tvFileName.setText(mList.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof Folder) {
            return TYPE_FOLDER;
        } else {
            return TYPE_FILE;
        }
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvFolderName;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFolderName = itemView.findViewById(R.id.tv_folder_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Folder folder = ((Folder) mList.get(position));
            if (folder.isExpanded()) {
                // Đóng folder lại
                collapseFolder(folder, position);
            } else {
                // Mở folder ra
                expandFolder(folder, position);
            }
        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFileName;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
        }
    }
}
