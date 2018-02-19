package kjd.romannumerals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.function.IntBinaryOperator;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The numeric system represented by Roman numerals originated in ancient Rome and 
 * remained the usual way of writing numbers throughout Europe well into the Late Middle 
 * Ages. Numbers in this system are represented by combinations of letters from the 
 * Latin alphabet. Roman numerals, as used today, are based on seven symbols:
 * I, V, X, L, C, D and M.
 * <p>
 * RomanNumerals are immutable.
 * <p>
 * A RomanNumeral can be created by passing an Integer or a String (containing only available
 * RomanNumeral Symbols) to the {@link #parseInt(int)} or {@link #parse(String)} method
 * respectively.
 * 
 * @author kendavidson
 *
 */
public class RomanNumeral {
	
	/**
	 * Kinds of numeral characters available within the Roman numeral system.  Roman numerals are 
	 * created by prepending or appending lower value characters (no more than three in a row).
	 * 
	 * @author kendavidson
	 *
	 * @since 0.0.1
	 */
	public static enum Symbol {
		
		/**
		 * Numeral character denoting multiples of 1000
		 */
		M(1000),
		
		/**
		 * Numeral character denoting 500 
		 */
		D(500),
		
		/**
		 * Numeral character denoting multiples of 100
		 */
		C(100),
		
		/**
		 * Numeral character denoting 50
		 */
		L(50),
		
		/**
		 * Numeral character denoting multiples of 10
		 */
		X(10),
		
		/**
		 * Numeral character denoting 5
		 */
		V(5),
		
		/**
		 * Numeral character denoting multiples of 1
		 */
		I(1);
		
		/**
		 * Decimal value associated to numeral type.
		 */
		private int integer;
		public int integer() { return this.integer; }
		
		/**
		 * Private constructor.
		 * 
		 * @param decimal
		 */
		private Symbol(int integer) {
			this.integer = integer; 
		}
	}	
	
	/**
	 * Regular expression used to validate whether a String is valid.
	 */
	private static final Pattern NUMERAL_PATTERN 
		= Pattern.compile("^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$", Pattern.CASE_INSENSITIVE);
	
	/**
	 * TreeMap of RomanNumeral Symbols by their Integer value.
	 */
	private static final TreeMap<Integer, Symbol> NUMERALS_BY_VALUE 
		= new TreeMap<Integer, Symbol>();
	
	/**
	 * TreeMap of RomanNumeral Integer values by their symbols.
	 */
	private static final TreeMap<Symbol, Integer> VALUES_BY_NUMERAL 
		= new TreeMap<Symbol, Integer>();
	
	/*
	 * Builds the appropriate TreeMaps used for conversion of values.
	 */
	static {
		Stream.of(Symbol.values())
			.forEach(numeral -> {
				NUMERALS_BY_VALUE.put(numeral.integer(), numeral);
				VALUES_BY_NUMERAL.put(numeral, numeral.integer());
			});		
	}

	/**
	 * Array of Symbols noting the RomanNumeral value.
	 */
	private Symbol[] numerals;
	
	/**
	 * Creates a new RomanNumeral using the supplied Array of Symbol objects.
	 * 
	 * @param symbols
	 */
	public RomanNumeral(Symbol[] symbols) {
		this(Arrays.stream(symbols)
				.map(s -> s.toString())
				.reduce("", (a, b) -> a + b));
	}
	
	/**
	 * Creates a new RomanNumeral using a supplied String of Symbol representations.
	 * 
	 * @param symbols
	 */
	public RomanNumeral(String symbols) {
		this.numerals = parseString(symbols);
	}
	
	/**
	 * Creates a new RomanNumeral using a supplied Integer value.
	 * 
	 * @param intValue
	 */
	public RomanNumeral(int intValue) {
		this.numerals = parseInt(intValue);
	}
	
	/**
	 * Validates whether a String is valid Roman Numeral.
	 * 
	 * @param symbols
	 * @return
	 */
	public static boolean validate(String symbols) {
		return NUMERAL_PATTERN.matcher(symbols).matches();
	}
	
	/**
	 * Validates whether an Integer is a valid Roman Numeral.
	 * @param intValue
	 * @return
	 */
	public static boolean validate(int intValue) {
		return (intValue >= 0 && intValue < 4000);
	}	
	
	/**
	 * Parses a String of RomanNumeral characters into a valid Symbols Array.  If the provided
	 * String doesn't match the required Roman Numeral format, then an Illgal Argument exception
	 * is thrown.  
	 * 
	 * @param symbols
	 * @return
	 * @throws IllegalArgumentException if any of the String of symbols cannot be converted
	 * 		to a RomanNumeral Symbol
	 */
	protected static Symbol[] parseString(String symbols) throws IllegalArgumentException {		
		if (!validate(symbols)) {
			throw new IllegalArgumentException(String.format("%s is not a valid Roman Numeral", symbols));
		}
		
		return symbols.codePoints()
			.mapToObj(i -> Symbol.valueOf(Character.toString((char) i).toUpperCase()))
			.toArray(s -> new Symbol[s]);		
	}
	
	/**
	 * Attempts to parse an Integer into a RomanNumeral.
	 * 
	 * @param intValue
	 * @return
	 */
	protected Symbol[] parseInt(int intValue) {
		if (!validate(intValue)) {
			throw new IllegalArgumentException(String.format("Only integers between 0 and 3999 are valid Roman Numeral values."));
		}
		
		if (intValue == 0) {
			return new Symbol[] {};
		}
		
		String symbols = recursiveParse(intValue, new ArrayList<String>()).stream()
			.reduce((a,b) -> a + b)
			.orElse("");
		
		return parseString(symbols);			
	}
	
	/**
	 * Recursively parse an Integer into a String of RomanNumeral symbols.  There
	 * are four steps to calculating the Roman Numerals matching each iteration
	 * of the integer value.
	 * <ul>
	 * 	<li>
	 * 		Calculate the required values: key (decimal value of the lowest Symbol), times (the number 
	 * 		of times that values Symbol is needed) and remain (the value remaining).
	 * 	</li>
	 * 	<li>
	 * 		Add a String containing the required number of Symbols required for the found value.  If this String
	 * 		happens to have 4 of the same character, we replace the the last three with the next highest Symbol.
	 * 		For example: IIII will become IX
	 * 	</li>
	 * 	<li>
	 * 		Determine if we need to add the current value to the previous value, this gets us around
	 * 		our 9[0]* values.  These are normally taken as special values in arrays, but I wanted something
	 * 		more dynamic in case we ever need a 5000 symbol.
	 * 		<ul>
	 * 			<li>900 = 500 + 400 = D + CD = CM</li>
	 * 			<li>700 = 500 + 200 = DCC (no)</li>
	 * 			<li>90 = 50 + 40 = L + XL = LX</li>
	 * 		</ul>
	 * 	</li>
	 * </ul>
	 * 
	 * @param intValue
	 * @param symbols
	 * @return
	 */
	private static ArrayList<String> recursiveParse(int intValue, ArrayList<String> symbols) {
		
		final int key = NUMERALS_BY_VALUE.floorKey(intValue);
		final int times = intValue / key;
		final int remain = intValue % (key * times);
		
		symbols.add(IntStream.range(0, times)
				.mapToObj(i -> NUMERALS_BY_VALUE.get(key).toString())
				.reduce((a,b) -> a + b)
				.map(s -> {
					if (s.length() > 3) {
						return s.substring(0, 1) + NUMERALS_BY_VALUE.ceilingEntry(key+1).getValue();
					}
					
					return s;
				})
				.get());
				
		if (symbols.size() > 1) {
			// Check for instances of 9x* values.  90, 900, etc.  In this case
			// we need to replace D, CD with CX.
			int size = symbols.size();
			String prev = symbols.get(size-2);
			String last = symbols.get(size-1);
			
			if (prev.length() == 1 && last.endsWith(prev)) {
				symbols.remove(size-1);
				symbols.remove(size-2);
				
				int prevKey = VALUES_BY_NUMERAL.get(Symbol.valueOf(prev));
				String nextNum = NUMERALS_BY_VALUE.ceilingEntry(prevKey+1).getValue().toString();
				symbols.add(last.replace(prev, nextNum));
			}
		}		
		
		if (remain > 0) {
			recursiveParse(remain, symbols);
		}
		 
		return symbols;
	}
	
	/**
	 * Converts the RomanNumeral to it's Integer value.
	 * 
	 * @return
	 */
	public int toInteger() {
		return new StringBuilder(toString())
				.reverse()
				.toString()
				.codePoints()
				.mapToObj(c -> String.valueOf((char) c))
				.mapToInt(s -> Symbol.valueOf(s).integer())
				.reduce(0, new IntBinaryOperator() {
					private int max = 0;
					
					@Override
					public int applyAsInt(int left, int right) {
						int total = left;		// Account for Identifier
						total = (right >= this.max) ? total + right : total - right;
						this.max = Integer.max(this.max, right);
						return total;
					}
				});
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Arrays.stream(numerals)
				.map(n -> n.toString())
				.reduce((a,b) -> a + b)
				.orElse("");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(numerals);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RomanNumeral other = (RomanNumeral) obj;
		if (!Arrays.equals(numerals, other.numerals))
			return false;
		return true;
	}
	
	
}
