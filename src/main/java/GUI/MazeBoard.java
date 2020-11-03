package GUI;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Screen;
import lombok.Setter;

import java.util.*;

public class MazeBoard extends GridPane {

    private Cell [][] mazeCells;
    private Set<Cell> connectedCells;
    @Setter
    private int height;
    @Setter
    private int width;

    private int playerCol;
    private int playerRow;

    private static final double squareSize = 14.d;

    // Forcing the start and end to be on the left and right side, the height of where will be random though
    private int startRow, endRow;

    public static final MazeBoard INSTANCE = new MazeBoard();

    private MazeBoard(){

    }

    public void createMaze(){
        startRow = (int)(Math.random()*height);
        endRow = (int)(Math.random()*height);
        // The runtime JVM usually is pretty good with garbage collection which will trigger JavaFxBinding to unrender and re-render
        mazeCells = new Cell[width][height];
        connectedCells = new HashSet<Cell>();

        for( int row = 0; row < height; row++){
            for( int col = 0; col < width; col++){
                Cell newCell = new Cell(col, row);
                mazeCells[col][row] = newCell;
                this.add(newCell, col, row);
            }
        }

        this.makePath();

        this.playerCol = 0;
        this.playerRow = startRow;
        mazeCells[playerCol][playerRow].setBackground(Cell.greenBackground);

        // Make sure they are centered and look pretty
        for (int i = 0; i < width; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(squareSize, squareSize, squareSize, Priority.NEVER, HPos.CENTER, true));
        }
        for (int i = 0; i < height; i++) {
            this.getRowConstraints().add(new RowConstraints(squareSize, squareSize, squareSize, Priority.NEVER, VPos.CENTER, true));
        }
    }

    public void resetMaze(){
        this.getChildren().clear();
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();
        height = MainUI.INSTANCE.getMazeHeight();
        width = MainUI.INSTANCE.getMazeWidth();
    }

    // Algorithm will take the starting point, then do a recursive search and use a disjoint set to see if the cell is already reachable
    private void makePath(){
        Cell startCell = this.mazeCells[0][startRow];
        Cell endCell = this.mazeCells[width-1][endRow];
        try {
            startCell.deleteWall(Cell.WallDirection.WEST);
            endCell.deleteWall(Cell.WallDirection.EAST);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        recursivePathMake(0, startRow);
    }


    private void recursivePathMake(int column, int row)
    {
        Cell visitedCell = this.mazeCells[column][row];
        this.connectedCells.add(visitedCell);

        // Used with the shuffle functor to randomize how we delete walls in the maze
        List<Cell.WallDirection> directionList = new ArrayList<>();
        directionList.add(Cell.WallDirection.NORTH);
        directionList.add(Cell.WallDirection.EAST);
        directionList.add(Cell.WallDirection.SOUTH);
        directionList.add(Cell.WallDirection.WEST);
        Collections.shuffle(directionList);

        for(Cell.WallDirection direction : directionList){
            try {
                if (direction == Cell.WallDirection.NORTH) {
                    // we cant go north if we are on the top
                    if (row <= 0) {
                        continue;
                    }
                    Cell northCell = this.mazeCells[column][row - 1];
                    // is this already reachable?
                    if (this.connectedCells.contains(northCell)) {
                        continue;
                    }
                    // It is currently not reachable so destroy the wall, add it to our disjoint set and recurse
                    visitedCell.deleteWall(Cell.WallDirection.NORTH);
                    northCell.deleteWall(Cell.WallDirection.SOUTH);

                    recursivePathMake(column, row-1);
                } else if (direction == Cell.WallDirection.EAST) {
                    // we cant go east if we are on the last column
                    if (column >= width-1) {
                        continue;
                    }
                    Cell eastCell = this.mazeCells[column+1][row];
                    // is this already reachable?
                    if (this.connectedCells.contains(eastCell)) {
                        continue;
                    }
                    // It is currently not reachable so destroy the wall, add it to our disjoint set and recurse
                    visitedCell.deleteWall(Cell.WallDirection.EAST);
                    eastCell.deleteWall(Cell.WallDirection.WEST);

                    recursivePathMake(column+1, row);
                } else if (direction == Cell.WallDirection.SOUTH) {
                    // we cant go north if we are on the top
                    if (row >= height-1) {
                        continue;
                    }
                    Cell southCell = this.mazeCells[column][row+1];
                    // is this already reachable?
                    if (this.connectedCells.contains(southCell)) {
                        continue;
                    }
                    // It is currently not reachable so destroy the wall, add it to our disjoint set and recurse
                    visitedCell.deleteWall(Cell.WallDirection.SOUTH);
                    southCell.deleteWall(Cell.WallDirection.NORTH);

                    recursivePathMake(column, row+1);
                } else if (direction == Cell.WallDirection.WEST) {
                    // we cant go north if we are on the top
                    if (column <= 0) {
                        continue;
                    }
                    Cell westCell = this.mazeCells[column-1][row];
                    // is this already reachable?
                    if (this.connectedCells.contains(westCell)) {
                        continue;
                    }
                    // It is currently not reachable so destroy the wall, add it to our disjoint set and recurse
                    visitedCell.deleteWall(Cell.WallDirection.WEST);
                    westCell.deleteWall(Cell.WallDirection.EAST);

                    recursivePathMake(column-1, row);
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    //Helper movement functions
    private boolean playerCanMoveUp(){
        if(playerRow == 0 || mazeCells[playerCol][playerRow].getHasNorth().getValue())
            return false;
        return true;
    }
    private boolean playerCanMoveDown(){
        if(playerRow == (this.height-1) || mazeCells[playerCol][playerRow].getHasSouth().getValue())
            return false;
        return true;
    }
    private boolean playerCanMoveRight(){
        if(playerCol == (this.width-1) || mazeCells[playerCol][playerRow].getHasEast().getValue())
            return false;
        return true;
    }
    private boolean playerCanMoveLeft(){
        if(playerCol == 0 || mazeCells[playerCol][playerRow].getHasWest().getValue())
            return false;
        return true;
    }

    // If we move to a cell that was not occupied (tan background, then our old cell will be marked as visited and the new cell will be marked
    // as the active cell as green
    // If we "take back a move"... ie move to a square that was previously occupied, then we set the cell we were on to tan.
    private void changeCellColor(int oldCol, int oldRow, int newCol, int newRow){
        Cell oldCell = this.mazeCells[oldCol][oldRow];
        Cell newCell = this.mazeCells[newCol][newRow];

        // Were moving forward to a new cell
        if(newCell.getBackground().equals(Cell.tanBackground)){
            newCell.setBackground(Cell.greenBackground);
            oldCell.setBackground(Cell.blueBackground);
        }
        else if(newCell.getBackground().equals(Cell.blueBackground)){
            newCell.setBackground(Cell.greenBackground);
            oldCell.setBackground(Cell.tanBackground);
        }

        // If the player reaches the final cell then he has beat the game
        if(newCol == this.width-1 && newRow == this.endRow){
            MainUI.INSTANCE.endMaze();
        }
    }

    public void movePlayer(KeyCode code) {
        switch( code ){
            case W :
                if(playerCanMoveUp()){
                    changeCellColor(playerCol, playerRow, playerCol, playerRow-1);
                    playerRow -= 1;
                }
                break;
            case D:
                if(playerCanMoveRight()){
                    changeCellColor(playerCol, playerRow, playerCol+1, playerRow);
                    playerCol += 1;
                }
                break;
            case S:
                if(playerCanMoveDown()){
                    changeCellColor(playerCol, playerRow, playerCol, playerRow+1);
                    playerRow += 1;
                }
                break;
            case A:
                if(playerCanMoveLeft()){
                    changeCellColor(playerCol, playerRow, playerCol-1, playerRow);
                    playerCol -= 1;
                }
                break;
        }
    }
}
