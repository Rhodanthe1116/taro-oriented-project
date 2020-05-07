package genomu.fire_image_helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RawImageHandler {

    public static Bitmap getBitmap(Context context, File file){
        return getBitmap(context,Uri.fromFile(file));
    }

    public static File getJpg(Context context, String fileName){
        File dir = context.getDir("image",Context.MODE_PRIVATE);
        return new File(dir, fileName + ".jpg");
    }

    public static Bitmap getBitmap(Context context, Uri uri) {

        Bitmap map = null;
        try {
            int[] bounds = getBitmapBounds(context,uri);
            if ((bounds[0] == -1) || (bounds[1] == -1))
                return null;
            int sampleSize = getSampleSize(bounds[0],bounds[1]);
            map = compressBitmap(resizeBitmap(context,uri,getOptions(sampleSize,false)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static Bitmap resizeBitmap(Context context, Uri uri, BitmapFactory.Options options) throws IOException {
        InputStream stream = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        stream.close();
        return bitmap;
    }
    private static int[] getBitmapBounds(Context context,Uri uri) throws IOException {
        InputStream stream = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = getOptions(0,true);
        BitmapFactory.decodeStream(stream, null, onlyBoundsOptions);
        stream.close();
        return new int[]{onlyBoundsOptions.outWidth,onlyBoundsOptions.outHeight};
    }
    private static Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,85,baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm);
    }

    private static int getSampleSize(int originalWidth,int originalHeight){
        int require_h = 300;
        int require_w = 300;
        int sampleSize = 1;
        if (originalWidth > originalHeight && originalWidth > require_w) {
            sampleSize = originalHeight / require_h;
        } else if (originalWidth < originalHeight && originalHeight > require_h) {
            sampleSize = originalWidth / require_w;
        }
        if (sampleSize <= 0)
            return 1;
        return sampleSize;
    }

    private static BitmapFactory.Options getOptions(int sampleSize,boolean isJustBounds){
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        if(isJustBounds){
            bitmapOptions.inJustDecodeBounds = true;
        }else {
            bitmapOptions.inSampleSize = sampleSize;
        }
        bitmapOptions.inDither = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return bitmapOptions;
    }
}
