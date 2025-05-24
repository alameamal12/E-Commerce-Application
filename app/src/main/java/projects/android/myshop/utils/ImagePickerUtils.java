package projects.android.myshop.utils;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public final class ImagePickerUtils {
    public static final int PICK_IMAGE_REQUEST = 1;

    private ImagePickerUtils() {
    }

    public static void pickImage(@NonNull Fragment fragment, @NonNull String title) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(Intent.createChooser(intent, title), PICK_IMAGE_REQUEST);
    }


    public static void pickImage(@NonNull AppCompatActivity activity, @NonNull String title) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(intent, title), PICK_IMAGE_REQUEST);
    }


    public static void onActivityResult(int requestCode, int resultCode, Intent data, @NonNull OnImageSelectedListener listener) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImageUri = null;
            if (data != null) {
                selectedImageUri = data.getData();
            }
            listener.onImageSelected(selectedImageUri);
        }
    }

    public interface OnImageSelectedListener {
        void onImageSelected(@Nullable Uri selectedImageUri);
    }


}
