package com.side.UI_Controllers;

import com.side.Controller;
import com.side.Utility.CreatedTime;
import com.side.DataModel.UserData;
import com.side.DataModel.UserNote;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class ClipBoard extends CreatedTime {


    //sets the width and height of each clip box
    public int clipBoxWidth = 522 ;
    public int clipBoxHeight = 146 ;

    private Controller controller=new Controller();

    BorderPane clipToolPane;
    HBox toolHPane;
    StackPane addNotebtn;

    // a global variable to hold the content
    String paneContent;

    //for copying content from the pane to the system
    Toolkit toolkit=Toolkit.getDefaultToolkit();
    Clipboard sysCB=toolkit.getSystemClipboard();

    //holds the date of creation
    Date currentDate=new Date();


    //holds the temporary content to be displayed
    String displayContent="";
    String displayContent2="";
    
    // counts the number of clipBoards created to give the an id
    public static int clipid = -1;
    public int currentClipId ;

    //the main pane to be returned to the caller
    StackPane clipPane;


    public StackPane makeClip(boolean onNoteType, String content) throws FileNotFoundException {

//        if (content == "this is a text"){
//
//            return new FalsePane().makePane();
//
//        }

        //-------------------------------------------------------------------------------
        paneContent=content;
        //iterate the clip id everytime a new clip is created

//        clipid++;
//        currentClipId = clipid;

        //check if the content is longer that the possible view length if so
        //cut it without distroying the real content
        if(content.length() > 222){
            displayContent = content.trim().substring(0, 221)  + "...";
        }
        else{
            displayContent = content;
        }

        //checkif the content is longer than 4 lines if so
        //cut it without distroying the real content
        int i=0;
        for (String retval: displayContent.trim().split("\r", 9)){
            if(i<3){
                displayContent2 += retval + "\r";
                i++;
            }

        }

        Text clipText = new Text(displayContent2);
        clipText.setWrappingWidth(clipBoxWidth-30);
        clipText.getStyleClass().addAll("clip-text");


        Circle deleteTool = new Circle(9);
        deleteTool.getStyleClass().addAll("clip-tools", "delete-tool");
        ImageView deleteIcon = new ImageView( new Image(new FileInputStream("src/com/side/image/Icon feather-trash-2" +
                ".png"), 19, 19, true, true));
        StackPane deletebtn = new StackPane(deleteTool,deleteIcon);
        deletebtn.getStyleClass().addAll("tools", "deletebtn");
        deletebtn.setOnMouseClicked(e -> {
            try {
                deletePane(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Circle addNoteTool = new Circle(9);
        // editTool.setFill(Color.PURPLE);
        addNoteTool.getStyleClass().addAll("clip-tools", "edit-tool");
        ImageView addNoteIcon = new ImageView( new Image(new FileInputStream("src/com/side/image/Add Button.png"), 20,
                20, true, true));
        addNotebtn = new StackPane(addNoteTool,addNoteIcon);
        addNotebtn.getStyleClass().addAll("tools", "editbtn");
        addNotebtn.setOnMouseClicked(event -> {
            try {
                addPane(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        Circle copyTool = new Circle(9);
        // clipTool.setFill(Color.PURPLE);
        copyTool.getStyleClass().addAll("clip-tools", "clip-tool");
        ImageView copyIcon = new ImageView( new Image(new FileInputStream("src/com/side/image/Icon awesome-copy.png"),
                19, 19, true, true));
        StackPane copybtn = new StackPane(copyTool,copyIcon);
        copybtn.getStyleClass().addAll("tools", "clipbtn");
        copybtn.setOnMouseClicked(event -> copyPane(event));

        Circle viewTool = new Circle(9);

        viewTool.getStyleClass().addAll("clip-tools", "clip-tool");
        ImageView viewIcon = new ImageView( new Image(new FileInputStream("src/com/side/image/icons8_eye_16.png"),
                19, 19, true, true));
        StackPane viewbtn = new StackPane(viewTool,viewIcon);
        viewbtn.getStyleClass().addAll("tools", "clipbtn");
        viewbtn.setOnMouseClicked(e -> {
            try {
                showView(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });



        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        //if the content is a note add a delete button
        if (onNoteType == true){
            toolHPane = new HBox(12,spacer,viewbtn,copybtn,deletebtn);
            UserData.getInstance().setNote(true);

            clipid++;
            currentClipId = clipid;
            System.out.println("the current clip id is " + currentClipId);
            System.out.println("the  clip id is " + clipid);
        }
        //if the content is a clip add a addNote button
        else{
            toolHPane = new HBox(12,spacer,viewbtn,addNotebtn,copybtn);
            UserData.getInstance().setNote(false);

        }

        HBox.setMargin(toolHPane, new Insets(4,3,15,0));
        toolHPane.setAlignment(Pos.TOP_RIGHT);
        toolHPane.setMargin(copybtn,new Insets(5,8,0,0));
        toolHPane.setMargin(deletebtn,new Insets(5,8,0,0));



        //get and set time of creation
        String TOC = new CreatedTime().getDate();
        Text timeDisplay = new Text(TOC);
        timeDisplay.getStyleClass().add("toc");

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox timeHPane = new HBox(spacer2,timeDisplay);
        timeHPane.setAlignment(Pos.BOTTOM_RIGHT);
        timeHPane.setMargin(timeDisplay, new Insets(0,8,8,0));

        Region spacer3 = new Region();
        VBox.setVgrow(spacer3, Priority.ALWAYS);
        VBox clipToolTimePane = new VBox(15,spacer3,timeHPane);
        clipToolTimePane.setAlignment(Pos.BOTTOM_RIGHT);

        clipToolPane = new BorderPane();
        clipToolPane.setTop(toolHPane);
        clipToolPane.setBottom(clipToolTimePane);
        //invisible by default but is visible when hovering
        toolHPane.setVisible(false);



        clipPane = new StackPane(clipText,clipToolPane);
        clipPane.setMaxWidth(clipBoxWidth);
        clipPane.setMinWidth(clipBoxWidth);
        clipPane.setPrefWidth(clipBoxWidth);

        clipPane.setMaxHeight(clipBoxHeight);
        clipPane.setMinHeight(clipBoxHeight);
        clipPane.setPrefHeight(clipBoxHeight);

        //to make buttons visible when hovering
        clipPane.setOnMouseEntered(e -> showTools(e));
        //to make buttons invisible when hovering is over
        clipPane.setOnMouseExited(e -> hideTools(e));


        clipPane.getStyleClass().addAll("clip-pane");


        //return the whole pane to the caller to be added to a list and pane
        return clipPane;
    }

    public void showView(MouseEvent e) throws IOException {

        controller.viewClicked(paneContent);

    }

    //show tool buttons
    public void showTools(MouseEvent e){

        toolHPane.setVisible(true);

    }

    //hide tool buttons
    public void hideTools(MouseEvent e){

        toolHPane.setVisible(false);

    }

    //called when addNote button is clicked
    public void addPane(MouseEvent e) throws IOException {


        //add clip content to the note file
        UserNote newNote=new UserNote(paneContent, currentDate);
        UserData.getInstance().addNote(newNote);
        UserData.getInstance().storeData();
        System.out.println("pane is "+ paneContent);

        //add clip content to the note pane to be displayed
        controller.addNoteClicked(paneContent);

        //make the addNote button no longer accessable
        addNotebtn.setVisible(false);
    }

    //called when the delete button is clicked
    public void deletePane(MouseEvent e) throws IOException {

        //delete content from list and pane
        controller.deleteClicked(currentClipId);
    }

    //called when the copy button is clicked
    public void copyPane(MouseEvent e)
    {
        //addes the content to the current ClipBoard
        StringSelection selection= new StringSelection(paneContent);
        sysCB.setContents(selection,null);
    }

}