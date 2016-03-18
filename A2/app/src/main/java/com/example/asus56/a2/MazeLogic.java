package com.example.asus56.a2;

/**
 * Created by Mir Abbas on 2/15/2016.
 */
public class MazeLogic extends Thread{

    private int maze[][];
    private int numOfRows;
    private int numOfColumns;
    private int walls[][];
    private Position source;
    private Position destination;
    private boolean sourceCell= false;
    private boolean destinationCell = false;
    private boolean isSourceCell= false;
    private boolean isDestinationCell= false;
    private boolean finish = false;

    public MazeLogic (int numOfRows, int numOfColumns){
        maze = new int[numOfRows][numOfColumns];
        source = new Position(0,0);
        destination = new Position(9,9);

    }
    public void init(int[][] walls){
        for (int i = 0; i< walls.length; i++){
            for (int j = 0; j< walls[i].length; j++){

                if(walls[i][j] == Consts.Empty)
                    maze[i][j] = Consts.Empty;
                else if (walls[i][j]== Consts.Wall)
                    maze[i][j] = Consts.Wall;
                else
                    maze[i][j] = walls[i][j];
            }
        }
    }

    public void run(){

        if(isSourceSet() && isDestinationSet()) {
            if (solve(source.getX(), source.getY())) {
                finish = true;
                MainActivity.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.getInstance().postedFinish();
                    }
                });
            } else {
                MainActivity.getInstance().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.getInstance().notSuccessfull();
                    }
                });
            }
        }
    }


    private boolean solve(int r, int c) {

//        print();
        MainActivity.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                MainActivity.getInstance().setPath(maze);
            }
        });
        try {
            Thread.sleep(400);

        }catch (Exception e){

        }
        //if it found destination
        if (maze[r][c] == Consts.Destination) {
            return true;
        }
        // mark the current cell as on the path
        maze[r][c] = Consts.VisitedPath;


        // try all available neighbours - if any of these return true then we're solved
        if (available(r, c + 1) && solve(r, c + 1)) {
            return true;
        }
        if (available(r, c - 1) && solve(r, c - 1)) {
            return true;
        }
        if (available(r + 1, c) && solve(r + 1, c)) {
            return true;
        }

        if (available(r - 1, c) && solve(r - 1, c)) {
            return true;
        }

        // nothing found
        maze[r][c] = Consts.Empty;

        return false;
    }

    // cell is available if it is in the maze and either a clear space or the
    // goal - it is not available if it is a wall or already on the current path
    private boolean available(int r, int c) {
        return r >= 0 && r < maze.length
                && c >= 0 && c < maze[r].length
                && (maze[r][c] == 0 || maze[r][c] == 3);
    }

    public boolean isFinish(){
        return finish;
    }
    public void setSource(int x , int y){
        source = new Position(x,y);
        isSourceCell = true;
    }

    public Position getSource() {
        return source;
    }


    public Position getDestination() {
        return destination;
    }

    public boolean isSourceSet(){
        return isSourceCell;
    }

    public void unSetSourceCell(){
        source = new Position(0,0);
        isSourceCell = false;
    }
    public void unSetDestinationCell(){
        destination = new Position(0,0);
        isDestinationCell = false;
    }

    public boolean isDestinationSet(){
        return isDestinationCell;
    }
    public void setDestination(int x , int y){
        destination = new Position(x,y);
        maze[x][y] = Consts.Destination;
        isDestinationCell = true;

    }

}
