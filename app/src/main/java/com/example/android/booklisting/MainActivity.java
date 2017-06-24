package com.example.android.booklisting;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesrichardson on 24/05/2017.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * URL for Google Books data from the Google
     */
    private static final String GOOGLE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    // Adapter for the list of books
    private BookAdapter mAdapter;

    // TextView that is displayed when the list is empty and no items have been searched
    private TextView mEmptyStateTextView;

    // EditText to capture the user's search term
    private EditText mSearchEditTextView;

    // Button to send the request for the search term
    private Button mSearchButtonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListVIre

        // Create an {@link AsyncTask} to perform the HTTP request to the given URL
        // on a background thread. When the result is received on the main UI thread,
        // then update the UI.
        BookAsyncTask task = new BookAsyncTask();
        task.execute(GOOGLE_REQUEST_URL);

        // Find a reference the (@link ListView) in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of Books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // List that will be populated with the Book searched
        bookListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        mSearchEditTextView = (EditText) findViewById(R.id.search_editText);
        mSearchButtonView = (Button) findViewById(R.id.search_button);

        // Set a listener to the Button
        mSearchButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = mSearchEditTextView.getText().toString();
                String updateQueryUrl = GOOGLE_REQUEST_URL + searchTerm;

                DownloadTask task = new DownloadTask();
                task.execute(updatedQueryUrl);
            }
        });

    String searchTerm = mSearchEditTextView.getText().toString();
    String updatedQueryUrl = GOOGLE_REQUEST_URL + searchTerm;

    DownloadTask task = new DownloadTask();
    task.execute(updatedQueryUrl);

    // Create a new adapter that takes an empty list of Books as input
    mAdapter = new BookAdapter(this, new ArrayList<Book>());

    // List that will be populated with the Book searched
        booklistView.setAdapter(mAdapter);
        }
    }

    private class BookAsyncTask extends AsyncTask<String, Void, Book> {

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first earthquake in the response.
     */

        @Override
        protected Book doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            Book result = Utils.fetchData(urls[0]);
            return result;
        }

        private void updateUi(List<Book> Book) {
            // Hooks the data to the Adapter
            mAdapter.clear();
            mAdapter.addAll(Book);
            mAdapter.notifyDataSetChanged();
        }

        private class DownloadTask extends AsyncTask<String, Void, List<Book>> {}

        /**
         * This method is invoked on the main UI thread after the background work has been
         * completed.
         * <p>
         * It IS okay to modify the UI within this method. We take the {@link Book} object
         * (which was returned from the doInBackground() method) and update the views on the screen.
         */
        protected void onPostExecute(Book result) {
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }
        }
    }
}