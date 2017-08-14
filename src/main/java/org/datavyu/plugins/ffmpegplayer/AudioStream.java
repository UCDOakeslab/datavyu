package org.datavyu.plugins.ffmpegplayer;

import javax.sound.sampled.AudioFormat;

/**
 * Extends the time stream through audio play back functionality. Data is 
 * provided in chunks. The size of these does not directly correspond to the 
 * size of the underlying audio frames.
 */
public interface AudioStream extends TimeStream {

	/** The supported mono format; blank values are from the input audio */
	AudioFormat MONO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 0, 0, 1, 0, 0, false);

	/** The supported stereo format; blank values are from the input audio */
	AudioFormat STEREO_FORMAT = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 0, 0, 2, 0, 0, false);

	/**
	 * Read the the next audio data (not necessarily audio frame).
	 * 
	 * This method assumes that the buffer has been allocated for 
	 * getAudioBufferSize many bytes. 
	 * 
	 * This method blocks if called and there is no available data.
	 * 
	 * @param buffer The buffer where to put the data.
	 * @return
	 */
	int readAudioData(byte[] buffer);

	/**
	 * Get the audio format for the read data.
	 * 
	 * @return The audio format.
	 */
	AudioFormat getAudioFormat();

	/**
	 * Get the size of the audio buffer in bytes.
	 * 
	 * @return The size of the buffer in bytes.
	 */
	int getAudioBufferSize();

	/**
	 * Find if there is any audio data available in the stream.
	 * 
	 * @return True if there is data available; otherwise false.
	 */
	boolean availableAudioData();
}