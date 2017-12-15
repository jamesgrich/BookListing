package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.Toast;


/**
 * Created by jamesrichardson on 24/05/2017.
 */

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    /**
     * URL for Google Books data from the Google API
     */

    private static final String GOOGLE_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?maxResults=20&q=";

    /**
     * Constant value for the book loader ID
     */
    private static final int BOOK_LOADER_ID = 0;

    private EditText querySearchBoxEdx;
    private Button querysearchTrigerBtn;
    private ListView bookListView;
    private String query;
    private TextView emptyStateTextView;
    private BookAdapter bookAdapter;
    private boolean hasPerformedInitialLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        querySearchBoxEdx = (EditText) findViewById(R.id.search_box);
        querysearchTrigerBtn = (Button) findViewById(R.id.search_button);
        bookListView = (ListView) findViewById(R.id.list);
        emptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        bookListView.setEmptyView(findViewById(R.id.empty_view_holder));

        bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(bookAdapter);

        querysearchTrigerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    String queryKey = querySearchBoxEdx.getText().toString();
                    String updatedUrl = GOOGLE_REQUEST_URL + queryKey;
                    // Prepare the loader.  Either re-connect with an existing one,
                    // or start a new one.
                    Bundle bundle = new Bundle();
                    bundle.putString("keyToUrl", updatedUrl);

                    if (hasPerformedInitialLoad == false) {
                        getLoaderManager().initLoader(BOOK_LOADER_ID, bundle, MainActivity.this);
                        hasPerformedInitialLoad = true;
                    } else {
                        //Restart the Loader upon the search query(execute the search)
                        getLoaderManager().restartLoader(BOOK_LOADER_ID, bundle, MainActivity.this);
                    }
                }
            }
        });
    }

    /**
     * Returns true if network is available or about to become available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.d("onCreateLoader", "This is the onCreateLoader");
        // Retrieve the given query from the user
        String requestUrl = bundle.getString("keyToUrl");
        // Create a new loader for the given URL
        Log.d("onCreateLoader", "This is the URL which I will use " + requestUrl);
        return new BookLoader(this, requestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        if (books == null) {
            Log.d("onLoadFinished", "This a null books result");
        } else {
            Log.d("onLoadFinished", "This not a null books result" + books.size());
        }
        // Clear the adapter of previous Book data
        bookAdapter.clear();

        // If there is a valid list of {@link Book'}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            bookAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.d("onLoadReset", "This is the onLoadReset");
        // Loader reset, so we can clear out our existing data.
        bookAdapter.clear();
    }
}
