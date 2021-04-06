package com.side.UI_Controllers;

import com.side.DataModel.UserData;
import com.side.DataModel.UserNote;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.Date;

public class DialogController {
    @FXML
    private TextArea detailTextArea;

    public UserNote processNote()
    {
        String userNote=detailTextArea.getText().trim();
        Date date=new Date();
        UserNote newNote=new UserNote(userNote, date);
        UserData.getInstance().addNote(newNote);
        return newNote;
    }

}
