package com.healthcare.model;

public class Prescription {

    private int id;
    private String medication;
    private String dosage;
    private String duration;

    // Default constructor
    public Prescription() {
    }

    // Constructor with all fields
    public Prescription(int id, String medication, String dosage, String duration) {
        this.id = id;
        this.medication = medication;
        this.dosage = dosage;
        this.duration = duration;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Prescription [id=" + id + ", medication=" + medication + ", dosage=" + dosage + ", duration=" + duration + "]";
    }
}
