package Core;

import java.util.ArrayList;

public class Mixer implements AudioComponent{

    private ArrayList<AudioComponent> inputs;

    public Mixer() {
        this.inputs = new ArrayList<>();
    }

    @Override
    public AudioClip getClip() {
        // Initialize an empty AudioClip to store the mixed output
        AudioClip mixedClip = new AudioClip();

        // Check if there are any inputs connected
        if (!inputs.isEmpty()) {
            // Scale factor to prevent blowing out the speaker, could be adjusted based on the inputs count
            double scaleFactor = 0.4 / inputs.size();

            // Mix audio from all input components
            for (AudioComponent input : inputs) {
                AudioClip inputClip = input.getClip();
                for (int i = 0; i < AudioClip.TOTAL_SAMPLES; i++) {
                    // Scale the sample from each input before adding it to the mixed output
                    int scaledSample = (int) (inputClip.getSample(i) * scaleFactor);
                    mixedClip.setSample(i, mixedClip.getSample(i) + scaledSample);
                }
            }
        }

        return mixedClip;
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