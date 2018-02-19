package kjd.romannumerals.fx.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import kjd.romannumerals.RomanNumeral;

/**
 * ConversionPane provides all the UI elements responsible for entering both Integer and
 * Roman Numeral values and converting between them.  The UI Panel contains two TextFields
 * (Roman Numeral and Integer) and two Buttons (Convert and Reset)
 * 
 * @author kendavidson
 *
 */
public class ConversionPane extends VBox {
	
	@FXML
	private RomanNumeralTextField tfRomanNumeral = new RomanNumeralTextField();
	
	@FXML
	private TextField tfInteger = new TextField();
	
	@FXML
	private CheckBox cbUppercase = new CheckBox("Uppercase");
	
	public ConversionPane() {
		super();
		init();
	}
	
	public ConversionPane(double spacing) {
		super(spacing);		
		init();
	}

	public ConversionPane(Node... parent) {
		super(parent);
		init();
	}
	
	public ConversionPane(double spacing, Node... parent) {
		super(spacing, parent);
		init();
	}

	protected void init() {
		setPadding(new Insets(10.0, 10.0, 10.0, 10.0));		
						
		tfRomanNumeral.setPromptText("Roman Numeral");
		tfRomanNumeral.setMaxWidth(Double.MAX_VALUE);
		tfRomanNumeral.setMinWidth(200);
		tfRomanNumeral.uppercaseProperty().bind(cbUppercase.selectedProperty());
		tfRomanNumeral.textProperty().addListener((obj,o,n) -> {
			if (o != n) {
				tfInteger.setText(Integer.toString(new RomanNumeral(n).toInteger()));
			}
		});
		
		tfInteger.setPromptText("Number");
		tfInteger.setMaxWidth(Double.MAX_VALUE);
		tfInteger.setMinWidth(200);
		tfInteger.textProperty().addListener((obj,o,n) -> {
			if (!n.matches("[0-9]+")) {
				tfInteger.setText(n.replaceAll("[^0-9]", ""));
			}
			
			int val = tfInteger.getText().isEmpty() ? 0 : Integer.parseInt(n);
			tfRomanNumeral.setText(new RomanNumeral(val));
		});
		
		getChildren().addAll(cbUppercase, tfRomanNumeral, tfInteger);				
	}
	
	
	public StringProperty romanNumeralProperty() {
		return tfRomanNumeral.textProperty();
	}
	
	public String getRomanNumeral() {
		return romanNumeralProperty().get();
	}
	
	public void setRomanNumeral(String numeral) {
		romanNumeralProperty().set(numeral);
	}
	
	public StringProperty integerProperty() {
		return tfInteger.textProperty();
	}
	
	public String getInteger() {
		return integerProperty().get();
	}
	
	public void setInteger(String integer) {
		integerProperty().set(integer);
	}	
	
	public void setInteger(int integer) {
		integerProperty().set(Integer.toString(integer));
	}
}
