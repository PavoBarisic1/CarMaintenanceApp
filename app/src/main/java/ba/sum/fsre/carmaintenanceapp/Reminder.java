package ba.sum.fsre.carmaintenanceapp;

public class Reminder {
    private String id;
    private String reminderName;
    private String reminderDate;

    public Reminder() {
        // Potreban prazan konstruktor za Firestore
    }

    public Reminder(String serviceType, String serviceDate) {
        this.reminderName = serviceType;
        this.reminderDate = serviceDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getreminderName() {
        return reminderName;
    }

    public void setreminderName(String serviceType) {
        this.reminderName = serviceType;
    }

    public String getreminderDate() {
        return reminderDate;
    }

    public void setreminderDate(String serviceDate) {
        this.reminderDate = serviceDate;
    }
}
