package com.example.android.booklisting;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jamesrichardson on 04/06/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Activity context, ArrayList<Book> words) {
        // Initialising the Adapter
        super(context, 0, words);
    }

    static class ViewHolder {
        TextView text1;
        TextView text2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // Checking to see if the view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.authorName);

            viewHolder.text2 = (TextView) convertView.findViewById(R.id.titleInformation);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Get the {@link Book} object located at this position in the list
        Book currentAttraction = getItem(position);

        // Set this text on the name TextView
        viewHolder.text1.setText(currentAttraction.getAuthorName());

        // Set this text on the name TextView
        viewHolder.text2.setText(currentAttraction.getTitleInformation());

        // Return the whole list item layout (containing 1 TextView and an ImageView)
        // so that it can be shown in the ListView
        return convertView;
    }
}
