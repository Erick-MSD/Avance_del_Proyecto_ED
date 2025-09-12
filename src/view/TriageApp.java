package view;

import dao.DBConnection;
import controller.TriageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TriageApp extends Application {
    private static TriageManager manager; // singleton simple para demo
    public static TriageManager getManager(){ return manager; }
    @Override
    public void start(Stage primaryStage) throws Exception {
        DBConnection db = new DBConnection();
        manager = new TriageManager(db);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/triage_view.fxml"));
        // Controller con inyección manual
        loader.setControllerFactory(c -> new TriageController(manager));
        Parent root = loader.load();
        primaryStage.setTitle("Gestión de Triage - Hospital");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
