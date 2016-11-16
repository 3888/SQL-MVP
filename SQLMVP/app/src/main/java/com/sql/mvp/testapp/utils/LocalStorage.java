package com.sql.mvp.testapp.utils;

import de.ad.sharp.api.SharedPreference;

@SharedPreference
public interface LocalStorage {

    int getCachedLimit();

    void setCachedLimit(int value);

    int getStoredPageCount();

    void setStoredPageCount(int value);

}