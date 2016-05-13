package com.example.peterpan.nearme.custom_listview;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peterpan.nearme.R;
import com.example.peterpan.nearme.model.Bookmarks;
import com.firebase.client.Firebase;

import java.util.List;

/**
 * Created by Peterpan on 5/13/2016 AD.
 */
public class CustomListviewAdapter extends ArrayAdapter<Bookmarks> {
    private Context context;
    private List<Bookmarks> bookmarks;

    public CustomListviewAdapter(Context context, List<Bookmarks> objects) {
        super(context, R.layout.listview_row, objects);

        this.context = context;
        this.bookmarks = objects;
    }

    @Override
    public int getCount() {
        return bookmarks.size();
    }

    @Override
    public Bookmarks getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null)
            view = mInflater.inflate(R.layout.listview_row, parent, false);

        TextView tName = (TextView) view.findViewById(R.id.bookmark_name);
        TextView tAddress = (TextView) view.findViewById(R.id.bookmark_address);
        TextView tPhone = (TextView) view.findViewById(R.id.bookmark_phone);
        final ImageView star = (ImageView) view.findViewById(R.id.bookmark_fav);
        star.setTag(position);

        tName.setText(bookmarks.get(position).getName());
        tAddress.setText(bookmarks.get(position).getAddress());
        tPhone.setText(bookmarks.get(position).getPhone_number());


        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int position=(Integer)view.getTag();
                System.out.println("SELECTED POSITION : "+ position);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Discard bookmark?");
                builder.setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        star.setImageResource(R.drawable.star);
                        Firebase ref = new Firebase("https://nearmeapp.firebaseio.com/");
                        ref.child("bookmarks").child(bookmarks.get(position).getPlace_id()).removeValue();
                        Toast.makeText(context, "Remove from Bookmarks ",Toast.LENGTH_SHORT).show();
                        bookmarks.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        return view;
    }
}
