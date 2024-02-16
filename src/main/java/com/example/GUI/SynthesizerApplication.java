package com.example.GUI;
import Core.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
import java.util.*;

public class SynthesizerApplication extends Application {

    //set member variables ------------------------------------------------------------------
    AnchorPane mainCenter;
    HBox bottomPanel;
    HBox topPanel;
    double frequency_ = 250;
    public static Circle speaker;
    public static ArrayList<AudioComponentWidget> widgets = new ArrayList<>();
    public static ArrayList<AudioComponentWidget> connectedWidgets = new ArrayList<>();
    public static ArrayList<NoteWidget> notes = new ArrayList<>();
    public static ArrayList<NoteWidget> connectedNotes = new ArrayList<>();
    private VolumeWidget vw;
    //---------------------------------------------------------------------------------------

    @Override
    public void start(Stage stage) throws IOException {
        //set the main layout and the panels in it -------------------------------------------
        BorderPane mainLayout = new BorderPane();
        mainCenter = new AnchorPane();
        mainCenter.setStyle("-fx-background-color: #faf2de");

        bottomPanel = new HBox();
        bottomPanel.setStyle("-fx-background-color: #edeae1");
        bottomPanel.setAlignment(Pos.CENTER);

        topPanel = new HBox();
        topPanel.setStyle("-fx-background-color: #edeae1");
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(0, 0, 10, 0));
        //-------------------------------------------------------------------------------------

        //create nodes-------------------------------------------------------------------------
        Button sinewaveBtn = new Button("Custom Frequency");
        sinewaveBtn.setStyle("-fx-background-color: white");
        speaker = new Circle(400, 200, 15);
        speaker.setFill(Color.BLACK );
        Label speakerLabel = new Label("Mixer");
        speakerLabel.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(speakerLabel, 160.0);
        AnchorPane.setLeftAnchor(speakerLabel, 385.0);
        //play button
        Button playBtn = new Button("PLAY!");
        playBtn.setStyle("-fx-background-color: #60e665");
        playBtn.setPrefWidth(100);
        playBtn.setPrefHeight(40);

        //-------------------------------------------------------------------q------------------

        createVolumeAdj();
        //set nodes on layout------------------------------------------------------------------
        topPanel.getChildren().add(sinewaveBtn);
        mainCenter.getChildren().addAll(speaker, speakerLabel);
        mainLayout.setCenter(mainCenter);
        bottomPanel.getChildren().add(playBtn);
        mainLayout.setBottom(bottomPanel);
        mainLayout.setTop(topPanel);
        //-------------------------------------------------------------------------------------

        //set notes on top panel
        setNotes(mainLayout, topPanel);

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
        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setTitle("Synthesizer");
        stage.setScene(scene);
        stage.show();
        //-------------------------------------------------------------------------------------
    }

    private void setNotes(BorderPane mainLayout, HBox topPanel) {
        Label notesLabel = new Label("Waveform & Notes");

        //defining the color for each note
        Map<String, String> noteColors = Map.of(
                "A", "red",
                "B", "orange",
                "C", "yellow",
                "D", "#38d642", //green
                "E", "#4091f5", //blue
                "F", "#a567f0", //purple
                "G", "violet" //pink
        );
        List<Button> buttons = new ArrayList<>(Arrays.asList(
                new Button("A"),
                new Button("B"),
                new Button("C"),
                new Button("D"),
                new Button("E"),
                new Button("F"),
                new Button("G")
        ));

        //each button should call the createNoteNode method when pressed
        for(Button b : buttons) {
            b.setOnAction(e -> createNoteNode(b, e));
            //getting the color from my hashmap to assign to each button
            String color = noteColors.get(b.getText());
            b.setStyle("-fx-background-color: " + color + ";");
        }

        //spacing them out  evenly
        topPanel.setSpacing(30);

        //add all the buttons to the top panel
        topPanel.getChildren().addAll(buttons);

        //combine the label and the buttons into one vbox container to stack them
        VBox notesContainer = new VBox();
        notesContainer.setStyle("-fx-background-color: #edeae1");
        notesContainer.setAlignment(Pos.CENTER);
        notesContainer.getChildren().addAll(notesLabel, topPanel);
        notesContainer.setSpacing(5);

        //set the notes container to the top of main layout
        mainLayout.setTop(notesContainer);
    }

    private void createNoteNode(Button key, ActionEvent e){
        System.out.println(key.getText() + " key pressed!");
        int freq = getFreq(key.getText());
        AudioComponent aFreq = new SineWave(freq);
        String label = key.getText()+ " Note";
        NoteWidget aWidget = new NoteWidget(aFreq, mainCenter, this, label);
        mainCenter.getChildren().add(aWidget);
        notes.add(aWidget);
    }

    private int getFreq(String key){
        if(key.equals("A")){
            return 220;
        }
        if(key.equals("B")){
            return 247;
        }
        if(key.equals("C")){
            return 262;
        }
        if(key.equals("D")){
            return 294;
        }
        if(key.equals("E")){
            return 330;
        }
        if(key.equals("F")){
            return 349;
        }
        if(key.equals("G")){
            return 392;
        }
        return 0;
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

        for(NoteWidget n: connectedNotes){
            AudioComponent ac = n.ac_;
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