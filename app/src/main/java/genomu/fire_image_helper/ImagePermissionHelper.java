package genomu.fire_image_helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ImagePermissionHelper {
    private Activity activity;
    private boolean pickable;
    public static final int PERMISSION_CODE = 2748;
    public static final int IMAGE_PICK_CODE = 5874;

    public ImagePermissionHelper(Activity activity) {
        this.activity = activity;
        pickable = false;
        versionCheck();
    }

    private void versionCheck(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            permissionCheck();
        }else{
            // system os is less than marshmallow
            pickable = true;
        }
    }

    public void pickImage() {
        if(pickable){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activity.startActivityForResult(intent,IMAGE_PICK_CODE);
        }else {
            versionCheck();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permissionCheck() {
        if(activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
            // permission not granted, request it
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            activity.requestPermissions(permissions,PERMISSION_CODE);
        }else {
            // permission granted
            pickable = true;
        }
    }

    public void respondToPermissionResult(int requestCode,int[] grantResults){
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED){
                pickable = true;
            }else {
                Toast.makeText(activity,"取得權限失敗",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
