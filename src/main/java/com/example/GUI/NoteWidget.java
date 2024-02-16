package com.example.GUI;

import Core.AudioComponent;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import java.util.Map;

//this does NOT extend acw because it doesn't have a frequency slider
public class NoteWidget extends Pane {
    //define member variables---------------------------------------------------------------------
    public AudioComponent ac_;
    AnchorPane parent_;
    VBox verticalLayout;
    HBox baseLayout;
    SynthesizerApplication parentApplication;
    protected double mouseX;
    protected double mouseY;
    Line line;
    //--------------------------------------------------------------------------------------------

    public NoteWidget(AudioComponent ac, AnchorPane parent, SynthesizerApplication parentApplication, String myWidgetLabel){
        //define variables -----------------------------------------------------------------------
        ac_ = ac;
        parent_ = parent;
        this.parentApplication = parentApplication;
        //----------------------------------------------------------------------------------------

        //set the layout --------------------------------------------------------------------------
        baseLayout = new HBox();
        baseLayout.setPrefWidth(80);
        baseLayout.setPrefHeight(60);
        verticalLayout = new VBox();
        verticalLayout.setAlignment(Pos.CENTER);
        //baseLayout.setStyle("-fx-border-color: black; -fx-background-color: lightgrey;");
        VBox rightSide = new VBox();
        rightSide.setSpacing(5);
        rightSide.setAlignment(Pos.CENTER_RIGHT);
        //----------------------------------------------------------------------------------------

        //color hash map --------------------------------------------------------------------------
        Map<String, String> noteColors = Map.of(
                "A Note", "#ff7069",
                "B Note", "#ffd36b",
                "C Note", "#faec70",
                "D Note", "#a9ff9c",
                "E Note", "#9cf0ff",
                "F Note", "#d59eff",
                "G Note", "#fcb3fb"
        );

        String color = noteColors.getOrDefault(myWidgetLabel, "white");
        this.setStyle("-fx-background-color: " + color + "; -fx-border-color: black;");

        //----------------------------------------------------------------------------------------

        //create label, output, and exit ----------------------------------------------------------
        Label noteLabel = new Label(myWidgetLabel);
        noteLabel.setAlignment(Pos.TOP_CENTER);
        noteLabel.setTextAlignment(TextAlignment.CENTER);
        Button closeButton = new Button("X");
        Circle output = new Circle(10);
        //----------------------------------------------------------------------------------------

        //handle actions ------------------------------------------------------------------
        verticalLayout.setOnMousePressed(e -> getMousePos(e));
        verticalLayout.setOnMouseDragged(e-> handleMove(e));
        closeButton.setOnAction(e -> closeBox());
        //----------------------------------------------------------------------------------------

        //handle drawing the line -------------------------------------------------------------------
        output.setOnMousePressed(e-> startConnection(e, output));
        output.setOnMouseDragged(e-> moveConnection(e, output));
        output.setOnMouseReleased(e-> endConnection(e, output));
        //-------------------------------------------------------------------------------------------

        //set children on layouts ----------------------------------------------------------------
        rightSide.getChildren().add(closeButton);
        rightSide.getChildren().add(output);
        baseLayout.getChildren().add(rightSide);
        verticalLayout.getChildren().add(noteLabel);
        baseLayout.getChildren().add(verticalLayout);
        this.getChildren().add(baseLayout);
        //----------------------------------------------------------------------------------------

        //set where the widget will spawn -----------------------------------------------------------
        this.setLayoutX(650 - (SynthesizerApplication.notes.size()*10));
        this.setLayoutY(50 + (SynthesizerApplication.notes.size()*10));
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

    private void closeBox() {
        parent_.getChildren().remove(this);
        SynthesizerApplication.connectedNotes.remove(this);
        SynthesizerApplication.notes.remove(this);
        if(line != null) {
            parent_.getChildren().remove(line);
        }
    }

    private void endConnection(MouseEvent e, Circle output) {
        Circle speaker = SynthesizerApplication.speaker;
        Bounds speakerBounds = speaker.localToScene(speaker.getBoundsInLocal());

        double distance = Math.sqrt(Math.pow(speakerBounds.getCenterX() - e.getSceneX(), 2.0)
                + Math.pow(speakerBounds.getCenterY() - e.getSceneY(), 2.0));

        if (distance < 10) {
            SynthesizerApplication.connectedNotes.add(this);
            System.out.println("connected");
        } else {
            parent_.getChildren().remove(line);
            line = null;
        }
    }

    private void moveConnection(MouseEvent e, Circle output) {
        Bounds parentBounds = parent_.getBoundsInParent();
        line.setEndX(e.getSceneX() - parentBounds.getMinX());
        line.setEndY(e.getSceneY() - parentBounds.getMinY());
    }

    private void startConnection(MouseEvent e, Circle output) {
        if (line != null){
            parent_.getChildren().remove(line);
        }
        Bounds parentBounds = parent_.getBoundsInParent();
        Bounds widgetBounds = output.localToScene(output.getBoundsInLocal());

        line = new Line();
        line.setStrokeWidth(5);

        line.setStartX(widgetBounds.getCenterX() - parentBounds.getMinX());
        line.setStartY(widgetBounds.getCenterY() - parentBounds.getMinY());;

        line.setEndX(e.getSceneX());
        line.setEndY(e.getSceneY());

        parent_.getChildren().add(line);
    }
}
