package com.example.fm_client_22;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Map<String, List<Event>> personEvents;
    public Set<String> paternalAncestors;
    public Set<String> maternalAncestors;
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
}
