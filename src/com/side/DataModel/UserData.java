package com.side.DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class UserData {
    private static UserData instance=new UserData();
    private static String filename="UserNote.txt";
    private boolean isNote=false;

    public boolean isNote() {
        return isNote;
    }

    public void setNote(boolean note) {
        isNote = note;
    }

    private ObservableList<UserNote> userNoteList;
    private DateFormat dateFormatter;



    public static UserData getInstance(){
        return instance;
    }
    private UserData(){
        this.dateFormatter=new SimpleDateFormat("dd-MM-yyyy");
    }

    public ObservableList<UserNote> getUserNoteList() {
        return userNoteList;
    }

    public void setUserNoteList(ObservableList<UserNote> userNoteList) {
        this.userNoteList = userNoteList;
    }

    public void loadNotes() throws IOException {
        userNoteList= FXCollections.observableArrayList();
        Path path= Paths.get(filename);
        BufferedReader br= Files.newBufferedReader(path);
        String input;
        try{
            while((input=br.readLine())!=null){
                String[] noteData=input.split("\t");

                String noteDetail=noteData[0];
                Date timeOfNoteCreation=new Date();


                UserNote data=new UserNote(noteDetail, timeOfNoteCreation);
                userNoteList.add(data);
            }
        }finally{
            if(br!=null){
                br.close();
            }
        }
    }

    public void storeData() throws IOException {
        Path path=Paths.get(filename);
        BufferedWriter bw=Files.newBufferedWriter(path);
        try{
            Iterator<UserNote> userNoteIterator=userNoteList.iterator();
            while(userNoteIterator.hasNext()){
                UserNote note=userNoteIterator.next();
                bw.write(String.format("%s\t%s", note.userNoteData, dateFormatter.format(note.timeOfNoteCreateion)));
                bw.newLine();
            }

        }finally{
            if(bw!=null){
                bw.close();
            }
        }
    }

    public void addNote(UserNote newNote) {
        userNoteList.add(newNote);
    }



    public void deleteNote(int noteIndex) {
//        Date currentTime=new Date();

//        UserNote note = new UserNote("this is a text", currentTime);
        userNoteList.remove(noteIndex);
//        userNoteList.add(noteIndex, note);
    }
    public void clearNotes(){
        userNoteList.clear();
    }
}
