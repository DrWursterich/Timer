package util;

public enum Ressource {
	CSS("css", "css"),
	FXML("fxml", "fxml");

	private final String folder;
	private final String extension;

	Ressource(final String folder, final String extension) {
		this.folder = folder;
		this.extension = extension;
	}

	protected String getFolder() {
		return this.folder;
	}

	protected String getExtension() {
		return this.extension;
	}
}
