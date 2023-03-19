package com.example.customlayoutproject;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Demigod implements Parcelable{
    String name, parent, camp, bulkInfo, so, opinion, species;
    int image, age;
    boolean alive;

    public Demigod(String name, String parent, String camp, String species, boolean alive, int age, String so, String opinion, int image){
        this.name = name;
        this.species = species;
        this.image = image;
        this.alive = alive;
        this.parent = parent;
        this.camp = camp;
        this.age = age;
        this.so = so;
        this.opinion = opinion;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public boolean getAlive(){
        return alive;
    }

    public String getParent() { return parent; }

    public String getCamp() { return camp; }

    public int getAge() { return age; }

    public void setBulkInfo(String bulkInfo) {
        this.bulkInfo = bulkInfo;
    }

    public String getBulkInfo() {
        return bulkInfo;
    }

    public String getSo() {
        return so;
    }

    public String getOpinion() {
        return opinion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(parent);
        parcel.writeString(camp);
        parcel.writeString(species);
        parcel.writeBoolean(alive);
        parcel.writeInt(age);
        parcel.writeString(so);
        parcel.writeString(opinion);
        parcel.writeInt(image);
    }

    public Creator<Demigod> CREATOR = new Creator<Demigod>(){
        @Override
        public Demigod createFromParcel(Parcel parcel) {
            return new Demigod(name, parent, camp, species, alive, age, so, opinion, image);
        }

        @Override
        public Demigod[] newArray(int i) {
            return new Demigod[i];
        }
    };

    public String toString () {
        return this.name;
    }


}
