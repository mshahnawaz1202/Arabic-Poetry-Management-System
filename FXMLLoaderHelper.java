package pl.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class FXMLLoaderHelper {
    
    /**
     * Load an FXML file and return both the root node and controller
     */
    public static <T> FXMLLoadResult<T> load(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            FXMLLoaderHelper.class.getResource("/fxml/" + fxmlPath)
        );
        Parent root = loader.load();
        T controller = loader.getController();
        return new FXMLLoadResult<>(root, controller);
    }
    
    /**
     * Load an FXML file and create a new stage with it
     */
    public static <T> Stage loadStage(String fxmlPath, String title) throws IOException {
        FXMLLoadResult<T> result = load(fxmlPath);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(result.getRoot()));
        return stage;
    }
    
    /**
     * Load an FXML file and create a new stage with custom size
     */
    public static <T> Stage loadStage(String fxmlPath, String title, 
                                      double width, double height) throws IOException {
        FXMLLoadResult<T> result = load(fxmlPath);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(result.getRoot(), width, height));
        return stage;
    }
    
    /**
     * Result class containing both the root node and controller
     */
    public static class FXMLLoadResult<T> {
        private final Parent root;
        private final T controller;
        
        public FXMLLoadResult(Parent root, T controller) {
            this.root = root;
            this.controller = controller;
        }
        
        public Parent getRoot() {
            return root;
        }
        
        public T getController() {
            return controller;
        }
    }
}