package ba.sum.fsre.carmaintenanceapp;
import java.io.Serializable;
public class Vehicle {
    private String vehicleType;
    private String manufacturer;
    private String model;
    private String modelYear;
    private String licensePlate;
    private String fuelType;
    private String mileage;
    private String vehicleInfo;
    private String manufacturerAndModel;  // Add this field

    public Vehicle() {}

    public Vehicle(String vehicleType, String manufacturer, String model, String modelYear, String licensePlate, String fuelType, String mileage) {
        this.vehicleType = vehicleType;
        this.manufacturer = manufacturer;
        this.model = model;
        this.modelYear = modelYear;
        this.licensePlate = licensePlate;
        this.fuelType = fuelType;
        this.mileage = mileage;
        this.manufacturerAndModel = manufacturer + " " + model;  // Set value here
        this.vehicleInfo = generateVehicleInfo();
    }

    // Getter and setter for manufacturerAndModel
    public String getManufacturerAndModel() {
        return manufacturerAndModel;
    }

    public void setManufacturerAndModel(String manufacturerAndModel) {
        this.manufacturerAndModel = manufacturerAndModel;
        updateVehicleInfo();
    }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; updateVehicleInfo(); }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; updateVehicleInfo(); }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; updateVehicleInfo(); }

    public String getModelYear() { return modelYear; }
    public void setModelYear(String modelYear) { this.modelYear = modelYear; updateVehicleInfo(); }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getMileage() { return mileage; }
    public void setMileage(String mileage) { this.mileage = mileage; }

    public String getVehicleInfo() { return vehicleInfo; }
    public void setVehicleInfo(String vehicleInfo) { this.vehicleInfo = vehicleInfo; }

    private String generateVehicleInfo() {
        return vehicleType + " " + manufacturer + " " + model + ", " + modelYear;
    }

    private void updateVehicleInfo() {
        this.vehicleInfo = generateVehicleInfo();
        this.manufacturerAndModel = manufacturer + " " + model;  // Update manufacturerAndModel if necessary
    }
}
