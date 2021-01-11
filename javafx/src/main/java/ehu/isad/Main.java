package ehu.isad;


import ehu.isad.controller.HasieraKud;
import ehu.isad.model.CaptchaTaula;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    private CaptchaTaula captchaTaula =null;

    private Stage stageHasiera;
    private Scene sceneHasiera;
    private Parent hasieraUI;
    private HasieraKud hasieraKud;

    private void pantailakKargatu() throws IOException {
        FXMLLoader loaderHasiera = new FXMLLoader(getClass().getResource("/hasiera.fxml"));
        hasieraUI = (Parent) loaderHasiera.load();
        hasieraKud = loaderHasiera.getController();
        hasieraKud.setMainApp(this);
        sceneHasiera = new Scene(hasieraUI);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        pantailakKargatu();

        stageHasiera = new Stage();
        stageHasiera.setScene(sceneHasiera);
        stageHasiera.show();

    }
    //main
    public static void main(String[] args) {
        Application.launch(args);
    }
}