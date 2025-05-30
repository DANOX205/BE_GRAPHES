package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.AlgorithmPanel;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.Drawing.AlphaMode;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import java.awt.Color;

public class Launch {


    /**
     * Create a new Drawing inside a JFrame an return it.
     *
     * @return The created drawing.
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception {

        // visit these directory to see the list of available files on commetud.
        final String mapName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        final String pathName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        final Graph graph;
        final Path path;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {

            // DONE: read the graph
            graph = reader.read();  
            //graph = null;
        }

        // create the drawing
        final Drawing drawing = createDrawing();

        // DONE: draw the graph on the drawing
        drawing.drawGraph(graph);

        // DONE: create a path reader
        try (final PathReader pathReader = new BinaryPathReader(new DataInputStream(
            new BufferedInputStream(new FileInputStream(pathName))))) {

            // DONE: read the path
            path=pathReader.readPath(graph);            
            //path = null;
        }

        // DONE: draw the path on the drawing
        drawing.drawPath(path);


        // On va 'essayer' d'automatiser Dijkstra
        // Premier Test : Chemin Moyen ("Normal")
        /* 
        Point P_Origin = new Point(10,100);
        Point P_Destination = new Point(250,200);

        Node Origin = new Node(10,P_Origin);
        Node Destination = new Node(50,P_Destination);
        */

        // PREMIER TEST : Trajet long 
        Node Origin = graph.get(796);
        Node Destination = graph.get(1057);

        List<ArcInspector> Inspecteur = ArcInspectorFactory.getAllFilters();
        //ArcInspector Inspecteur = getAllFilters().get(1);

        ShortestPathData chemin = new ShortestPathData(graph,Origin,Destination,Inspecteur.get(2)); // Modifier l'arc Inspector
        DijkstraAlgorithm Dijkstra = new DijkstraAlgorithm(chemin);
        ShortestPathSolution Solution = new ShortestPathSolution(chemin, Status.UNKNOWN); 

        Solution = Dijkstra.doRun();
        drawing.drawPath(Solution.getPath());

        // TROISIEME TEST : Chemin court
        Node Origin3 = graph.get(452);
        Node Destination3 = graph.get(761);
        
        ShortestPathData chemin3 = new ShortestPathData(graph,Origin3,Destination3,Inspecteur.get(2));
        
        DijkstraAlgorithm Dijkstra3 = new DijkstraAlgorithm(chemin3);
        ShortestPathSolution Solution3 = new ShortestPathSolution(chemin3, Status.UNKNOWN); 
        
        Solution3 = Dijkstra3.doRun();
    
        drawing.drawPath(Solution3.getPath());
        
                
        
        // DEUXIEME TEST : Chemin de longueur NULLE
        Node Origin2 = graph.get(68);
        Node Destination2 = graph.get(68);

        ShortestPathData chemin2 = new ShortestPathData(graph,Origin2,Destination2,Inspecteur.get(2));

        DijkstraAlgorithm Dijkstra2 = new DijkstraAlgorithm(chemin2);
        ShortestPathSolution Solution2 = new ShortestPathSolution(chemin2, Status.UNKNOWN); 

        Solution2 = Dijkstra2.doRun();
        if (Solution2.getPath().size() == 1){
            System.out.println("Le trajet est nul ! \n");
            drawing.drawMarker(Origin2.getPoint(), Color.GREEN, Color.RED,AlphaMode.OPAQUE);
        }else {
            drawing.drawPath(Solution2.getPath());
        }

        // QUATRIEME TEST : Destination inaccessible
        Node Origin4 = graph.get(244);
        Node Destination4 = graph.get(742);
        
        ShortestPathData chemin4 = new ShortestPathData(graph,Origin4,Destination4,Inspecteur.get(2));
        
        DijkstraAlgorithm Dijkstra4 = new DijkstraAlgorithm(chemin4);
        ShortestPathSolution Solution4 = new ShortestPathSolution(chemin4, Status.UNKNOWN); 
        
        Solution3 = Dijkstra4.doRun();
        if (Solution3.getPath()==null){
            System.out.println("Le trajet est inaccessible ! \n");
            drawing.drawMarker(Origin4.getPoint(), Color.RED, Color.RED,AlphaMode.OPAQUE);
            drawing.drawMarker(Destination4.getPoint(), Color.RED, Color.RED,AlphaMode.OPAQUE);
        } else {
            drawing.drawPath(Solution4.getPath());
        }
        // On compare le résultat de DIJKSTRA avec PATH sur la Solution1
        Node Origin1 = path.getOrigin();
        Node Destination1 = path.getDestination();

        ShortestPathData chemin1 = new ShortestPathData(graph,Origin1,Destination1,Inspecteur.get(2)); // Modifier l'arc Inspector
        DijkstraAlgorithm Dijkstra1 = new DijkstraAlgorithm(chemin1);
        ShortestPathSolution Solution1 = new ShortestPathSolution(chemin1, Status.UNKNOWN); 

        Solution1 = Dijkstra1.doRun();

        drawing.drawPath(Solution1.getPath());

        System.out.println("Temps du Chemin calculé par Dijkstra : " + Solution1.getCost() + "\n");
        System.out.println("Temps du Chemin calculé par Path :" + Solution1.getPath().getMinimumTravelTime() + "\n");

        // On fait le test avec BELLMAN-FORD
        BellmanFordAlgorithm Bellman1 = new BellmanFordAlgorithm(chemin1);
        ShortestPathSolution Solution11 = new ShortestPathSolution(chemin1, Status.UNKNOWN); 
        Solution11 = Bellman1.doRun();
        drawing.drawPath(Solution11.getPath());
        System.out.println("Temps du Chemin calculé par Bellman-Ford : " + Solution11.getCost() + "\n");

        // CINQUIEME TEST : A*
        
        Node Origin5 = graph.get(200);
        Node Destination5 = graph.get(138);

        ShortestPathData chemin5 = new ShortestPathData(graph,Origin5,Destination5,Inspecteur.get(2));

        AStarAlgorithm AStar1 = new AStarAlgorithm(chemin5);
        ShortestPathSolution Solution5 = new ShortestPathSolution(chemin5, Status.UNKNOWN); 

        Solution5 = AStar1.doRun();
        if (Solution5.getPath().size() == 1){
            System.out.println("Le trajet est nul ! \n");
            drawing.drawMarker(Origin5.getPoint(), Color.GREEN, Color.RED,AlphaMode.OPAQUE);
        }else {
            drawing.drawMarker(Origin5.getPoint(), Color.GREEN, Color.green,AlphaMode.OPAQUE);
            drawing.drawMarker(Destination5.getPoint(), Color.GREEN, Color.GREEN,AlphaMode.OPAQUE);
            drawing.drawPath(Solution5.getPath(),Color.green);
        }
        
    }

}
