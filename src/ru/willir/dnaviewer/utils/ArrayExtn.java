package ru.willir.dnaviewer.utils;

public class ArrayExtn {

    /**
     * Searches the array for a given value and 
     * returns the corresponding key if successful.
     * 
     * @param needle The searched value.
     * @param haystack The array.
     * @return Returns the key for needle if it is found in the array, 
     *         "-1" otherwise.
     * 
     * @author Rapetov Anton <willir29@yandex.ru>
     */
    public static int searchArr(Object needle, Object haystack[]) {
        if(haystack.length == 0)
            return -1;

        for(int i=0; i<haystack.length; i++){
            if(haystack[i].equals(needle))
                return i;
        }

        return -1;
    }

    /**
     * @param needle The searched value.
     * @param haystack The array.
     * @return true if haystack contain needle, false otherwise
     * 
     * @author Rapetov Anton <willir29@yandex.ru>
     */
    public static boolean hasValue(Object needle, Object haystack[]) {
        if(searchArr(needle, haystack) != -1)
            return true;
        else
            return false;
    }

    /**
     * Merges the elements of one or more arrays together 
     * so that the values of one are appended to the end of the previous one. 
     * It returns the resulting array.
     * @param arr
     * @return The resulting array.
     */
    public static String[] merge(String[]... arr) {
        int arrSize = 0;
        for (String[] array : arr) {
            arrSize += array.length;
        }

        String[] result = new String[arrSize];
        int j = 0;
        for (String[] array : arr) {
            for (String s : array) {
                result[j++] = s;
            }
        }

        return result;
    }

    /**
     * Merges the elements of one or more arrays together 
     * so that the values of one are appended to the end of the previous one. 
     * It returns the resulting array.
     * @param arr
     * @return The resulting array.
     */
    public static byte[] merge(byte[]... arr) {
        int arrSize = 0;
        for (byte[] array : arr) {
            arrSize += array.length;
        }

        byte[] result = new byte[arrSize];
        int j = 0;
        for (byte[] array : arr) {
            for (byte s : array) {
                result[j++] = s;
            }
        }

        return result;
    }

    public static String[] delElFromArray(String arr1[], int elToDelete){
        String arr2[] = new String[arr1.length - 1];

        if(elToDelete < 0 || elToDelete >= arr1.length)
            return arr1;

        int i = 0;
        int j = 0;

        while (i < arr1.length && j < arr2.length) {
            if (i == elToDelete) {
                i++;
            } else {
                arr2[j] = arr1[i];
                i++;
                j++;
            }
        }

        return arr2;
    }

    /**
     * Function add new element(newElement) to array(arr).
     * 
     * @param arr
     * @param newElement
     * @return arr with newElement.
     */
    public static String[] addElToArray(String arr[], String newElement) {

        String arrNew[] = new String[arr.length + 1];
        System.arraycopy(arr, 0, arrNew, 0, arr.length);
        arrNew[arr.length] = newElement;

        return arrNew;
    }

    /**
     * Function add new element(newElement) to array(arr).
     * If newElement not exist in arr.
     * 
     * @param arr
     * @param newElement
     * @return arr with newElement.
     */
    public static String[] addElToArrayIfNotExist(String arr[], String newElement) {

        if (!ArrayExtn.hasValue(newElement, arr)) {
            arr = ArrayExtn.addElToArray(arr, newElement);
        }
        return arr;
    }

}
