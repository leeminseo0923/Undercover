package univ.soongsil.undercover.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StringListConverters {
    @TypeConverter
    public String listToJson(List<String> value) {
        return new Gson().toJson(value);
    }

    @TypeConverter
    public List<String> jsonToList(String value) {
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }
}
