package org.openshapa.plugins.spectrum.engine;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.nio.ShortBuffer;

import org.gstreamer.Buffer;
import org.gstreamer.Element;

import org.gstreamer.elements.AppSink;
import org.gstreamer.elements.AppSink.NEW_BUFFER;

import org.openshapa.plugins.spectrum.models.StereoAmplitudeData;

import com.sun.jna.Pointer;


/**
 * Selects a fixed number of equally spaced points from the buffer.
 */
public class FixedBufferProcessor implements NEW_BUFFER {

    /** The sink to pull buffers from. */
    private AppSink sink;

    /** Number of audio channels in the buffer. */
    private int mediaChannels;

    /** Buffer to store picked points in. */
    private StereoAmplitudeData data;

    /** Time interval between processing intervals. */
    private long interval;

    /** Timestamp of the previous buffer. */
    private long prevBufTime;

    /** Time of the next value to extract. */
    private long next;

    /**
     * Constructs a new buffer processor.
     *
     * @param sink
     *            The sink to pull data from.
     * @param mediaChannels
     *            Number of audio channels.
     * @param data
     *            Result buffer.
     * @param numSamples
     *            Number of samples to extract.
     */
    public FixedBufferProcessor(final AppSink sink, final int mediaChannels,
        final StereoAmplitudeData data, final int numSamples) {
        this.sink = sink;
        this.mediaChannels = mediaChannels;
        this.data = data;

        next = prevBufTime = NANOSECONDS.convert(data.getDataTimeStart(),
                    data.getDataTimeUnit());

        interval = (long) (NANOSECONDS.convert(
                    data.getDataTimeEnd() - data.getDataTimeStart(),
                    data.getDataTimeUnit()) / (double) numSamples);
    }

    @Override public void newBuffer(final Element elem,
        final Pointer userData) {
        Buffer buf = sink.pullBuffer();

        if (buf != null) {

            /*
             * Divide by two to convert from byte buffer length
             * to short buffer length.
             */
            int size = buf.getSize() / 2;

            ShortBuffer sb = buf.getByteBuffer().asShortBuffer();

            // System.out.println("Buffer size=" + size + ", Timestamp="
            // + buf.getTimestamp().toMillis());

            // 1. Calculate the time interval between each magnitude value.
            double bufferInterval = (buf.getTimestamp().toNanos()
                    / ((double) size / mediaChannels));

            // 2. While the next value to extract is in the current buffer...
            while (next < buf.getTimestamp().toNanos()) {

                // 3. Find where the next value is for channels one and two.
                if (mediaChannels >= 1) {
                    int idx = (int) (Math.ceil(
                                (next - prevBufTime) / bufferInterval));
                    // System.out.println("Next=" + next + ", Prev=" +
                    // prevBufTime
                    // + ", BufInt=" + bufferInterval + ", Idx="
                    // + ((next - prevBufTime) / bufferInterval));

                    // Left channel
                    data.addDataL(sb.get(toEvenIndex(idx)));

                    // Right channel
                    if (mediaChannels >= 2) {
                        data.addDataR(sb.get(toOddIndex(idx)));
                    }
                }

                // 4. Calculate the next value to extract.
                next += interval;
            }

            // 5. Update the time of the previous buffer.
            prevBufTime = buf.getTimestamp().toNanos();
        }
    }

    /**
     * Returns the next odd number in the series. If 'number' is 2k, then 2k+1
     * is returned. If 'number' is 2k+1, then 2k+1 is returned.
     *
     * @param number
     * @return Returns the next odd number in the series. If 'number' is 2k,
     *         then 2k+1
     *         is returned. If 'number' is 2k+1, then 2k+1 is returned.
     */
    private int toOddIndex(final int number) {

        if ((number % 2) == 0) {
            return number + 1;
        }

        return number;
    }

    /**
     * Returns the next event number in the series. If 'number' is 2k+1, then
     * 2k+2 is returned. If 'number' is 2k, then 2k is returned.
     *
     * @param number
     * @return Returns the next event number in the series. If 'number' is 2k+1,
     *         then
     *         2k+2 is returned. If 'number' is 2k, then 2k is returned.
     */
    private int toEvenIndex(final int number) {

        if ((number % 2) != 0) {
            return number + 1;
        }

        return number;
    }

}