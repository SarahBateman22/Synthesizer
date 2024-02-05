package Core;
import javax.sound.sampled.*;

public class Main {
    public static void main(String[] args) throws LineUnavailableException {

        // Get properties from the system about samples rates, etc.

        Clip c = AudioSystem.getClip();

        // This is the format that we're following, 44.1 KHz mono audio, 16 bits per sample.
        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );

        AudioComponent gen = new SineWave(220); // Your code
        AudioListener listener = new AudioListener(c);

        // Create instances of audio components
        AudioComponent gen1 = new SineWave(220);
        AudioComponent gen2 = new SineWave(440);
        //AudioComponent gen3 = new SineWave(140);


        // Create a mixer
        Mixer mixer = new Mixer();

        VolumeAdjuster lowerVolume = new VolumeAdjuster(1.25);

        lowerVolume.connectInput(gen);

        // Connect audio components to the mixer
        mixer.connectInput(gen1);
        mixer.connectInput(gen2);

        AudioComponent lowerVol = new VolumeAdjuster(0.5);

        lowerVol.connectInput(mixer);

        AudioClip mixedClip = lowerVol.getClip();

        c.open(format16, mixedClip.getData(), 0, mixedClip.getData().length);

        System.out.println("About to play...");
        c.start(); // Plays it.
        c.loop( 0); // Plays it 2 more times if desired, so 6 seconds total

        // Makes sure the program doesn't quit before the sound plays.
        while (c.getFramePosition() < AudioClip.TOTAL_SAMPLES || c.isActive() || c.isRunning()) {
            // Do nothing while we wait for the note to play.
        }

        System.out.println("Done.");
        c.close();
    }
}
