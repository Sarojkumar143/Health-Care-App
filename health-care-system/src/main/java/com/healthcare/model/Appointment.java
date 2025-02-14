package com.healthcare.model;

import java.util.Date;

public class Appointment {
    private int id;
    private String doctorName;
    private Date date;
    private String notes;
    private String patientName;
    private String status;
    private String doctorUserName;

    // Constructor
    public Appointment(int id, String doctorName, Date date, String notes, String patientName, String status) {
        this.id = id;
        this.date = date;
        this.notes = notes;
        this.doctorName = doctorName;
        this.doctorUserName = doctorName;
        this.patientName = patientName;
        this.status = status;
    }

    public Appointment(int id, String doctorName, String doctorUserName, Date date, String notes, String patientName, String status) {
        this.id = id;
        this.date = date;
        this.notes = notes;
        this.doctorName = doctorName;
        this.doctorUserName = doctorUserName;
        this.patientName = patientName;
        this.status = status;
    }

    public Appointment(int id, String patientName, Date date, String notes, String status) {
        this.id = id;
        this.patientName = patientName;
        this.date = date;
        this.notes = notes;
        this.status = status;
    }

    public Appointment() {

    }

    public String getDoctorUserName() {
        return doctorUserName;
    }

    public void setDoctorUserName(String doctorUserName) {
        this.doctorUserName = doctorUserName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", doctorName='" + doctorName + '\'' +
                ", date=" + date +
                ", notes='" + notes + '\'' +
                ", patientName='" + patientName + '\'' +
                ", status='" + status + '\'' +
                ", doctorUserName='" + doctorUserName + '\'' +
                '}';
    }
}
