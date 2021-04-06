package com.side;

import com.side.UI_Controllers.ClipBoard;
import com.side.UI_Controllers.DialogController;
import com.side.UI_Controllers.FalsePane;
import com.side.DataModel.UserData;
import com.side.DataModel.UserNote;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

public class Controller {

    //----------------------------FXML declaration-----------------------//


    @FXML
    private BorderPane mainWindow;

    @FXML
    private VBox allClipPane;

    @FXML
    private  VBox allNotePane;

    @FXML
    private HBox allList;

    @FXML
    private Label subTitle;

    @FXML
    private ImageView clipBoardList;

    //-------------------------------------------------------------//
    //inherit the whole stage from the main class.
    public Stage mainStage = Main.mainStage;

    //---------------------------ClipBoard----------------------------//
    // get the clipboard content
    Clipboard sysCB=Clipboard.getSystemClipboard();
    static String currentCB;        //stores the current clipboard text
    String lastCB;                  //stores the last clipboard text


    //----------------------------minimizer and close button-----------//

    //minimizes the whole window when minimizer button is clicked.
    public void minimizeClicked(MouseEvent event){  mainStage.setIconified(true); }

    //close the whole window when close button is clicked.
    public void closeClicked(MouseEvent event){
        System.exit(0);
    }


//------------------------declarations for the  delete function------//
    //a flag to know when delete button is clicked
    public static boolean deleteClicked = false;
    //countes the number of notes created
    public static int noteId = -1;

//------------------------declarations for the  addNote function------//

    //holds the content from the pane temporarly so that we can add the clip content as a note
    public static String tempPaneContent;
    //a flag to know when addNote button is clicked
    public static Boolean addNoteClicked= false;

    //--------------------------list of all clips , notes and a temporary list----//
    // a list of allclipBoard to be filled by
    public static LinkedList<StackPane> allClipList = new LinkedList<>();
    public static LinkedList<StackPane> allNoteList = new LinkedList<>();
    public static LinkedList<StackPane> tempList = new LinkedList<>();

    //holdes the temporary id of the pane clicked to be deleted
    public static int  tempNoteId = 0;



    //-------------------------------------------------------------//

    //holds activities to be run as soon as the app starts
    public void initialize(){
        // animation whn the app starts
        notificationTray("Side","Side have started running in the background");

        //set current CB to last CB
        lastCB = sysCB.getString();
        //check and see is the clip has been changed ever 1 second
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e->{

                    //check and see if the delete button has been clicked
                    if (deleteClicked== true){
                        try {
                            deletePane(tempNoteId);
                        } catch (IOException ex) {
                            ex.printStackTrace();UserData.getInstance().getUserNoteList().size();
                        }
                    }

                    if (addNoteClicked== true){
                        addClipToNote(tempPaneContent);
                    }

                    if (viewClicked == true){
                        viewWindowShow(tempPaneContent);
                    }

                    currentCB=sysCB.getString();
                    if(currentCB!=null){
                        if(!currentCB.equals(lastCB)){
                            System.out.println(currentCB);
                            try {
                                allclipBoardMTD(false,currentCB);
                            } catch (FileNotFoundException e1) {
                                System.out.println("allclipBoardMTD not working");
                                e1.printStackTrace();
                            }
                            lastCB=currentCB;
                        }
                    }
                })
        );
        //runs as long as the app is open
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        for(int i=0;i<UserData.getInstance().getUserNoteList().size();i++){
            try {
                allclipBoardMTD(true ,UserData.getInstance().getUserNoteList().get(i).getUserNote());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    //function that makes a custom pane
    //arg1 boolean true if note false if clip content
    //arg2 string accepts the content to be set in the pane
    public void allclipBoardMTD( boolean onNoteType ,String content) throws FileNotFoundException {

        //call the class that makes panes and put it on the list of panes
        tempList.addFirst(new ClipBoard().makeClip(onNoteType, content));

        //if note add to note list
        //if clip add to clip list
        if (UserData.getInstance().isNote()== true) {

            allNoteList.addFirst(tempList.getFirst());

            allNotePane.getChildren().add(0, allNoteList.getFirst());
        }


        else{
            allClipList.addFirst(tempList.getFirst());

            allClipPane.getChildren().add( 0 , allClipList.getFirst());
        }
    }

    //used to switch from note pane window to clip pane window
    //starts with the notepane set to visible
    public boolean noteDiplay = true;
    @FXML
    public void filterNoteList() throws FileNotFoundException {

        if (noteDiplay==true){

            noteDiplay=false;

            subTitle.setText("ClipBoard");

            Image icon = new Image(new FileInputStream("src/com/side/image/Add Button@2x.png"), 19, 19, true, true);

            clipBoardList.setImage(icon);

            //changes the window by clearing the vbox and adding the clip pane first
            allList.getChildren().add(new Label("  "));

            allList.getChildren().clear();

            allList.getChildren().add(allClipPane);



        }
        else {

            noteDiplay=true;

            subTitle.setText("Note");

            Image icon = new Image(new FileInputStream("src/com/side/image/Icon awesome-clipboard-list.png"), 19, 19, true, true);

            clipBoardList.setImage(icon);

            //changes the window by clearing the vbox and adding the clip pane first
            allList.getChildren().add(new Label("  "));

            allList.getChildren().clear();

            allList.getChildren().add(allNotePane);

        }

    }
    //called when the addNote button is clicked
    //acceptes text from the user and calles a function to make a custom pane to be displayed
    @FXML
    public void addNote() throws IOException {
        //called when the addNote button is clicked
        //creates a pane to accept text note from the user
        Dialog<ButtonType> dialog =new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("UI_Screens/addNote.fxml"));

        try
        {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            System.out.println("Content can't be displayed");
            e.printStackTrace();
            return;
        }

        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Optional<ButtonType> result = dialog.showAndWait();

        //if yes button clicked add to note list and make a pane
        if(result.isPresent()&&result.get()==ButtonType.OK){
            DialogController controller=new DialogController();
            controller=fxmlLoader.getController();
            UserNote newItem=controller.processNote();
            allclipBoardMTD(true,newItem.getUserNote());
            UserData.getInstance().storeData();
        }
        //if cancel button clicked abort and go back
        else{
            System.out.println("cancel pressed");
        }
    }

    //called when the clear all button is clicked
    //clears all the content in the list and pane
    @FXML
    public void clearPane() throws IOException {

        //ask if user is sure about clearing all the content
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Clear All Your Notes");
        alert.setContentText("Are you sure you want to clear all your note?");
        Optional<ButtonType> result=alert.showAndWait();

        //if yes clicked
        if(result.isPresent()&&result.get()==ButtonType.OK){

            //if clicked on the note pane clear the note pane and note list
            if (noteDiplay==true) {
                allNotePane.getChildren().clear();
                allNoteList.clear();

                //set the id to start again from empty in the CLipBoard class
                ClipBoard.clipid= -1;

                //clear all the content from the file
                UserData.getInstance().clearNotes();
                UserData.getInstance().storeData();
            }
            //if clicked on the clip pane clear the clip pane and clip list
            else{
                allClipPane.getChildren().clear();
                allClipList.clear();
            }
        }


    }



    //called when the delete button clicked
    //arg1 int accepts the ID of the pane from the ClipBoard class
    public void deleteClicked(int clipId){

        //gives the note id to the global variable
        tempNoteId=clipId;

        //sets the flag that the delete button is clicked
        deleteClicked=true;
    }

    //called when the deleteClick flag is true
    //arg1 int accept clipId that will be deleted from the list and pane
    public void deletePane(int clipId) throws IOException {
        //delete from the note file
        UserData.getInstance().deleteNote(clipId);
        UserData.getInstance().storeData();



        //reverse the number to be accepted by the list index
        clipId = allNotePane.getChildren().size()- clipId - 1;




        //remove from the allnotelist pane visually
        allNotePane.getChildren().remove(clipId);
        //remove from the allNoteList backend
        allNoteList.remove(clipId);
        //create and add a dummy pane and add to the list
        allNotePane.getChildren().add(clipId, new FalsePane().makePane());
        allNoteList.add(clipId, new FalsePane().makePane());
        //set the deleteClicked set to false
        deleteClicked=false;
    }


    //called when the addNote button clicked
    //arg1 String accepts the paneContent from the ClipBoard class
    public void addNoteClicked(String paneContent){
        //gives the paneContent to the global variable
        tempPaneContent=paneContent;

        //sets the flag that the addNote button is clicked
        addNoteClicked=true;
    }

    public static boolean viewClicked = false;

    public void viewClicked(String paneContent){

        tempPaneContent=paneContent;

        viewClicked = true;

    }

    public void viewWindowShow(String paneContent){

       Alert alert=new Alert(Alert.AlertType.NONE);
//       alert.getDialogPane().setStyle("-fx-fill:#eeeeee; -fx-background-color:#393e46");
       alert.setHeaderText("Note Detail");
       alert.setContentText(paneContent);
       alert.initStyle(StageStyle.UNDECORATED);
       alert.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        alert.show();

        viewClicked = false;

    }


    //called when the addNoteClick flag is true
    //arg1 int accept paneContent that will be added to the pane list and pane
    public void addClipToNote(String paneContent){
        try {
            allclipBoardMTD(true,paneContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            addNoteClicked=false;
        }
    }

    //called when the app starts and notifies the user that the app is running in the background
    public void notificationTray(String title, String notificationMessage){
        NotificationType notificationType = NotificationType.CUSTOM;
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Side is running");
        tray.setMessage("Side is active in the background.");
        tray.setNotificationType(notificationType);
        tray.setRectangleFill(Paint.valueOf("#000000"));
        tray.showAndDismiss(Duration.millis(3000));
    }



}









