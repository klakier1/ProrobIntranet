package com.klakier.proRobIntranet.old;

public class DbNewsletter {

    private int id;
    private String email;

    public DbNewsletter(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public DbNewsletter(String id, String email) {
        this.id = Integer.parseInt(id);
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return id + " - " + email;
    }
}
