package kjd.romannumerals.fx.control;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kjd.romannumerals.fx.control.RomanNumeralTextField;

@RunWith(JUnit4.class)
public class RomanNumeralTextFieldTest extends ApplicationTest {
	
	private static class TestBox extends VBox {
		public TestBox() {
			RomanNumeralTextField rnTextField = new RomanNumeralTextField();
			getChildren().add(rnTextField);
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(new Scene(new TestBox()));
		stage.show();
	}
	
	private void type_and_assert_character(String str, KeyCode code) {
		RomanNumeralTextField rn = lookup(item -> item instanceof RomanNumeralTextField).query();
		rn.setText("");
		type(code);
		assertEquals(str.toUpperCase(), rn.getText().toUpperCase());
	}	
	
	private void set_and_assert_string(String str, boolean valid) {
		RomanNumeralTextField rn = lookup(item -> item instanceof RomanNumeralTextField).query();
		rn.setText(str);
		assertEquals(valid, rn.getText().equals(str));
	}
	
	@Test	
	public void should_allow_symbol_characters() {		
		type_and_assert_character("i", KeyCode.I);		// 1
		type_and_assert_character("v", KeyCode.V);		// 5
		type_and_assert_character("x", KeyCode.X);		// 10
		type_and_assert_character("l", KeyCode.L);		// 50
		type_and_assert_character("c", KeyCode.C);		// 100
		type_and_assert_character("d", KeyCode.D);		// 500
		type_and_assert_character("m", KeyCode.M);		// 1000
	}
	
	@Test
	public void should_allow_whitespace_characters() {
		type_and_assert_character("", KeyCode.BACK_SPACE);
		type_and_assert_character("", KeyCode.DELETE);
	}
	
	@Test
	public void shouldnt_allow_non_symbol_characters() {
		type_and_assert_character("", KeyCode.A);		
		type_and_assert_character("", KeyCode.B);		
		type_and_assert_character("", KeyCode.E);		
		type_and_assert_character("", KeyCode.F);
		type_and_assert_character("", KeyCode.F);
		type_and_assert_character("", KeyCode.G);
		type_and_assert_character("", KeyCode.H);
		type_and_assert_character("", KeyCode.J);
		type_and_assert_character("", KeyCode.K);
		type_and_assert_character("", KeyCode.N);
		type_and_assert_character("", KeyCode.O);
		type_and_assert_character("", KeyCode.P);
		type_and_assert_character("", KeyCode.Q);
		type_and_assert_character("", KeyCode.R);
		type_and_assert_character("", KeyCode.S);
		type_and_assert_character("", KeyCode.T);
		type_and_assert_character("", KeyCode.U);
		type_and_assert_character("", KeyCode.W);
		type_and_assert_character("", KeyCode.Y);
		type_and_assert_character("", KeyCode.Z);	
	}
	
	@Test
	public void should_allow_valid_roman_numerals() {
		set_and_assert_string("II", true);
		set_and_assert_string("IV", true);
		set_and_assert_string("IX", true);
		set_and_assert_string("XX", true);
		set_and_assert_string("CM", true);
		set_and_assert_string("MC", true);
	}
	
	@Test
	public void should_not_allow_invalid_numerals() {
		set_and_assert_string("THE", false);
		set_and_assert_string("TEST", false);
		set_and_assert_string("FAILS", false);
	}
	
	@Test
	public void should_replace_invalid_with_previous() {
		RomanNumeralTextField rn = lookup(item -> item instanceof RomanNumeralTextField).query();
		set_and_assert_string("XIII", true);
		set_and_assert_string("FAIL", false);
		assertEquals("XIII", rn.getText());
	}
}
