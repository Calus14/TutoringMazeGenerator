import GUI.MainUI;
import GUI.MazeBoard;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MazeGame extends Application {

    public static void main(String[] args){
        launch(args);
    }

    public static Scene mainScene;

    //Key interceptor for the arrow keys, will only fire if the game is running
    public static EventHandler<KeyEvent> keyListener = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if ( !MainUI.INSTANCE.isGameRunning() ) {
                return;
            }

            if(event.getCode() == KeyCode.W || event.getCode() == KeyCode.S ||
                    event.getCode() == KeyCode.D || event.getCode() == KeyCode.A) {
                MazeBoard.INSTANCE.movePlayer(event.getCode());
            }
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception {

        mainScene = new Scene(MainUI.INSTANCE,
                Screen.getPrimary().getBounds().getWidth()/2.f,
                Screen.getPrimary().getBounds().getHeight()/2.f);

        mainScene.setOnKeyPressed(keyListener);
        primaryStage.setTitle("Usage Monitor");
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }
}
