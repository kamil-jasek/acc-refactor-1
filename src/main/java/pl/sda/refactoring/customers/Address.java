package pl.sda.refactoring.customers;

public final class Address {

    private final String addrStreet;
    private final String addrCity;
    private final String addrZipCode;
    private final String addrCountryCode;

    public Address(String addrStreet, String addrCity, String addrZipCode, String addrCountryCode) {
        this.addrStreet = addrStreet;
        this.addrCity = addrCity;
        this.addrZipCode = addrZipCode;
        this.addrCountryCode = addrCountryCode;
    }

    public String getAddrStreet() {
        return addrStreet;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public String getAddrZipCode() {
        return addrZipCode;
    }

    public String getAddrCountryCode() {
        return addrCountryCode;
    }
}