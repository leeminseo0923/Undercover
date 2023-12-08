package univ.soongsil.undercover.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import univ.soongsil.undercover.domain.Coordinate;

public class CoordinateConverter {
    @TypeConverter
    public String coordinateToJson(Coordinate coordinate) {
        return new Gson().toJson(coordinate);
    }

    @TypeConverter
    public Coordinate jsonToCoordinate(String value) {
        return new Gson().fromJson(value, Coordinate.class);
    }

    @TypeConverter
    public String coordinateListToJson(List<Coordinate> coordinates) {
        return new Gson().toJson(coordinates);
    }

    @TypeConverter
    public List<Coordinate> jsonToCoordinateLists(String value) {
        Type type = new TypeToken<List<Coordinate>>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }
}
