package com.side.UI_Controllers;

import javafx.scene.layout.StackPane;

public class FalsePane {
    StackPane falsePane;

    //create a dummy pane to add to the notelist and pane
    public StackPane makePane(){
        falsePane = new StackPane();
        falsePane.setPrefWidth(0);
        falsePane.setPrefHeight(0);
        falsePane.setVisible(false);

        return falsePane;
    }

}
