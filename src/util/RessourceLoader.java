package util;

import static util.Ressource.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public interface RessourceLoader {
	public static final String PROJECT_PATH =
			System.getProperty("user.dir").replace('\\', File.separatorChar);

	public default URL loadRessourceURL(Ressource ressource, String element)
			throws MalformedURLException {
		return new URL(
				String.join(
						File.separator,
						"file:",
						RessourceLoader.PROJECT_PATH,
						ressource.getFolder(),
						element.replace('.', File.separatorChar))
				+ "." + ressource.getExtension());
	}

	public default URL loadURL(Ressource ressource)
			throws ClassNotFoundException, MalformedURLException {
		return this.loadRessourceURL(
				ressource,
				this.getClass().getPackage().getName()
						+ File.separator
						+ this.getClass().getSimpleName());
	}

	public default Parent createRessource(String element)
			throws MalformedURLException, IOException {
		Parent ressource = FXMLLoader.load(
				this.loadRessourceURL(FXML, element));
		ressource.getStylesheets().add(
				this.loadRessourceURL(
					CSS,
					element
				).toExternalForm());
		return ressource;
	}
}
