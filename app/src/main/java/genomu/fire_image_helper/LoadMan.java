package genomu.fire_image_helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;

public class LoadMan extends StorageMan {
    public LoadMan(String storeKey, Activity activity) {
        super(storeKey, activity);
    }
    public void loadImage(final ImageView imageView){
        loadImage(imageView,null);
    }

    public void loadImage(final ImageView imageView, final ProgressBar bar){
        LoadImageTask loadImageTask = new LoadImageTask(imageView);
        loadImageTask.setListener(new LoadImageTask.LoadImageListener() {
            @Override
            public void onFailure() {
                Toast.makeText(activity,"嘗試下載照片",Toast.LENGTH_SHORT).show();
                downloadImage(imageView,bar);
            }

            @Override
            public void onSuccess() {
                Toast.makeText(activity,"取得照片成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bitmap bitmap) {

            }
        });
        loadImageTask.execute(RawImageHandler.getJpg(activity,getParseKey()));
    }

    private void downloadImage(final ImageView imageView, final ProgressBar bar) {
        if(bar!=null){
            bar.setVisibility(View.VISIBLE);
        }
        faFa.seekData(RawImageHandler.getJpg(activity,getParseKey())).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    imageView.setImageURI(Uri.fromFile(RawImageHandler.getJpg(activity,getParseKey())));
                }else {
                    Toast.makeText(activity,"取得照片失敗",Toast.LENGTH_SHORT).show();
                }
                if(bar!=null){
                    bar.setVisibility(View.GONE);
                }
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                int progress = (int) (taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount())*100;
                Log.d("LoadMan", "onProgress: "+progress);
                if(bar!=null){
                    bar.setProgress(progress);
                }
            }
        });
    }
}
