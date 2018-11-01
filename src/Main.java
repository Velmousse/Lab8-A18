import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {
    private Scene scene;

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

        scene = new Scene(gp);
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

    private void setImageViews() {
        boolean used[] = new boolean[9];
        for (int i = 0; i < used.length; i++)
            used[i] = false;

        int rnd = (int)(Math.random() * 9);

        for (int i = 0; i < 9; i++) {
            while (used[rnd])
                rnd = (int)(Math.random() * 9);

            used[rnd] = true;
            imageViews[i] = new ImageView(mario[rnd]);
        }
    }

    private void setGridPane(GridPane gp) {
        int i = 0;
        gp.getChildren().clear();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                gp.add(imageViews[i++], y, x);
            }
        }

        setDragAndDrop(gp);
    }

    private void setDragAndDrop(GridPane gp) {
        for (ImageView iv : imageViews) {
            iv.setOnDragDetected(event -> {
                Dragboard dragboard = iv.startDragAndDrop(TransferMode.ANY);
                ClipboardContent contenu = new ClipboardContent();
                contenu.putString("");
                dragboard.setContent(contenu);
            });

            iv.setOnDragOver(event -> event.acceptTransferModes(TransferMode.ANY));

            iv.setOnDragDropped(event -> {
                ImageView source = (ImageView) event.getGestureSource();
                Image temp = iv.getImage();

                iv.setImage(source.getImage());
                source.setImage(temp);

                event.setDropCompleted(true);
            });

            iv.setOnDragDone(event -> {
                if (checkIfDone(gp)) {
                    Alert alerte = new Alert(Alert.AlertType.CONFIRMATION);
                    alerte.setTitle("Victoire!");
                    alerte.setHeaderText("Félicitations! Vous avez gagné.");
                    alerte.setContentText("Voulez-vous rejouer?");
                    ButtonType ok = alerte.showAndWait().get();
                    if (ok == ButtonType.OK) {
                        setImageViews();
                        setGridPane(gp);
                    }
                    else System.exit(0);
                }
            });
        }
    }

    private boolean checkIfDone(GridPane gp) {
        ArrayList<ImageView> nodes = new ArrayList<>();

        for (Node node : gp.getChildren()) {
            nodes.add((ImageView) node);
        }

        for (int i = 0; i < 9; i++) {
            if (!(mario[i] == nodes.get(i).getImage())) return false;
        }

        return true;
    }
}