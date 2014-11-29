package com.apps.rodolphe.cameranotes.other;

import android.content.Intent;

import java.io.File;
import java.io.IOException;

/**
 * Created by rodolphe on 29/11/14.
 */
public interface CameraInterface {
    public void dispatchTakePictureIntent(int request, int pos);
    public void onActivityResult(int requestCode, int resultCode, Intent data);
    public File createImageFile() throws IOException;
    public void addPhotoToGallery();
    }
