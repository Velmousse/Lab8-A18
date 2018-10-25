import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.sound.sampled.Clip;

public class Main extends Application {
    private Image[] mario = {new Image("Images/mario0.jpg"), new Image("Images/mario1.jpg"),
            new Image("Images/mario2.jpg"), new Image("Images/mario3.jpg"), new Image("Images/mario4.jpg"),
            new Image("Images/mario5.jpg"), new Image("Images/mario6.jpg"), new Image("Images/mario7.jpg"),
            new Image("Images/mario8.jpg")};

    private ImageView[] imageViews = new ImageView[9];

    public static void main(String[] args) { launch(args); }

    public void start(Stage stage) {
        setImageViews();

        GridPane gp = new GridPane();
        setGridPane(gp);

        Scene scene = new Scene(gp);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.M && event.isControlDown())
                setGridPane(gp);
        });

        stage.setResizable(false);
        stage.setHeight(603);
        stage.setWidth(603);
        stage.setTitle("Casse-tête");
        stage.setScene(scene);
        stage.show();
    }

    private void setImageViews() {  //Ajouter d'autres puzzles plus tard
        for (int i = 0; i < mario.length; i++)
            imageViews[i] = new ImageView(mario[i]);
    }

    private void setGridPane(GridPane gp) {
        gp.getChildren().clear();

        boolean used[] = new boolean[9];
        for (int i = 0; i < used.length; i++)
            used[i] = false;

        int rnd = (int)(Math.random() * 9);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                while (used[rnd])
                    rnd = (int)(Math.random() * 9);

                used[rnd] = true;
                gp.add(imageViews[rnd], x, y);
            }
        }

        setDragAndDrop(gp);
    }

    private void setDragAndDrop(GridPane gp) {
        for (ImageView iv : imageViews) {
            iv.setOnDragDetected(event -> {
                Dragboard dragboard = iv.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent contenu = new ClipboardContent();
                contenu.putString("");
                dragboard.setContent(contenu);

            });

            iv.setOnDragDropped(event -> {
                event.setDropCompleted(true);
            });
        }
    }

    private Node getNodeFromGridPane(GridPane gp, int x, int y) {
        for (Node node : gp.getChildren())
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y)
                return node;
        return null;
    }
}
