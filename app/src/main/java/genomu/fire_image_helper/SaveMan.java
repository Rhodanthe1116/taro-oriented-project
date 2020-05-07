package genomu.fire_image_helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.storage.StorageMetadata;

public class SaveMan extends StorageMan {
    public SaveMan(String storeKey, Activity activity) {
        super(storeKey, activity);
    }

    public void saveImage(Bitmap bitmap){
        new SaveImageTask(getParseKey(),activity).execute(bitmap);
    }

    public void uploadImage(Uri uri){
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setCustomMetadata("storeKey",storeKey)
                .build();
        if(progressListener!=null){
            faFa.sproutData(uri,metadata).addOnProgressListener(progressListener);
        }else {
            faFa.sproutData(uri,metadata);
        }
    }
}
