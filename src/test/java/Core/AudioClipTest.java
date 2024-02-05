package Core;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AudioClipTest {

    @Test
    void tests() {

        //creating a test of type audioclip to test on
        AudioClip testClip = new AudioClip();

        //testing setting and getting
        testClip.setSample(5, 5);
        assertEquals(testClip.getSample(5), 5);

        testClip.setSample(20601, -12);
        assertEquals(testClip.getSample(20601), 44444);


    }



}