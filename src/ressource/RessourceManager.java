package ressource;

import static ressource.RessourceType.CSS;
import static ressource.RessourceType.FXML;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class RessourceManager {
	public static final String PROJECT_PATH =
			System.getProperty("user.dir").replace('\\', File.separatorChar);
	private static final HashMap<String, Parent> ressources = new HashMap<>();

	public static URL loadURL(RessourceType ressource, String element)
			throws MalformedURLException {
		return new URL(
				String.join(
						File.separator,
						"file://",
						RessourceManager.PROJECT_PATH,
						ressource.getFolder(),
						element.replace('.', File.separatorChar))
				+ "." + ressource.getExtension());
	}

	public static URL loadURL(RessourceType ressource)
			throws ClassNotFoundException, MalformedURLException {
		return RessourceManager.loadURL(
				ressource,
				RessourceManager.class.getPackage().getName()
						+ File.separator
						+ RessourceManager.class.getSimpleName());
	}

	public static Parent getRessource(String element)
			throws MalformedURLException, IOException {
		if (!RessourceManager.ressources.containsKey(element)) {
			Parent ressource = FXMLLoader.load(
					RessourceManager.loadURL(FXML, element));
			ressource.getStylesheets().add(
					RessourceManager.loadURL(CSS, element).toExternalForm());
			RessourceManager.ressources.put(element, ressource);
		}
		return RessourceManager.ressources.get(element);
	}
}
