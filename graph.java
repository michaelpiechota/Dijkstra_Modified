/*
Course: Comp 282
Semester: Summer 2016
Assignment: Modified Dijkstra's Algorithm (Spoon Delivery)
FileName: graph.java
Author: Piechota, Michael
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class graph
{
    private final boolean roundSpoons;
    private boolean[][] edges = new boolean[52][52];
    private final List<Target> targets = new ArrayList<>();


    //nested "main" class; takes input and creates a graph.
    public static void main(String[] strings)
    {
        new graph(true).dijkstrasModifiedAlgorithm("input.txt");
    }


    private graph(boolean roundSpoons)
    {
        this.roundSpoons = roundSpoons;
    }


    //the method "read" reads the graph and the target in the given
    // path.
    private void read(String path)
    {
        for(boolean[] pointEdges : edges)
        {
            Arrays.fill(pointEdges, false);
        }
        // Read the file.
        try(Scanner scanner = new Scanner(new File(path)))
        {
            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim();
                if(line.length() == 2)
                {
                    int from = this.toIndex(line.charAt(0));
                    int to = this.toIndex(line.charAt(1));
                    edges[from][to] = edges[to][from] = true;
                }
                else
                {
                    int start = this.toIndex(line.charAt(0));
                    int end = this.toIndex(line.charAt(1));
                    double finalSpoons =
                            Double.parseDouble(line.substring(2));
                    targets.add(new Target(start, end, finalSpoons));
                }
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    //toIndex converts the given place code to the corresponding
    //index in the graph.
    private int toIndex(char code)
    {
        if(code >= 'a'&& code <= 'z')
        {
            // it is a town.
            return code - 'a';
        }
        else if(code >= 'A'&& code <= 'Z')
        {
            //it is a city.
            return code - 'A' + 26;
        }
        else
        {
            //it is invalid
            return -1;
        }
    }


    //toCode converts the given index in the graph to the
    //corresponding place code.
    private char toCode(int index)
    {
        if(index >= 0&& index < 26)
        {
            return (char) ('a' + index);
        }
        else if(index >= 26&& index < 52)
        {
            return (char) ('A' + index - 26);
        }
        else
        {
            throw new IndexOutOfBoundsException();
        }
    }


    //isCity checks if the spot of the given index is a city or a
    // town.
    private boolean isCity(int index)
    {
        return index >= 26;
    }


    //sets the target
    private void dijkstrasModifiedAlgorithm(String path)
    {
        this.read(path);
        for(Target target : targets)
        {
            this.dijkstrasModifiedAlgorithm(target);
        }
    }


    //modified Dijsktra's Algorithm needed in order to find the
    //"cheapest" path from 1 node to another node
    private void dijkstrasModifiedAlgorithm(Target target)
    {
        Node[] nodes = new Node[edges.length];

        for(int index = 0;index < nodes.length;index++)
        {
            char code = this.toCode(index);
            nodes[index] = new Node(index, code);
            nodes[index].spoons = Double.MAX_VALUE;
        }

        nodes[target.end].spoons = target.finalSpoons;
        Set<Node> toVisits = new HashSet<>();
        toVisits.add(nodes[target.end]);

        while(!toVisits.isEmpty())
        {
            //now find the cheapest place to visit.
            Node toNode = null;
            for(Node toVisit : toVisits)
            {
                if(toNode == null|| toVisit.spoons < toNode.spoons)
                {
                    toNode = toVisit;
                }
            }
            toVisits.remove(toNode);
            toNode.visited = true;
            if(toNode.index == target.start)
            {
                break;
            }

            //now check all the neighbors of the cheapest place.
            for(int from = 0;from < edges.length;from++)
            {
                Node fromNode = nodes[from];
                if(fromNode.visited)
                {
                    continue;
                }

                double fromSpoons = this.calculateFromSpoons
                        (toNode.index,
                        from, toNode.spoons);
                if(fromSpoons < fromNode.spoons)
                {
                    /*
                    System.out.println(fromNode.code +
                    "(" + fromSpoons + ") -> " + toNode.code +
                    "(" + toNode.spoons + ")");
                    */
                    fromNode.spoons = fromSpoons;
                    toVisits.add(fromNode);
                    fromNode.nextNode = toNode;
                }
            }
        }
        //check the cheapest path.
        Node startNode = nodes[target.start];
        if(!startNode.visited)
        {
            System.out.println("Unreachable from " + startNode.code +
                    " to " + nodes[target.end].code + ".");
            return;
        }
        //the following prints the cheapest path.
        Node node = startNode;
        do
        {
            System.out.print(node.code);
            node = node.nextNode;
        }
        while(node != null);
        System.out.println(startNode.spoons);
    }

    
    private double calculateFromSpoons(int to, int from,
                                       double toSpoons)
    {
        //the following checks if a node is reachable.
        if(!edges[from][to])
        {
            return Double.MAX_VALUE;
        }
        // Calculate tax.
        double fromSpoons;
        if(this.isCity(to))
        {
            //city tax: 20% of the spoons you are entering with.
            fromSpoons = toSpoons / .8;
        }
        else
        {
            //town tax: 2 spoons.
            fromSpoons = toSpoons + 2;
        }
        //the following rounds to an integer.
        if(roundSpoons&& fromSpoons > (long) fromSpoons)
        {
            fromSpoons = (long) fromSpoons + 1;
        }
        return fromSpoons;
    }


    private static final class Target
    {
        private final int start;
        private final int end;
        private final double finalSpoons;

        public Target(int start, int end, double finalSpoons)
        {
            this.start = start;
            this.end = end;
            this.finalSpoons = finalSpoons;
        }
    }


    private static final class Node
    {
        private final int index;
        private final char code;
        private double spoons;
        private Node nextNode;
        private boolean visited;

        public Node(int index, char code)
        {
            this.index = index;
            this.code = code;
        }
    }
}
