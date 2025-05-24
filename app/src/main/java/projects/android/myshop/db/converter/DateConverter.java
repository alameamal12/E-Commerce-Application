package projects.android.myshop.db.converter;

import androidx.room.TypeConverter;

import java.util.Date;

// converter class for mills to date and date to mills
public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}