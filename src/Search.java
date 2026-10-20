

import java.util.*;
public class Search {

     public static void main(String[] args){

        // Location p = new Location(7,8);
        // System.out.println(p);
        // System.out.println(p.row());
        // System.out.println(p.column());
        // Location q = new Location(7,8);

        // Set<Location> visitedLocations = new HashSet<>();
        // visitedLocations.add(p);
        // System.out.println(visitedLocations.contains(q));

        // Set<int[]> visited = new HashSet<>();
        // int[] a = new int[]{5,8};
        // int[] b = new int[]{2,1};
        // int[] c = new int[]{5,8};

        // visited.add(a);

        // System.out.println(visited.contains(a));
        // System.out.println(visited.contains(b));
        // System.out.println(visited.contains(c));

        char[][] grid = {
            {'o', 'o', 'o', 'o', 'c', 'w', 'c', 'o'},
            {'w', 'o', 'o', 'w', 'w', 'c', 'w', 'o'},
            {'o', 'o', 'o', 'o', 'R', 'w', 'o', 'o'},
            {'o', 'o', 'w', 'w', 'w', 'o', 'o', 'o'},
            {'o', 'o', 'o', 'o', 'c', 'o', 'o', 'o'}
            };

            System.out.println(nearestCheese(grid));
            
     }
    /** ToDo 
     * 
     *  
    change comment to relfect that we re returning path instead of final location now.
    */
    
     /**
     * Finds the location of the nearest reachable cheese from the rat's position.
     * Returns a 2-element int array: [row, col] of the closest 'c'. If there are multiple
     * cheeses that are tied for the same shortest distance to reach, return
     * any one of them.
     * 
     * 'R' - the rat's starting position (exactly one)
     * 'o' - open space the rat can walk on
     * 'w' - wall the rat cannot pass through
     * 'c' - cheese that the rat wants to reach
     * 
     * If no rat is found, throws EscapedRatException.
     * If more than one rat is found, throws CrowdedMazeException.
     * If no cheese is reachable, throws HungryRatException
     *
     * oooocwco
     * woowwcwo
     * ooooRwoo
     * oowwwooo
     * oooocooo
     *
     * The method will return [0,4] as the nearest cheese.
     *
     * @param maze 2D char array representing the maze
     * @return int[] location of the closest cheese in row, column format
     * @throws EscapedRatException if there is no rat in the maze
     * @throws CrowdedMazeException if there is more than one rat in the maze
     * @throws HungryRatException if there is no reachable cheese
     */
     public static List<Location> nearestCheese(char[][] maze) throws EscapedRatException, CrowdedMazeException, HungryRatException {
        Location start = ratLocation(maze);
        
        Set<Location> hasBeenQeued = new HashSet<>();
        Queue<Location> q = new ArrayDeque<>();
        Map<Location, Location> prevs = new HashMap<>();
        

        q.add(start);

        while(!q.isEmpty()) {
            Location current = q.remove();
            if(hasBeenQeued.contains(current)) continue;
            hasBeenQeued.add(current);

            if(maze[current.row()][current.column()] == 'c') {
                List<Location> path = new LinkedList<>();
                Location step = current;
                while(step != null ){
                    path.addFirst(step);
                    step = prevs.get(step);
                }
                return path;
                // return path
            }

            List<Location> neighbors = possibleMoves(maze, current);
            for(Location neighbor : neighbors) {

                if(!hasBeenQeued.contains(neighbor)) {
                    hasBeenQeued.add(current);
                    prevs.put(neighbor, current);
                    q.add(neighbor);
                    }
                
            }
        }

        throw new HungryRatException(); 
    }

    public static List<Location> possibleMoves(char[][] maze, Location current ) {
        int[][] moves = new int[][] {
            {-1,0},    // UP
            {1,0},     // DOWN
            {0,-1},    // LEFT
            {0,1}    // RIGHT
        };


        List<Location> validMoves = new ArrayList<>();
        for(int[] move : moves) {
            Location possibleMove = new Location(current.row() + move[0], 
                                                 current.column() + move[1]);
            if(possibleMove.row() >= 0 && possibleMove.row() < maze.length && 
                possibleMove.column() >= 0 && possibleMove.column() < maze[0].length && 
                maze[possibleMove.row()]  [possibleMove.column()] != 'w') {
                validMoves.add(possibleMove);
            }
        }


        

        return validMoves;
    }

    public static Location ratLocation(char[][] maze) {
        Location location = null;
        for(int r = 0; r < maze.length; r++) {
            for(int c = 0; c < maze[r].length; c++) {
                if(maze[r][c] == 'R') {
                    if(location == null) {
                        location = new Location(r,c);
                    } else {
                        throw new CrowdedMazeException();
                    }
                }
            }
        }
        if(location == null) throw new EscapedRatException();
        return location;
    }
}