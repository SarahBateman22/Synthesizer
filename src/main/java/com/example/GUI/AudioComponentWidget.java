package com.example.GUI;

import Core.AudioComponent;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

public class AudioComponentWidget extends Pane {
    //define member variables---------------------------------------------------------------------
    public AudioComponent ac_;
    static AnchorPane parent_;
    VBox verticalLayout;
    HBox baseLayout;
    SynthesizerApplication parentApplication;
    protected double mouseX;
    protected double mouseY;
    static Line line;
    //--------------------------------------------------------------------------------------------

    public AudioComponentWidget(AudioComponent ac, AnchorPane parent, SynthesizerApplication parentApplication, String myWidgetLabel){
        //define variables -----------------------------------------------------------------------
        ac_ = ac;
        parent_ = parent;
        this.parentApplication = parentApplication;
        //----------------------------------------------------------------------------------------

        //Set the layout --------------------------------------------------------------------------
        baseLayout = new HBox();
        baseLayout.setPrefWidth(175);
        baseLayout.setPrefHeight(60);
        verticalLayout = new VBox();
        verticalLayout.setAlignment(Pos.CENTER);
        baseLayout.setStyle("-fx-border-color: #edeae1; -fx-background-color: #edeae1;");
        VBox rightSide = new VBox();
        rightSide.setSpacing(5);
        //----------------------------------------------------------------------------------------

        //set nodes ------------------------------------------------------------------------------
        Label freqLabel = new Label(myWidgetLabel);
        freqLabel.setAlignment(Pos.TOP_CENTER);
        freqLabel.setTextAlignment(TextAlignment.CENTER);
        //----------------------------------------------------------------------------------------

        //handle actions ------------------------------------------------------------------
        verticalLayout.setOnMousePressed(e -> getMousePos(e));
        verticalLayout.setOnMouseDragged(e-> handleMove(e));
        //----------------------------------------------------------------------------------------

        //set children on layouts ----------------------------------------------------------------
        verticalLayout.getChildren().add(freqLabel);
        baseLayout.getChildren().add(rightSide);
        baseLayout.getChildren().add(verticalLayout);
        this.getChildren().add(baseLayout);
        //----------------------------------------------------------------------------------------
    }

    private void getMousePos(MouseEvent e) {
        mouseX = e.getSceneX();
        mouseY = e.getSceneY();
    }

    private void handleMove(MouseEvent e) {
        double deltaX = e.getSceneX() - mouseX;
        double deltaY = e.getSceneY() - mouseY;
        this.setLayoutX(this.getLayoutX() + deltaX);
        this.setLayoutY(this.getLayoutY() + deltaY);
        mouseX = e.getSceneX();
        mouseY = e.getSceneY();
    }

    public void handleSlider(javafx.scene.input.MouseEvent e, Slider freqSlider, TextField freqField, String unit){
        int result = (int) freqSlider.getValue();
        freqField.setText(result + unit);
    }
}