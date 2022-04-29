/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package problem.dynamic.df;

import algorithm.Individual;
import problem.DynamicProblem;
import problem.DynamicProblem;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class DF12 implements DynamicProblem {
    
    double[] lower;
    double[] upper;
    
    String problemName;
    static int contEval=0;
    int nVars;
    int nObj;
    
    //
    double time;
    boolean changeStatus = false;
    int tauT = 20;
    int nT = 200;
    
    public DF12() {
        problemName = "DF12";
        nVars = 2;
        nObj = 3;
        lower = new double[nVars];
        upper = new double[nVars];

//        lower[0] = 0.0;
//        upper[0] = 1.0;
        for(int i=0; i<nVars; i++) {
            lower[i] = 0.0;
            upper[i] = 1.0;
        }
    }
    
    @Override
    public void costFunction(Individual ind) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public double g(Individual ind) {
        double valG = 0.0;
        double sum = 0.0;
        double mult = 0.0;
        for(int i=2; i>=0; i--) {
            sum += Math.pow(ind.getPosition()[i] - Math.sin(this.time * ind.getPosition()[0]), 2);
        }
        
        double kt = 10*Math.sin(Math.PI * this.time);
        double r = 1 - kt % 2;
        for(int j=0; j<2; j++) {
            mult *= Math.sin(Math.floor(kt * (2 * ind.getPosition()[j] - r)) * Math.PI / 2);   
        }
        
        valG = 1 + sum + Math.abs(mult);
        
        return valG;
    }

    @Override
    public void update(Integer counter) {
        time = ((double)1/(double)this.nT)/Math.floor((double)counter/(double)this.tauT);
        System.out.println("Time: " + time);
        setChanged();
    }

    @Override
    public boolean hasChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearChanged() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    @Override
    public int getNumVars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getProblemName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getVarMin() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getVarMax() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] getLowerBound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double[] getUpperBound() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getContEvals() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
