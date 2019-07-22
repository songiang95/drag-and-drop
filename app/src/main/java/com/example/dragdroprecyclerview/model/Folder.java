package com.example.dragdroprecyclerview.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Folder implements BookmarkItem, File.FileCallback {

    private String name;
    private List<BookmarkItem> mListFile;
    private boolean isExpanded;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public Folder(String name) {
        this.name = name;
        mListFile = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void addFile(BookmarkItem item) {
        if (item instanceof File) {
            ((File) item).setCallback(this);
            ((File) item).setBelongToFolder(true);
            mListFile.add(item);
        }
    }

    public void removeFile(BookmarkItem item) {
        if (item instanceof File && mListFile.contains(item)) {
            Log.d("abba", "file removed: ");
            mListFile.remove(item);
        }
    }

    public boolean isContain(BookmarkItem item) {
        if (item instanceof File && mListFile.contains(item)) {
            return true;
        }
        return false;
    }


    public File getFile(int position) {
        return (File) mListFile.get(position);
    }

    public List getAllFile() {
        return mListFile;
    }

    public int getNumberOfFile() {
        return mListFile.size();
    }

    @Override
    public Folder getFolder() {
        return this;
    }
}
