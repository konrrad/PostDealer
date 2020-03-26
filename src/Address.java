public class Address {
    public final String street;
    public final String suite;
    public final String city;
    public final String zipcode;
    public final GeoCoordinates geo;
    public Address(String street, String suite, String city, String zipcode, GeoCoordinates geo) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.geo = geo;
    }


}
