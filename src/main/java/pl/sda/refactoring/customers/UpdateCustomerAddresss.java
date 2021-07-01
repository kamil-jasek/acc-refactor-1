package pl.sda.refactoring.customers;

import java.util.UUID;

public class UpdateCustomerAddresss {

    private final UUID cid;
    private final String str;
    private final String zipcode;
    private final String city;
    private final String country;

    /**
     * @param cid
     * @param str
     * @param zipcode
     * @param city
     * @param country
     */
    public UpdateCustomerAddresss(UUID cid, String str, String zipcode, String city, String country) {
        this.cid = cid;
        this.str = str;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
    }

    public UUID getCustomerId() {
        return cid;
    }

    public String getStr() {
        return str;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
}
