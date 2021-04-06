package com.side;

import com.side.DataModel.UserData;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.*;
import java.util.List;
public class Main extends Application {
    //***MAIN SIDE WINDOW CUSTOMIZATION***//

    Stop[] stops = new Stop[] { new Stop(0, Color.rgb(57, 62, 70,0.95)), new Stop(1, Color.GRAY)};  //MAIN SIDE WINDOW BACKGROUND COLOR
    LinearGradient linearG = new LinearGradient(0, 0, 0, 0, true, CycleMethod.NO_CYCLE, stops); //SETS THE GRADIENT BUT THERE'S NO GRADIENT
    Rectangle2D screen= Screen.getPrimary().getBounds();
    double screenH=screen.getHeight()-30;//****SIDE WINDOW HEIGHT****//
    double screenW=screen.getWidth();    //****SIDE WINDOW WIDTH****//
    int w=100;
    int viewW=80;
    File preD=new File("C:/Side");
    File preF=new File("C:/Side/pre.txt");
    Line line=new Line(viewW,0,viewW,screenH);  //***INVISIBLE LEFT BORDER LINE FOR THE SIDE WINDOW***//
    Rectangle dropWhat=new Rectangle(15,0,33,27.5);        //DRAGGING WINDOW
    Rectangle dropWhatTemp=new Rectangle(15,0,33,27.5);    //DRAGGING TEMPORARY WINDOW
    Text dropWhatTxt=new Text();       //******FONT STYLE WHEN DRAGGING**********//
    Text dropWhatTxtTemp=new Text();  //****TEMPORARY FONT STYLE WHEN DRAGGING**//
    VBox sideBar=new VBox();//THE BAR CONTAINING THE ICONS AND THE DELETE BUTTON//
    // HBox popUpHbox=new HBox();
    Text popUpTxt=new Text();
    Text popUpExitB=new Text("X");
    BoxBlur blur=new BoxBlur(3,3,3);





    //*********************************************************//
    //****************************EXIT BUTTON n CLIP BUTTON****************************//
    //*********************************************************//



    Rectangle exitB=new Rectangle(35,screenH-55,20,20);   //EXIT BUTTON CUSTOMIZATION
    Rectangle clipB=new Rectangle(35,screenH-92,20,20);
    VBox bottombox=new VBox(clipB,exitB);//***VERTICAL BOX THAT CONTAINS CLIPBOARD BUTTON AND EXIT BUTTON


    //*************************************************************************//
    //*****************************MAIN UI GROUP*******************************//
    //****************************CONTAINS ALL THE SHIT************************//
    //*************************************************************************//

    Group root=new Group(sideBar,line,bottombox);
    Scene scene=new Scene(root);

    // Scene popUpScene=new Scene(popUpHbox);

    Stage popUp=new Stage();
    FadeTransition fade=new FadeTransition();

    //******************************************************************************************//
    //************AFTER CURSOR MOVED FROM THE SIDE BAR THE WINDOW BECOME HIDDEN*****************//
    //******************************************************************************************//

    KeyFrame sideBarFadeKF=new KeyFrame(Duration.millis(150), event ->
    {

        clipB.setFill(Color.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
    });


    //*****************************************************************//
    //*******************SCROLL DOWN FUNCTION**************************//
    //*****************************************************************//
    Timeline sideBarFadeTL=new Timeline(sideBarFadeKF);




    void scrollUp(){

        sideBar.setTranslateY(sideBar.getTranslateY()+40);
        if(sideBar.getTranslateY()>32) sideBar.setTranslateY(32);
    }
    void scrollDown(){
        sideBar.setTranslateY(sideBar.getTranslateY()-40);
        if(sideBar.getTranslateY()<750-sideBar.getHeight()) sideBar.setTranslateY(750-sideBar.getHeight());

    }




    //**************************************************************************//
    //*************************ICON FRAME CUSTOMIZATION*************************//
    //**************************************************************************//


    void mkIcon(Image icon, String title, String location) throws FileNotFoundException  //**************METHOD THAT USED TO MAKE THE MAIN ICONS*************//
    {
        Rectangle frame=new Rectangle(30,30);    // WIDTH AND HEIGHT OF THE FRAME OF THE ICONS

        ImagePattern whatInZframe=new ImagePattern(icon);            //*********IMAGE PATTERN FOR THE MAIN ICONS*********//

        frame.setFill(whatInZframe);
        frame.setArcHeight(5);
        frame.setArcWidth(5);
        VBox.setMargin(frame, new Insets(0,0,13,13)); //****MARGIN FOR THE ICONS****//
        frame.setCursor(Cursor.HAND);
        frame.setOnMousePressed(event->{
            if(Own.util.$.Str(event.getButton())=="PRIMARY" & event.getClickCount()==2){
                try{Runtime.getRuntime().exec("explorer.exe"+" "+location);}
                catch(Exception err){popUpTxt.setText("Err @ Running [Explorer.exe]"); popUp.show();};
            }
        });

        //***********************************************************************//
        //********************** DELETE BUTTON PROPERTIES ***********************//
        //***********************************************************************//

        Rectangle deleteB=new Rectangle(17,17);
        deleteB.setCursor(Cursor.HAND);
        deleteB.setFill(Color.TRANSPARENT);
        Image iconDelete=new Image(new FileInputStream("sideImg/deleteIco.png"));

        deleteB.setOpacity(1);
        VBox.setMargin(deleteB, new Insets(-48,12,0,-38));         //**LOCATION OF THE DELETE BUTTON RELATIVE TO THE ICON
        deleteB.setTranslateY(-5);
        // FadeTransition deleteBfade=new FadeTransition(Duration.millis(1500), deleteB);
        //deleteBfade.setFromValue(0);    // RED EXIT THE BUTTON WILL FADE AWAY //
        //deleteBfade.setToValue(1);

        //***********************************************************************************//
        //***************************DELETE BUTTON ON CLICK ACTION***************************//
        //***********************************************************************************//

        KeyValue initdeleteBKV=new KeyValue(deleteB.opacityProperty(), 1);
        KeyValue enddeleteBKV=new KeyValue(deleteB.opacityProperty(), 0);
        Timeline deleteBTL=new Timeline(new KeyFrame(Duration.ZERO,initdeleteBKV), new KeyFrame(Duration.millis(1500),enddeleteBKV));
        deleteB.setOnMouseClicked(event->{
                    int index=sideBar.getChildren().indexOf(frame);
                    int lineDel=(sideBar.getChildren().indexOf(frame)/3);

                    sideBar.getChildren().remove(index,index+3);  //THIS REMOVES THE SHORTCUT BUT
                    if(preF.exists()){
                        preF.setWritable(true);
                        try {
                            BufferedReader pre=new BufferedReader(new FileReader(preF));
                            Object[] allLines=(pre.lines()).toArray();
                            pre.close();
                            BufferedWriter preW=new BufferedWriter(new FileWriter(preF));
                            int i=0;
                            for(Object line: allLines){
                                if(i!=lineDel)
                                {
                                    preW.write(Own.util.$.Str(line));
                                    preW.newLine();
                                }
                                i++;
                            }
                            preW.close();
                        } catch (Exception e) {popUpTxt.setText("Err @ editing [Pre]"); popUp.show();}

                    }
                }
        );

        //*********************************************************************************//
        //******************HOVER ACTION WHEN A CURSOR HOVERS ON THE ICONS*****************//
        //*********************************************************************************//
        clipB.setOnMouseEntered(e->  //*****CLIP BUTTON HOVER ON ACTION****//
        {
            clipB.setScaleX(1.1);
            clipB.setScaleY(1.1);
        });


        exitB.setOnMouseEntered(e->    //*******EXIT BUTTON HOVER ON ACTION******//
        {
            exitB.setScaleX(1.1);
            exitB.setScaleY(1.1);
        });



        frame.setOnMouseEntered(e->{
            deleteB.setFill(new ImagePattern(iconDelete));
            frame.setScaleX(1.1);
            frame.setScaleY(1.1);
            deleteBTL.setRate(-1);
            deleteBTL.play();
        });

        //***********************************************************************************//
        //******************HOVER ACTION WHEN A CURSOR HOVERS OFF THE ICONS******************//
        //***********************************************************************************//
        clipB.setOnMouseExited(e-> //***CLIP BUTTON HOVER OFF ACTION****//
        {
            clipB.setScaleX(1);
            clipB.setScaleY(1);

        });


        exitB.setOnMouseExited(e->    //*******EXIT BUTTON HOVER OFF ACTION******//
        {
            exitB.setScaleX(1);
            exitB.setScaleY(1);
        });

        frame.setOnMouseExited(e->{
                    frame.setScaleX(1);
                    frame.setScaleY(1);
                    deleteBTL.setRate(1);
                    deleteBTL.play();
                }
        );







        //***************************************************************************************//
        //***********************************TEXT AND FONT***************************************//
        //***************************************************************************************//
        Text mainTitle=new Text(title);
        mainTitle.setFill(Color.rgb(255,255,255));
        mainTitle.setFont(Font.font(11)); //**TITLE FONT SIZE**//
        Tooltip titleTooltip=new Tooltip(location);
        titleTooltip.setFont(Font.font(10));
        Tooltip.install(mainTitle,titleTooltip);

        /////////////////////
        //****************************************************************************//
        //********************POSITION OF THE ICONS WHEN DRAGGED**********************//
        //****************************************************************************//

        frame.setOnDragDetected(e->{
            root.getChildren().addAll(dropWhat,dropWhatTxt);
            frame.setFill(null);
            mainTitle.setFill(null);
            VBox.setMargin(mainTitle, new Insets(0,0,-30,0));
            deleteB.setFill(null);
            deleteB.setStroke(null);
        });
        //////////////////////
        frame.setOnMouseDragged(e->{
            ImagePattern dropWhatIP=new ImagePattern(icon);
            dropWhatTxt.setText(title);
            dropWhat.setFill(dropWhatIP);
            dropWhatTxtTemp.setText(title);
            if(e.getSceneY()>45) {
                dropWhatTxt.setY(e.getSceneY()+32);
                dropWhat.setY(e.getSceneY()-13);
            }
            else dropWhat.setY(32);
            ///////////////
            int draggdedIndex=sideBar.getChildren().indexOf(frame);
            double pointY=32-sideBar.getTranslateY()+e.getSceneY();
            sideBar.getChildren().removeAll(dropWhatTemp,dropWhatTxtTemp);
            int draggdedtoIndex=((int)((pointY-10)/(sideBar.getHeight()/(sideBar.getChildren().size()/3))))*3;
            int maxindex=sideBar.getChildren().size();
            if(draggdedIndex<draggdedtoIndex) draggdedtoIndex+=3;
            if(draggdedtoIndex>maxindex) draggdedtoIndex=maxindex;
            sideBar.getChildren().add(draggdedtoIndex, dropWhatTemp);
            sideBar.getChildren().add(draggdedtoIndex+1, dropWhatTxtTemp);
            if(sideBar.getHeight()>718){
                if(e.getSceneY()<30 & sideBar.getTranslateY()<32){
                    scrollUp();
                }
                if(e.getSceneY()>730 & sideBar.getTranslateY()>750-sideBar.getHeight()){
                    scrollDown();
                }
            }
            ////////////

        });
        frame.setOnMouseReleased(e->{
            root.getChildren().removeAll(dropWhat,dropWhatTxt,dropWhatTemp,dropWhatTxtTemp);
            sideBar.getChildren().removeAll(dropWhatTemp,dropWhatTxtTemp);
            double pointY=32-sideBar.getTranslateY()+e.getSceneY();
            int draggdedIndex=sideBar.getChildren().indexOf(frame);
            int draggdedtoIndex=((int)((pointY-10)/(sideBar.getHeight()/(sideBar.getChildren().size()/3))))*3;
            int maxindex=sideBar.getChildren().size()-3;
            if(draggdedtoIndex>maxindex) draggdedtoIndex=maxindex;
            sideBar.getChildren().remove(draggdedIndex, draggdedIndex+3);
            frame.setFill(whatInZframe);
            mainTitle.setFill(Color.ROYALBLUE);
            VBox.setMargin(mainTitle, new Insets(0,0,0,0));//***POSITION OF THE MAIN TITLE****//
            deleteB.setFill(Color.TRANSPARENT);
            deleteB.setStroke(Color.TRANSPARENT);
            sideBar.getChildren().add(draggdedtoIndex, frame);
            sideBar.getChildren().add(draggdedtoIndex+1, deleteB);
            sideBar.getChildren().add(draggdedtoIndex+2, mainTitle);
            if(preF.exists()){
                int from=draggdedIndex/3;
                int to=draggdedtoIndex/3;
                if(from!=to){
                    int f=0,t=0;
                    try {
                        BufferedReader pre=new BufferedReader(new FileReader(preF));
                        Object[] allLines=pre.lines().toArray();
                        pre.close();
                        String[] newallLines=new String[allLines.length];
                        if(from>to) {
                            while(t<allLines.length){
                                if(f==from) f++;
                                if(t!=to) {
                                    newallLines[t]= Own.util.$.Str(allLines[f]);
                                    f++; t++;
                                }
                                else {
                                    newallLines[to]= Own.util.$.Str(allLines[from]);
                                    t++;
                                }
                            }
                        }
                        else if(from<to){
                            while(f<allLines.length){
                                if(t==to) t++;
                                if(f!=from) {
                                    newallLines[t]= Own.util.$.Str(allLines[f]);
                                    f++; t++;
                                }
                                else {
                                    newallLines[to]= Own.util.$.Str(allLines[from]);
                                    f++;
                                }
                            }
                        }
                        preF.setWritable(true);
                        BufferedWriter preW=new BufferedWriter(new FileWriter(preF));
                        for(String line: newallLines){
                            preW.write(line);
                            preW.newLine();
                        }
                        preW.close();
                    } catch (Exception err) {
                        popUpTxt.setText("Err @ editing for Drag [Pre]"); popUp.show();}
                }
            }
        });


        //****************************************************************************************//
        //                        FILE, DIRECTORIES AND FILE PATHS                                //
        //****************************************************************************************//

        //*****SIDE BAR ELEMENTS******//
        sideBar.getChildren().addAll(frame,deleteB,mainTitle);
    }

    void editPre(String toBappended){
        preD.mkdir();  //****CREATES A DIRECTORY WHICH IS NAMED BY THIS MKDIR METHOD****//
        preF.setWritable(true);//***ALLOWING TO WRITE ON THE FILE***//
        try

        {
            preF.createNewFile();
            BufferedReader pre=new BufferedReader(new FileReader(preF));
            Object[] allLines=pre.lines().toArray();
            pre.close();
            BufferedWriter preW=new BufferedWriter(new FileWriter(preF));
            for(Object line: allLines)
            {
                preW.write(Own.util.$.Str(line));
                preW.newLine();
            }

            preW.write(toBappended);
            preW.close();
        } catch (Exception err) {popUpTxt.setText("Err @ editing [Pre]"); popUp.show();}
        preF.setReadOnly();
    }

















       //-------------------------------------------------------------------//
      //__________________________________________________________________-//
     //---------------------------ClipBoard-------------------------------//
    //____________________________________________________________________//
      protected static Stage mainStage;
    Clipboard sysCB=Clipboard.getSystemClipboard();
    static String currentCB;
    String lastCB;

    //-------------------------------------------------------------//






       //***********************************************************************************************//
      //*************************************FUCKING START METHOD**************************************//
     //***********************************************************************************************//

    @Override
    public void start(Stage S) throws Exception{

        //******************************************************************************//
        //****************************** ICONS   ***************************************//
        //******************************************************************************//
        
        Image icoExit=new Image(new FileInputStream("sideImg/exitIco.png"));
        Image iconDelete=new Image(new FileInputStream("sideImg/deleteIco.png"));
        Image iconClip= new Image(new FileInputStream("sideImg/clipIco.png"));   //***IMAGE FOR THE CLIPBOARD BUTTON****//
        Image deleteIco= new Image(new FileInputStream("sideImg/deleteIco.png"));
        Image htmlIco=new Image(new FileInputStream("sideImg/htmlIco.png"));
        Image cssIco=new Image(new FileInputStream("sideImg/cssIco.png"));
        Image jsIco=new Image(new FileInputStream("sideImg/jsIco.png"));
        Image pdfIco=new Image(new FileInputStream("sideImg/pdfIco.png"));
        Image sideLogo=new Image(new FileInputStream("sideImg/sideLogo.png"));
        Image folderIco=new Image(new FileInputStream("sideImg/folderIco.png"));
        Image bookIco=new Image(new FileInputStream("sideImg/bookIco.png"));
        Image rawTxtIco=new Image(new FileInputStream("sideImg/rawTxtIco.png"));
        Image exeIco=new Image(new FileInputStream("sideImg/exeIco.png"));
        Image vidIco=new Image(new FileInputStream("sideImg/vidIco.png"));
        Image txtIco=new Image(new FileInputStream("sideImg/txtIco.png"));
        Image codeIco=new Image(new FileInputStream("sideImg/codeIco.png"));
        Image sideIco=new Image(new FileInputStream("sideImg/sideIco.png"));
        Image musicIco=new Image(new FileInputStream("sideImg/musicIco.png"));
        Image zipIco=new Image(new FileInputStream("sideImg/zipIco.png"));
        Image imgIco=new Image(new FileInputStream("sideImg/imgIco.png"));



        preD.mkdir();
        preF.createNewFile();
        preF.setReadOnly();
        BufferedReader pre=new BufferedReader(new FileReader(preF));
        Object[] allLines=(pre.lines()).toArray();
        pre.close();
        int i=1;
        for(Object line: allLines){
            Image icon=folderIco;
            String[] whatInZline= Own.util.$.Str(line).split("\\|");
            if((new File(whatInZline[2])).exists()){
                if(whatInZline.length==3){
                    i++;
                    switch(whatInZline[0])
                    {
                        case "self": icon=new Image(new FileInputStream(whatInZline[2])); break; // IMAGE ICON SHOWS THE IMAGE ITSELF AS AN ICON
                        //case "bookIco": icon=bookIco; break;
                        case "rawTxtIco": icon=rawTxtIco; break;
                        case "vidIco": icon=vidIco; break;
                        case "exeIco": icon=exeIco; break;
                        case "codeIco": icon=codeIco; break;
                        case "txtIco": icon=txtIco; break;
                        case "musicIco": icon=musicIco; break;
                        case "zipIco": icon=zipIco; break;
                        case "imgIco": icon=imgIco; break;
                        case "default": icon=exeIco; break;
                    }
                    mkIcon(icon, whatInZline[1], whatInZline[2]);
                } else {
                    popUpTxt.setText("Pre has been courapted [line "+i+"]");
                    popUp.show();
                }
            }
        }
        //*******************************************************************************************//
        //*************************************EXIT BUTTON EVENT*************************************//
        //*******************************************************************************************//
        exitB.setOnMouseClicked(e->{
            if(e.getClickCount()==1)//THE TIMES OF CLICKING TO MAKE THE BUTTON WORK
            {
                System.out.println("closed");
                if(Own.util.$.Str(e.getButton())=="PRIMARY") S.close();
                if(Own.util.$.Str(e.getButton())=="SECONDARY")
                {

                    sideBar.getChildren().remove(0,sideBar.getChildren().size());
                    preF.delete();
                }
            }
        });


        //*****************************************************************************//
        //****************************CLIPBUTTON EVENT********************************//
        //*****************************************************************************//

        clipB.setOnMouseClicked(e->
        {

            mainStage.show();


        });



        //
        scene.setOnScroll(e->
                {
                    if(sideBar.getHeight()>718 & (sideBar.getTranslateY()<32 | e.getDeltaY()<0) & (sideBar.getTranslateY()>750-sideBar.getHeight() | e.getDeltaY()>0)){
                        sideBar.setTranslateY(sideBar.getTranslateY()+e.getDeltaY());
                        if(sideBar.getTranslateY()>32) sideBar.setTranslateY(32);
                        else if(sideBar.getTranslateY()<750-sideBar.getHeight()) sideBar.setTranslateY(750-sideBar.getHeight());
                    }
                }
        );
        scene.setOnKeyPressed(e-> {
            if(sideBar.getHeight()>718){
                if(Own.util.$.Str(e.getCode())=="DOWN" & sideBar.getTranslateY()>730-sideBar.getHeight()){
                    scrollDown();
                }
                else if(Own.util.$.Str(e.getCode())=="UP"  & sideBar.getTranslateY()<32) {
                    scrollUp();
                }
//            else if($.Str(e.getCode())=="ENTER") {
//                $.println(Clipboard.getSystemClipboard().getString());
//            }
            }
        });


        //******************************************************************//
        //******* **  HOVERING ON THE INVISIBLE MAIN SIDE WINDOW  ** *******//
        //******************************************************************//

        scene.setOnMouseEntered(e->{

            bottombox.setMinSize(80,100);
            bottombox.setStyle("-fx-background-color: rgba(57, 62, 70)");
            scene.setFill(linearG);
            fade.setRate(1);
            fade.play();
            exitB.setFill(new ImagePattern(icoExit));
            clipB.setFill(new ImagePattern(iconClip));


        });


        //**************************************************************//
        //******* **  HOVERING OFF FROM THE MAIN SIDE WINDOW  ** *******//
        //**************************************************************//
        scene.setOnMouseExited(e->{
            bottombox.setStyle("-fx-background-color: transparent");
            bottombox.setMinSize(80,100);
            fade.setRate(-1);
            fade.play();
            sideBarFadeTL.play();
            exitB.setFill(Color.TRANSPARENT);
            clipB.setFill(Color.TRANSPARENT);


        });

        //*************************************************************************************************//
        //**********************DRAGGING AND DROPPING A FILE INTO THE SIDE WINDOW**************************//
        //*************************************************************************************************//
        EventHandler<DragEvent> handleDragOver=new EventHandler<DragEvent>()
        {
            public void handle(DragEvent e){
                Clipboard guest=e.getDragboard();
                if(guest.hasFiles()) e.acceptTransferModes(TransferMode.LINK);//**TRANSFERS THE FILE PATH OR LINK IT DOES NO MOVE OR COPY THE FILES**//
                else e.acceptTransferModes(TransferMode.ANY);
            }
        };
        EventHandler<DragEvent> handleDragDropped=new EventHandler<DragEvent>(){
            public void handle(DragEvent e)
            {
                Clipboard guest=e.getDragboard();
                String title="";
                String toBeStore="";
                if(guest.hasFiles()){
                    List<File> files=guest.getFiles();
                    for(File file: files){
                        Image icon=folderIco;
                        String iconCode="folderIco";
                        title=file.getName();   //**GETS THE NAME OF THE FILE**//

                        if(title.equals("")){
                            title="ROOT("+(file.toString()).charAt(0)+")";//TAKES THE NAME OF THE FOLDER
                        }
                        else if(!file.isDirectory())
                        {


                            String extension=title.substring(title.lastIndexOf(".")+1);//**DETERMINING THE FILE TYPE USING MULTIPLE INTERNET MAIL EXTENSION eg(.pdf or.txt)**//
                            switch(extension.toLowerCase())//**USING SWITCH CASE STATEMENT TO SET THEN ICONS FOR THE FILE**//
                            {
                                //case "epub":
//                                case "pdf": icon=pdfIco; iconCode="pdfIco"; break;
//                                case "txt": icon=txtIco; iconCode="txtIco"; break;
//                                case "docx": icon=txtIco; iconCode="txtIco"; break;
//                                case "html": icon=htmlIco; iconCode="htmlIco"; break;
//                                case "css": icon=cssIco; iconCode="cssIco"; break;
//                                case "js": icon=jsIco;iconCode="jsIco"; break;
                                //case "php":icon=codeIco;iconCode="codeIco";break;
                                //case "cpp": icon=codeIco; iconCode="cppIco"; break;
                                case "exe": icon=exeIco; iconCode="exeIco"; break;
                                case "mp3": icon=musicIco; iconCode="musicIco"; break;
                                case "mkv":
                                case "mp4": icon=vidIco; iconCode="vidIco"; break;
//                                case "png":
//                                case "jpg":
//                                case "jpeg":
//                                case "gif":
//                                case "eps":

//                           try  //****TRY CATCH STATEMENT IF AN ERROR OCCURS****//
//                                {
//                                    icon=new Image(new FileInputStream(file));
//                                    iconCode="self";
//                                }
//                                catch(Exception err){popUpTxt.setText("Err @ Seting icon to self"); popUp.show();
//                           }
//                                break;
                                case "zip": icon=zipIco; iconCode="zipIco"; break;
                                case "ico": icon=imgIco; iconCode="imgIco"; break;
                                default: icon=codeIco; iconCode="codeIco";
                            }
                        }
                        if(title.length()>=7) title=title.substring(0, 7)+"..";
                        try {
                            mkIcon(icon,title,file.toString());
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }
                        toBeStore+=iconCode + "|" + title + "|" + file.toString()+"\r\n";
                    }
                }
                else if(guest.hasImage()){
                    popUpTxt.setText("-->IMAGE FOUND<--"); popUp.show();
                }
//                else if(guest.hasHtml() | guest.hasString() | guest.hasUrl()){
//                    String dragedTxt=guest.getString();
//                    $.println(dragedTxt);
//                }
                editPre(toBeStore);
            }
        };
        popUpExitB.setOnMouseClicked(e->{
            if(Own.util.$.Str(e.getButton())=="PRIMARY" & e.getClickCount()==2){
                popUp.close();
            }
        });

        //*******************************************************************************************//
        //************************PHYSICAL PROPERTIES OF ALL THE THINGS******************************//
        //*******************************************************************************************//
        line.setOpacity(0.1);

        //*****DRAGGING PROPERTY*****//



//        bottombox.setMargin(clipB, new Insets(screenH-100,0,13,30)); //****MARGIN FOR THE CLIP BUTTON****//
//        bottombox.setMargin(exitB, new Insets(0,0,13,30)); //****MARGIN FOR THE EXIT BUTTON****//
//



        VBox.setMargin(clipB,new Insets(7,0,3,35));
        VBox.setMargin(exitB,new Insets(3,0,0,35));

        bottombox.setTranslateX(0);
        bottombox.setTranslateY(screenH-56);  //
        bottombox.setStyle("-fx-background-color: transparent");
        exitB.setFill(Color.TRANSPARENT);
        clipB.setFill(Color.TRANSPARENT);
        dropWhatTemp.setFill(Color.GRAY);
        dropWhatTemp.setOpacity(0.4);
        dropWhatTemp.setArcHeight(5);
        dropWhatTemp.setArcWidth(5);
        dropWhatTemp.setTranslateX(5);
        dropWhatTxtTemp.setFill(Color.WHITE);
        dropWhatTxtTemp.setOpacity(0.4);
        dropWhatTxtTemp.setFont(Font.font(9));
        dropWhatTxt.setFill(Color.WHITE);
        dropWhatTxt.setFont(Font.font(9));
        dropWhatTxt.setX(6);
        //******SIDE UI PROPERTY*******//
        //sideBar.setEffect(blur);
        sideBar.setTranslateY(32);
        sideBar.setPrefWidth(viewW);//****WIDTH OF THE SIDE UI****//
        sideBar.setMaxHeight(50);
        sideBar.setFillWidth(true);
        sideBar.setSpacing(10);
        sideBar.setAlignment(Pos.CENTER);//**********ALIGNMENT TO THE CENTER********//
        sideBar.setOpacity(0); //*********OPACITY OF THE TOP SIDE BAR****************//
        popUp.initStyle(StageStyle.TRANSPARENT);
        exitB.setCursor(Cursor.HAND);
        clipB.setCursor(Cursor.HAND);
        //clipB.setCursor(Cursor.HAND);
        popUpTxt.setFill(Color.DARKRED);
        popUpExitB.setFill(Color.GRAY);
        popUpExitB.setFont(Font.font(15));
        popUpExitB.setCursor(Cursor.HAND);

        //*********FADING PROPERTY*********//
        fade.setNode(sideBar);
        fade.setDuration(Duration.millis(300));
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setInterpolator(Interpolator.EASE_IN);
        //popUpExitB.setOnMouseEntered(e->popUpExitB.setFill(Color.DARKRED));
        //popUpExitB.setOnMouseExited(e->popUpExitB.setFill(Color.GRAY));
        //popUpExitB.setTranslateY(10);
        //popUpExitB.setTranslateX(4);
        //popUpTxt.setTranslateY(12);
        // popUpHbox.getChildren().addAll(popUpExitB,popUpTxt);
        //popUpHbox.setSpacing(6);
        //popUpScene.setFill(Color.BLACK);
        //popUp.setScene(popUpScene);
        //popUp.setWidth(screenW*0.2);
        //popUp.setHeight(screenH*0.05); //********HEIGHT OF THE SIDE WINDOW*********//
        //popUp.setX(screenW-screenW*0.2-10);
        //popUp.setY(screenH-screenH*0.1-20);
        //popUp.setAlwaysOnTop(true);
        //popUp.getIcons().add(sideLogo);
        scene.setFill(Color.TRANSPARENT);
        clipB.setFill(Color.TRANSPARENT);
        scene.setOnDragOver(handleDragOver);
        scene.setOnDragDropped(handleDragDropped);

        //*******************************************************************//
        //***********************MAIN STAGE PROPERTIES***********************//
        //*******************************************************************//
        S.setScene(scene);

        S.setAlwaysOnTop(true);
        S.setX(screenW-viewW);
        S.setY(0);
        S.setHeight(screenH);
        S.setWidth(w);
        S.initStyle(StageStyle.TRANSPARENT);
        S.setTitle("Side");
        S.getIcons().add(sideLogo);
       S.show();








          //===========================================================================================//
         //============================ = CLIPBOARD MAIN STAGE = =====================================//
        //===========================================================================================//
        Stage primaryStage = new Stage();
//        lastCB=sysCB.getString();

        mainStage = primaryStage;

        // -------------------------------------------------------------------------
        // get the mesuremnts of the screen and the availble screen length and height
        Screen screen = Screen.getPrimary();
        Rectangle2D visbleScreen = screen.getVisualBounds();


        //set the margin of the main clip window
         int extraSpaceX = 90;
         int extraSpaceY = 50;
        //set the width and height
        int primaryStageWidth = 600;
        int primaryStageHeight =400;

        //positioning it at the screen
        int windowXpos = (int) (visbleScreen.getMaxX() - primaryStageWidth - extraSpaceX);
        int windowYpos = (int) (visbleScreen.getMaxY() - primaryStageHeight - extraSpaceY);

        mainStage.setX(windowXpos);
        mainStage.setY(windowYpos);

        Parent root = FXMLLoader.load(getClass().getResource("UI_Screens/clip.fxml"));
        Scene mainScene = new Scene(root, primaryStageWidth, primaryStageHeight);
        mainScene.getStylesheets().add("com/side/styles/mainSceneStyle.css");
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        //wont show when the app startes at first
        //primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }


//    @Override
    public void init() throws Exception {
        try{
            //calling the load method to start in the begginig
            UserData.getInstance().loadNotes();
            System.out.println(UserData.getInstance().getUserNoteList().size());
        }catch(IOException e){
            System.out.println(e);
        }
    }




    @Override
    public void stop() throws Exception {
        try{
            //calling the load method to stop in the begginig
            UserData.getInstance().storeData();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
