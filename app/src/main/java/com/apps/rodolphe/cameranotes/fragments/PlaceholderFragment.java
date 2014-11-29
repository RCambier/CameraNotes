package com.apps.rodolphe.cameranotes.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.apps.rodolphe.cameranotes.R;
import com.apps.rodolphe.cameranotes.activities.CameraActivity;
import com.apps.rodolphe.cameranotes.database.Path;
import com.apps.rodolphe.cameranotes.database.PathsDataSource;
import com.apps.rodolphe.cameranotes.other.CameraInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends BaseFragment implements Button.OnClickListener, CameraInterface {

    CameraActivity activity;
    int rowPosition;
    ListView lv;
    private PathsDataSource datasource;
    List<Path> values;

    // Activity result key for camera
    static final int REQUEST_TAKE_PHOTO = 11111;

    // Image view for showing our image.
    private ImageView mImageView;
    private ImageView mThumbnailImageView;

    OnFragmentInteractionListener mCallback;



    public PlaceholderFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("ONCREATEVIEW","");

        activity = (CameraActivity) getActivity();

        datasource = new PathsDataSource(getActivity());
        datasource.open();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        lv = (ListView) rootView.findViewById(R.id.apps_fragment_list);
        ImageButton takePictureButton = (ImageButton) rootView.findViewById(R.id.button1);

        // Set OnItemClickListener so we can be notified on button clicks
        takePictureButton.setOnClickListener(this);

        values = datasource.getAllPaths();

        final ArrayAdapter adapter = new StableArrayAdapter(getActivity(),
                R.layout.row_layout, values,activity,this);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                Log.e("values.get(0)",values.get(0).getPath());
                mCallback.onFragmentInteractionReplace(values.get(position).getPath());


            }

        });




        return rootView;


    }






    /**
     * Start the camera by dispatching a camera intent.
     */
    public void dispatchTakePictureIntent(int request, int pos) {

        this.rowPosition = pos;
        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, request);
            }
        }
    }

    /**
     * The activity returns with the photo.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(),"dans acivityresult du fragment",Toast.LENGTH_LONG);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            addPhotoToGallery();


            // Show the full sized image.
            //setFullImageFromFilePath(activity.getCurrentPhotoPath(), mImageView);
            //setFullImageFromFilePath(activity.getCurrentPhotoPath(), mThumbnailImageView);
            // add to database
            datasource.createPath(activity.getCurrentPhotoPath());

            values = datasource.getAllPaths();

            final ArrayAdapter adapter = new StableArrayAdapter(getActivity(),
                    R.layout.row_layout, values,activity,this);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {

                    Log.e("values.get(0)",values.get(0).getPath());
                    mCallback.onFragmentInteractionReplace(values.get(position).getPath());


                }

            });


        } else if (requestCode == 22222 && resultCode == Activity.RESULT_OK) {
            addPhotoToGallery();

            Log.e("FRAGMENT","INSIDE ONACTIVITYRESULT");
            // Show the full sized image.
            //setFullImageFromFilePath(activity.getCurrentPhotoPath(), mImageView);
            //setFullImageFromFilePath(activity.getCurrentPhotoPath(), mThumbnailImageView);
            // add to database


            datasource.createShape(activity.getCurrentPhotoPath(),rowPosition);

            values = datasource.getAllPaths();




        }
        else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Creates the image file to which the image must be saved.
     * @return
     * @throws IOException
     */
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents

        activity.setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }

    /**
     * Add the picture to the photo gallery.
     * Must be called on all camera images or they will
     * disappear once taken.
     */
    public void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }

    /**
     * Deal with button clicks.
     * @param v
     */
    @Override
    public void onClick(View v) {
        dispatchTakePictureIntent(REQUEST_TAKE_PHOTO, -1);
    }



}