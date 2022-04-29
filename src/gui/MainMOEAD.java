

package gui;

import algorithm.moead.MOEAD;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class MainMOEAD {
    public static void main(String[] args) {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        MOEAD algoritm = new MOEAD();
        algoritm.execute();
    }
}
