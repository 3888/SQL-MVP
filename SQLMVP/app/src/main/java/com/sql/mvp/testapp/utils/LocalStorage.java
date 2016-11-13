package com.sql.mvp.testapp.utils;

import de.ad.sharp.api.SharedPreference;

@SharedPreference
public interface LocalStorage {

    int getStoredCount();

    void setStoredCount(int value);

}
