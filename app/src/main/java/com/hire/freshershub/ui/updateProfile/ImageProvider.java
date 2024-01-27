package com.hire.freshershub.ui.updateProfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;

public class ImageProvider {
    static void deleteImage(Context context){
        String fileName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (fileName != null) {
            fileName = fileName.replace(" ", "_");
        }
        fileName+=".png";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir, fileName);
        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (isDeleted) {
                Log.v("FileDeletion", "The file was successfully deleted");
            } else {
                Log.v("FileDeletion", "The file could not be deleted");
            }
        } else {
            Log.v("FileDeletion", "The file does not exist");
        }
    }

    public static Bitmap getImage(Context context){
        String fileName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (fileName != null) {
            fileName = fileName.replace(" ", "_");
        }
        fileName+=".png";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir, fileName);
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    static void saveImage(Context context, Bitmap bitmap) {
        String fileName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (fileName != null) {
            fileName = fileName.replace(" ", "_");
        }
        fileName+=".png";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
