package com.hire.freshershub.ui.assistantTpo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;

public class AssistantTpoImageProvider {
    public static Bitmap getImage(Context context, String name){
        String fileName = name;
        fileName+=".png";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir, fileName);
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    static void saveImage(Context context, Bitmap bitmap, String name) {
        String fileName = name;
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
