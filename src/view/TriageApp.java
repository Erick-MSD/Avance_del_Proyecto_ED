package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TriageApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/triage_view.fxml"));
        primaryStage.setTitle("Gestión de Triage - Hospital");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
