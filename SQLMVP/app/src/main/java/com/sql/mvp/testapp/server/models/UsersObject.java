package com.sql.mvp.testapp.server.models;

import com.google.gson.annotations.SerializedName;

public class UsersObject {

    @SerializedName("first_name")
    String firstName;
    @SerializedName("last_name")
    String lastName;

    private UsersObject(Builder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

        public UsersObject build() {
            return new UsersObject(this);
        }
    }
}