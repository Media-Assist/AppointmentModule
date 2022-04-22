package com.example.appointmentmodule.RealtimeDB;

public class Data {
    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String slot, email;

    public Data(String slot, String email) {
        this.slot = slot;
        this.email = email;
    }
}
