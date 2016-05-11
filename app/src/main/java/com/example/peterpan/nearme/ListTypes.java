package com.example.peterpan.nearme;

import java.util.List;

/**
 * Created by Peterpan on 5/7/2016 AD.
 */
public class ListTypes {
    private List<Place> atm;
    private List<Place> bakery;
    private List<Place> bank;
    private List<Place> beauty_salon;
    private List<Place> bus_station;
    private List<Place> cafe;
    private List<Place> convenience_store;
    private List<Place> department_store;
    private List<Place> hospital;
    private List<Place> gas_station;
    private List<Place> supermarket;
    private List<Place> pharmacy;
    private List<Place> police;
    private List<Place> restaurant;

    @Override
    public String toString() {
        return "ListTypes{" +
                "atm=" + (atm==null) +
                ", bakery=" + (bakery==null) +
                ", bank=" + (bank==null) +
                ", beauty_salon=" + (beauty_salon==null) +
                ", bus_station=" + (bus_station==null) +
                ", cafe=" + (cafe==null) +
                ", convenience_store=" + (convenience_store==null) +
                ", department_store=" + (department_store==null) +
                ", hospital=" + (hospital==null) +
                ", gas_station=" + (gas_station==null) +
                ", supermarket=" + (supermarket==null) +
                ", pharmacy=" + (pharmacy==null) +
                ", police=" + (police==null) +
                ", restaurant=" + (restaurant==null) +
                '}';
    }

    public void setList(String type, List<Place> place){
        if(type.equalsIgnoreCase("atm")) {
            setAtm(place);
        } else if (type.equalsIgnoreCase("bakery")) {
            setBakery(place);
        } else if (type.equalsIgnoreCase("bank")) {
            setBank(place);
        } else if (type.equalsIgnoreCase("beauty_salon")) {
            setBeauty_salon(place);
        } else if (type.equalsIgnoreCase("bus_station")) {
            setBus_station(place);
        } else if (type.equalsIgnoreCase("cafe")) {
            setCafe(place);
        } else if (type.equalsIgnoreCase("convenience_store")) {
            setConvenience_store(place);
        } else if (type.equalsIgnoreCase("department_store")) {
            setDepartment_store(place);
        } else if (type.equalsIgnoreCase("hospital")) {
            setHospital(place);
        } else if (type.equalsIgnoreCase("gas_station")) {
            setGas_station(place);
        } else if (type.equalsIgnoreCase("grocery_or_supermarket")) {
            setSupermarket(place);
        } else if (type.equalsIgnoreCase("pharmacy")) {
            setPharmacy(place);
        } else if (type.equalsIgnoreCase("police")) {
            setPolice(place);
        } else if (type.equalsIgnoreCase("restaurant")) {
            setRestaurant(place);
        }
    }
    public List<Place> getAtm() {
        return atm;
    }

    public void setAtm(List<Place> atm) {
        this.atm = atm;
    }

    public List<Place> getBakery() {
        return bakery;
    }

    public void setBakery(List<Place> bakery) {
        this.bakery = bakery;
    }

    public List<Place> getBank() {
        return bank;
    }

    public void setBank(List<Place> bank) {
        this.bank = bank;
    }

    public List<Place> getBeauty_salon() {
        return beauty_salon;
    }

    public void setBeauty_salon(List<Place> beauty_salon) {
        this.beauty_salon = beauty_salon;
    }

    public List<Place> getBus_station() {
        return bus_station;
    }

    public void setBus_station(List<Place> bus_station) {
        this.bus_station = bus_station;
    }

    public List<Place> getCafe() {
        return cafe;
    }

    public void setCafe(List<Place> cafe) {
        this.cafe = cafe;
    }

    public List<Place> getConvenience_store() {
        return convenience_store;
    }

    public void setConvenience_store(List<Place> convenience_store) {
        this.convenience_store = convenience_store;
    }

    public List<Place> getDepartment_store() {
        return department_store;
    }

    public void setDepartment_store(List<Place> department_store) {
        this.department_store = department_store;
    }

    public List<Place> getHospital() {
        return hospital;
    }

    public void setHospital(List<Place> hospital) {
        this.hospital = hospital;
    }

    public List<Place> getGas_station() {
        return gas_station;
    }

    public void setGas_station(List<Place> gas_station) {
        this.gas_station = gas_station;
    }

    public List<Place> getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(List<Place> supermarket) {
        this.supermarket = supermarket;
    }

    public List<Place> getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(List<Place> pharmacy) {
        this.pharmacy = pharmacy;
    }

    public List<Place> getPolice() {
        return police;
    }

    public void setPolice(List<Place> police) {
        this.police = police;
    }

    public List<Place> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(List<Place> restaurant) {
        this.restaurant = restaurant;
    }
}
