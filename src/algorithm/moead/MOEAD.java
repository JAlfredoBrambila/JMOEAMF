/**
 * Java multi-Objective Evolutionary Algorithm Mini Framework (JMOEAMF)
 * Clase: MOEAD.java
 * Paquete: algorithm.moead
 * Info: MOEA/D Multiobjective Evolutionary Algorithm Based on Decomposition
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

package algorithm.moead;

import algorithm.Individual;
import algorithm.Operator;
import algorithm.Plot;
import algorithm.Utils;
import problem.mop.MOP2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import problem.Problem;
import problem.zdt.ZDT1;
import problem.zdt.ZDT3;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class MOEAD {

    int nVar; // tamaño cromosoma
    double[] varMin;
    double[] varMax;
    int nObjectives; // numero de objetivos
    int maxIt; //numero de iteraciones maximo
    int nPop;// tamaño de la poblacion
    int nArchive; 
    int T;
    double gamma;
    LinkedList<SubProblem> sp;
    double[] z;

    
    LinkedList<Individual> pop; // Listado de poblacion POP
    LinkedList<Individual> ndPop;
    Problem problem; // Problema
    LinkedList<Individual> EP; // 
    
    public MOEAD() {
        
        // seleccionar problema [ MOP2 | MOP4 | ZDT1 | ZDT2 | ZDT3 | ZDT4]
        problem = new MOP2();
        
        System.out.println("Selected problem " + problem.getProblemName());
        
        nVar = problem.getNumVars();
        varMin = problem.getLowerBound();
        varMax = problem.getUpperBound();
        nObjectives = problem.getNumObjectives();
        maxIt = 100; //100
        nPop = 50;//50
        nArchive = 50;
        T = (int)Math.max(Math.ceil(0.15*nPop), 2); // numero de vecinos
        T = (int)Math.min(Math.max(T, 2), 15);
        gamma = 0.5;
        sp = createSubProblems(); // Create Sub-problems
        z = zeros(this.nObjectives); // Initialize Goal Point
        
        pop = new LinkedList<Individual>();
        EP = new LinkedList<Individual>();
        ndPop = new LinkedList<Individual>();
    }

    /**
     * Funcion principal de la clase MOEA/D
     */
    public void execute() {
        generatePop();
        
        determineDomination(pop); // Determine Population Domination Status
        EP = getNonDominated(pop); // Initialize Estimated Pareto Front
        
        int k1=0;
        int k2=0;
        int j1=0;
        int j2=0;
        Individual p1;
        Individual p2;
        Individual y;
        for(int it = 0; it<this.maxIt; it++) {
            for(int i=0; i<this.nPop; i++) {
                k1 = Utils.getRandomIntNumber(0, T);
                k2 = Utils.getRandomIntNumber(0, T);
                while(k1 == k2) {
                    k2 = Utils.getRandomIntNumber(0, T);
                }
                
                j1 = (int)sp.get(i).getNeighbors()[k1];
                p1 = new Individual();
                p1.clone(pop.get(j1));
                
                j2 = (int)sp.get(i).getNeighbors()[k2];
                p2 = new Individual();
                p2.clone(pop.get(j2));
                
                y = new Individual();
                y.clone(Operator.crossover(p1, p2, gamma, varMin, varMax));
                //y.clone(Operator.crossoverSBXM(p1, p2, varMin, varMax));
                problem.costFunction(y);
                
                //z = Math.min(z, y.get)
                for(int j=0; j<nObjectives; j++) {
                    z[j] = Math.min(z[j], y.getCost()[j]);
                }
                
                for(double j : sp.get(i).getNeighbors()) {
                    y.setG(this.decomposedCost(y, z, sp.get((int)j).getLambda()));
                    if(y.getG() <= pop.get((int)j).getG()) {
                        pop.get((int)j).clone(y);
                    }
                }

            }
            
            determineDomination(pop);
            ndPop = getNonDominated(pop);
            
            Utils.mergeList(EP, ndPop);
            
            EP = getNonDominated(EP);
            
            EP = getNonDominated(EP);

            int valIndice=0;
            if(EP.size() > this.nArchive) {                
                while(EP.size() > this.nArchive) {
                    valIndice = Utils.getRandomIntNumber(0, EP.size());
                    EP.remove(valIndice);
                }
            }

        }
        
        System.out.println("EP ");
        for(Individual ind : EP) {
            System.out.println(ind);
        }
        Plot.plotFront(EP);
    }
    
    /**
     * Genera la poblacion inicial aleatoria
     */
    public void generatePop() {
        for(int i=0; i<this.nPop; i++) {
            // crea nuevo individuo
            Individual ind = new Individual();
            
            //genera valores reales aletatorios(entre varMin y varMax) para el cromosoma de tamaño nVar
            ind.setPosition(Utils.getRandomPos(varMin, varMax, nVar));
            
            // crea vector de costos de tamaño nObj para el individuo 
            ind.setCost(new double[this.nObjectives]);
            
            
            // aplica la funcion de costo y obtiene los nObj costos
            problem.costFunction(ind);
            ind.setIndex(i);
            
            // set z
            for(int j=0; j<nObjectives; j++) {
                z[j] = Math.min(z[j], ind.getCost()[j]);
            }
            
            // set g
            ind.setG(decomposedCost(ind,z,sp.get(i).getLambda()));
            //Agrega el nuevo individuo a la poblacion
            pop.add(ind);
        }
    }
    
    public LinkedList<SubProblem> createSubProblems() {
        LinkedList<SubProblem> subProblems = new LinkedList<SubProblem>();
        SubProblem subProb;
        
        double[] lambda = new double[this.nObjectives];
        double norma = 0.0;
        
        for (int i = 0; i < this.nPop; i++) {
            subProb = new SubProblem();
            lambda = new double[this.nObjectives];
            for (int j = 0; j < this.nObjectives; j++) {
                lambda[j] = Math.random();
            }

            // devuelve la norma euclidiana del vector v
            norma = euclideanNorm(lambda);
            for (int j = 0; j < this.nObjectives; j++) {
                lambda[j] = lambda[j] / norma;
            }
            
            subProb.setLambda(lambda);
            //System.out.println("Add: " + subProb.getLambda()[0] + " "  + subProb.getLambda()[1]);
            subProblems.add(subProb);
        }
        
        //
        /*for (int i = 0; i < subProblems.size(); i++) {
            System.out.println("::sp: " + subProblems.get(i).getLambda()[0] + " " + subProblems.get(i).getLambda()[1]);
        }*/
        
        double[][] D = euclideanDistance(subProblems);
        
        double[] SO;
        for (int i = 0; i < this.nPop; i++) {
            SO = getSortArrayPos(D[i]);
            subProblems.get(i).setNeighbors(SO);
            //System.out.println("i: " + subProblems.get(i));
        }
        
        //
        
        return subProblems;
    }
    
    public double[] getSortArrayPos(double[] a) {
        double[] posArray = new double[T];
        double[][] arr = new double[a.length][2];
        
        for(int i=0; i<a.length; i++) {
            arr[i][0] = a[i];
            arr[i][1] = i;
        }
        
        Utils.Sort2DArrayBasedOnColumnNumber(arr, 1);
        
        for(int i=0; i<T; i++) {
            posArray[i] = arr[i][1];
        }
        
        return posArray;
    }
    
    public double euclideanNorm(double[] v) {
        
        double sum = 0.0;
        for(int i=0; i<v.length; i++) {
            sum += Math.pow(v[i], 2);
        }
        
        return Math.sqrt(sum);
    }
    
    public double[][] euclideanDistance(LinkedList<SubProblem> x) {
        double[][] d = new double[x.size()][x.size()];
        
        /*for(int i=0; i<x.size(); i++) {
            System.out.println(":: " + x.get(i).getLambda()[0] + " " + x.get(i).getLambda()[1]);
        }*/
        
        double sum = 0.0;
        for(int i=0; i<x.size(); i++) {
            for(int j=0; j<x.size(); j++) {
                sum = 0.0;
                //System.out.println("0:: " + x.get(i).getLambda()[0] + " " + x.get(i).getLambda()[1] + " - "  + x.get(j).getLambda()[0] + " " + x.get(j).getLambda()[1]);
                for(int k=0; k<this.nObjectives; k++) {
                    //System.out.println("1:: " + x.get(i).getLambda()[0] + " " + x.get(i).getLambda()[1] + " - "  + x.get(j).getLambda()[0] + " " + x.get(j).getLambda()[1]);
                    sum += Math.pow(x.get(i).getLambda()[k] - x.get(j).getLambda()[k], 2);
                    //System.out.println("2:: " + x.get(i).getLambda()[0] + " " + x.get(i).getLambda()[1] + " - "  + x.get(j).getLambda()[0] + " " + x.get(j).getLambda()[1]);
                }
                d[j][i] = Math.sqrt(sum);
            }
        }
        
        return d;
    }
    
    public double[] zeros(int n) {
        double[] v = new double[n];
        for(int i=0; i<n; i++) {
            v[i] = 0;
        }
        
        return v;
    }
    
    public void determineDomination(LinkedList<Individual> pList) {
        for(int i=0; i<pList.size(); i++) {
            pList.get(i).setIsDominated(false);
        }
        
        for(int i=0; i<pList.size(); i++) {
            for(int j=i+1; j<pList.size(); j++) {
                if(Operator.dominates(pop.get(i), pop.get(j))) {
                    pop.get(j).setIsDominated(true);
                } else if(Operator.dominates(pop.get(j), pop.get(i))) {
                    pop.get(i).setIsDominated(true);
                }
            }
        }
    }
    
    public LinkedList<Individual> getNonDominated(LinkedList<Individual> pList) {
        LinkedList<Individual> nondominated = new LinkedList<Individual>();
        
        Individual indTemp;
        for(int i=0; i<pList.size(); i++) {
            if(!pList.get(i).isIsDominated()) {
                indTemp = new Individual();
                indTemp.clone(pList.get(i));
                nondominated.add(indTemp);
            }
        }
        
        return nondominated;
    }
    
    public double decomposedCost(Individual ind, double[] z, double[] lambda) {
        
        //double[] fx = new double[this.nObjectives];
        ArrayList<Double> fxl = new ArrayList<Double>(); 
        
        for(int i=0; i<this.nObjectives; i++) {
            //fx[i] = lambda[i] * Math.abs(ind.getCost()[i] - z);
            fxl.add(lambda[i] * Math.abs(ind.getCost()[i] - z[i]));
        }
        
        
        return Collections.max(fxl);
    }
    
}
