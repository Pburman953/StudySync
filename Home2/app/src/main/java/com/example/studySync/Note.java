package com.example.studySync;

import java.util.Date;

public class Note {

    public String noteText;
    public Date createdAt;

    public Note(String noteText,Date createdAt){
        this.noteText = noteText;
        this.createdAt = createdAt;
    }

    public String getNoteText() {return noteText;}
    public Date getCreatedAt() {return createdAt;}


}
