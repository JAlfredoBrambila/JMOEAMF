/**
 * Java multi-Objective Evolutionary Algorithm Mini Framework (JMOEAMF)
 * Clase: NSGAII.java
 * Paquete: algorithm.nsgaii
 * Info: NSGA-II Non-dominated Sorting Genetic Algorithm
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

package algorithm.nsgaii;

import problem.mop.MOP2;
import algorithm.Utils;
import algorithm.Plot;
import algorithm.Individual;
import algorithm.Individual;
import algorithm.Operator;
import algorithm.Plot;
import algorithm.Utils;
import problem.Problem;
import problem.*;
import java.util.LinkedList;
import java.util.Random;
import problem.zdt.ZDT1;

/**
 *
 * @author J. Alfredo Brambila H. <alfredo.brambila@outlook.com>
 */
public class NSGAII {
    
    int nVar; // tamaño cromosoma
    double[] varMin;
    double[] varMax;
    int nObj; // numero de objetivos
    int maxIt; //numero de iteraciones maximo
    int nPop;// tamaño de la poblacion
    double pCrossover; // porcentaje de cruza
    double nCrossover; // numero de individuos a cruzarse
    double pMutation; // porcentaje de muta
    double nMutation; // numero de individuos que mutan
    double mu; // mu
    double[] sigma; // sigma
    
    LinkedList<Individual> pop; // Listado de poblacion POP
    Problem problem; // Problema
    LinkedList<Front> F; // Listado de frentes
    
    public NSGAII() {
        
        // seleccionar problema [ MOP2 | MOP4 | ZDT1 | ZDT2 | ZDT3 | ZDT4]
        problem = new ZDT1();
        
        System.out.println("Selected problem " + problem.getProblemName());
        
        nVar = problem.getNumVars();
        varMin = problem.getLowerBound();
        varMax = problem.getUpperBound();
        nObj = problem.getNumObjectives();
        maxIt = 100; //100
        nPop = 50;//50
        pCrossover = 0.7;
        nCrossover = 2 * Math.round(pCrossover * nPop / 2);
        pMutation = 0.2;
        nMutation = Math.round(pMutation * nPop);
        mu = 0.02;
        //sigma = 0.1 * (varMax - varMin);
        sigma = Utils.getSigmaValue(0.1, varMin, varMax, nVar);
        
        pop = new LinkedList<Individual>();
        F = new LinkedList<Front>();
    }

    /**
     * Funcion principal de la clase NSGAII
     */
    public void execute() {
        
        // Generar poblacion inicial
        generarePop();
        System.out.println("POP: ");
        Utils.printList(pop);
        
        // Aplicar non-dominated Sorting a POP y generar el listado de frentes en F
        F = Operator.nonDominatedSort(pop);
        
        // Obtener crowding distance para cada individuo de POP
        Operator.crowdingDistance(pop, F);
 
        // Revisar *****
        // Ordenar POP por crowding distance (mayor a menor) y por frente (menor a mayor)
        Operator.sortPopList(pop,F);

        //POPC lista de hijos
        LinkedList<Individual> popc;
        
        //POPM lista de individuos mutados
        LinkedList<Individual> popm;
        
        Individual p1;
        Individual p2;
        Individual p;
        Individual[] pt;
        Individual pm;
        
        // Ciclo principal hasta maxIt
        for(int it=0; it<this.maxIt; it++) {

            // Proceso de Cruza
            popc = new LinkedList<Individual>();
            for(int k=0; k<this.nCrossover/2; k++) {

                // generar indices aleatorios para cruza de padres
                Random rand = new Random();
                int i1 = rand.nextInt(this.nPop);
                int i2 = rand.nextInt(this.nPop);

                // obtener padres a travez de los indices generados
                p1 = this.pop.get(i1);
                p2 = this.pop.get(i2);

                // Obtiene 2 hijos, se almacenan en un vector de objetos
                //pt = Operator.crossover(p1, p2);
                pt = Operator.crossoverSBX(p1, p2,this.varMin,this.varMax); 

                // se calculan los costos para los nuevos hijos
                problem.costFunction(pt[0]);
                problem.costFunction(pt[1]);
                
                // se agregan los hijos a POPC
                popc.add(pt[0]);
                popc.add(pt[1]);
            }
            
            // Proceso de Mutacion
            popm = new LinkedList<Individual>();
            for(int k=0; k<this.nMutation; k++) {
                // Obtener indice de individuo a mutar
                Random rand = new Random();
                //int i = Utils.getRandomIntNumber(0, this.nPop);
                int i = rand.nextInt(this.nPop);
                
                // obtener individuo a mutar a partir de indice
                p = this.pop.get(i);

                //double[] sigmaV = new double[1];
                //sigmaV = sigma;

                //if(k==9) 
                //    System.out.println("++");
                // aplicar operador de mutacion, obtiene nuevo individuo
                //pm = Operator.mutation(p, mu, sigma);
                pm = Operator.polinomialMutation(p, this.pMutation, this.varMin, this.varMax, 20.0);
                
                // calcular costo de nuevo individuo (mutado)
                problem.costFunction(pm);
                
                //agregar individuo mutado a POPM
                popm.add(pm);
            }

            
            // unir las tres listas POP, POPC y POPM en una sola
            Utils.mergePopList(pop, popc, popm);

            // Aplicar non-dominated Sorting a POP y generar el listado de frentes en F
            F = null;
            F = Operator.nonDominatedSort(pop);

            // Obtener crowding distance para cada individuo de POP
            Operator.crowdingDistance(pop, F);

            // Ordenar POP por crowding distance (mayor a menor) y por frente (menor a mayor)
            Operator.sortPopList(pop,F);

            // truncar lista POP a nPop Individuos
            Utils.truncatePopList(pop, this.nPop);

            // volver a Aplicar non-dominated Sorting a POP(truncada) y generar el listado de frentes en F
            F = null;
            F = Operator.nonDominatedSort(pop);
        
            // Obtener crowding distance para cada individuo de POP
            Operator.crowdingDistance(pop, F);

            // Ordenar POP por crowding distance (mayor a menor) y por frente (menor a mayor)
            Operator.sortPopList(pop,F);
            
            //System.out.println("POPi");
            //Utils.printList(pop);

        }
        
        System.out.println("Evaluaciones: " + problem.getContEvals());
        // Imprimir lista de resultados
        System.out.println("\n*** Final Pop List ***\n");
        Utils.printList(pop);
        //System.out.println("");
        Utils.printObj(pop);
        
        // Gráfica individuos de lista POP que estan en frente 1
        Plot.plotFront(pop, 1);
       
    }
    
    /**
     * Genera la poblacion inicial aleatoria
     */
    public void generarePop() {
        for(int i=0; i<this.nPop; i++) {
            // crea nuevo individuo
            Individual ind = new Individual();
            
            //genera valores reales aletatorios(entre varMin y varMax) para el cromosoma de tamaño nVar
            ind.setPosition(Utils.getRandomPos(varMin, varMax, nVar));
            
            // crea vector de costos de tamaño nObj para el individuo 
            ind.setCost(new double[this.nObj]);
            
            // aplica la funcion de costo y obtiene los nObj costos
            problem.costFunction(ind);
            ind.setIndex(i);
            
            //Agrega el nuevo individuo a la poblacion
            pop.add(ind);
        }
    }
    
}
