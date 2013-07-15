package ru.willir.dnaviewer.utils;

public class DnaViewNative {

    public static native DnaAbiData parseAbiFile(String filePathJ);

    static {
        System.loadLibrary("dna_viewer");
    }
}
