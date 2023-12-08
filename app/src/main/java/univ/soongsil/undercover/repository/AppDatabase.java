package univ.soongsil.undercover.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import univ.soongsil.undercover.converter.CoordinateConverter;
import univ.soongsil.undercover.converter.DoubleListConverters;
import univ.soongsil.undercover.converter.StringListConverters;
import univ.soongsil.undercover.domain.Place;
import univ.soongsil.undercover.domain.Restaurant;
import univ.soongsil.undercover.domain.Route;
import univ.soongsil.undercover.domain.Sight;

@Database(
        entities = {Restaurant.class, Sight.class, Route.class, Place.class},
        version = 1
)
@TypeConverters({CoordinateConverter.class, DoubleListConverters.class, StringListConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract RestaurantDao restaurantDao();

    public abstract SightDao sightDao();

    public abstract RouteDao routeDao();
}
