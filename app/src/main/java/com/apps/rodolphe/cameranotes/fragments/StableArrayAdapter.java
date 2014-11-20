package com.apps.rodolphe.cameranotes.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.rodolphe.cameranotes.R;
import com.apps.rodolphe.cameranotes.database.Path;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by rodolphe on 18/11/14.
 */
public class StableArrayAdapter extends ArrayAdapter<Path> {

    private final Context context;
    private final List<Path> values;

    public StableArrayAdapter(Context context, int textViewResourceId,
                              List<Path> values) {
        super(context, R.layout.row_layout,values);
        this.context = context;
        this.values = values;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.rowtext);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.rowpic);

        textView.setText("Image capturée le ...");
        //imageView.setImageResource(R.drawable.test);


        String pathImage = values.get(position).toString();
        //get the name of your image from its path
        Log.e("adapter",pathImage);

        File imgFile = new  File(pathImage);

        if(imgFile.exists()){
            Log.e("adapter","imgFile exists!");

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            myBitmap = myBitmap.createScaledBitmap(myBitmap, 270, 180, false);
            myBitmap = imageOreintationValidator(myBitmap,pathImage);
            ImageView myImage = (ImageView) rowView.findViewById(R.id.rowpic);
            myImage.setImageBitmap(myBitmap);



        }

      //  pathImage = pathImage.substring(pathImage.lastIndexOf('/'), pathImage.lastIndexOf('.'));
      //  //get image resource from iamge name
      //  int imageResource = context.getResources().getIdentifier(pathImage, "drawable", context.getPackageName());
      //  //set your image in imageview
      // imageView.setImageResource(imageResource);

        return rowView;
    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

}