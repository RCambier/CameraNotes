package com.apps.rodolphe.cameranotes.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apps.rodolphe.cameranotes.R;

/**
 * Created by rodolphe on 18/11/14.
 */
public class PictureFragment extends Fragment {

    ImageView pic;

    public PictureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_picture, container, false);

        pic = (ImageView) rootView.findViewById(R.id.pic);
        pic.setImageResource(R.drawable.test);
        return rootView;


    }


}
