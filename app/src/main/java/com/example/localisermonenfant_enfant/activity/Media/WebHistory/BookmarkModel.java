package com.example.localisermonenfant_enfant.activity.Media.WebHistory;

public class BookmarkModel {

    // Bookmark getter and setter
    private String bookmark_title, bookmark_url;

    public BookmarkModel(String bookmark_title, String bookmark_url) {
        this.bookmark_title = bookmark_title;
        this.bookmark_url = bookmark_url;

    }

    public String getBookmarkTitle() {
        return bookmark_title;
    }

    public String getBookmarkUrl() {
        return bookmark_url;
    }

}