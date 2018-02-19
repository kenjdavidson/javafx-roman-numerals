package kjd.romannumerals.fx.control;

import java.util.function.UnaryOperator;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import kjd.romannumerals.RomanNumeral;

/**
 * TextField which only allows entry of RomanNumerals symbols.
 * 
 * @author kendavidson
 *
 * @since 0.0.1
 */
public class RomanNumeralTextField extends TextField {
	
	private static final String ROMAN_NUMERAL_TEXT_FIELD_CLASS = "roman-numeral-text-field";
	
	/**
	 * Control whether Roman Numerals are displayed in upper or lower case
	 */
	private BooleanProperty uppercase = new SimpleBooleanProperty(this, "uppercase", true);
	
	/**
	 * Provides KeyPressed Event validation.  Validation ensures that the Key(s) being
	 * pressed are only those available for a RomanNumeral.  If a non RomanNumeral
	 * symbol is typed, the event is consumed and the character never reaches the screen. 
	 * Some other special characters are available:
	 * <ul>
	 * 	<li>Backspace</li>
	 * 	<li>Delete</li>
	 * 	<li>Tab</li>
	 * </ul>
	 */
	private EventHandler<KeyEvent> validateCharatersHandler = (event) -> {
		switch(event.getCode()) {
		case BACK_SPACE:
		case DELETE:
		case TAB:
			return;	
		default:
			try {
				RomanNumeral.Symbol.valueOf(event.getText().toUpperCase());
			} catch (IllegalArgumentException e) {
				event.consume();
			}		
		}
	};	
	
	/**
	 * Provides {@link TextField#textProperty()} listening used to validate that the text being 
	 * entered is a valid RomanNumeral.  If an invalid number is set, then it's reverted to 
	 * the last known value (which should always be successful).
	 */
	private ChangeListener<String> validateTextListener = (target,oldValue, newValue) -> {
		if (!oldValue.equals(newValue) && !RomanNumeral.validate(newValue)) {
			textProperty().set(oldValue);
		}
	};
	
	private ChangeListener<String> caseListener = (target,oldValue,newValue) -> {
		if (!oldValue.equals(newValue)) {
			String str = uppercase.get() ? newValue.toUpperCase() : newValue.toLowerCase();
			textProperty().set(str);
		}
	};
	
	/**
	 * Creates an empty 
	 */
	public RomanNumeralTextField() {
		super();
		initialize();
	}
	
	/**
	 * Creates a new RomanNumeralTextField, passing in an initial value.  The text is
	 * validated against the RomanNumeral allowed characters.  If successful the 
	 * text is set.
	 * 
	 * @param text
	 * 
	 * @throws IllegalArgumentException if the provided text is not a valid RomanNumeral
	 */
	public RomanNumeralTextField(String text) {
		super();		
		initialize();
	}
	
	/**
	 * Initializes the RomanNumeralTextField by setting all the appropriate filters and 
	 * listeners. 
	 */
	@FXML
	public void initialize() {
		getStyleClass().add(ROMAN_NUMERAL_TEXT_FIELD_CLASS);				
		addEventFilter(KeyEvent.KEY_PRESSED, validateCharatersHandler);
		textProperty().addListener(validateTextListener);
		textProperty().addListener(caseListener);
		
		// Change case of textProperty on the fly
		uppercaseProperty().addListener((obj,o,n) -> {
			if (n) textProperty().set(textProperty().get().toUpperCase());
			else textProperty().set(textProperty().get().toLowerCase());
		});
	}
	
	public final BooleanProperty uppercaseProperty() {
		return this.uppercase;
	}
	

	public final boolean isUppercase() {
		return this.uppercaseProperty().get();
	}
	

	public final void setUppercase(final boolean uppercase) {
		this.uppercaseProperty().set(uppercase);
	}
			
	public final void setText(final RomanNumeral roman) {
		this.setText(roman.toString());
	}
}
