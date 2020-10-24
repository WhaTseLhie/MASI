package com.example.asus.masi.about;

import android.net.Uri;

public class About {

    int aboutId;
    String appAbout, appServiceHours, appAddress;

    public About(int aboutId, String appAbout, String appServiceHours, String appAddress) {
        this.aboutId = aboutId;
        this.appAbout = appAbout;
        this.appServiceHours = appServiceHours;
        this.appAddress = appAddress;
    }

    public int getAboutId() {
        return aboutId;
    }

    public void setAboutId(int aboutId) {
        this.aboutId = aboutId;
    }

    public String getAppAbout() {
        return appAbout;
    }

    public void setAppAbout(String appAbout) {
        this.appAbout = appAbout;
    }

    public String getAppServiceHours() {
        return appServiceHours;
    }

    public void setAppServiceHours(String appServiceHours) {
        this.appServiceHours = appServiceHours;
    }

    public String getAppAddress() {
        return appAddress;
    }

    public void setAppAddress(String appAddress) {
        this.appAddress = appAddress;
    }
}