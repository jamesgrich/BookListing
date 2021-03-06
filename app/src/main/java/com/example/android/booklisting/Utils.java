package com.example.android.booklisting;

/**
 * Created by jamesrichardson on 18/06/2017.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class with methods to help perform the HTTP request and
 * parse the response.
 */
public final class Utils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Query the Google API and return an {@link Book} object to represent a single book.
     */
    public static List<Book> fetchData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Book> book = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return book;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            Log.d("createUrl", "This is the " + stringUrl);
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link Book} object by parsing out information
     * about the first book from the input bookJSON string.
     */
    private static List<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            if (baseJsonResponse.has("items")) {
                JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

                Log.d("itemsArray", "This is the items array" + itemsArray.toString());

                List<Book> bookList = new ArrayList<>();
                // If there are results in the items array
                if (itemsArray.length() > 0) {

                    for (int i = 0; i <= itemsArray.length() - 1; i++) {
                        // Extract out the first items
                        if (baseJsonResponse.has("items")) {
                            JSONObject firstItem = itemsArray.getJSONObject(i);
                            JSONObject volumeInfo = firstItem.getJSONObject("volumeInfo");
                            Log.d("volumeInfo", "This is the volumesInfo" + volumeInfo.toString());

                            // Extract out the title, number of people, and perceived strength values
                            String title = volumeInfo.getString("title");
                            String authors = "N/A";
                            if (volumeInfo.has("authors")) {
                                // Extract out the second item
                                JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                                // Convert all authors int one String
                                authors = authorsArray.toString();
                            }

                            // Create a new {@link Event} object
                            Book book = new Book(authors, title);
                            bookList.add(book);
                        }
                    }
                }

                return bookList;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        return null;
    }
}