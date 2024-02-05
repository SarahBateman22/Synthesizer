package Core;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class AudioClip {
    static final double duration = 2.0;
    static double sampleRate = 44100;

    public static double TOTAL_SAMPLES=duration*sampleRate;

    int arraySize = (int) (sampleRate*duration*2);
    byte[] byteArray = new byte[arraySize];


    int getSample(int index){
        byte firstbyte = byteArray[index*2];
        byte secondbyte = byteArray[index*2 +1];
        return (secondbyte<<8 | (firstbyte & 0xFF));
    }

    void setSample(int index, int value){
        byte firstByte = (byte) (value >> 8);
        byte secondByte = (byte) (value & 0xFF);
        byteArray[(index*2 +1)] = firstByte;
        byteArray[(index*2)] = secondByte;
    }

    public byte[] getData(){
        byte[] array = Arrays.copyOf(byteArray, arraySize);
        return array;
    }




}





