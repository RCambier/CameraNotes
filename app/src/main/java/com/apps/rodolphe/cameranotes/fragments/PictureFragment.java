package com.apps.rodolphe.cameranotes.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apps.rodolphe.cameranotes.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by rodolphe on 18/11/14.
 */
public class PictureFragment extends Fragment {

    ImageView pic;
    String path;
    DisplayImageOptions options;

    public PictureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        path = this.getArguments().getString("path");
        View rootView = inflater.inflate(R.layout.fragment_picture, container, false);
        pic = (ImageView) rootView.findViewById(R.id.pic);


        String pathURI = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.FILE.wrap(path);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // default
                .cacheInMemory(true)
                .considerExifParams(true)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(pathURI,pic,options);
        //pic.getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        return rootView;
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
