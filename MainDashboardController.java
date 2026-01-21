package pl.controller;

import bl.BLFacade;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import pl.Main;

public class MainDashboardController {

    @FXML
    private Button manageBooksBtn;
    @FXML
    private Button tokenizeBtn;
    @FXML
    private Button lemmatizeBtn;
    @FXML
    private Button segmentizeBtn;
    @FXML
    private Button rootExtractBtn;
    @FXML
    private Button ngramBtn;
    @FXML
    private Button indexBtn;
    @FXML
    private Button searchBtn;
    @FXML
    private Button frequencyBtn;
    @FXML
    private Button browseBtn;
    @FXML
    private Button exitBtn;

    private bl.IBLFacade facade;

    public void setFacade(bl.IBLFacade facade) {
        this.facade = facade;
    }

    private bl.IBLFacade getFacade() {
        if (facade == null) {
            return Main.getBLFacade();
        }
        return facade;
    }

    // Generic method to open a new window and inject BLFacade if needed
    private void openWindow(String fxmlPath, String title, double width, double height, Object controllerWithFacade) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (controllerWithFacade != null) {
                // Dynamically set the BLFacade for controllers that need it
                // Note: This logic seems unused if controllerWithFacade is passed as null in
                // handle methods.
                // The handle methods below create loaders themselves.
                // But for completeness:
                if (controllerWithFacade instanceof TokenController tc)
                    tc.setFacade(getFacade());
                else if (controllerWithFacade instanceof SearchController sc)
                    sc.setFacade(getFacade());
                // Add other controllers here as needed
            } else {
                // Even if controllerWithFacade is null, we might need to get the controller
                // from loader if we want to inject facade.
                // But this openWindow method as implemented in original file loads the FXML
                // *again*?
                // Wait, original openWindow loads FXML. "controllerWithFacade" arg seems to be
                // a flag or expected instance?
                // Actually looking at original code:
                // openWindow("/pl/view/BookView.fxml", ..., null)
                // It ignores the controller if null.
                // But for handleTokenize(), it does NOT use openWindow. It manually loads.
                // So openWindow is only used for: Manage Books, NGram, Frequency.
                // NGram and Frequency controller likely need facade.
                // I should check if loader.getController() should be called here.

                Object controller = loader.getController();
                // Check specific controller types and inject facade
                if (controller instanceof NGramController ng)
                    ng.setFacade(getFacade());
                if (controller instanceof FrequencyController fc)
                    fc.setFacade(getFacade());
                // BookController?
                if (controller instanceof BookController bc)
                    bc.setFacade(getFacade());
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening " + title + ": " + e.getMessage());
        }
    }

    @FXML
    private void handleManageBooks() {
        openWindow("/pl/view/BookView.fxml", "Manage Books", 1000, 700, null);
    }

    @FXML
    private void handleTokenize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/view/TokenView.fxml"));
            Parent root = loader.load();

            // Get the controller instance created by FXMLLoader
            TokenController controller = loader.getController();
            controller.setFacade(Main.getBLFacade());

            Stage stage = new Stage();
            stage.setTitle("Tokenize Verses");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening Tokenize Verses: " + e.getMessage());
        }
    }

    @FXML
    private void handleLemmatize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/view/LemmaView.fxml"));
            Parent root = loader.load();

            // Get the controller instance created by FXMLLoader
            LemmaController controller = loader.getController();
            controller.setFacade(Main.getBLFacade());

            Stage stage = new Stage();
            stage.setTitle("Lemmatize Verses");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening Lemmatize Verses: " + e.getMessage());
        }
    }

    @FXML
    private void handleSegmentize() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/view/SegmentView.fxml"));
            Parent root = loader.load();

            // Get the controller instance created by FXMLLoader
            SegmentController controller = loader.getController();
            controller.setFacade(Main.getBLFacade());

            Stage stage = new Stage();
            stage.setTitle("Segmentize Verses");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening Segmentize Verses: " + e.getMessage());
        }
    }

    @FXML
    private void handleRootExtract() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/view/RootView.fxml"));
            Parent root = loader.load();

            // Get the controller instance created by FXMLLoader
            RootController controller = loader.getController();
            controller.setFacade(Main.getBLFacade());

            Stage stage = new Stage();
            stage.setTitle("Root Extraction");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening Root Extraction: " + e.getMessage());
        }
    }

    @FXML
    private void handleNGramSimilarity() {
        openWindow("/pl/view/NGramView.fxml", "N-Gram Similarity", 900, 700, null);
    }

    @FXML
    private void handleViewBookIndex() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/view/IndexView.fxml"));
            Parent root = loader.load();

            // Get the controller instance created by FXMLLoader
            IndexController controller = loader.getController();
            controller.setFacade(Main.getBLFacade());

            Stage stage = new Stage();
            stage.setTitle("Hierarchical Book Index");
            stage.setScene(new Scene(root, 1000, 750));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening Index: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/view/SearchView.fxml"));
            Parent root = loader.load();

            // Get the controller instance created by FXMLLoader
            SearchController controller = loader.getController();
            controller.setFacade(Main.getBLFacade());

            Stage stage = new Stage();
            stage.setTitle("Search Verses");
            stage.setScene(new Scene(root, 900, 700));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening Search: " + e.getMessage());
        }
    }

    @FXML
    private void handleBrowsing() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/view/BrowseView.fxml"));
            Parent root = loader.load();

            // Get the controller instance created by FXMLLoader
            BrowseController controller = loader.getController();
            controller.setFacade(Main.getBLFacade());

            Stage stage = new Stage();
            stage.setTitle("Browse Poetry");
            stage.setScene(new Scene(root, 900, 700));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening Browse: " + e.getMessage());
        }
    }

    @FXML
    private void handleFrequency() {
        openWindow("/pl/view/FrequencyView.fxml", "Frequency Analysis", 900, 700, null);
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
