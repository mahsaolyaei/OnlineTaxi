package ir.maktab.model;

public class Location {
    private int longitude;
    private int latitude;

    public Location(int longitude, int latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double calculateDistance(Location there) {
        return Math.sqrt(Math.pow(Math.abs(this.longitude - there.longitude), 2)
                        + Math.pow(Math.abs(this.latitude - there.latitude), 2));
    }

    @Override
    public String toString() {
        return "{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }
}
