package com.sql.mvp.testapp.utils.data;

public class UsersData {

    private int id;
    private String firstName;
    private String lastName;

    public UsersData(int id, String firstName , String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UsersData(Builder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public static final class Builder {
        private String firstName;
        private String lastName;

        public Builder() {
        }

        public Builder firstName(String val) {
            firstName = val;
            return this;
        }

        public Builder lastName(String val) {
            lastName = val;
            return this;
        }

        public UsersData build() {
            return new UsersData(this);
        }
    }
}
