/**
 * Java multi-Objective Evolutionary Algorithm Mini Framework (JMOEAMF)
 * Clase: Operator.java
 * Paquete: algorithm
 * Info: Contiene diversos operadores Genéticos
 * @version: 0.7
 * @autor: José Alfredo Brambila Hernámdez <alfredo.brambila@outlook.com>
 * @autor: Miguel Angel Garcia Morales <talivan12@hotmail.com>
 * @autor: Hector Fraire Huacuja <hector.fraire2014@gmail.com>
 * Proyecto Biblioteca de clases para JMOEAMF 
 * Desarrollo: Enero de 2022
 * Actualización: abril de 2022
 * Se permite el uso total o parcial de este código fuente siempre y cuando se le dé el crédito correspondiente a los autores
 *
 */

package algorithm;

import algorithm.nsgaii.Front;
import algorithm.Utils;
import algorithm.Individual;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class Operator {
    
    public static LinkedList<Individual> selection(LinkedList<Individual> pop, double percentage) {
        LinkedList<Individual> parents = new LinkedList<Individual>();
        
        return parents;
    }
    
    public static Individual[] crossover(Individual ind1, Individual ind2) {
        Individual[] offspring = new Individual[2];

        double[] alpha;

        offspring[0] = new Individual();
        offspring[0].copyConfigAtributes(ind1);
        offspring[1] = new Individual();
        offspring[1].copyConfigAtributes(ind2);

        int size = ind1.getPosition().length;
        alpha = new double[size];
        
        for(int i=0; i<size; i++) {
            Random rand = new Random();
            alpha[i] = rand.nextDouble();
        }

        for(int i=0; i<size; i++) {
            offspring[0].setPosition(i, (alpha[i] * ind1.getPosition()[i] + (1-alpha[i]) * ind2.getPosition()[i]));
            offspring[1].setPosition(i, (alpha[i] * ind2.getPosition()[i] + (1-alpha[i]) * ind1.getPosition()[i]));
        }
        
        return offspring;
    }
    
    public static Individual crossover(Individual ind1, Individual ind2, double gamma, double[] l, double[] u) {
        Individual offspring = new Individual();
        int size = ind1.getPosition().length;
        double[] alpha = new double[size];
        
        offspring.clone(ind1);
        
        for(int i=0; i<size; i++) {
            //Random rand = new Random();
            alpha[i] = Utils.getRandomDoubleNumber(-1*gamma, 1+gamma);
        }
        
        for(int i=0; i<size; i++) {
            offspring.getPosition()[i] = alpha[i] * ind1.getPosition()[i] + (1 - alpha[i]) * ind2.getPosition()[i];
            
            offspring.getPosition()[i] = Math.min(Math.max(offspring.getPosition()[i], l[i]), u[i]);
        }
        
        return offspring;
    }
    
    public static Individual[] crossoverSBX(Individual ind1, Individual ind2, double[] lo, double[] up) {
        Individual[] offspring = new Individual[2];
        
        offspring[0] = new Individual();
        offspring[0].copyConfigAtributes(ind1);
        offspring[1] = new Individual();
        offspring[1].copyConfigAtributes(ind2);

        int size = ind1.getPosition().length;
        
        double nc = 1;
	double u = Utils.getRandomNumber0_1();
        
	double beta = 0;
	if(u <= 0.5) {
	    beta = Math.pow((2*u), (1/(nc+1)));
	} else {
	    beta = Math.pow((1/(2*(1-u))), (1/(nc+1)));
	}
        
        for(int i=0; i<size; i++) {
            offspring[0].setPosition(i,Utils.repairSolutionVariableValue((0.5*((ind1.getPosition()[i]+ind2.getPosition()[i])-beta*Math.abs(ind2.getPosition()[i]-ind1.getPosition()[i]))), lo[i], up[i]));
            offspring[1].setPosition(i,Utils.repairSolutionVariableValue((0.5*((ind1.getPosition()[i]+ind2.getPosition()[i])+beta*Math.abs(ind2.getPosition()[i]-ind1.getPosition()[i]))), lo[i], up[i]));
            //offspring[0].getPosition()[i] = 0.5*((ind1.getPosition()[i]+ind2.getPosition()[i])-beta*Math.abs(ind2.getPosition()[i]-ind1.getPosition()[i]));
            //offspring[1].getPosition()[i] = 0.5*((ind1.getPosition()[i]+ind2.getPosition()[i])+beta*Math.abs(ind2.getPosition()[i]-ind1.getPosition()[i]));
        }
        
        return offspring;
    }
    
    //public static Individual[] crossoverSBX(Individual ind1, Individual ind2, double[] lo, double[] up) {
    //Individual ind1, Individual ind2, double gamma, double[] l, double[] u
    public static Individual crossoverSBXM(Individual ind1, Individual ind2, double[] lo, double[] up) {
        Individual offspring = new Individual();
        
        offspring = new Individual();
        offspring.copyConfigAtributes(ind1);
        //offspring[1] = new Individual();
        //offspring[1].copyConfigAtributes(ind2);

        int size = ind1.getPosition().length;
        
        double nc = 1;
	double u = Utils.getRandomNumber0_1();
        
	double beta = 0;
	if(u <= 0.5) {
	    beta = Math.pow((2*u), (1/(nc+1)));
	} else {
	    beta = Math.pow((1/(2*(1-u))), (1/(nc+1)));
	}
        
        for(int i=0; i<size; i++) {
            offspring.setPosition(i,Utils.repairSolutionVariableValue((0.5*((ind1.getPosition()[i]+ind2.getPosition()[i])+beta*Math.abs(ind2.getPosition()[i]-ind1.getPosition()[i]))), lo[i], up[i]));
            if(offspring.getPosition()[i] > up[i]) {
               offspring.getPosition()[i] = up[i];
            }
            if(offspring.getPosition()[i] < lo[i]) {
                offspring.getPosition()[i] = lo[i];
            }
            //offspring[1].setPosition(i,Utils.repairSolutionVariableValue((0.5*((ind1.getPosition()[i]+ind2.getPosition()[i])+beta*Math.abs(ind2.getPosition()[i]-ind1.getPosition()[i]))), lo[i], up[i]));
            //offspring[0].getPosition()[i] = 0.5*((ind1.getPosition()[i]+ind2.getPosition()[i])-beta*Math.abs(ind2.getPosition()[i]-ind1.getPosition()[i]));
            //offspring[1].getPosition()[i] = 0.5*((ind1.getPosition()[i]+ind2.getPosition()[i])+beta*Math.abs(ind2.getPosition()[i]-ind1.getPosition()[i]));
        }
        
        return offspring;
    }
    
    public static Individual mutation(Individual ind, double mu, double[] sigmaV) {
        Individual ind2 = new Individual();
        
        int nVars = ind.getPosition().length;
        int nMu = (int) (Math.ceil(mu * nVars));
        
        int j=0;

        j = Utils.getRandomIntNumber(0,nVars);
        //j = Utils.getRandomIntNumber(nMu,nVars);
        
        
        
        double sigma = 0.0;
        
        if(sigmaV.length>1){
            sigma = sigmaV[j];
        } else {
            sigma = sigmaV[0];
        }
        //sigma = 0.8;
        //System.out.println("Sigma " + sigma);
        ind2.copy(ind);
        ind2.setPosition(j, (ind.getPosition()[j] + sigma * Math.random()));
        ind2.setIndex(ind.getIndex());
        return ind2;
    }
    
    public static Individual polinomialMutation(Individual ind, double muP, double[] lo, double[] up, double distIndex) {
        Individual ind2 = new Individual();
        
        ind2.copy(ind);
        //ind2.setPosition(j, (ind.getPosition()[j] + sigma * Math.random()));
        ind2.setIndex(ind.getIndex());
        double rnd, delta1, delta2, mutPow, deltaq;
        double y, yl, yu, val, xy;
        
        for(int i=0; i<ind.getPosition().length; i++) {
            if(Utils.getRandomNumber0_1() <= muP) {
                y = ind.getPosition()[i];
                yl = lo[i];
                yu = up[i];
                if(yl == yu) {
                    y = yl;
                } else {
                    delta1 = (y - yl) / (yu - yl);
                    delta2 = (yu - y) / (yu - yl);
                    rnd = Utils.getRandomNumber0_1();
                    mutPow = 1.0 / (distIndex + 1.0);
                    
                    if (rnd <= 0.5) {
                        xy = 1.0 - delta1;
                        val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distIndex + 1.0));
                        deltaq = Math.pow(val, mutPow) - 1.0;
                    } else {
                        xy = 1.0 - delta2;
                        val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distIndex + 1.0));
                        deltaq = 1.0 - Math.pow(val, mutPow);
                    }
                    y = y + deltaq * (yu - yl);
                    y = Utils.repairSolutionVariableValue(y, yl, yu);
                    //y = solutionRepair.repairSolutionVariableValue(y, yl, yu);
                }
                ind.getPosition()[i] = y;
            }
        }
        return ind2;
    }
    
    public static boolean dominates1(Individual a, Individual b) {
        boolean dominate = false;
        int n = a.getCost().length;

        boolean[] cmp = new boolean[n];
        for(int i=0; i<n; i++) {
            cmp[i] = (a.getCost()[i] <= b.getCost()[i]) && (a.getCost()[i] < b.getCost()[i]);
        }
        
        dominate = cmp[0];
        for(int i=1; i<n; i++) {
            dominate = dominate && cmp[i];
        }
        
        return dominate;
    }
    
    public static boolean dominates(Individual a, Individual b) {
        boolean dominate = false;
        int n = a.getCost().length;

        int c=0;
        for (int i = 0; i < n; i++) {
            if(a.getCost()[i] < b.getCost()[i]) {
                c++;
            } else if (a.getCost()[i] > b.getCost()[i]) {
                c--;
            }
        }
        
        if(c > 0)
            dominate = true;
        
        return dominate;
    }
    
    public static void crowdingDistance(LinkedList<Individual> pop, LinkedList<Front> F) {
        int nF = F.size();
        double[][] cost=null;
        
        int nObj = pop.get(0).getCost().length;
        int n = 0;
        for(int k=1; k<=nF; k++) {
            cost = Utils.getCostByFront(pop, k);
            n = F.get(k-1).getFronts().size();

            for(int j=0; j<nObj; j++) {
                Utils.Sort2DArrayBasedOnColumnNumber (cost, j+2);
                cost[0][j+3] = Double.POSITIVE_INFINITY;

                for(int i=1; i<n-1; i++) {
                    cost[i][j+3] = Math.abs(cost[i+1][j+1] - cost[i-1][j+1]) / Math.abs(cost[0][j+1] - cost[cost.length-1][j+1]);
                }
                cost[cost.length-1][j+3] = Double.POSITIVE_INFINITY;
            }
            
            Utils.Sort2DArrayBasedOnColumnNumber (cost, 1);
            //Utils.printMatrixAddCol(cost);
        
            setCrowdingDistancePop(cost, pop,k);
            
        }
    }
    
    public static void setCrowdingDistancePop(double[][] cost, LinkedList<Individual> pop, int F) {
        int index = 0;
        for(int i=0; i<cost.length; i++) {
            index = (int) cost[i][0];
            pop.get(index).setCrowdingDistance(cost[i][cost[0].length-2] + cost[i][cost[0].length-1]);
        }
    }
    
    public static LinkedList<Front> nonDominatedSort(LinkedList<Individual> pop) {
        LinkedList<Front> fList = new LinkedList<Front>(); // frente
        
        int nPop = pop.size();

        for(int i=0; i<nPop; i++) {
            pop.get(i).getDominationSet().clear(); // limpia lista de soluciones a las q domina
            pop.get(i).setDominatedCount(0); // limpia contador de soluciones q la dominan
        }
        
        Front f = null;
        f = new Front(1);
  
        for(int i=0; i<nPop; i++) {
            for(int j=i+1; j<nPop; j++) {
                
                Individual p = pop.get(i);
                Individual q = pop.get(j);

                if(dominates(p,q)) {
                    p.addTodominationSet(j);
                    q.dominatedCountIncrement();
                }
                
                if(dominates(q,p)) {
                    q.addTodominationSet(i);
                    p.dominatedCountIncrement();
                }

                pop.set(i, p);
                pop.set(j, q);
 
            }

            if(pop.get(i).getDominatedCount() == 0) {
                f.addToFront(i);
                pop.get(i).setRank(1);
            }
        }
        
        fList.addLast(f); //
        
        int k = 1;
        
        while(true) {
            LinkedList<Integer> Q = new LinkedList<Integer>();
            
            for(Integer i : f.getFronts()) {
                
                Individual p = pop.get(i);
                
                for(Integer j : p.getDominationSet()) {
                    Individual q = pop.get(j);
                    
                    q.dominatedCountDecrement(); // --

                    if(q.getDominatedCount() == 0) {
                        Q.addLast(j);
                        q.setRank(k+1);
                    }
                    
                    pop.set(j, q);
                }
            }
            
            if(Q.size() == 0) {
                break;
            }

            
            f = new Front(k+1);
            f.addFromList(Q);
            fList.add(f);
            k = k + 1; // k++

        }
        
        return fList;
    }
    
    public static void sortPopList(LinkedList<Individual> pop, LinkedList<Front> F) {
        pop.sort(Comparator.comparing(Individual::getCrowdingDistance).reversed());
        pop.sort(Comparator.comparing(Individual::getRank));
        Utils.reorganizeIndex(pop, F);
        for(int i=0; i<F.size(); i++) {
            F.get(i).sortList();
        }

    }
    
}
