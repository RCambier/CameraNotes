package com.apps.rodolphe.cameranotes.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.apps.rodolphe.cameranotes.R;
import com.apps.rodolphe.cameranotes.activities.CameraActivity;
import com.apps.rodolphe.cameranotes.database.Path;
import com.apps.rodolphe.cameranotes.database.PathsDataSource;
import com.apps.rodolphe.cameranotes.other.CameraInterface;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by rodolphe on 18/11/14.
 */
public class StableArrayAdapter extends ArrayAdapter<Path>{

    CameraActivity activity;

    private final Context contextActivity;
    private List<Path> values;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    static final int REQUEST_TAKE_PHOTO = 22222;
    private PathsDataSource datasource;
    private CameraInterface listener;



    public StableArrayAdapter(Context contextActivity, int textViewResourceId,
                              List<Path> values,CameraActivity activity, CameraInterface listener) {
        super(contextActivity, R.layout.row_layout,values);
        this.contextActivity = contextActivity;
        this.values = values;
        imageLoader = ImageLoader.getInstance();
        this.activity = activity;
        this.listener = listener;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) contextActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);

        //ImageView imageView = (ImageView) rowView.findViewById(R.id.rowpic);

        //imageView.setImageResource(R.drawable.test);

        ImageButton takePictureButton = (ImageButton) rowView.findViewById(R.id.rowbutton);
        takePictureButton.setTag(position);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){


                    int pos = (Integer) v.getTag();
                    listener.dispatchTakePictureIntent(REQUEST_TAKE_PHOTO, pos);

                }
        });

        datasource = new PathsDataSource(contextActivity);
        datasource.open();
        values = datasource.getAllPaths();

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // default
                .cacheInMemory(true)
                .considerExifParams(true)
                .build();

        String pathImage = values.get(position).getPath();
        String path = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.FILE.wrap(pathImage);
        ImageView myImage = (ImageView) rowView.findViewById(R.id.rowpic);
        imageLoader.displayImage(path, myImage,options);



        String pathShape= values.get(position).getShape();
        if(pathShape != null) {
            String shape = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.FILE.wrap(pathShape);
            ImageView myShape = (ImageView) rowView.findViewById(R.id.shape);
            imageLoader.displayImage(shape, myShape,options);

        }




        return rowView;
    }






}