package Core;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class AudioListener {
    public AudioListener (Clip c){

        clip_ = c;
    }

//    @Override
//    public void update(LineEvent event) {
//        if( event.getType() == LineEvent.Type.STOP ) {
//            // System.out.println("close clip");
//            clip_.close();
//        }
//    }
    private Clip clip_;
}
