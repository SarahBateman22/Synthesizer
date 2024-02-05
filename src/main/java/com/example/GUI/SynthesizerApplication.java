package com.example.GUI;
import Core.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;

public class SynthesizerApplication extends Application {

    //set member variables ------------------------------------------------------------------
    AnchorPane mainCenter;
    HBox bottomPanel;
    double frequency_ = 250;
    public static Circle speaker;
    public static ArrayList<AudioComponentWidget> widgets = new ArrayList<>();
    public static ArrayList<AudioComponentWidget> connectedWidgets = new ArrayList<>();
    private VolumeWidget vw;
    //---------------------------------------------------------------------------------------

    @Override
    public void start(Stage stage) throws IOException {
        //set the main layout and the panels in it -------------------------------------------
        BorderPane mainLayout = new BorderPane();
        mainCenter = new AnchorPane();
        mainCenter.setStyle("-fx-background-color: lavender");
        //right panel
        VBox rightpanel = new VBox();
        rightpanel.setStyle("-fx-background-color: lightgrey");
        rightpanel.setPadding(new Insets(4));
        //bottom Panel
        bottomPanel = new HBox();
        bottomPanel.setStyle("-fx-background-color: lightgrey");
        bottomPanel.setAlignment(Pos.CENTER);
        //-------------------------------------------------------------------------------------

        //create nodes-------------------------------------------------------------------------
        Button sinewaveBtn = new Button("SineWave");
        speaker = new Circle(400, 200, 15);
        speaker.setFill(Color.LIGHTBLUE );
        Label speakerLabel = new Label("Mixer");
        speakerLabel.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(speakerLabel, 160.0);
        AnchorPane.setLeftAnchor(speakerLabel, 385.0);
        Button playBtn = new Button("Play");
        //-------------------------------------------------------------------q------------------

        createVolumeAdj();
        //set nodes on layout------------------------------------------------------------------
        rightpanel.getChildren().add(sinewaveBtn);
        rightpanel.setAlignment(Pos.CENTER);
        mainCenter.getChildren().addAll(speaker, speakerLabel);
        mainLayout.setCenter(mainCenter);
        mainLayout.setRight(rightpanel);
        bottomPanel.getChildren().add(playBtn);
        mainLayout.setBottom(bottomPanel);
        //-------------------------------------------------------------------------------------

        //set actions--------------------------------------------------------------------------
        sinewaveBtn.setOnAction(e -> createSineWave(e));
        playBtn.setOnAction(e -> {
            try {
                playAudio(e);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        });
        //-------------------------------------------------------------------------------------

        //set scene and show-------------------------------------------------------------------
        Scene scene = new Scene(mainLayout, 600, 400);
        stage.setTitle("Synthesizer");
        stage.setScene(scene);
        stage.show();
        //-------------------------------------------------------------------------------------
    }

    private void playAudio(ActionEvent e) throws LineUnavailableException {
        System.out.println("About to play...");
        Clip c = AudioSystem.getClip();
        AudioFormat format16 = new AudioFormat(44100, 16, 1, true, false);
        Mixer mixer = new Mixer();
        for(AudioComponentWidget w: connectedWidgets){
            AudioComponent ac = w.ac_;
            mixer.connectInput(ac);
        }
        double volScale = vw.getSliderNum();
        VolumeAdjuster volAdj = new VolumeAdjuster(volScale);
        volAdj.connectInput(mixer);
        AudioClip clip = volAdj.getClip();
        try {
            c.open(format16, clip.getData(), 0, clip.getData().length);
        } catch (LineUnavailableException ex) {
            throw new RuntimeException(ex);
        }
        c.start();
        c.loop(0);
        while (c.getFramePosition() < AudioClip.TOTAL_SAMPLES || c.isActive() || c.isRunning()) {
        }
        c.setFramePosition(0);
        System.out.println("Done.");
    }

    private void createSineWave(ActionEvent e) {
        AudioComponent sineWave = new SineWave(frequency_);
        AudioComponentWidget acw = new SineWaveWidget(sineWave, mainCenter, this);
        mainCenter.getChildren().add(acw);
        widgets.add(acw);
    }

    private void createVolumeAdj() {
        VolumeAdjuster volAdj = new VolumeAdjuster(.5);
       vw = new VolumeWidget(volAdj, mainCenter, this);
       bottomPanel.getChildren().add(vw);
       bottomPanel.setSpacing(100);
       widgets.add(vw);
    }

    public static void main (String[]args){
        launch();
    }
}