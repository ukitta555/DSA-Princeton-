/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;


public class BaseballElimination {
    private final ArrayList<String> certificateOfElimination;
    private final int numberOfTeams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remainingGames;
    private final ST<String, Integer> STTeamNumber;
    private final int[][] gamesMatrix;
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename)
    {
        In in = new In(filename);
        this.numberOfTeams = in.readInt();
        this.STTeamNumber = new ST<>();
        this.wins = new int[numberOfTeams];
        this.losses = new int[numberOfTeams];
        this.remainingGames = new int[numberOfTeams];
        this.gamesMatrix = new int[numberOfTeams][numberOfTeams];
        this.certificateOfElimination = new ArrayList<>();

        for (int i = 0; i < numberOfTeams; i++)
        {
            String teamName = in.readString();
            STTeamNumber.put(teamName, i);
            int teamWins = in.readInt();
            wins[i] = teamWins;
            int teamLosses = in.readInt();
            losses[i] = teamLosses;
            int gamesToPlay = in.readInt();
            remainingGames[i] = gamesToPlay;
            for (int j = 0; j < numberOfTeams; j++)
            {
                int Gij = in.readInt();
                gamesMatrix [i][j] = Gij;
            }
        }

        // test
        /*
        for (int i = 0; i < numberOfTeams; i++)
        {
            System.out.printf ("Team %s: Wins %d Losses %d Remaining %d", teams[i], wins[i], losses[i], remainingGames[i]);
            System.out.println();
        }

        for (int i = 0; i < numberOfTeams; i++)
        {
            for (int j = 0; j < numberOfTeams; j++)
            {
                System.out.print (gamesMatrix[i][j]+" ");
            }
            System.out.println ();
        }
        */
        //System.out.println("constructor works");


    }
    // number of teams
    public int numberOfTeams()
    {
        return numberOfTeams;
    }
    // all teams
    public Iterable<String> teams()
    {
        return STTeamNumber.keys();
    }
    // number of wins for given team
    public int wins(String team)
    {
        if (team == null || STTeamNumber.get(team) == null) throw new IllegalArgumentException();
        return wins[STTeamNumber.get(team)];
    }
    // number of losses for given team
    public int losses(String team)
    {
        if (team == null || STTeamNumber.get(team) == null) throw new IllegalArgumentException();
        return losses[STTeamNumber.get(team)];
    }
    // number of remaining games for given team
    public int remaining(String team)
    {
        if (team == null || STTeamNumber.get(team) == null) throw new IllegalArgumentException();
        return remainingGames[STTeamNumber.get(team)];
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2)
    {
        if (team1 == null || team2 == null || STTeamNumber.get(team1) == null || STTeamNumber.get(team2) == null) throw new IllegalArgumentException();
        return gamesMatrix [STTeamNumber.get(team1)][STTeamNumber.get(team2)];
    }
    // is given team eliminated?
    public boolean isEliminated(String team)
    {
        if (team == null || STTeamNumber.get(team) == null) throw new IllegalArgumentException();
        // clear the array list so that the teams don't repeat after several queries
        certificateOfElimination.clear();
        // get the index of the team to find the place where the data about stores
        int queryTeamIndex = STTeamNumber.get(team);
        // check the simple condition where the team is trivially eliminated
        for (int i = 0; i < numberOfTeams; i++)
        {
            if (wins[queryTeamIndex] + remainingGames[queryTeamIndex] < wins[i])
            {
                for (String teamName : STTeamNumber.keys())
                {
                    int tmp = STTeamNumber.get(teamName);
                    if (tmp == i)
                    {
                        certificateOfElimination.add(teamName);
                    }
                }
                return true;
            }
        }
        // else create flow network
        // but first, calculate the number of vertices needed to compute maxflow
        // games vertices = (n-1)(n-2)/2, team vertices = n-1, source + target = 2
        int amountOfVertices = (((numberOfTeams-1)*(numberOfTeams-2))/2) + (numberOfTeams - 1) + 2;
        // 0 corresponds to source vertex, amountOfVertices-1 = target vertex
        FlowNetwork flowNetwork = new FlowNetwork(amountOfVertices);
        // counter for vertex enumeration
        int counter = 0;
        // connect source vertex with games vertices
        for (int j = 0; j < numberOfTeams; j++)
        {
            for (int k = j+1; k < numberOfTeams; k++)
            {
                if (j != queryTeamIndex && j != k && k != queryTeamIndex)
                {
                    FlowEdge flowEdge = new FlowEdge(0, ++counter, gamesMatrix[j][k]);
            //        System.out.printf("Game vertex that corresponds to teams %d and %d has been added \n", j, k);
                    flowNetwork.addEdge(flowEdge);
                }
            }
        }

        // test
        /*
        System.out.println ("Printing out edges connected to source vertex");
        for (FlowEdge flowEdge : flowNetwork.adj (0))
        {
            System.out.println (flowEdge.toString());
        }
        */

        // n - shorthand for numberOfTeams
        int n = numberOfTeams;
        // the number from which we start to enumerate team vertices
        int startingVertexForTeamVertices = (((n-1)*(n-2))/2)+1;
        // current game vertex
        int currentGameVertex = 1;

        // test
        // System.out.printf ("Games vertices: %d \n",   startingVertexForTeamVertices );

        // for each game vertex, create two edges that connect both teams with the game in which they are involved
        for (int i = 0; i < numberOfTeams - 2; i++)
        {
            for (int j = i+1; j < numberOfTeams - 1; j++)
            {
                int team1Vertex = startingVertexForTeamVertices + i;
                int team2Vertex = startingVertexForTeamVertices + j;
                FlowEdge flowEdge1 = new FlowEdge(currentGameVertex, team1Vertex, Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge1);
                FlowEdge flowEdge2 = new FlowEdge(currentGameVertex,  team2Vertex, Double.POSITIVE_INFINITY);
                flowNetwork.addEdge(flowEdge2);
                //test
          //      System.out.printf("Game %d - %d is vertex %d, and it is connected to vertices %d and %d\n", i, j, currentGameVertex, team1Vertex, team2Vertex);
                currentGameVertex++;
            }
        }

        int delta = 0;
        for (int i = startingVertexForTeamVertices; i < amountOfVertices-1; i++)
        {
            // if delta = 1, it means that we have skipped through the query team
            if (i - startingVertexForTeamVertices == queryTeamIndex) delta = 1;
            int currentTeamIndex = i - startingVertexForTeamVertices + delta;
            int capacityOfTheEdge = wins[queryTeamIndex] + remainingGames [queryTeamIndex] - wins [currentTeamIndex];
            FlowEdge flowEdge = new FlowEdge(i, amountOfVertices-1, capacityOfTheEdge);
        //    System.out.printf("Edge between team %d and target vertex had been added \n", currentTeamIndex);
            flowNetwork.addEdge(flowEdge);
        }
        FordFulkerson ff = new FordFulkerson(flowNetwork, 0, amountOfVertices-1);
        boolean flag = false;
        delta = 0;
        // TODO write loop that adds teams from mincut to the certificate
        for (int i = startingVertexForTeamVertices; i < amountOfVertices-1; i++)
        {
            if (i - startingVertexForTeamVertices == queryTeamIndex) delta = 1;
            int currentTeamIndex = i - startingVertexForTeamVertices + delta;
          //  System.out.println (currentTeamIndex);
            if (ff.inCut(i))
            {
                for (String teamName : STTeamNumber.keys())
                {
                    int tmp = STTeamNumber.get(teamName);
               //     System.out.println(tmp+" "+teamName);
                    if (currentTeamIndex == tmp)
                    {
                        certificateOfElimination.add(teamName);
                    }
                }
                flag = true;
            }
        }
        // System.out.println ("weird flex but ok");
        return flag;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team)
    {
        if (team == null || STTeamNumber.get(team) == null) throw new  IllegalArgumentException();
        if (isEliminated(team)) return certificateOfElimination;
        else return null;
    }

    public static void main(String [] args)
    {
        BaseballElimination division = new BaseballElimination(args[0]);
        System.out.println(division.numberOfTeams());
        System.out.println(division.teams());
        System.out.println(division.wins("Atlanta"));
        System.out.println(division.losses("Atlanta"));
        System.out.println(division.remaining("Atlanta"));
        System.out.println(division.against("Atlanta", "New_York"));

        System.out.println(division.remaining("xd"));

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
