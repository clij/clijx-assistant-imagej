package ij.plugin.filter;

import ij.process.ImageProcessor;

// This class extists in order to access ij.process.Binary.fill which is package-private
public class MyBinary {
    public static void fill(ImageProcessor ip, int foreground, int background) {
        new Binary().fill(ip, foreground, background);
    }
}
