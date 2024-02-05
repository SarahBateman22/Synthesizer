package Core;

public class SineWave implements AudioComponent{


    double frequency;
    double sampleRate = 44100;

    public SineWave(double freq){
        this.frequency = freq;
    }

    SineWave(){
    }


    @Override
    public AudioClip getClip() {
        AudioClip testClip = new AudioClip();
        for(int i = 0; i <AudioClip.TOTAL_SAMPLES; i++)
        {
            int result = (int) (Short.MAX_VALUE * Math.sin(2 * Math.PI * frequency * i / sampleRate));
            testClip.setSample(i, result);
        }
        return testClip;
    }

    @Override
    public boolean hasInput(){
        return false;
    }

    @Override
    public void connectInput( AudioComponent input){
    }

    public void updateFrequency (double freq){
        this.frequency = freq;
    }

}