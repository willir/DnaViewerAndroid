package ru.willir.dnaviewer.utils;

public class DnaViewNative {

    public static native DnaAbiData test1(String filePathJ);

    static {
        System.loadLibrary("dna_viewer");
    }
}
