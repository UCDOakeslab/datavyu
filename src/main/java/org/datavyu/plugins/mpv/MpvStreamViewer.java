package org.datavyu.plugins.mpv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.datavyu.Datavyu;
import org.datavyu.models.Identifier;
import org.datavyu.plugins.StreamViewerDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import org.datavyu.util.ClockTimer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MpvStreamViewer extends StreamViewerDialog {

    /** The logger for this class */
    private static Logger logger = LogManager.getFormatterLogger(MpvStreamViewer.class);

    /** The player this viewer is displaying */
    private MpvPlayer player;

    /** Currently is seeking */
    private boolean isSeeking = false;

    private ClockTimer clockTimer;

    MpvStreamViewer(final Identifier identifier, final File sourceFile, final Frame parent, final boolean modal) {
        super(identifier, parent, modal);
        logger.info("Opening file: " + sourceFile.getAbsolutePath());
        player = new MpvPlayer(this, sourceFile);
        setSourceFile(sourceFile);
        clockTimer = Datavyu.getVideoController().getClockTimer();
        clockTimer.registerListener(this);
    }

    private void launch(Runnable task) {
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            try {
                SwingUtilities.invokeLater(task);
            } catch (Exception e) {
                logger.error("Failed task. Error: ", e);
            }
        }
    }

    @Override
    protected void setPlayerVolume(float volume) {
        logger.debug("Setting Volume to " + volume);
        player.setVolume(volume);
    }

    @Override
    protected Dimension getOriginalVideoSize() {
        logger.debug("Getting Image Dimension");
        return player.getOriginalVideoSize();
    }

    @Override
    public void setCurrentTime(long time) {
        launch(() -> {
            try {
                if (!isSeeking) {
                    EventQueue.invokeLater(() -> {
                        isSeeking = true;
                        logger.info("Set time to: " + time + " milliseconds.");
                        player.setCurrentTime(time / 1000.0);
                        isSeeking = false;
                    });
                }
            } catch (Exception e) {
                logger.error("Unable to set time to " + time + " milliseconds, due to error: ", e);
            }
        });
    }

    @Override
    public void setCurrentFrame(int frame) {
        throw new NotImplementedException();
    }

    @Override
    public void start() {
        launch(() -> {
            if (!isPlaying()) {
                logger.info("Starting the video");
                player.play();
            }
        });
    }

    @Override
    public void stop() {
        launch(() -> {
            if (isPlaying()) {
                logger.info("Stopping the video");
                player.stop();
            }
        });
    }

    @Override
    public void pause() {
        launch(() -> {
            if (isPlaying()) {
                logger.info("Pausing the video");
                player.pause();
            }
        });
    }

    @Override
    public void setRate(float speed) {
        logger.info("Setting playback speed to: " + speed + "X");
        launch(() -> {
            playBackRate = speed;
            if(isSeekPlaybackEnabled()){
                player.setMute(true);
            }else{
                player.setMute(false);
            }
            if (speed == 0) {
                player.stop();
            } else {
                player.setPlaybackSpeed(speed);
            }
        });
    }

    @Override
    protected float getPlayerFramesPerSecond() {
        logger.debug("Getting the video Frame Per Second");
        return (float) player.getFPS();
    }

    @Override
    public long getDuration() {
        logger.debug("Getting video duration");
        return (long) (player.getDuration() * 1000);
    }

    @Override
    public long getCurrentTime() {
        return (long) (player.getCurrentTime() * 1000);
    }

    @Override
    protected void cleanUp() {
        logger.info("Destroying the Player");
        clockTimer.unRegisterListener(this);
        player.cleanUp();
    }

    @Override
    public void stepForward() {
        logger.info("Step forward");
        launch(() -> player.stepForward());
    }

    @Override
    public void stepBackward() {
        logger.info("Step backward");
        launch(() -> player.stepBackward());
    }

    @Override
    protected void resizeVideo(float scale) {
        super.resizeVideo(scale);
        logger.info("Resizing video to scale %2.2f", scale);
        player.setScale(scale);
        notifyChange();
    }

    @Override
    public boolean isStepEnabled() { return false; }

    @Override
    public boolean isPlaying() { return player != null && player.isPlaying(); }

    @Override
    public boolean isSeekPlaybackEnabled() { return player.isSeekPlaybackEnabled(); }
}
