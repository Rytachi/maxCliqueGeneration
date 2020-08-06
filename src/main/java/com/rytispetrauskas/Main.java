package com.rytispetrauskas;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main {

    private int nodesCount;

    private ArrayList<Vertex> graph = new ArrayList<>();

    private int addCounter = 0;

    private static ArrayList<String> cliques = new ArrayList<>();

    private void initGraph() {
        graph.clear();
        for (int i = 0; i < nodesCount; i++) {
            Vertex V = new Vertex();
            V.setX(i);
            graph.add(V);
        }
    }

    private void consturctGraphAll() {
        for (int i = 0; i < 1000; i++) {
            Vertex v = new Vertex();
            v.setX(i);
            graph.add(v);
        }

        for (int i = 0; i < 1000; i++) {
            Vertex v = new Vertex();
            v.setX(i);
            for (int j = i + 1; j < 1000; j++) {
                Vertex u = new Vertex();
                u.setX(j);
                graph.get(i).addNbr(graph.get(j));
            }
        }
    }

    private void readNextGraph(BufferedReader bufReader) throws Exception {
        try {
            nodesCount = Integer.parseInt(bufReader.readLine());
            int edgesCount = Integer.parseInt(bufReader.readLine());
            initGraph();

            for (int k = 0; k < edgesCount; k++) {
                String[] strArr = bufReader.readLine().split(" ");
                int u = Integer.parseInt(strArr[0]) - 1;
                int v = Integer.parseInt(strArr[1]) - 1;
                Vertex vertU = graph.get(u);
                Vertex vertV = graph.get(v);
                vertU.addNbr(vertV);

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void addAdditionalTriangle() {
        int n = 2;
        int index = graph.size();
        for (int i = 0; i < n; i++) {
            Vertex v = new Vertex();
            v.setX(graph.size());
            graph.add(v);
        }

        graph.get(addCounter).addNbr(graph.get(index));
        index++;
        graph.get(addCounter).addNbr(graph.get(index));
        graph.get(index - 1).addNbr(graph.get(index));
        addCounter++;
    }

    private void constructGraph() {
        int n = 3;
        graph = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Vertex v = new Vertex();
            v.setX(i);
            graph.add(v);
        }

    }

    private void createRandomGraph(int n, int m) {
        graph = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Vertex v = new Vertex();
            v.setX(i);
            graph.add(v);
        }

        int edgeCounter = 0;
        Random generate = new Random();
        while (edgeCounter < m) {
            int u = generate.nextInt(n);
            int v = generate.nextInt(n);

            Vertex vertV = graph.get(v);
            Vertex vertU = graph.get(u);

            if (vertU.getX() == vertV.getX()) {
                continue;
            }

            if (vertV.addNbr(vertU)) {
                edgeCounter++;
            }
        }
    }

    private ArrayList<Vertex> getNbrs(Vertex v) {
        int i = v.getX();
        return graph.get(i).nbrs;
    }

    private ArrayList<Vertex> intersect(ArrayList<Vertex> arlFirst,
                                        ArrayList<Vertex> arlSecond) {
        ArrayList<Vertex> arlHold = new ArrayList<>(arlFirst);
        arlHold.retainAll(arlSecond);
        return arlHold;
    }

    private ArrayList<Vertex> union(ArrayList<Vertex> arlFirst,
                                    ArrayList<Vertex> arlSecond) {
        ArrayList<Vertex> arlHold = new ArrayList<>(arlFirst);
        arlHold.addAll(arlSecond);
        return arlHold;
    }

    private ArrayList<Vertex> removeNbrs(ArrayList<Vertex> arlFirst, Vertex v) {
        ArrayList<Vertex> arlHold = new ArrayList<>(arlFirst);
        arlHold.removeAll(v.getNbrs());
        return arlHold;
    }

    private void Bron_KerboschWithPivot(ArrayList<Vertex> R, ArrayList<Vertex> P,
                                        ArrayList<Vertex> X, String pre) {

        // System.out.print(pre + " " + printSet(R) + ", " + printSet(P) + ", "
        //     + printSet(X));

        if ((P.size() == 0) && (X.size() == 0)) {
            saveClique(R);
            //printClique(R);
            return;
        }
        //System.out.println();
        ArrayList<Vertex> P1 = new ArrayList<>(P);
        Vertex u = getMaxDegreeVertex(union(P, X));

        P = removeNbrs(P, u);

        for (Vertex v : P) {
            R.add(v);
            Bron_KerboschWithPivot(R, intersect(P1, getNbrs(v)),
                    intersect(X, getNbrs(v)), pre + "\t");
            R.remove(v);
            P1.remove(v);
            X.add(v);
        }
        return;
    }

    private void Bron_KerboschWithPivot1(ArrayList<Vertex> R, ArrayList<Vertex> P,
                                         ArrayList<Vertex> X, String pre) {

        //System.out.print(pre + " " + printSet(R) + ", " + printSet(P) + ", "
        //        + printSet(X));

        if ((P.size() == 0) && (X.size() == 0)) {
            return;
        }
        //System.out.println();
        ArrayList<Vertex> P1 = new ArrayList<>(P);
        Vertex u = getMaxDegreeVertex(union(P, X));

        P = removeNbrs(P, u);
        //System.out.println(pre + " " + u.getX() + " " + P.size());
        for (Vertex v : P) {
            // System.out.println(pre + " " + v.getX() + " P1=" + printSet(P1) + " P=" + printSet(P));
            R.add(v);
            Bron_KerboschWithPivot1(R, intersect(P1, getNbrs(v)),
                    intersect(X, getNbrs(v)), pre + "\t");
            // System.out.println(pre + "GRIZIMAS " + v.getX() + " " + printSet(P1) + " P=" + printSet(P));
            R.remove(v);
            P1.remove(v);
            X.add(v);
        }
        return;
    }

    private Vertex getMaxDegreeVertex(ArrayList<Vertex> g) {
        Collections.sort(g);

        //System.out.println();
        return g.get(g.size() - 1);
    }

    private void Bron_KerboschPivotExecute() {

        ArrayList<Vertex> X = new ArrayList<>();
        ArrayList<Vertex> R = new ArrayList<>();
        ArrayList<Vertex> P = new ArrayList<>(graph);
        Bron_KerboschWithPivot(R, P, X, "");
    }

    private void Bron_KerboschPivotExecute1() {

        ArrayList<Vertex> X = new ArrayList<>();
        ArrayList<Vertex> R = new ArrayList<>();
        ArrayList<Vertex> P = new ArrayList<>(graph);
        Bron_KerboschWithPivot1(R, P, X, "");
    }

    private void saveClique(ArrayList<Vertex> R) {
        StringBuilder clique = new StringBuilder();
        clique.append("{");
        for (int i = 0; i < R.size(); i++) {
            if (i == R.size() - 1) {
                clique.append(String.valueOf(R.get(i).getX() + 1));
            } else {
                clique.append(String.valueOf(R.get(i).getX() + 1)).append(",");
            }
        }
        clique.append("}");
        cliques.add(clique.toString());
    }

    private void printClique(ArrayList<Vertex> R) {
        System.out.print("  -------------- Maximal Clique : ");
        for (Vertex v : R) {
            System.out.print(" " + (v.getX() + 1));
        }
        System.out.println();
    }

    private String printSet(ArrayList<Vertex> X) {
        StringBuilder strBuild = new StringBuilder();

        strBuild.append("{");
        for (Vertex v : X) {
            strBuild.append("").append(v.getX()).append(",");
        }
        if (strBuild.length() != 1) {
            strBuild.setLength(strBuild.length() - 1);
        }
        strBuild.append("}");
        return strBuild.toString();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {

            Main ff = new Main();
            int n = 1000;
            int m = 20000;
            for (int i = 0; i < 12; i++) {
                ff.createRandomGraph(n, m);
                long startTime = System.currentTimeMillis();
                ff.Bron_KerboschPivotExecute1();
                long endTime = System.currentTimeMillis();
                double completionTime = (endTime - startTime) / 1000.;
                m += 10000;
                System.out.println(i + " " + completionTime);
            }
        }
        if (args.length == 3) {
            int testNumber = Integer.parseInt(args[2]);
            BufferedReader bufReader;
            File file = new File(args[0]);
            try {
                bufReader = new BufferedReader(new FileReader(file));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Main ff = new Main();
            try {

                ff.readNextGraph(bufReader);
                long startTime = System.currentTimeMillis();
                ff.Bron_KerboschPivotExecute();
                for (int j = 0; j < testNumber - 1; j++) {
                    ff.Bron_KerboschPivotExecute1();
                }
                long endTime = System.currentTimeMillis();
                double completionTime = (endTime - startTime) / 1000.;

                if (args[1].equals("-f")) {

                    BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"));
                    writer.write("ALL MAXIMAL CLIQUES:\n");
                    for (String clique : cliques) {
                        writer.write(clique + "\n");
                    }
                    writer.write("Completion time: " + completionTime);
                    writer.close();
                } else if (args[1].equals("-s")) {
                    System.out.println("ALL MAXIMAL CLIQUES:");
                    for (String clique : cliques) {
                        System.out.println(clique);
                    }
                    System.out.println("Completion time: " + completionTime);
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Exiting : " + e);
            } finally {
                try {
                    bufReader.close();
                } catch (Exception f) {
                    f.printStackTrace();
                }
            }
        }
    }
}