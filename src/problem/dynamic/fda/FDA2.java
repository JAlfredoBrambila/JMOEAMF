/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package problem.dynamic.fda;

import algorithm.Individual;
import problem.DynamicProblem;
import problem.DynamicProblem;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class FDA2 implements DynamicProblem {
    
    double[] lower;
    double[] upper;
    
    String problemName;
    static int contEval=0;
    int nVars;
    int nObj;
    
    //
    double time;
    boolean changeStatus = false;
    int tauT = 25; // 20      tT
    int nT = 200; // 200     Tmax
    
    public FDA2() {
        problemName = "FDA2";
        nVars = 13;
        nObj = 2;
        lower = new double[nVars];
        upper = new double[nVars];

        lower[0] = 0.0;
        upper[0] = 1.0;
        for(int i=1; i<nVars; i++) {
            lower[i] = -1.0;
            upper[i] = 1.0;
        }
    }
    
    @Override
    public void costFunction(Individual ind) {
        
        // 1   1 1 1 1 1    1 1 1 1 1 1 1
        int n = ind.getPosition().length;
        
        double g = 0.0;
        double ht = 0.0;
        double h = 0.0;
        double[] f = new double[2];
        f[0] = 0.0;
        f[1] = 0.0;
        
        
        //
        // XII 5var, XIII 7var
        int lInf = 1;
        int lSup = 6;
        
        f[0] = ind.getPosition()[0]; // X1
        //XII
        for(int i=lInf; i<lSup; i++) {
            g+= Math.pow(ind.getPosition()[i], 2.0);
        }
        g = 1.0 + g;
        
        //XIII
        double H = 2 * Math.sin(0.5 * Math.PI * (this.time - 1));
        for(int i=lSup; i<n; i++) {
            ht+= Math.pow(ind.getPosition()[i] - H/4, 2.0);
        }
        
        h = 1 - Math.pow((f[0]/g), (Math.pow(2, H+ht)));
        
        f[1] = g * h;

        contEval++;
        ind.setCost(f);
    }
    
    

    @Override
    public void update(Integer counter) {
        
        //time = (1.0d / (double) nT) * Math.floor(counter / (double) tauT);
        time = (2 * Math.floor((double)counter / (double) tauT)) * ((double) tauT / ((double) nT - (double) tauT));
        //time=2*Math.floor(counter/nT)*(tauT/(tauT*nT-nT));
        //System.out.println("Time: " + time);
        //setChanged();
    }

    @Override
    public boolean hasChanged() {
        return changeStatus;
    }

    @Override
    public void setChanged() {
        changeStatus = true;
    }

    @Override
    public void clearChanged() {
        changeStatus = false;
    }

    

    @Override
    public int getNumVars() {
        return this.nVars;
    }

    @Override
    public String getProblemName() {
        return this.problemName;
    }

    @Override
    public double getVarMin() {
        return this.lower[0];
    }

    @Override
    public double getVarMax() {
        return this.upper[0];
    }

    @Override
    public double[] getLowerBound() {
        return this.lower;
    }

    @Override
    public double[] getUpperBound() {
        return this.upper;
    }

    @Override
    public int getContEvals() {
        return contEval;
    }

}
