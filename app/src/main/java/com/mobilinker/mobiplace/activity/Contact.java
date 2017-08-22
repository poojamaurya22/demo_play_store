package com.mobilinker.mobiplace.activity;

import android.app.Activity;

/**
 * Created by Pooja on 25-02-2017.
 */

public class Contact extends Activity{
    String name, phone, email;

    public void setUname(String uname){
        this.name = uname;
    }
    public String getUname(){
        return this.name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return this.phone;
    }
}
