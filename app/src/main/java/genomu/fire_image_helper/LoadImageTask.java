package genomu.fire_image_helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LoadImageTask extends AsyncTask<File,Boolean, Bitmap> {
    private ImageView imageView;
    public interface LoadImageListener {
        void onFailure();
        void onSuccess();
        void onComplete(Bitmap bitmap);
    }

    public void setListener(LoadImageListener listener) {
        this.listener = listener;
    }

    private LoadImageListener listener;
    public LoadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        if(listener!=null)
            listener.onComplete(bitmap);
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
        if(listener!=null){
            if (values[0]) {
                listener.onSuccess();
            } else {
                listener.onFailure();
            }
        }
    }

    @Override
    protected Bitmap doInBackground(File... files) {
        try {
            FileInputStream fis = new FileInputStream(files[0]);
            Bitmap image = BitmapFactory.decodeStream(fis);
            publishProgress(image!=null);
            return image;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            publishProgress(false);
        }
        return null;
    }
}
