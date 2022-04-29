

package problem.zdt;

import algorithm.Individual;
import problem.Problem;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class ZDT2 implements Problem {
    //double[] rangeValues;
    double[] lower;
    double[] upper;
    
    String problemName;
    static int contEval=0;
    int nVars;
    int nObjectives;
    
    public ZDT2() {
        problemName = "ZDT2";
        nVars = 30;
        nObjectives = 2;
        lower = new double[nVars];
        upper = new double[nVars];

        for(int i=0; i<nVars; i++) {
            lower[i] = 0.0;
            upper[i] = 1.0;
        }
    }

    @Override
    public void costFunction(Individual ind) {
        
        int n = ind.getPosition().length;
        
        double g = 0;
        double[] f = new double[2];
        f[0] = 0.0;
        f[1] = 0.0;
        
        f[0] = ind.getPosition()[0];
        for(int i=1; i<ind.getPosition().length; i++) {
            g+= ind.getPosition()[i];
        }
        
        g = 1.0 + 9.0 * g / (ind.getPosition().length - 1);
        
        f[1] = g * (1.0 - Math.pow((f[0] / g),2));
        
        contEval++;
        ind.setCost(f);
    }

    @Override
    public String getProblemName() {
        return problemName;
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
    public int getContEvals() {
        return contEval;
    }

    @Override
    public int getNumVars() {
        return nVars;
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
    public int getNumObjectives() {
        return this.nObjectives;
    }

}
