package genomu.fire_image_helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImageTask extends AsyncTask<Bitmap,Void, File> {
    private String fileName;
    private Context context;
    public SaveImageTask(String fileName,Context context) {
        this.fileName = fileName;
        this.context = context;
    }

    @Override
    protected File doInBackground(Bitmap... bitmaps) {
        File file = RawImageHandler.getJpg(context,fileName);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
