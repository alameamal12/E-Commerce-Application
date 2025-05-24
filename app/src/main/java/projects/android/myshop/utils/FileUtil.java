package projects.android.myshop.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import projects.android.myshop.ui.category.AdminCategoriesDetailsActivity;

public final class FileUtil {
    private static final String LOG_TAG = "FileUtil";
    private static final String CATEGORY_IMAGES_DIRECTORY = "Category Images";
    private static final String PRODUCT_IMAGES_DIRECTORY = "Product Images";

    private FileUtil() {
    }


    @Nullable
    public static File createCategoryImagesDirectory(@NonNull Context context) {
        return createImagesDirectory(context, CATEGORY_IMAGES_DIRECTORY);
    }

    @Nullable
    public static File createProductImagesDirectory(@NonNull Context context) {
        return createImagesDirectory(context, PRODUCT_IMAGES_DIRECTORY);
    }


    @Nullable
    private static File createImagesDirectory(@NonNull Context context, @NonNull String directoryName) {

        File directory = new File(context.getApplicationContext().getFilesDir(), directoryName);

        try {
            if (!directory.exists()) {
                directory.mkdirs();
            }
        } catch (Exception e) {
            directory = null;
            Log.e(LOG_TAG, e.getMessage());
        }
        return directory;
    }


    public static void copyFile(@NonNull File sourceFile, @NonNull File destFile) throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(sourceFile);
            outputStream = new FileOutputStream(destFile);
            FileChannel sourceChannel = inputStream.getChannel();
            FileChannel destChannel = outputStream.getChannel();
            long size = sourceChannel.size();
            sourceChannel.transferTo(0, size, destChannel);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static File getFileFromUri(@NonNull Context context, @NonNull Uri uri) {
        File file = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            file = new File(filePath);
        }
        return file;
    }


}
