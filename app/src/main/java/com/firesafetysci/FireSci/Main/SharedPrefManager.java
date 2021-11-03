package com.firesafetysci.FireSci.Main;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "shared_pref";
    private static final String KEY_FIRESCI_PIN = "firesci_pin";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "lname";
    private static final String KEY_EMAIL_ADDRESS = "email_address";
    private static final String KEY_INSTALLER_OR_CUSTOMER = "ins_or_cus";
    private static final String KEY_KEEP_ME_SIGNED_IN = "k_m_s_i";

    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void setFiresciPin(String firesciPin) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_FIRESCI_PIN, firesciPin);
        editor.apply();
    }

    public String getFireSciPin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FIRESCI_PIN, "");
    }

    public void setFirstName(String firstName) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_FIRST_NAME, firstName);
        editor.apply();
    }

    public String getFirstName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FIRST_NAME, "Dear Customer");
    }

    public void setLastName(String lastName) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_LAST_NAME, lastName);
        editor.apply();
    }

    public String getLastName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LAST_NAME, "Dear Customer");
    }

    public void setEmailAddress(String emailAddress) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_EMAIL_ADDRESS, emailAddress);
        editor.apply();
    }

    public String getEmailAddress() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL_ADDRESS, "");
    }

    public void setInstallerOrCustomer(int installerOrCustomer) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_INSTALLER_OR_CUSTOMER, installerOrCustomer);
        editor.apply();
    }

    public int getInstallerOrCustomer() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_INSTALLER_OR_CUSTOMER, -1);
    }

    public void setKeepMeSignedIn(boolean keepMeSignedIn) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(KEY_KEEP_ME_SIGNED_IN, keepMeSignedIn);
        editor.apply();
    }

    public boolean getKeepMeSignedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_KEEP_ME_SIGNED_IN, false);
    }
}
