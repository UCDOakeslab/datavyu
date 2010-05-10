package org.openshapa.uitests;

import static org.fest.reflect.core.Reflection.method;

import java.awt.Frame;
import java.awt.Point;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.Iterator;

import javax.swing.filechooser.FileFilter;

import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JPanelFixture;
import org.fest.swing.fixture.PlaybackVFixture;
import org.fest.swing.fixture.SpreadsheetPanelFixture;
import org.fest.swing.timing.Timeout;
import org.fest.swing.util.Platform;

import org.openshapa.OpenSHAPA;

import org.openshapa.util.UIImageUtils;
import org.openshapa.util.UIUtils;

import org.openshapa.views.OpenSHAPAFileChooser;
import org.openshapa.views.PlaybackV;
import org.openshapa.views.continuous.PluginManager;
import org.openshapa.views.discrete.SpreadsheetPanel;

import org.testng.Assert;

import org.testng.annotations.Test;


/**
 * Bug733 Description: test that aspect ratio of window remains the same after
 * resize.
 */
public final class UIVideoAspectRatioTest extends OpenSHAPATestClass {

    /**
     * Test Bug 733.
     */
    @Test public void testBug733() throws IOException {
        System.err.println(new Exception().getStackTrace()[0].getMethodName());

        // 1. Open video
        // a. Get Spreadsheet
        JPanelFixture jPanel = UIUtils.getSpreadsheet(mainFrameFixture);
        SpreadsheetPanelFixture ssPanel = new SpreadsheetPanelFixture(
                mainFrameFixture.robot, (SpreadsheetPanel) jPanel.component());

        // b. Open Data Viewer Controller
        mainFrameFixture.clickMenuItemWithPath("Controller",
            "Data Viewer Controller");
        mainFrameFixture.dialog().moveTo(new Point(0, 100));

        final PlaybackVFixture pvf = new PlaybackVFixture(
                mainFrameFixture.robot,
                (PlaybackV) mainFrameFixture.dialog().component());

        // c. Open video
        String root = System.getProperty("testPath");
        final File videoFile = new File(root + "/ui/head_turns.mov");
        Assert.assertTrue(videoFile.exists());

        if (Platform.isOSX()) {
            final PluginManager pm = PluginManager.getInstance();

            OpenSHAPAFileChooser fc = new OpenSHAPAFileChooser();
            fc.setVisible(false);

            for (FileFilter f : pm.getPluginFileFilters()) {
                fc.addChoosableFileFilter(f);
            }

            fc.setSelectedFile(videoFile);
            method("openVideo").withParameterTypes(OpenSHAPAFileChooser.class)
                .in(OpenSHAPA.getPlaybackController()).invoke(fc);
        } else {
            pvf.button("addDataButton").click();

            JFileChooserFixture jfcf = pvf.fileChooser(Timeout.timeout(30000));
            jfcf.selectFile(videoFile).approve();
        }

        // 2. Get window
        Iterator it = pvf.getDataViewers().iterator();

        Frame vid = ((Frame) it.next());
        FrameFixture vidWindow = new FrameFixture(mainFrameFixture.robot, vid);

        vidWindow.moveTo(new Point(pvf.component().getWidth() + 10, 100));

        vidWindow.resizeHeightTo(600 + vid.getInsets().bottom
            + vid.getInsets().top);
        vid.setAlwaysOnTop(true);

        File refImageFile = new File(root + "/ui/head_turns600h0t.png");
        vid.toFront();

        BufferedImage vidImage = UIImageUtils.captureAsScreenshot(vid);
        Assert.assertTrue(UIImageUtils.areImagesEqual(vidImage, refImageFile));

        // 3. Get aspect video dimensions
        double beforeResizeWidth = UIImageUtils.getInternalRectangle(vid)
            .getWidth();
        double beforeResizeHeight = UIImageUtils.getInternalRectangle(vid)
            .getHeight();

        // 4. Make window a quarter height
        vidWindow.resizeHeightTo((int) (beforeResizeHeight / 4)
            + vid.getInsets().bottom + vid.getInsets().top);

        // a. Check that ratio remains the same
        refImageFile = new File(root + "/ui/head_turns150h0t.png");
        vid.toFront();
        vidImage = UIImageUtils.captureAsScreenshot(vid);
        Assert.assertTrue(UIImageUtils.areImagesEqual(vidImage, refImageFile));

        Assert.assertTrue(Math.abs(
                UIImageUtils.getInternalRectangle(vid).getWidth()
                - (beforeResizeWidth / 4)) < 3,
            ""
            + Math.abs(
                UIImageUtils.getInternalRectangle(vid).getWidth()
                - (beforeResizeWidth / 4)));

        // 5. Make window a triple height
        beforeResizeWidth = UIImageUtils.getInternalRectangle(vid).getWidth();
        beforeResizeHeight = UIImageUtils.getInternalRectangle(vid).getHeight();
        vidWindow.resizeHeightTo((int) (beforeResizeHeight * 3)
            + vid.getInsets().bottom + vid.getInsets().top);

        // a. Check that ratio remains the same
        refImageFile = new File(root + "/ui/head_turns450h0t.png");
        vid.toFront();
        vidImage = UIImageUtils.captureAsScreenshot(vid);
        Assert.assertTrue(UIImageUtils.areImagesEqual(vidImage, refImageFile));

        Assert.assertTrue(Math.abs(
                UIImageUtils.getInternalRectangle(vid).getWidth()
                - (beforeResizeWidth * 3)) < 3,
            ""
            + Math.abs(
                UIImageUtils.getInternalRectangle(vid).getWidth()
                - (beforeResizeWidth * 3)));

        /* BugzID:1452
         * //6. Make window half the width beforeResizeWidth =
         * vidWindow.component().getWidth(); beforeResizeHeight =
         * vidWindow.component().getHeight();
         * vidWindow.resizeWidthTo(beforeResizeWidth / 2); //a. Check that ratio
         * remains the same Assert.assertTrue(Math.abs(
         * vidWindow.component().getHeight() - beforeResizeHeight / 2) < 3);
         *
         * //7. Make window double the width beforeResizeWidth =
         * vidWindow.component().getWidth(); beforeResizeHeight =
         * vidWindow.component().getHeight();
         * vidWindow.resizeWidthTo(beforeResizeWidth * 2); //a. Check that ratio
         * remains the same Assert.assertTrue(Math.abs(
         * vidWindow.component().getHeight() - beforeResizeHeight * 2) < 3);
         */


    }
}