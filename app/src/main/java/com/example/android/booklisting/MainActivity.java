package com.example.android.booklisting;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jamesrichardson on 24/05/2017.
 */

public class MainActivity extends AppCompatActivity {

    /**
     * URL for Google Books data from the Google
     */
    private static final String GOOGLE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BookAsyncTask task = new BookAsyncTask();
        task.execute(GOOGLE_REQUEST_URL);

        // Create a fake list of earthquakes.
        ArrayList<Book> books = QueryUtils.extractEarthquakes();

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        BookAdapter adapter = new BookAdapter (this, books);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

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