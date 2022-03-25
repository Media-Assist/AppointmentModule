package com.example.appointmentmodule.DoctorProfile;
/**
 * This class is used for fetching the values from the firestore.
 * Same as User file
 */


public class DoctorsList {

    String FirstName, LastName, Specialization, Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public DoctorsList(){};

    public DoctorsList(String firstName, String lastName, String specialization, String email) {
        FirstName = firstName;
        LastName = lastName;
        Specialization = specialization;
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }
}
