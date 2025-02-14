package com.healthcare.model;

import java.util.Date;

public class Consultation {
    private int consultationId;
    private int userId;
    private int doctorId;
    private Date date;
    private String notes;

    // Getters and setters

    public Consultation() {
    }

    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
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

    @Override
    public String toString() {
        return "Consultation{" +
                "consultationId=" + consultationId +
                ", userId=" + userId +
                ", doctorId=" + doctorId +
                ", date=" + date +
                ", notes='" + notes + '\'' +
                '}';
    }
}