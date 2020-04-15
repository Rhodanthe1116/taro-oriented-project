package com.genomu.starttravel.util;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageFromURLTask extends AsyncTask<String,Integer,Drawable> {
    private ImageView image;
    public ImageFromURLTask(ImageView image){
        this.image = image;
    }

    @Override
    protected Drawable doInBackground(String... strings) {

        Drawable thumb_d = null;
        try {
            URL thumb = new URL(strings[0]);
            thumb_d = Drawable.createFromStream(thumb.openStream(), "testThumb");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return thumb_d;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        super.onPostExecute(drawable);
        image.setImageDrawable(drawable);
    }
}
