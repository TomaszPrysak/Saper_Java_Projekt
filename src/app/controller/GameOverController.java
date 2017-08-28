package app.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class GameOverController {

    @FXML
    private ImageView imag_game_over;

    @FXML
    private Button brn_new_game;

    @FXML
    private Button btn_exit;

    @FXML
    void exitAction(MouseEvent event) {
    	System.exit(0);
    }

    @FXML
    void newGameAction(MouseEvent event) throws IOException {
    		Stage stageGame = new Stage();
    		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/app/view/GameView.fxml"));
    		Scene sceneGame = new Scene(parent);
    		stageGame.setScene(sceneGame);
    		stageGame.setTitle("Saper - plansza gry");
    		stageGame.setResizable(false);
    		stageGame.show();
    }
}