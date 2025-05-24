package projects.android.myshop.db.converter;

import android.net.Uri;

import androidx.room.TypeConverter;


// converter class for Uri to String and Uri to String
public class UriTypeConverter {

    @TypeConverter
    public static Uri fromString(String value) {
        return value != null ? Uri.parse(value) : null;
    }

    @TypeConverter
    public static String toString(Uri uri) {
        return uri != null ? uri.toString() : null;
    }
}
