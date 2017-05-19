package com.claroenergy.android.claroenergy;

/**
 * Created by vengalrao on 17-05-2017.
 */

public class Family {
    public int family_size;
    public Person[] family;
    public Family(int no){
        family_size=no;
        family = new Person[no];
    }
}
