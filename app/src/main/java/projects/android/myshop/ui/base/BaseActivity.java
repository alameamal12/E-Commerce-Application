package projects.android.myshop.ui.base;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected final CompositeDisposable disposable = new CompositeDisposable();

    protected abstract void initClicks();

    protected abstract void initViews();

    protected abstract void initMethods();

    protected abstract void subscribe();

    protected void logE(String tag, String msg) {
        if (tag != null && msg != null) {
            Log.e(tag, msg);
        }
    }

    protected void logE(String tag, String msg, Throwable t) {
        if (tag != null && msg != null && t != null) {
            Log.e(tag, msg, t);
        }
    }

    protected void logD(String tag, String msg, Throwable t) {
        if (tag != null && msg != null && t != null) {
            Log.d(tag, msg, t);
        }
    }


    protected void logE(String tag, Exception e) {
        if (tag != null && e != null) {
            Log.e(tag, e.getMessage());
        }
    }

    protected void logE(String tag, Throwable t) {
        if (tag != null && t != null) {
            Log.e(tag, t.getMessage());
        }
    }

    protected void logD(String tag, String msg) {
        if (tag != null && msg != null) {
            Log.e(tag, msg);
        }
    }

    protected void logD(String tag, Exception e) {
        if (tag != null && e != null) {
            Log.e(tag, e.getMessage());
        }
    }

    protected void logD(String tag, Throwable t) {
        if (tag != null && t != null) {
            Log.e(tag, t.getMessage());
        }
    }


    protected void makeToast(String msg) {
        BaseActivity.this.runOnUiThread(() -> Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initClicks();
        initMethods();
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribe();
    }

    protected static void afterTextChanged(AppCompatEditText editText, TextInputLayout layout, String errorMsg) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                layout.setError(errorMsg);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        disposable.clear();
    }

    public static boolean isValidEmail(@NonNull String email) {

        // Regular expression pattern for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
