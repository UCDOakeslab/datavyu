/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.datavyu.plugins.quicktime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.datavyu.plugins.StreamViewerDialog;
import org.datavyu.util.Constants;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.qd.QDDimension;
import quicktime.std.StdQTConstants;
import quicktime.std.StdQTException;
import quicktime.std.clocks.TimeRecord;
import quicktime.std.movies.Movie;
import quicktime.std.movies.TimeInfo;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.Media;

import javax.swing.*;
import java.awt.*;
import java.io.File;


/**
 * The viewer for a quicktime video file.
 * <b>Do not move this class, this is for backward compatibility with 1.07.</b>
 */
public final class QTDataViewerDialog extends StreamViewerDialog {

    /** How many milliseconds in a second? */
    private static final int MILLI = 1000;

    /** How many frames to check when correcting the FPS */
    private static final int CORRECTION_FRAMES = 5;

    /** The logger for this class */
    private static Logger logger = LogManager.getLogger(QTDataViewerDialog.class);

    private static float FALLBACK_FRAME_RATE = 29.97f;

    /** The quicktime movie this viewer is displaying */
    private Movie movie;

    /** The visual track for the above quicktime movie */
    private Track visualTrack;

    /** The visual media for the above visual track */
    private Media visualMedia;

    public QTDataViewerDialog(final java.awt.Frame parent, final boolean modal) {
        super(parent, modal);
        movie = null;
        try {
            QTSession.open();
        } catch (Throwable e) {
            logger.error("Unable to create " + this.getClass().getName() + ".\nError: ", e);
        }
    }

    @Override
    protected void setPlayerVolume(final float volume) {
        if (movie != null) {
            try {
                movie.setVolume(volume);
            } catch (StdQTException e) {
                logger.error("Unable to set volume: " + volume + ". Error: ", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDuration() {
        if (movie != null) {
            try {
                return (long) Constants.TICKS_PER_SECOND  * (long) movie.getDuration() / movie.getTimeScale();
            } catch (StdQTException e) {
                logger.error("Unable to determine QT movie duration. Error: ", e);
            }
        }
        return -1;
    }

    @Override
    protected void setPlayerSourceFile(final File playerSourceFile) {
        try {
            OpenMovieFile omf = OpenMovieFile.asRead(new QTFile(playerSourceFile));
            movie = Movie.fromFile(omf);
            movie.setVolume(0.7F);

            // Set the time scale for our movie to milliseconds (i.e. 1000 ticks per second.
            movie.setTimeScale(Constants.TICKS_PER_SECOND);

            visualTrack = movie.getIndTrackType(1,
                    StdQTConstants.visualMediaCharacteristic,
                    StdQTConstants.movieTrackCharacteristic);
            visualMedia = visualTrack != null ? visualTrack.getMedia() : null;

            // WARNING there seems to be a bug in QTJava where the video will be rendered as blank if the QT
            // component is added before the window is displayable/visible
            add(QTFactory.makeQTComponent(movie).asComponent());
            setCurrentTime(0L);
        } catch (QTException e) {
            logger.error("Unable to " + playerSourceFile.getAbsolutePath() + ". Error: ", e);
        }
    }

    @Override
    protected Dimension getOriginalVideoSize() {
        try {
            if (visualTrack != null) {
                QDDimension vtDim = visualTrack.getSize();
                return new Dimension(vtDim.getWidth(), vtDim.getHeight());
            }
        } catch (QTException e) {
            logger.error("Unable to get original video size. Error: ", e);
        }
        return new Dimension(1, 1);
    }

    @Override
    protected float getPlayerFramesPerSecond() {
        float fps = 0;
        try {
            if (visualMedia != null) {
                try {
                    fps = (float) visualMedia.getSampleCount() / visualMedia.getDuration() * visualMedia.getTimeScale();
                } catch (Exception e) {
                    logger.error("Could not find fps the normal way, trying the fake route. Error: ", e);
                }
                if ((visualMedia.getSampleCount() == 1.0) || (visualMedia.getSampleCount() == 1) || fps == 0) {
                    fps = correctFPS();
                }
                if (fps == 1 || fps < 1.0f) {
                    throw new QTException(0);
                }
            }
        } catch (QTException e) {
            logger.warn("Unable to calculate FPS, assuming " + FALLBACK_FRAME_RATE + ". Error: ", e);
            isAssumedFramesPerSecond = true;
            fps = FALLBACK_FRAME_RATE;
        }
        return fps;
    }

    /**
     * If there was a problem getting the fps, we use this method to fix it. The
     * first few frames (number of which is specified by CORRECTION_FRAMES) are
     * inspected, with the delay between each measured; the two frames with the
     * smallest delay between them are assumed to represent the fps of the
     * entire movie.
     *
     * @return The best fps found in the first few frames.
     */
    private float correctFPS() {
        float minFrameLength = MILLI; // Set this to one second, as the "worst"
        float curFrameLen = 0;
        int curTime = 0;
        for (int iFrame = 0; iFrame < CORRECTION_FRAMES; iFrame++) {
            try {
                TimeInfo timeObj = visualTrack.getNextInterestingTime(StdQTConstants.nextTimeStep, curTime, 1);
                float candidateFrameLen = timeObj.time - curFrameLen;
                curFrameLen = timeObj.time;
                curTime += curFrameLen;
                minFrameLength = Math.min(candidateFrameLen, minFrameLength);
            } catch (QTException e) {
                logger.error("Error getting time. Error: ", e);
            }
        }
        return MILLI / minFrameLength;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        super.start();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (movie != null) {
                        movie.setRate(getPlaybackSpeed());
                    }
                } catch (QTException e) {
                    logger.error("Unable to start. Error: ", e);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        super.stop();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (movie != null) {
                        movie.stop();
                    }
                } catch (QTException e) {
                    logger.error("Unable to stop. Error: ", e);
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentTime(final long position) {
        try {
            stop();
            if (movie != null && position != getCurrentTime()) {
                TimeRecord time = new TimeRecord(Constants.TICKS_PER_SECOND,
                        Math.min(Math.max(position, 0), getDuration() - 1));
                movie.setTime(time);
            }
        } catch (QTException e) {
            logger.error("Unable to find position: " + position + ". Error: ", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCurrentTime() {
        try {
            return movie.getTime();
        } catch (QTException e) {
            logger.error("Unable to get current time. Error: ", e);
        }
        return 0;
    }

    @Override
    protected void cleanUp() {
        // TODO: Check if we need to do some cleanup?
    }

    @Override
    public boolean isStepEnabled() {
        return false;
    }

    @Override
    public void step() {
        // Nothing to do here
    }
}