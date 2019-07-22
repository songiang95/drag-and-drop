package com.example.dragdroprecyclerview.model;

public class File implements BookmarkItem {
    private String name;
    private boolean isBelongToFolder;
    private FileCallback mCallback;

    public boolean isBelongToFolder() {
        return isBelongToFolder;
    }

    public void setBelongToFolder(boolean belongToFolder) {
        isBelongToFolder = belongToFolder;
    }

    public void setCallback(FileCallback mCallback) {
        this.mCallback = mCallback;
    }

    public File(String name) {
        this.name = name;
    }

    public File(File file) {
        this.name = file.name;
        this.isBelongToFolder = file.isBelongToFolder;
        this.mCallback = file.mCallback;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public interface FileCallback {
        Folder getFolder();
    }

    public Folder getContainerFolder() {
        if (mCallback != null) {
            return mCallback.getFolder();
        }
        return null;
    }

}
