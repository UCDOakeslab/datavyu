package org.datavyu.plugins.mpv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.datavyu.plugins.MediaPlayer;
import org.datavyu.plugins.PlayerStateEvent;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MpvPlayer extends JPanel {

	/** Identifier for object serialization */
	private static final long serialVersionUID = 7498084201876769022L;

  /** The logger for this class */
  private static Logger logger = LogManager.getFormatterLogger(MpvPlayer.class);
	
	/** The movie stream for this movie player */
	private MediaPlayer mediaPlayer;

	/**
	 * Construct an MpvPlayer by creating the underlying movie stream provider
	 * and registering stream listeners for the video and audio. The stream
	 * listener for the video will show the image in this JPanel.
	 *
	 * @param viewer The ffmpeg viewer
     * @param sourceFile The source file
	 */
	MpvPlayer(MpvStreamViewer viewer, File sourceFile) {
		setLayout(new BorderLayout());
		try {
			mediaPlayer = new MpvAwtMediaPlayer(sourceFile.toURI(), viewer);
			mediaPlayer.init();
		}catch (Exception e) {
			logger.error("Cannot initialize MPV player due to error: ", e);
		}
	}

	/**
	 * Get the duration of the opened video/audio stream of this player in seconds.
	 *
	 * @return Duration of the opened stream.
	 */
	public double getDuration() {	return mediaPlayer.getDuration(); }

	/**
	 * Get the original stream size (not the size when a viewing window is used).
	 *
	 * @return Original stream size: width, height.
	 */
	public Dimension getOriginalVideoSize() {
		return new Dimension(mediaPlayer.getImageWidth(), mediaPlayer.getImageHeight());
	}

	/**
	 * Get the current time in seconds.
	 *
	 * @return Current time in seconds.
	 */
	public double getCurrentTime() { return mediaPlayer.getPresentationTime(); }

	/**
	 * Seek to the position.
	 *
	 * @param position Position in seconds.
	 */
	public void setCurrentTime(double position) {
      logger.info("Seeking position: " + position);
			mediaPlayer.seek(position);
	}

	/**
	 * Clean up the player before closing.
	 */
	public void cleanUp() {
	    logger.info("Closing stream.");
		  mediaPlayer.dispose();
	}

	/**
	 * Set the start back speed for this player.
	 *
	 * @param playbackSpeed The start back speed.
	 */
	public void setPlaybackSpeed(float playbackSpeed) {
		mediaPlayer.setRate(playbackSpeed);
	}

	/**
	 * Play the video/audio.
	 */
	public void play() { mediaPlayer.play(); }

	/**
	 * Stop the video/audio.
	 */
	public void stop() { mediaPlayer.stop(); }

	/**
	 * Pause the video/audio.
	 */
	public void pause() {
		mediaPlayer.pause();
	}


	@Deprecated
	public void setScale(float scale) { }

	/**
	 * Instead of isPlaying a sequence of frames just step by one frame.
	 */
	public void stepForward() { mediaPlayer.stepForward(); }

	public void stepBackward() { mediaPlayer.stepBackward(); }

	/**
	 * Set the audio volume.
	 *
	 * @param volume New volume to set.
	 */
	public void setVolume(float volume) { mediaPlayer.setVolume(volume); }

	public void setMute(final boolean newMute) { mediaPlayer.setMute(newMute); }

	public boolean isMute() {	return mediaPlayer.getMute(); }

	boolean isPlaying() { return mediaPlayer.getState() == PlayerStateEvent.PlayerState.PLAYING; }

  public double getFPS() { return mediaPlayer.getFps();	}

	public boolean isSeekPlaybackEnabled() { return mediaPlayer.isSeekPlaybackEnabled(); }
}
