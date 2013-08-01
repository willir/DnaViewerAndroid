package ru.willir.dnaviewer.utils;

import java.util.Arrays;

public class DnaAbiData {

    public static class AddInfo {
        public int []doubleSignals;
        @Override
        public String toString() {
            StringBuilder res = new StringBuilder();
            res.append("{\n");
            res.append("    doubleSignals:" + Arrays.toString(doubleSignals));
            res.append("\n}");
            return res.toString();
        }
    }

    public DnaAbiData() {
    };

    public int trace[][] = null;
    public String nseq = null;
    public short basePositions[] = null;
    public int numPoints;
    public int lastNonTrashPoint;
    public int tmax;                // Max signal strength
    public String basesOrder;       // Sequnce bases order
    public AddInfo mAddInfo;

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("basesOrder:" + basesOrder + " nseq:" + nseq + "\n");
        res.append("numPoints:" + numPoints + " tmax:" + tmax + " ");
        res.append("lastNonTrashPoint:" + lastNonTrashPoint + "\n");
        res.append("basePositions:" + Arrays.toString(basePositions) + "\n");
        if (trace == null) {
            res.append("trace == null\n");
            return res.toString();
        }

        res.append("TRACE: " + trace.length + ":" + trace[0].length + "\n");
        res.append("mAddInfo:" + mAddInfo);
/*        for (int i = 0; i < trace.length; i++) {
            res.append("trace[" + i + "]:");
            res.append(Arrays.toString(trace[i]));
            res.append("\n");
        }
*/
        return res.toString();
    }

}
