package com.example.todoapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Task implements Parcelable {
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private String context;
    private String lien;

    public Task(String title, String description, String startDate, String endDate, String context, String lien) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.context = context;
        this.lien  = lien;
    }

    protected Task(Parcel in) {
        title = in.readString();
        description = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        context = in.readString();
        lien = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(context);
        dest.writeString(lien);
    }
}
