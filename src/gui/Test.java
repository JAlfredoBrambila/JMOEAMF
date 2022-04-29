/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gui;

import algorithm.Individual;
import algorithm.Operator;
import algorithm.Utils;
import problem.*;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class Test {
    
    static double time;
    static double tauT;
    static double nT;
    static double t;
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            long tiempoActual = System.currentTimeMillis();
            tauT = 25;
            nT = 200;
            t = tiempoActual / tauT;
            actTiempo(t);
            Thread.sleep(4000);
        }
    }
    
    public static void actTiempo(double counter) {
         time = (2 * Math.floor((double)counter / (double) tauT)) * ((double) tauT / ((double) nT - (double) tauT));
         System.out.println("Tiempo: " + time);
    }
    
}
