package com.example.csci310_group_project.data.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

// process limited in png format under 100000 bytes
public class ImageProcessor {
    StorageReference mStorage = null;


    public ImageProcessor() {
    }

    // this method takes in ImageView where user image uploaded with userName
    // store the image on cloud ./images with name userName.jpg
    /*public void upload(ImageView userImage, String userName){
        // Create a reference to "images/userName.jpg"
        StorageReference storageRef = storage.getReference();
        StorageReference userImageRef = storageRef.child("images/" + userName + ".png");

        // Turn the userImage to byte array
        Bitmap capture = Bitmap.createBitmap(userImage.getWidth(), userImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas captureCanvas = new Canvas(capture);
        userImage.draw(captureCanvas);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        capture.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();
        UploadTask task = userImageRef.putBytes(data);
        task.resume();
    }*/

    // this method can be used to obtain both userImage and eventImage
    /*public void download(Activity act, ImageView userImage, String userName){
        StorageReference userImageRef = storage.getReferenceFromUrl("gs://csci310-group-project.appspot.com/images/" + userName + ".png");
        Task<byte[]> data = userImageRef.getBytes(5000000);
        data.addOnCompleteListener(act, new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                InputStream bytes = new ByteArrayInputStream(data.getResult());
                Bitmap bitmap = BitmapFactory.decodeStream(bytes);
                Drawable d = new BitmapDrawable(act.getResources(), bitmap);
                userImage.setImageDrawable(d);
            }
        });
    }*/

    public void getEventImage(ImageView imageView, String eventName){
        mStorage = FirebaseStorage.getInstance().getReference("eventImage").child(eventName + ".png");
        try {
            final File local = File.createTempFile(eventName,"png");
            mStorage.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(local.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
