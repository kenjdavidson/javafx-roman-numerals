package kjd.romannumerals;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RomanNumeralTest {	
	
	@Rule
	public ExpectedException numberFormat = ExpectedException.none();
	
	@Rule
	public ExpectedException illegalArgument = ExpectedException.none();
	
	@Test
	public void numeralToDecimal_convertsCorrectly() {		
		assertEquals(1, new RomanNumeral("I").toInteger());		
		assertEquals(4, new RomanNumeral("IV").toInteger());
		assertEquals(6, new RomanNumeral("VI").toInteger());		
		assertEquals(40, new RomanNumeral("XL").toInteger());
		assertEquals(60, new RomanNumeral("LX").toInteger());				
		assertEquals(900, new RomanNumeral("CM").toInteger());
		assertEquals(1100, new RomanNumeral("MC").toInteger());		
		assertEquals(2000, new RomanNumeral("MM").toInteger());
	}
	
	@Test
	public void invalidNumberValidation_returnsFalse() {
		assertFalse(RomanNumeral.validate(-1));
		assertFalse(RomanNumeral.validate(4001));
	}
	
	@Test
	public void invalidNumeralValidation_returnsFalse() {
		assertFalse(RomanNumeral.validate("IIIII"));
		assertFalse(RomanNumeral.validate("MDD"));
	}
	
	@Test
	public void negativeDecimal_throwsException() {
		illegalArgument.expect(IllegalArgumentException.class);
		new RomanNumeral(-1);
	}
	
	@Test
	public void highDecimal_throwsException() {
		illegalArgument.expect(IllegalArgumentException.class);
		new RomanNumeral(4001);
	}
	
	@Test
	public void validDecimal_parsesCorrectly() {
		assertEquals("I", new RomanNumeral(1).toString());
		assertEquals("IV", new RomanNumeral(4).toString());
		assertEquals("VI", new RomanNumeral(6).toString());		
		assertEquals("XL", new RomanNumeral(40).toString());
		assertEquals("LX", new RomanNumeral(60).toString());				
		assertEquals("CM", new RomanNumeral(900).toString());
		assertEquals("MC", new RomanNumeral(1100).toString());		
		assertEquals("MM", new RomanNumeral(2000).toString());
		
		// Lets get extravagant
		assertEquals("DXCII", new RomanNumeral(592).toString());
		assertEquals("MMMDCCXXXII", new RomanNumeral(3732).toString());
	}
	
	@Test
	public void equalNumerals_returnsTrue() {
		RomanNumeral first = new RomanNumeral("I");
		RomanNumeral second = new RomanNumeral("I");
		
		assertTrue(first.equals(second));
		assertTrue(second.equals(first));
	}
	
	@Test
	public void nonEqualNumerals_returnFalse() {
		RomanNumeral first = new RomanNumeral("I");
		RomanNumeral second = new RomanNumeral("V");
		
		assertFalse(first.equals(second));
		assertFalse(second.equals(first));
	}	
	
}

