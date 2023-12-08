package univ.soongsil.undercover.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DoubleListConverters {
    @TypeConverter
    public String listToJson(List<Double> value) {
        return new Gson().toJson(value);
    }

    @TypeConverter
    public List<Double> jsonToList(String value) {
        Type type = new TypeToken<List<Double>>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }

}
