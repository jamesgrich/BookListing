package com.example.android.booklisting;

import android.util.Log;

/**
 * Created by jamesrichardson on 16/06/2017.
 */

public class Book {

    // Name of the Author
    public String mAuthorName;

    // Title
    public String mTitleInformation;

    /*
    * Create a new Book object
    * */

    public Book(String authorName, String titleInformation) {
        mAuthorName = authorName;
        mTitleInformation = titleInformation;
    }

    /**
     * Get the name of the Author
     */
    public String getAuthorName() {
        return mAuthorName;
    }

    /**
     * Get the title information
     */
    public String getTitleInformation() {
        return mTitleInformation;
    }

}
