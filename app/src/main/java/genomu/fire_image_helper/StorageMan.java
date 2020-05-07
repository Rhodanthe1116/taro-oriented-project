package genomu.fire_image_helper;

import android.app.Activity;

import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

public class StorageMan {
    protected String storeKey;
    protected Activity activity;
    protected FaFa faFa;
    protected OnProgressListener<? super UploadTask.TaskSnapshot> progressListener;

    public StorageMan(String storeKey, Activity activity) {
        this.storeKey = storeKey;
        this.activity = activity;
        faFa = new FaFa(storeKey);
    }

    public void setOnProgressListener(OnProgressListener<? super UploadTask.TaskSnapshot> progressListener) {
        this.progressListener = progressListener;
    }

    public String getParseKey(){
        String[] resolving = storeKey.split("/");
        return resolving[resolving.length-1];
    }
}
