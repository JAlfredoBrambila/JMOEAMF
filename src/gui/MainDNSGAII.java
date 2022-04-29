

package gui;

import algorithm.nsgaii.DNSGAII;
import algorithm.*;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class MainDNSGAII {
    public static void main(String[] args) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        DNSGAII algoritm = new DNSGAII();
        algoritm.execute();
    }
}
