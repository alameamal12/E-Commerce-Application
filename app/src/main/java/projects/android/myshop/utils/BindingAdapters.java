package projects.android.myshop.utils;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("doubleText")
    public static void setDoubleText(AppCompatEditText editText, double value) {
        editText.setText(String.valueOf(value));
    }
}
