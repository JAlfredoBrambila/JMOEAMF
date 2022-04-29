

package gui;

import algorithm.nsgaii.NSGAII;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class MainNSGAII {
    public static void main(String[] args) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        NSGAII algoritm = new NSGAII();
        algoritm.execute();
    }
}
