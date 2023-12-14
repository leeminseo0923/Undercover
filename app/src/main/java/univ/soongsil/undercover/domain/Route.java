package univ.soongsil.undercover.domain;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Route implements Parcelable {
    @PrimaryKey
    private Integer id;
    private boolean isActive;
    private List<String> names;
    private List<Coordinate> coordinates;
    private Integer currentProgress;

    private String region;
    private String date;

    public Route(List<String> names, List<Coordinate> coordinates, String region, String date, boolean isActive) {
        this.names = names;
        this.coordinates = coordinates;
        this.isActive = isActive;
        this.currentProgress = 0;
        this.region = region;
        this.date = date;
    }

    protected Route(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        isActive = in.readByte() != 0;
        names = in.createStringArrayList();
        coordinates = in.createTypedArrayList(Coordinate.CREATOR);
        if (in.readByte() == 0) {
            currentProgress = null;
        } else {
            currentProgress = in.readInt();
        }
        region = in.readString();
        date = in.readString();
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    public String getRegion() {
        return region;
    }

    public String getDate() {
        return date;
    }

    public Integer getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(Integer currentProgress) {
        this.currentProgress = currentProgress;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(region);
        dest.writeString(date);
        dest.writeInt(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(isActive);
        }
        dest.writeStringList(names);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeParcelableList(coordinates, PARCELABLE_WRITE_RETURN_VALUE);
        }
        dest.writeInt(currentProgress);
    }
}
