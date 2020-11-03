package GUI;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;

import java.util.function.UnaryOperator;

public class MainUI extends GridPane {

    private Text widthText = new Text("Maze Width");
    private Text heightText = new Text("Maze Height");

    private boolean validHeightAndWidth = true;

    @Getter
    private boolean gameRunning = false;
    @Setter
    private long gameStartTimeMillis;

    private long gameEndTimeMillis;

    private TextField widthField = new TextField();
    @Getter
    private int mazeWidth = 10;
    private TextField heightField = new TextField();
    @Getter
    private int mazeHeight = 10;

    private ScrollPane mazeScrollPane = new ScrollPane();

    private final static int minHeight = 5;
    private final static int maxHeight = 50;
    private final static int minWidth = 5;
    private final static int maxWidth = 75;

    private final Border defaultBorder = new Border(new BorderStroke(null, null, null, null));
    private final Border invalidBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(2)));


    private UnaryOperator<TextFormatter.Change> heightFormatter = change -> {
        try{
            int height = Integer.parseInt(change.getControlNewText());
            if(height < minHeight || height > maxHeight){
                throw new Exception();
            }
            heightField.setBorder(defaultBorder);
            mazeHeight = height;
        }
        catch(Exception e){
            heightField.setTooltip(new Tooltip("Must be a positive integer between 5 and "+maxHeight));
            heightField.setBorder(invalidBorder);
        }
        validHeightAndWidth = heightField.getBorder().equals(defaultBorder) && widthField.getBorder().equals(defaultBorder);
        return change;
    };
    private UnaryOperator<TextFormatter.Change> widthFormatter = change -> {
        try{
            int width = Integer.parseInt(change.getControlNewText());
            if(width < minHeight || width > maxHeight){
                throw new Exception();
            }
            widthField.setBorder(defaultBorder);
            mazeWidth = width;
        }
        catch(Exception e){
            heightField.setTooltip(new Tooltip("Must be a positive integer between 5 and "+maxHeight));
            widthField.setBorder(invalidBorder);
        }

        validHeightAndWidth = heightField.getBorder().equals(defaultBorder) && widthField.getBorder().equals(defaultBorder);
        return change;
    };

    private BooleanBinding validMazeSettings = new BooleanBinding(){
        {
            super.bind(heightField.textProperty(), widthField.textProperty());
        }

        @Override
        protected boolean computeValue() {

            return !validHeightAndWidth;
        }
    };

    private Button createButton = new Button("Create New Maze");
    private Button scoreBoardButton = new Button("View ScoreBoard");

    public static final MainUI INSTANCE = new MainUI();

    private MainUI(){
        this.setAlignment(Pos.TOP_LEFT);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(25, 25, 25,25 ));

        createButton.disableProperty().bind(validMazeSettings);
        widthField.setTextFormatter(new TextFormatter<>( widthFormatter));
        heightField.setTextFormatter(new TextFormatter<>( heightFormatter));

        this.add(widthText, 0, 0);
        this.add(widthField, 3, 0);
        this.add(heightText, 0, 1);
        this.add(heightField, 3, 1);
        widthField.setBorder(defaultBorder);
        heightField.setBorder(defaultBorder);
        widthField.setText(Integer.toString(mazeWidth));
        heightField.setText(Integer.toString(mazeHeight));

        this.add(createButton, 0, 2, 2, 1);
        this.setHalignment(createButton, HPos.CENTER);
        this.add(scoreBoardButton, 2, 2, 2, 1);
        this.setHalignment(scoreBoardButton, HPos.CENTER);

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MazeBoard.INSTANCE.resetMaze();
                MazeBoard.INSTANCE.createMaze();
                mazeScrollPane.setContent(MazeBoard.INSTANCE);

                startMaze();
            }
        });
        scoreBoardButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                //TODO show the scoreboard ontop of the maze
            }
        });

        mazeScrollPane.setMinWidth(Screen.getPrimary().getBounds().getWidth()/2.5f);
        mazeScrollPane.setMinHeight(Screen.getPrimary().getBounds().getHeight()/3.f);
        mazeScrollPane.setContent(MazeBoard.INSTANCE);
        mazeScrollPane.setBorder(Border.EMPTY);
        mazeScrollPane.setStyle("-fx-background-color:transparent;");

        this.add(mazeScrollPane, 0, 3, 4, 4);
    }

    public void startMaze(){
        gameRunning = true;
        gameStartTimeMillis = System.currentTimeMillis();
    }

    public void endMaze() {
        gameRunning = false;
        gameEndTimeMillis = System.currentTimeMillis();
    }
}
