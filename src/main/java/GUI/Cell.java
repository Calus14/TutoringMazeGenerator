package GUI;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Cell extends BorderPane {

    public enum WallDirection{
        NORTH,
        EAST,
        SOUTH,
        WEST
    };

    // Each wall can either exist or not exist
    @Getter
    private BooleanProperty hasNorth = new SimpleBooleanProperty(true);
    @Getter
    private BooleanProperty hasEast = new SimpleBooleanProperty(true);
    @Getter
    private BooleanProperty hasSouth = new SimpleBooleanProperty(true);
    @Getter
    private BooleanProperty hasWest = new SimpleBooleanProperty(true);

    public static final Background tanBackground = new Background(new BackgroundFill(Color.TAN, CornerRadii.EMPTY, Insets.EMPTY));
    public static final Background blueBackground = new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
    public static final Background greenBackground = new Background( new BackgroundFill(Color.LIMEGREEN, CornerRadii.EMPTY, Insets.EMPTY));

    private String borderStyle;
    private ChangeListener<Boolean> wallChangeListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            calculateStyle();
            setStyle(borderStyle);
        }
    };

    public Cell(int col, int row){
        super();
        this.setBackground(tanBackground);

        hasNorth.addListener( new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                calculateStyle();
                setStyle(borderStyle);
            }
        });
        hasEast.addListener( new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                calculateStyle();
                setStyle(borderStyle);
            }
        });
        hasSouth.addListener( new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                calculateStyle();
                setStyle(borderStyle);
            }
        });
        hasWest.addListener( new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                calculateStyle();
                setStyle(borderStyle);
            }
        });

        this.calculateStyle();
        this.setStyle(this.borderStyle);
    }

    private void calculateStyle(){
        int northWidth, eastWidth, southWidth, westWidth;
        northWidth = (hasNorth.get())  ? northWidth = 2 : 0;
        eastWidth = (hasEast.get())  ? eastWidth = 2 : 0;
        southWidth = (hasSouth.get())  ? southWidth = 2 : 0;
        westWidth = (hasWest.get())  ? westWidth = 2 : 0;
        this.borderStyle = "-fx-border-width: "+northWidth+" "+eastWidth+" "+southWidth+" "+westWidth+"; -fx-border-color: black;";
    }

    // Throws an exception if the wall was already deleted...
    public void deleteWall(WallDirection direction) throws Exception{
        switch(direction){
            case NORTH:
                if(this.hasNorth.get())
                    this.hasNorth.setValue(false);
                break;
            case EAST:
                if(this.hasEast.get())
                    this.hasEast.setValue(false);
                break;
            case SOUTH:
                if(this.hasSouth.get())
                    this.hasSouth.setValue(false);
                break;
            case WEST:
                if(this.hasWest.get())
                    this.hasWest.setValue(false);
                break;
        }
    }

}
