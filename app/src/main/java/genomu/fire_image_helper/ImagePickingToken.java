package genomu.fire_image_helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;


import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import static genomu.fire_image_helper.ImagePermissionHelper.IMAGE_PICK_CODE;

public abstract class ImagePickingToken extends AppCompatActivity {

    private static final String TAG = ImagePickingToken.class.getSimpleName();
    protected SaveMan saveMan;

    public void setStoreKey(String storeKey) {
        this.saveMan = new SaveMan(storeKey,this);
        helper.pickImage();
    }

    protected ImagePermissionHelper helper;
    protected abstract void setHelper();
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        helper.respondToPermissionResult(requestCode,grantResults);
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            Bitmap image = RawImageHandler.getBitmap(this,data.getData());
            saveMan.uploadImage(data.getData());
            onResolveImage(image);
        }
    }

    @CallSuper
    public void onResolveImage(Bitmap image){
        saveMan.saveImage(image);

    }

}
