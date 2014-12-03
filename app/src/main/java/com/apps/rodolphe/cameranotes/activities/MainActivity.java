package com.apps.rodolphe.cameranotes.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.apps.rodolphe.cameranotes.R;
import com.apps.rodolphe.cameranotes.fragments.BaseFragment;
import com.apps.rodolphe.cameranotes.fragments.PictureFragment;
import com.apps.rodolphe.cameranotes.fragments.PlaceholderFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class MainActivity extends CameraActivity implements  BaseFragment.OnFragmentInteractionListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .threadPoolSize(5)
        .memoryCacheSize(1048576 * 10)
        .build();


        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Handle Incoming messages from contained fragments.
     */

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onFragmentInteraction(String id) {

    }
    @Override
    public void onFragmentInteraction(int actionId) {

    }
    @Override
    public void onFragmentInteractionReplace(String value) {


        // get fragment manager
        FragmentManager fm = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("path",value );
        PictureFragment frag = new PictureFragment();
        frag.setArguments(bundle);
        // replace
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.container, frag).addToBackStack("fragBack");
        ft.commit();
    }


}
