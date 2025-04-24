package vandi;

import javax.sound.sampled.AudioInputStream; // for playing sound clips
import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap; // for storing sound clips

public class SoundManager { // a Singleton class
    private HashMap<String, Clip> clips;
    private static SoundManager instance = null; // keeps track of Singleton instance
    private float volume;
    private FloatControl engineVolumeControl;
    private Clip engineClip;

    private SoundManager() {
        Clip clip;
        clips = new HashMap<String, Clip>();

        // Load race sounds
        clip = loadClip("src/main/resources/sounds/engine.wav");
        clips.put("engine", clip);
        engineClip = clip;
        
        clip = loadClip("src/main/resources/sounds/shift.wav");
        clips.put("shift", clip);
        
        clip = loadClip("src/main/resources/sounds/downshift.wav");
        clips.put("downshift", clip);
        
        clip = loadClip("src/main/resources/sounds/nitro.wav");
        clips.put("nitro", clip);
        
        clip = loadClip("src/main/resources/sounds/countdown.wav");
        clips.put("countdown", clip);
        
        clip = loadClip("src/main/resources/sounds/win.wav");
        clips.put("win", clip);
        
        clip = loadClip("src/main/resources/sounds/lose.wav");
        clips.put("lose", clip);

        // Original sounds
        clip = loadClip("src/main/resources/sounds/hitSound.wav");
        clips.put("hit", clip);

        clip = loadClip("src/main/resources/sounds/appearSound.wav");
        clips.put("appear", clip);

        volume = 1.0f;
    }

    public static SoundManager getInstance() { // class method to retrieve instance of Singleton
        if (instance == null)
            instance = new SoundManager();

        return instance;
    }

    public Clip loadClip(String fileName) { // gets clip from the specified file
        AudioInputStream audioIn;
        Clip clip = null;

        try {
            File file = new File(fileName);
            if (file.exists()) {
                audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL());
                clip = AudioSystem.getClip();
                clip.open(audioIn);
                
                // Store volume control for engine sound
                if (fileName.contains("engine") && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    engineVolumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                }
            } else {
                System.out.println("Sound file not found: " + fileName);
            }
        } catch (Exception e) {
            System.out.println("Error opening sound file " + fileName + ": " + e);
        }
        return clip;
    }

    public Clip getClip(String title) {
        return clips.get(title);
    }

    public void playClip(String title, boolean looping) {
        Clip clip = getClip(title);
        if (clip != null) {
            clip.setFramePosition(0);
            if (looping)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();
        }
    }

    public void stopClip(String title) {
        Clip clip = getClip(title);
        if (clip != null) {
            clip.stop();
        }
    }
    
    public void setEnginePitch(double pitch) {
        Clip clip = getClip("engine");
        if (clip != null && clip.isControlSupported(FloatControl.Type.SAMPLE_RATE)) {
            FloatControl pitchControl = (FloatControl) clip.getControl(FloatControl.Type.SAMPLE_RATE);
            
            // Calculate pitch value within the control's range
            float originalRate = 44100.0f; // Assumed sample rate
            float newRate = (float)(originalRate * pitch);
            
            // Ensure the new rate is within bounds
            if (newRate > pitchControl.getMaximum()) {
                newRate = pitchControl.getMaximum();
            } else if (newRate < pitchControl.getMinimum()) {
                newRate = pitchControl.getMinimum();
            }
            
            pitchControl.setValue(newRate);
        }
    }
    
    public void setEngineVolume(double level) {
        if (engineVolumeControl != null) {
            // Convert level (0.0 to 1.0) to decibels (-80.0 to 6.0)
            float dB = (float) (Math.log10(level) * 20.0f);
            
            // Ensure within bounds
            if (dB < engineVolumeControl.getMinimum()) {
                dB = engineVolumeControl.getMinimum();
            } else if (dB > engineVolumeControl.getMaximum()) {
                dB = engineVolumeControl.getMaximum();
            }
            
            engineVolumeControl.setValue(dB);
        }
    }
}