package com.example.GUI;

import Core.AudioComponent;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;

public class VolumeWidget extends AudioComponentWidget {
    Slider volSlider;

    public VolumeWidget(AudioComponent ac, AnchorPane parent, SynthesizerApplication parentApplication) {
        super(ac, parent, parentApplication, "Volume");
        //make nodes --------------------------------------------------------------------------------
        TextField volField = new TextField();
        volField.setAlignment(Pos.TOP_CENTER);
        volField.setText("50%");
        volSlider = new Slider(0, 100, 50);
        //-------------------------------------------------------------------------------------------

        //handle actions ----------------------------------------------------------------------------
        volSlider.setOnMouseDragged(e -> handleSlider(e, volSlider, volField, "%"));

        //set children on layouts -------------------------------------------------------------------
        verticalLayout.getChildren().add(volSlider);
        verticalLayout.getChildren().add(volField);
        //-------------------------------------------------------------------------------------------
    }

    @Override
    public void handleSlider(javafx.scene.input.MouseEvent e, Slider freqSlider, TextField freqField, String unit) {
        super.handleSlider(e, freqSlider, freqField, unit);
    }

    public double getSliderNum(){
        return this.volSlider.getValue()/(20* SynthesizerApplication.widgets.size());
    }
}