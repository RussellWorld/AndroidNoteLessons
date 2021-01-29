package com.example.androidnotelessons;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

class Note implements Parcelable {
    private String name; //Имя заметки
    private String description; //Описание
    private long creationDateUnixTime; //Дата создания Unix time
    private boolean isImportant; //Признак важности
    private String content; //Содержимое заметки

    public Note(String name, String description, long creationDateUnixTime, boolean isImportant, String content) {
        this.name = name;
        this.description = description;
        this.creationDateUnixTime = creationDateUnixTime;
        this.isImportant = isImportant;
        this.content = content;
    }

    protected Note(Parcel in) {
        name = in.readString();
        description = in.readString();
        creationDateUnixTime = in.readLong();
        isImportant = in.readByte() != 0;
        content = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreationDateUnixTime() {
        return creationDateUnixTime;
    }

    public void setCreationDateUnixTime(long creationDateUnixTime) {
        this.creationDateUnixTime = creationDateUnixTime;
    }

    public String getFormatedCreationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(((long) getCreationDateUnixTime()));
        return String.format("%d/%d/%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(creationDateUnixTime);
        dest.writeByte((byte) (isImportant ? 1 : 0));
        dest.writeString(content);
    }

}