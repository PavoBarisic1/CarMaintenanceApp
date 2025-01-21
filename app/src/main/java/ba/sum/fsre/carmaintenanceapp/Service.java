package ba.sum.fsre.carmaintenanceapp;

import java.io.Serializable;

public class Service implements Serializable {
    private String serviceType;
    private String serviceDate;
    private String mileage;
    private String cost;
    private String notes;
    private String licensePlate;  // Dodan licensePlate

    public Service() {
    }
    public Service(String serviceType, String serviceDate, String mileage, String cost, String notes, String licensePlate) {
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.mileage = mileage;
        this.cost = cost;
        this.notes = notes;
        this.licensePlate = licensePlate;
    }

    // Getteri i setteri
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getServiceDate() { return serviceDate; }
    public void setServiceDate(String serviceDate) { this.serviceDate = serviceDate; }

    public String getMileage() { return mileage; }
    public void setMileage(String mileage) { this.mileage = mileage; }

    public String getCost() { return cost; }
    public void setCost(String cost) { this.cost = cost; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
}
