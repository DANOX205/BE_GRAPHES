package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
//import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.PriorityQueue;


public class DijkstraAlgorithm extends ShortestPathAlgorithm { // J'ai changé la classe protected en public

    public DijkstraAlgorithm(ShortestPathData data) { 
        super(data);
    }

    @Override
    public ShortestPathSolution doRun() { 

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();
        final int INFINITY = Integer.MAX_VALUE;


        // DONE: implement the Dijkstra algorithm
        // Associer un label à chaque Noeud (faire une liste)

        Graph graph = data.getGraph();
        final int nbNodes = graph.size();
        Label[] LabelListe = new Label[nbNodes];
        PriorityQueue<Label> Tas = new BinaryHeap<Label>();
        boolean found = false;
        // Initialiser l'origine
        notifyOriginProcessed(data.getOrigin()); // Modification v1
        System.out.println("Nombre de Nodes"+ nbNodes +"\n");

        //Initialisation
        for (int i=0;i<nbNodes;i++){

            if (data.getOrigin().equals(graph.getNodes().get(i))){ 
                Label LabelNode = new Label(graph.getNodes().get(i),true,0,null); // v1
                LabelListe[i]= LabelNode; // v1
                Tas.insert(LabelListe[data.getOrigin().getId()]);
            } else{ // v1
                Label LabelNode = new Label(graph.getNodes().get(i),false,INFINITY,null); // Problème on ne peut pas mettre null
                LabelListe[i]= LabelNode;
            } // v1
            // Ajouter LabelNode à la liste
        }
        System.out.println("Fin Init \n");
        
        //Itérations
        while ((!(Tas.isEmpty()) && !found)){ // On reste dans la boucle tant que le tas n'est pas vide
            //Label x = Tas.findMin();
            Label x = Tas.deleteMin(); // On enlève x du tas (comme un pop)
            System.out.println("J'enlève du tas : " + x.courant.getId() + " / et de coût:" + x.getCost() + "\n");
            x.marque=true; // On marque x

            for (Arc arc : x.courant.getSuccessors()) { // Je parcours les successeurs de x

                // Small test to check allowed roads...
                if (!data.isAllowed(arc)) {
                    continue;
                }

                Label voisin = LabelListe[arc.getDestination().getId()];

                if (voisin.getMarque() == false){ // Est-ce que ce voisin est marqué ?
                    double val=voisin.coutRevise;

                    if (voisin.coutRevise > x.coutRevise + data.getCost(arc)){
                        voisin.coutRevise = x.coutRevise + data.getCost(arc);
                    }

                    // On essaye de changer le coût du voisin pour qu'il soit plus petit

                    if (val!=voisin.coutRevise){ // Est-ce que le coût pour aller au voisin a changé ?
                        notifyNodeReached(voisin.courant);
                        Tas.insert(voisin); // On met ce voisin dans le tas
                        // System.out.println("Je rajoute dans le tas : " +voisin.courant.getId() + " / et de coût:" + voisin.getCost()+ "\n");
                        voisin.pere=x.courant; // Le père du voisin est x
                    }
                    
                    /*
                    // Amélioration de l'algorithme (qui aurait été bien mieux)
                    if (voisin.coutRevise > x.coutRevise + data.getCost(arc)){
                        notifyNodeReached(voisin.courant);
                        if (Exist(voisin, Tas)){ // Exist(voisin, Tas)
                            Tas.remove(voisin);
                        }
                        Tas.insert(voisin);
                    }
                    */
                }
            }
            if (LabelListe[data.getDestination().getId()].getMarque() == true){
                found = true;
            }
        }
        //notifyNodeReached(data.getDestination());
        System.out.println("Fin Itérations \n");
        ShortestPathSolution solution = null;

        // Destination has no predecessor, the solution is infeasible...
        if (LabelListe[data.getDestination().getId()].getMarque() == false) {
            System.out.println("Terminé ! Aucune Solution Trouvée ! \n");
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
            
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Node> Nodes = new ArrayList<>();
            Node Node = LabelListe[data.getDestination().getId()].getCourant(); 
            while (Node != null) {
                Nodes.add(Node);
                System.out.println(Node.getId());
                Node = LabelListe[Node.getId()].pere;
            }

            // Reverse the path...
            Collections.reverse(Nodes);
            System.out.println("Terminé ! On a une solution !\n");
            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, Path.createShortestPathFromNodes(graph, Nodes),LabelListe[data.getDestination().getId()].getCost());
            //,LabelListe[data.getDestination().getId()].getCost() 
        }
        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

    /* Inutile
    // Méthode pour voir s'il existe des sommets marqués
    public boolean IsMark(Label[] Liste, int taille){
        for(int i=0;i<taille;i++){
            if (Liste[i].getMarque()==true){
                return true;
            }
        }
        return false;
    }
    */

    // Méthode pour voir s'il existe un élément dans le tas (PSEUDO-CODE)
    public boolean Exist(Label voisin, PriorityQueue<Label> Tas1){
        PriorityQueue<Label> Tas = Tas1;
        for(int i=0;i<Tas1.size();i++){
            if (Tas.findMin().courant==voisin.courant){
                return true;
            }
            Tas.deleteMin();
        }
        return false;
    }
    
    public int Min(int a1, int a2){
        if (a1<a2){
            return a1;
        } else {
            return a2;
        }
    }

}
