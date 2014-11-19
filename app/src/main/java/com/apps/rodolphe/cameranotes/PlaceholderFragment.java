package com.apps.rodolphe.cameranotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment  {

    ListView lv;



    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        lv = (ListView) rootView.findViewById(R.id.apps_fragment_list);


        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };


        final ArrayAdapter adapter = new StableArrayAdapter(getActivity(),
                R.layout.row_layout, values);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                // get fragment manager
                FragmentManager fm = getFragmentManager();

                // replace
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.frag2_in, R.anim.frag1_out, R.anim.frag1_in, R.anim.frag2_out);
                ft.replace(R.id.container, new PictureFragment()).addToBackStack("fragBack");
                ft.commit();
            }

        });




        return rootView;


    }

}