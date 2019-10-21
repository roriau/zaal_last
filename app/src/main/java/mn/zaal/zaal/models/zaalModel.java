package mn.zaal.zaal.models;

public class zaalModel {
    private String name;
    private String description;
    private String city;
    private String district;
    private String khoroo;
    private String address;
    private String price_tal;
    private String price_buten;
    private String phone;
    private String map_lat;
    private String map_lng;
    private String zaal_pic;
    private boolean expanded;

    public zaalModel() {
    }

    public zaalModel(String name, String description, String city, String district, String khoroo, String address, String price_tal, String price_buten, String phone, String map_lat, String map_lng, String zaal_pic) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.district = district;
        this.khoroo = khoroo;
        this.address = address;
        this.price_tal = price_tal;
        this.price_buten = price_buten;
        this.phone = phone;
        this.map_lat = map_lat;
        this.map_lng = map_lng;
        this.zaal_pic = zaal_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getKhoroo() {
        return khoroo;
    }

    public void setKhoroo(String khoroo) {
        this.khoroo = khoroo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice_tal() {
        return price_tal;
    }

    public void setPrice_tal(String price_tal) {
        this.price_tal = price_tal;
    }

    public String getPrice_buten() {
        return price_buten;
    }

    public void setPrice_buten(String price_buten) {
        this.price_buten = price_buten;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMap_lat() {
        return map_lat;
    }

    public void setMap_lat(String map_lat) {
        this.map_lat = map_lat;
    }

    public String getMap_lng() {
        return map_lng;
    }

    public void setMap_lng(String map_lng) {
        this.map_lng = map_lng;
    }

    public String getZaal_pic() {
        return zaal_pic;
    }

    public void setZaal_pic(String zaal_pic) {
        this.zaal_pic = zaal_pic;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

}
