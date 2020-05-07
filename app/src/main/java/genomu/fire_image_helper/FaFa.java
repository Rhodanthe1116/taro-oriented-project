package genomu.fire_image_helper;

import android.net.Uri;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;


public class FaFa {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference reference;
    public FaFa(String pathString){
        reference = storage.getReference().child("images/"+pathString+".jpg");
    }

    UploadTask sproutData(Uri uri, StorageMetadata metadata){
        return reference.putFile(uri,metadata);
    }

    FileDownloadTask seekData(File file){
        return reference.getFile(file);
    }
}
