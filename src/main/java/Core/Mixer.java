package Core;

import java.util.ArrayList;

public class Mixer implements AudioComponent{

    private ArrayList<AudioComponent> inputs;

    public Mixer() {
        this.inputs = new ArrayList<>();
    }

    @Override
    public AudioClip getClip() {
        // Initialize an empty AudioClip
        AudioClip sample = new AudioClip();

        // Mix audio from all input components
        for (AudioComponent input : inputs) {

            VolumeAdjuster lowerVolume = new VolumeAdjuster(.25);
            lowerVolume.connectInput(input);

            AudioClip inputClip = lowerVolume.getClip();


            for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
                sample.setSample(i, (inputClip.getSample(i) + sample.getSample(i)));
            }
        }
        return sample;
    }

    //always return false so that you can add more inputs
    @Override
    public boolean hasInput() {
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {
        inputs.add(input);
    }

    public void disconnectInput(AudioComponent input) {
        inputs.remove(input);
    }
}