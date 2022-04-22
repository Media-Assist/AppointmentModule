package com.example.appointmentmodule.DoctorViewAppointment;

public class PatientDetails {
    String date, time, patientId, patientName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public PatientDetails(String date, String time, String patientId, String patientName) {
        this.date = date;
        this.time = time;
        this.patientId = patientId;
        this.patientName = patientName;
    }
}
