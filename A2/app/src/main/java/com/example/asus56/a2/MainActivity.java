package com.example.asus56.a2;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button myViews[][];
    private int maze[][];
    private int walls[][];
    private GridLayout myGridLayout;
    private Button solveBtn;

    private int numOfRow;
    private int numOfCol;
    private Position source;

    private static MainActivity instance;

    private Handler handler;
    private MazeLogic mazeL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numOfCol = 10;
        numOfRow = 10;
        instance = this;
        handler = new Handler();
        walls = new int[numOfRow][numOfCol];
        maze = new int[numOfRow][numOfCol];
        mazeL = new MazeLogic(numOfRow, numOfCol);

        solveBtn = (Button) findViewById(R.id.buttonGoo);
        myGridLayout = (GridLayout)findViewById(R.id.mygrid);
        myViews = new Button[numOfRow][numOfCol];

        mazeView();

        /*
        Initial Walls if any
         */
        for (int i = 0; i< myViews.length; i++){
            for (int j = 0; j< myViews[i].length; j++){

                if(walls[i][j] == Consts.Empty) {
                    myViews[i][j].setText(""+Consts.Empty);
                    myViews[i][j].setTextSize(1);

                }
                else if (walls[i][j]== Consts.Wall) {
                    myViews[i][j].setText(""+Consts.Wall);
                    myViews[i][j].setTextSize(1);

                }
            }
        }

/*
Onclick for Maze Grid
 */
        for(int r=0; r<numOfRow; r++) {
            for (int c = 0; c < numOfCol; c++) {
                myViews[r][c].setOnClickListener(this);

            }
        }

/*
Click Listener for Solve Button
 */
        solveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button)v;
                if (btn.getText().toString().equals(getString(R.string.reset_maze))){
                    //if Reset is clicked
                    resetAll();
                    btn.setText(getString(R.string.solve_maze));

                }else if(btn.getText().toString().equals(getString(R.string.solve_maze))) {
                    //If Solve is clicked
                    mazeL.init(maze);
                    mazeL.start();

                    btn.setEnabled(false);
                    for(int r=0; r<numOfRow; r++) {
                        for (int c = 0; c < numOfCol; c++) {
                            myViews[r][c].setEnabled(false);

                        }
                    }

                }
            }
        });
    }

    /*
    Onclick For Gird
     */
    @Override
    public void onClick(View v) {

        Button btn = (Button)v;

        for (int r = 0; r< myViews.length; r++) {
            for (int c = 0; c < myViews[r].length; c++) {
                if(myViews[r][c]  == btn) {

                    //if start is not selected
                    if (!mazeL.isSourceSet() && !btn.getText().toString().equals("" + Consts.Destination)) {
                        btn.setBackgroundColor(Color.RED);
                        btn.setText("" + Consts.Source);
                        maze[r][c] = Consts.Source;
                        source = new Position(r, c);
                        mazeL.setSource(r, c);

                    }
                    //if end is selected
                    else if (!mazeL.isDestinationSet() && !btn.getText().toString().equals("" + Consts.Source)) {
                        btn.setBackgroundColor(Color.BLUE);
                        maze[r][c] = Consts.Destination;
                        btn.setText("" + Consts.Destination);
                        mazeL.setDestination(r, c);
                    }
                    //if start is selected then unselect
                    else if (mazeL.isSourceSet() && maze[r][c] == Consts.Source) {
                        btn.setBackgroundColor(Color.YELLOW);
                        btn.setText("" + Consts.Empty);
                        maze[r][c] = Consts.Empty;
                        mazeL.unSetSourceCell();
                    }
                    //if end is selected then unselect
                    else if (mazeL.isDestinationSet() && maze[r][c] == Consts.Destination) {
                        btn.setBackgroundColor(Color.YELLOW);
                        btn.setText("" + Consts.Empty);
                        maze[r][c] = Consts.Empty;
                        mazeL.unSetDestinationCell();
                    }

                    //if start and end are selected construct Walls
                    else if (mazeL.isSourceSet() && mazeL.isDestinationSet()) {
                        if (btn.getText().toString().equals("" + Consts.Empty)) {
                            btn.setBackgroundColor(Color.BLACK);
                            btn.setText("" + Consts.Wall);
                            maze[r][c] = Consts.Wall;

                        } else if (btn.getText().toString().equals("" + Consts.Wall)) {
                            btn.setBackgroundColor(Color.YELLOW);
                            btn.setText("" + Consts.Empty);
                            maze[r][c] = Consts.Empty;

                        }
                    }
                }
            }
        }
    }

    /*
    Set Path Update Posted
     */
    public void setPath(int [][]maze){
        for (int r = 0; r < numOfRow; r++) {
            for (int c = 0; c < numOfCol; c++) {
                if (maze[r][c] == Consts.VisitedPath)
                    myViews[r][c].setBackgroundColor(Color.GREEN);
            }
        }
        myViews[source.getX()][source.getY()].setBackgroundColor(Color.RED);
    }

    /*
    Reset After User Click Reset
     */
    public  void resetAll(){
        maze = new int[numOfRow][numOfCol];
        walls = new int[numOfRow][numOfCol];
        mazeL = new MazeLogic(numOfRow, numOfCol);

        for(int row = 0; row < maze.length; ++row) {
            for(int col = 0; col < maze[row].length; ++col) {
                maze[row][col]= Consts.Empty;
                myViews[row][col].setBackgroundColor(Color.YELLOW);
                myViews[row][col].setEnabled(true);
            }
        }
    }

    /*
    Finish Message
     */
    public void postedFinish(){
        Toast.makeText(getApplicationContext(), getString(R.string.successMsg), Toast.LENGTH_SHORT).show();
        solveBtn.setEnabled(true);
        solveBtn.setText(getString(R.string.reset_maze));
    }
    /*
    Failiar Message
     */

    public void notSuccessfull(){
        Toast.makeText(getApplicationContext(), getString(R.string.failMsg), Toast.LENGTH_SHORT).show();
        solveBtn.setEnabled(true);
        solveBtn.setText(getString(R.string.reset_maze));

    }
    /*
    Make Maze Grid
     */
    private void mazeView(){
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        for(int yPos=0; yPos<numOfRow; yPos++) {
            for (int xPos = 0; xPos < numOfCol; xPos++) {

                Button b = new Button(this);
                GridLayout.LayoutParams  parms = new GridLayout.LayoutParams();
                parms.setMargins(2,2,2,2);
                parms.height = screenHeight/(numOfRow+2);
                parms.width = 103;

                b.setLayoutParams(parms);
                b.setBackgroundColor(Color.YELLOW);

                myViews[yPos][xPos] = b;
                myGridLayout.addView(b);
            }
        }
    }

    public Handler getHandler(){
        return handler;
    }

    public static MainActivity getInstance(){
        return instance;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
