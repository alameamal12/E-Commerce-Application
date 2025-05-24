package projects.android.myshop.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class Util {
    private Util() {
    }

    public static boolean isPermissionGranted(@NonNull Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static Bitmap drawableToBitmap(@NonNull Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static String saveProductDrawableToFile(@NonNull Context context, @DrawableRes int id) {
        Drawable drawable = getDrawable(context, id);
        File parentDir = FileUtil.createProductImagesDirectory(context);
        File productImage = new File(parentDir, System.currentTimeMillis() + ".png");
        saveDrawableToFile(drawable, productImage);
        return productImage.getAbsolutePath();
    }

    public static String saveCategoryDrawableToFile(@NonNull Context context, @DrawableRes int id) {
        Drawable drawable = getDrawable(context, id);
        File parentDir = FileUtil.createCategoryImagesDirectory(context);
        File productImage = new File(parentDir, System.currentTimeMillis() + ".png");
        saveDrawableToFile(drawable, productImage);
        return productImage.getAbsolutePath();
    }


    private static void saveDrawableToFile(@NonNull Drawable drawable, @NonNull File file) {

        Bitmap bitmap = drawableToBitmap(drawable);

        try (OutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        return ContextCompat.getDrawable(context, id);
    }

}
