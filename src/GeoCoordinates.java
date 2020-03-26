import java.math.*;
public class GeoCoordinates {
    public final double lat;
    public final double lng;
    public static final double EARTH_RADIUS=6371.0;

    public GeoCoordinates(int lat, int lng) {
        this.lat = lat;
        this.lng = lng;
    }
    //using Haversine formula -- return the shortest sferical path between two points
    public static double getDistanceInKilometers(GeoCoordinates a,GeoCoordinates b)
    {
        final double deltalat=Math.toRadians(a.lat-b.lat);
        final double deltalng=Math.toRadians(a.lng-b.lng);
        final double underRoot=Math.pow(Math.sin(deltalat/2.0),2.0)+Math.cos(Math.toRadians(a.lat))*Math.cos(Math.toRadians(b.lat))*Math.pow(Math.sin(deltalng/2.0),2.0);
        double result=2*Math.atan2(Math.sqrt(underRoot),Math.sqrt(1-underRoot));
        return EARTH_RADIUS*result;
    }
}
