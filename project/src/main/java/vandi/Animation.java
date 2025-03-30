package vandi;

import java.awt.Image;
import java.util.ArrayList;

public class Animation {

    private GamePanel panel; // JPanel on which animation is being displayed
    private ArrayList<AnimFrame> frames; // collection of frames for animation
    private int currFrameIndex; // current frame being displayed
    private long animTime; // time that the animation has run for already
    private long startTime; // start time of the animation or time since last update
    private long totalDuration; // total duration of the animation

    public Animation() {
        frames = new ArrayList<AnimFrame>();
        totalDuration = 0;
    }

    public synchronized void addFrame(Image image, long duration) {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }

    public synchronized void start() {
        animTime = 0; // reset time animation has run for to zero
        currFrameIndex = 0; // reset current frame to first frame
        startTime = System.currentTimeMillis(); // reset start time to current time
    }

    public synchronized void update() {
        long currTime = System.currentTimeMillis(); // find the current time
        long elapsedTime = currTime - startTime; // find how much time has elapsed since last update
        startTime = currTime; // set start time to current time

        if (frames.size() > 1) {
            animTime += elapsedTime; // add elapsed time to amount of time animation has run for
            if (animTime >= totalDuration) { // if the time animation has run for > total duration
                animTime = animTime % totalDuration; // reset time animation has run for
                currFrameIndex = 0; // reset current frame to first frame
            }

            while (animTime > getFrame(currFrameIndex).endTime) {
                currFrameIndex++; // set frame corresponding to time animation has run for
            }
        }

    }

    public synchronized Image getImage() {
        if (frames.size() == 0) {
            return null;
        } else {
            return getFrame(currFrameIndex).image;
        }
    }

    public int getNumFrames() { // find out how many frames in animation
        return frames.size();
    }

    private AnimFrame getFrame(int i) { // returns ith frame in the collection
        return frames.get(i);
    }

    private class AnimFrame { // inner class for the frames of the animation

        Image image;
        long endTime;

        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}
