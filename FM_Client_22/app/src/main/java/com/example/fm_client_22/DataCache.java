package com.example.fm_client_22;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import DAO_Models.Event;
import DAO_Models.Person;
import ReqRes.ResultMessage;

public class DataCache implements Parcelable {
    private static DataCache instance = new DataCache();

    protected DataCache(Parcel in) {
        authToken = in.readString();
    }

    public static final Creator<DataCache> CREATOR = new Creator<DataCache>() {
        @Override
        public DataCache createFromParcel(Parcel in) {
            return new DataCache(in);
        }

        @Override
        public DataCache[] newArray(int size) {
            return new DataCache[size];
        }
    };

    public static DataCache getInstance(){
        return instance;
    }

    private DataCache(){
    }

    public String serverHost = "";
    public String port = "";
    public String authToken = "";
    public Map<String, Person> people = new HashMap<>();
    public Map<String, Event> events = new HashMap<>();
    public Set<String> paternalAncestors = new HashSet<>();
    public Set<String> maternalAncestors = new HashSet<>();

    boolean lifeSetting = true;
    boolean treeSetting = true;
    boolean spouseSetting = true;
    boolean fatherSetting = true;
    boolean motherSetting = true;
    boolean maleSetting = true;
    boolean femaleSetting = true;

    public Person currentPerson;
    public ResultMessage recentResult = new ResultMessage(null, null, null,
            null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, "null", false);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(authToken);
    }

    public void clearData(){
        serverHost = "";
        port = "";
        authToken = "";
        people.clear();
        events.clear();
        paternalAncestors.clear();
        maternalAncestors.clear();
        currentPerson = null;
        recentResult = new ResultMessage(null, null, null,
                null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null, null, "null", false);

    }

    public boolean checkFilter(String id){
        if (!this.fatherSetting && this.paternalAncestors.contains(id)){
            return false;
        } else if (!this.motherSetting && this.maternalAncestors.contains(id)){
            return false;
        } else if (!this.maleSetting && Objects.equals(this.people.get(id).getGender(), "m")){
            return false;
        } else if (!this.femaleSetting && Objects.equals(this.people.get(id).getGender(), "f")){
            return false;
        }
        return true;
    }

    public boolean checkSearchFilter(String id){
        if (!this.fatherSetting && this.paternalAncestors.contains(id)){
            return false;
        } else if (!this.motherSetting && this.maternalAncestors.contains(id)){
            return false;
        }
        return true;
    }

}
