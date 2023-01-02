

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			// Localisation du fichier FXML.
			URL url = getClass().getResource("./view/mainView.fxml");
			// Creation du loader.
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			// Chargement du FXML.
			Pane root = (Pane) fxmlLoader.load();
			// Création de la scène.
			Scene scene = new Scene(root, 964, 765);
			primaryStage.setScene(scene);
		} catch (IOException ex) {
			System.err.println("Erreur au chargement: " + ex);
			System.out.println(ex.getMessage());
		}
		primaryStage.setTitle("KNN - J4");
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}

}
