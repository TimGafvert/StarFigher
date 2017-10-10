/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarFighter;

import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
//    static List<Clip> clipArray = new ArrayList<>();

    static List<Clip> clipList = new ArrayList<>();

    public static synchronized void play(final String fileName) {

//Thread thread = new Thread(new MyRunnable());
        // Note: use .wav files             
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(this.getClass().getResource((fileName)));
                    clip.open(inputStream);
                    clipList.add(clip);
                    FloatControl gainControl
                            = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    switch (fileName) {
                        case "MissileExplosion.wav":
                            gainControl.setValue(-20.0f);
                            break;
                        case "MissileWhoosh.wav":
                        {
                            gainControl.setValue(-25.0f);
                        }   break;
                        default:
                            gainControl.setValue(-10.0f);
                            break;
                    }
                    clip.start();
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
                }
            }
        }).start();
        
    }

    public static void cleanThreads() {

        for (Iterator<Clip> iterator = clipList.iterator(); iterator.hasNext();) {
            Clip clip = iterator.next();
            if (!clip.isRunning()) {
                clip.close();
                iterator.remove();
            }
        }

    }

}
