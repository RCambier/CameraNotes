package com.apps.rodolphe.cameranotes.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.rodolphe.cameranotes.R;
import com.apps.rodolphe.cameranotes.activities.CameraActivity;
import com.apps.rodolphe.cameranotes.database.Path;
import com.apps.rodolphe.cameranotes.database.PathsDataSource;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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



    public StableArrayAdapter(Context contextActivity, int textViewResourceId,
                              List<Path> values,CameraActivity activity) {
        super(contextActivity, R.layout.row_layout,values);
        this.contextActivity = contextActivity;
        this.values = values;
        imageLoader = ImageLoader.getInstance();
        this.activity = activity;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) contextActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);

        //ImageView imageView = (ImageView) rowView.findViewById(R.id.rowpic);

        //imageView.setImageResource(R.drawable.test);

        ImageButton takePictureButton = (ImageButton) rowView.findViewById(R.id.rowbutton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                PlaceholderFragment.onClick();
            }
        });

        datasource = new PathsDataSource(contextActivity);
        datasource.open();
        values = datasource.getAllPaths();


        String pathImage = values.get(position).getPath();
        String path = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.FILE.wrap(pathImage);
        ImageView myImage = (ImageView) rowView.findViewById(R.id.rowpic);

        String pathShape= values.get(position).getShape();
        String shape = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.FILE.wrap(pathShape);
        ImageView myShape = (ImageView) rowView.findViewById(R.id.shape);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // default
                .cacheInMemory(true)
                .considerExifParams(true)
                .build();


        //get the name of your image from its path
        imageLoader.displayImage(path, myImage,options);
        imageLoader.displayImage(shape, myShape,options);




        return rowView;
    }




    /**
     * Start the camera by dispatching a camera intent.
     */
    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = contextActivity;
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(context, "This device does not have a camera.", Toast.LENGTH_SHORT)
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
                ((Activity) contextActivity).startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * The activity returns with the photo.
     * @param requestCode
     * @param resultCode
     * @param data
     */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(contextActivity,"dans acivityresult du adapter",Toast.LENGTH_LONG);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            addPhotoToGallery();


            // Show the full sized image.
            //setFullImageFromFilePath(activity.getCurrentPhotoPath(), mImageView);
            //setFullImageFromFilePath(activity.getCurrentPhotoPath(), mThumbnailImageView);
            // add to database
            datasource.createShape(activity.getCurrentPhotoPath(),1);

            values = datasource.getAllPaths();




        } else {
            Toast.makeText(contextActivity, "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Creates the image file to which the image must be saved.
     * @return
     * @throws IOException
     */
    protected File createImageFile() throws IOException {
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
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.contextActivity.sendBroadcast(mediaScanIntent);
    }



}