/**
 * Java multi-Objective Evolutionary Algorithm Mini Framework (JMOEAMF)
 * Clase: Utils.java
 * Paquete: algorithm
 * Info: Contiene diversas utilerias
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class Utils {
    /**
     * Funcion nonDominatedSort 
     * @param pop lista con la poblacion 
     * @return 
     */
    public static void getCostByFront(LinkedList<Individual> pop, int F, double[][] obj1, double[][] obj2) {
        LinkedList<Individual> pF = new LinkedList<Individual>();
        for(Individual i : pop) {
            if(i.getRank() == F) {
                pF.add(i);
            }
        }
        
        obj1 = new double[pF.size()][pF.get(0).getPosition().length + 1];
        obj2 = new double[pF.size()][pF.get(0).getPosition().length + 1];
        
        if(pF.get(0).getCost().length > 2) {
            System.out.println("Debe ampliar manualmente array de costos en archivo Utils.Java linea 25,26,40,41");
        }
        
        int f = 0;
        for(Individual a : pF) {
            for(int po=0; po<a.getPosition().length; po++) {
                obj1[f][po] = a.getPosition()[po];
                obj2[f][po] = a.getPosition()[po];
            }
            obj1[f][a.getPosition().length] =  a.getCost()[0];
            obj2[f][a.getPosition().length] =  a.getCost()[1];
            f++;
        }
        
        
        
    }
    
    public static double[][] getCostByFront(LinkedList<Individual> pop, int F) {
        LinkedList<Individual> pF = new LinkedList<Individual>();
        for(Individual i : pop) {
            if(i.getRank() == F) {
                pF.add(i);
            }
        }

        double[][] obj = new double[pF.size()][pF.get(0).getCost().length + pF.get(0).getCost().length + 1];

        int f = 0;
        for(Individual a : pF) {
            obj[f][0] = a.getIndex();
            for(int po=1; po<=a.getCost().length; po++) {
                obj[f][po] =  a.getCost()[po-1];
            }
            f++;
        }
        
        return obj;
    }
    
    public static boolean isTerminated(LinkedList<Individual> pop) {
        boolean terminated = true;
        for(Individual i : pop) {
            if(i.getDominatedCount() > 0)
                return false;
        }
        return terminated;
    }
    
    public static  void Sort2DArrayBasedOnColumnNumber (double[][] array, final int columnNumber){
        Arrays.sort(array, new Comparator<double[]>() {
            @Override
            public int compare(double[] first, double[] second) {
                //System.out.println(":: COMPARE: " + first[columnNumber-1] + " > " + second[columnNumber-1] + " | " + (first[columnNumber-1] > second[columnNumber-1]));
               if(first[columnNumber-1] > second[columnNumber-1]) return 1;
               else return -1;
            }
        });
    }
    
    public static void printList(LinkedList<Individual> pop) {
        //System.out.println("");
        int p=1;
        int nVars = pop.get(0).getPosition().length;
        int nObj = pop.get(0).getCost().length;
        
        System.out.print("Ind\tFront\t");
        for(int i=0; i<nVars; i++) {
            System.out.print("VAR"+(i+1)+"\t");
        }
        for (int i = 0; i < nObj; i++) {
            System.out.print("Cost" + (i + 1) + "\t");
        }
        System.out.println("");
        //System.out.println("Cost");

        for(Individual ind : pop) {
            System.out.print(p+"\t"+ind.getRank()+"\t");
            for(int i=0; i<nVars; i++) {
                System.out.print(ind.getPosition()[i] + "\t");
            }
            for(int i=0; i<nObj; i++) {
                System.out.print(ind.getCost()[i] + "\t");   
            }
            System.out.println("");
            //System.out.println(ind.getCrowdingDistance()+" ");
            //System.out.println(ind.getPosition()[0] + "\t" + ind.getPosition()[1] + "\t" + ind.getPosition()[2] + "\t" + ind.getPosition()[3] + "\t" + ind.getPosition()[4] + "\t" + ind.getCost()[0] );
            p++;
        }
        System.out.println("");
    }
    
    public static void printObj(LinkedList<Individual> pop) {
        System.out.println("");
        int nObj = pop.get(0).getCost().length;
        for(Individual ind : pop) {
            for(int i=0; i<nObj; i++) {
                System.out.print(ind.getCost()[i] + "\t");   
            }
            System.out.println("");
        }
    }
    
    @Deprecated
    public static void printList2(LinkedList<Individual> pop) {
        for(Individual ind : pop) {
            
            System.out.println("Ind\t" +ind.getIndex() + "\t" + ind.getRank() + "\t" + ind.getDominatedCount() + "\t" + ind.getDominationSet().size() + "\t" + ind.getPosition()[0] + "\t" + ind.getPosition()[1] + "\t" + ind.getPosition()[2] + "\t" + ind.getCost()[0] + "\t" + ind.getCost()[1] + "\t" + ind.getCrowdingDistance());
        }
    }
    
    public static void printMatrix(double[][] mtx) {
        System.out.println("");
        for (int i = 0; i < mtx.length; i++) {
            for (int j = 0; j < mtx[0].length; j++) {
                System.out.print(mtx[i][j] + "\t");
            }
            System.out.println("");
        }
    }
    
    public static void printMatrixAddCol(double[][] mtx) {
        System.out.println("");
        for (int i = 0; i < mtx.length; i++) {
            for (int j = 0; j < mtx[0].length; j++) {
                System.out.print(mtx[i][j] + "\t");
            }
            System.out.print(mtx[i][mtx[0].length-2] + mtx[i][mtx[0].length-1]);
            System.out.println("");
        }
    }
    
    public static int getRandomIntNumber(int min, int max) {
        Random rand = new Random();
        return (int)(min+(max-min) * rand.nextDouble());
        //return (int) ((Math.random() * (max - min)) + min);
    }
    
    public static double getRandomDoubleNumber(double min, double max) {
        //return Math.random() * (max - min) + min;
        Random rand = new Random();
        return (min+(max-min) * rand.nextDouble());
    }
    
    public static double getRandomNumber0_1() {
        Random rand = new Random();
        return rand.nextDouble();
    }
    
    public static double[] getRandomPos(double min, double max, int nVar) {
        double[] obj = new double[nVar];
        double rand = 0.0;
        for(int i=0; i<nVar; i++) {
            obj[i] = getRandomDoubleNumber(min,max);
        }
        
        return obj;
    }
    
    public static double[] getRandomPos(double[] min, double[] max, int nVar) {
        double[] obj = new double[nVar];
        double rand = 0.0;
        for(int i=0; i<nVar; i++) {
            obj[i] = getRandomDoubleNumber(min[i],max[i]);
        }
        
        return obj;
    }
    
    public static double[] getSigmaValue(double sigmaStep, double[] min, double[] max, int nVar) {
        double[] sigma = new double[nVar];
        
        for(int i=0; i<nVar; i++) {
            sigma[i] = sigmaStep * (max[i] - min[i]);
        }
        return sigma;
    }
    
    public static void reorganizeIndex(LinkedList<Individual> pop, LinkedList<Front> F) {
        int k=0;
        for(int i=0; i<pop.size(); i++) {
            pop.get(i).setIndex(i);
            if(k != pop.get(i).getRank()) {
                k = pop.get(i).getRank();
                F.get(k-1).getFronts().clear();
                F.get(k-1).addToFront(i);
            } else {
                F.get(k-1).addToFront(i);
            }
        }
    }
    
    public static void truncatePopList(LinkedList<Individual> pop, int n) {
        for(int i=pop.size()-1; i>n-1; i--) {
            //System.out.println("Borrando indice: " + i);
            pop.remove(i);
            
        }
    } 
    
    public static void mergePopList(LinkedList<Individual> pop, LinkedList<Individual> cro, LinkedList<Individual> mu) {
        int index = pop.size();
        for(Individual ind : cro) {
            ind.setIndex(index);
            pop.add(ind);
            index++;
        }
        
        for(Individual ind : mu) {
            ind.setIndex(index);
            pop.add(ind);
            index++;
        }
    }
    
    public static void mergeList(LinkedList<Individual> a, LinkedList<Individual> b) {
        int index = a.size();
        for(Individual ind : b) {
            ind.setIndex(index);
            a.add(ind);
        }
    }
    
    public static double repairSolutionVariableValue(double value, double lowerBound, double upperBound) {

        double result = value;
        if (value < lowerBound) {
            result = lowerBound;
        }
        if (value > upperBound) {
            result = upperBound;
        }

        return result;
    }
    
}
