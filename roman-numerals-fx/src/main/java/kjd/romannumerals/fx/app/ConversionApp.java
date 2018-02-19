package kjd.romannumerals.fx.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kjd.romannumerals.fx.control.ConversionPane;

/**
 * Roman numeral conversion tool.
 * 
 * @author kendavidson
 *
 */
public class ConversionApp extends Application {
	
	/**
	 * Launches the ConversionTool {@link Application}
	 * @param args
	 */
    public static void main( String[] args )
    {
        Application.launch(ConversionApp.class, args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene(new ConversionPane(10));
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}
}
