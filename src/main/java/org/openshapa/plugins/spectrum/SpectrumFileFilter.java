package org.openshapa.plugins.spectrum;

import java.io.File;

import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;


public class SpectrumFileFilter extends FileFilter {

    SuffixFileFilter ff = null;
    List<String> ext = null;

    public SpectrumFileFilter() {
        ext = new LinkedList<String>();
        ext.add(".wav");
        ext.add(".mp3");
        ext.add(".ogg");
        ext.add(".avi");
        ext.add(".mov");
        ext.add(".mpg");
        ext.add(".mp4");
        ff = new SuffixFileFilter(ext, IOCase.INSENSITIVE);
    }

    @Override public boolean accept(final File file) {
        return ff.accept(file) || file.isDirectory();
    }

    @Override public String getDescription() {
        return "Spectrum display (.wav, .mp3, .ogg, avi, .mov, .mpg, .mp4)";
    }

}
