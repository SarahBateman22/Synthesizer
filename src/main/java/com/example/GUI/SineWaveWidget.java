package com.example.GUI;

import Core.AudioComponent;
import Core.SineWave;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class SineWaveWidget extends AudioComponentWidget {

    public SineWaveWidget(AudioComponent ac, AnchorPane parent, SynthesizerApplication parentApplication) {
        super(ac, parent, parentApplication, "Frequency");
        //set the layout ----------------------------------------------------------------------------
        VBox rightSide = new VBox();
        rightSide.setSpacing(5);
        rightSide.setAlignment(Pos.CENTER_RIGHT);
        baseLayout.setStyle("-fx-border-color: black; -fx-background-color: white;");
        //-------------------------------------------------------------------------------------------

        //make nodes ---------------------------------------------------------------------------------
        TextField freqField = new TextField();
        freqField.setAlignment(Pos.TOP_CENTER);
        freqField.setText("250 Hz");
        Slider freqSlider = new Slider(20, 500, 250);
        Button closeButton = new Button("X");
        Circle output = new Circle(10);
        //-------------------------------------------------------------------------------------------

        //handle drawing the line -------------------------------------------------------------------
        output.setOnMousePressed(e-> startConnection(e, output));
        output.setOnMouseDragged(e-> moveConnection(e, output));
        output.setOnMouseReleased(e-> endConnection(e, output));
        //-------------------------------------------------------------------------------------------

        //handle other actions ----------------------------------------------------------------------
        closeButton.setOnAction(e -> closeBox());
        freqSlider.setOnMouseDragged(e-> handleSlider(e, freqSlider, freqField, " Hz"));
        //-------------------------------------------------------------------------------------------

        //set children on layouts -------------------------------------------------------------------
        rightSide.getChildren().add(closeButton);
        rightSide.getChildren().add(output);
        baseLayout.getChildren().add(rightSide);
        verticalLayout.getChildren().add(freqSlider);
        verticalLayout.getChildren().add(freqField);
        //-------------------------------------------------------------------------------------------

        //set where the widget will spawn -----------------------------------------------------------
        this.setLayoutX(50 + (SynthesizerApplication.widgets.size()*10));
        this.setLayoutY(50 + (SynthesizerApplication.widgets.size()*10));

    }

    private void endConnection(MouseEvent e, Circle output) {
        Circle speaker = SynthesizerApplication.speaker;
        Bounds speakerBounds = speaker.localToScene(speaker.getBoundsInLocal());

        double distance = Math.sqrt(Math.pow(speakerBounds.getCenterX() - e.getSceneX(), 2.0)
                + Math.pow(speakerBounds.getCenterY() - e.getSceneY(), 2.0));

        if (distance < 10) {
            SynthesizerApplication.connectedWidgets.add(this);
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


    @Override
    public void handleSlider(javafx.scene.input.MouseEvent e, Slider freqSlider, TextField freqField, String unit){
        super.handleSlider(e, freqSlider, freqField, unit);
        ((SineWave)ac_).updateFrequency(freqSlider.getValue());
    }

    private void closeBox() {
        parent_.getChildren().remove(this);
        SynthesizerApplication.connectedWidgets.remove(this);
        SynthesizerApplication.widgets.remove(this);
        if(line != null) {
            parent_.getChildren().remove(line);
        }
    }
}