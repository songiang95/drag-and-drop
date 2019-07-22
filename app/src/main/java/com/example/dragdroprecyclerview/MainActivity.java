package com.example.dragdroprecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.dragdroprecyclerview.model.BookmarkItem;
import com.example.dragdroprecyclerview.model.File;
import com.example.dragdroprecyclerview.model.Folder;
import com.example.dragdroprecyclerview.model.SimpleItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private List<BookmarkItem> mListData;
    private Adapter mAdapter;
    private ItemTouchHelper.Callback mItemTouchHelperCallback;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerview = findViewById(R.id.rv_list);
        initData();
        mAdapter = new Adapter(this, mListData);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mItemTouchHelperCallback = new SimpleItemTouchHelper(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerview);
    }

    public void initData() {
        mListData = new ArrayList<>();
        Folder folder1 = new Folder("Work");
        Folder folder2 = new Folder("Study");
        File file1 = new File("youtube.com");
        File file2 = new File("google.com");
        File file3 = new File("facebook.com");
        File file4 = new File("netflix.com");
        folder1.addFile(file1);
        folder2.addFile(file2);
        mListData.add(folder1);
        mListData.add(folder2);
        mListData.add(file3);
        mListData.add(file4);
    }


}
