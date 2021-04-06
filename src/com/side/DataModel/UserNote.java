package com.side.DataModel;

import java.util.Date;

public class UserNote {
    String userNoteData;
    Date timeOfNoteCreateion;



    public UserNote(String userNoteData, Date timeOfNoteCreateion) {
        this.userNoteData = userNoteData;
        this.timeOfNoteCreateion = timeOfNoteCreateion;
    }

    public String getUserNote() {
        return userNoteData;
    }

    public void setUserNote(String userNote) {
        this.userNoteData = userNote;
    }

    public Date getTimeOfNoteCreateion() {
        return timeOfNoteCreateion;
    }

    public void setTimeOfNoteCreateion(Date timeOfNoteCreateion) {
        this.timeOfNoteCreateion = timeOfNoteCreateion;
    }


}
