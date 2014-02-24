package utils;

import dataCreator.Tuple;

public class Functions {
	public static boolean function1 (Tuple person) {
		if ( ( person.getValue(2) < 40 ) || ( person.getValue(2) >= 60 ) )
			return false;
		return true;
	}

	public static boolean function2 (Tuple person) {
		if ( ( ( person.getValue(2) < 40 ) && ( 50000 <= person.getValue(0) ) && ( person.getValue(0) <= 100000 ) ) || 
				( ( 40 <= person.getValue(2) ) && ( person.getValue(2) < 60 ) && ( 75000 <= person.getValue(0) ) && ( person.getValue(0) <= 125000 ) ) || 
				( ( person.getValue(2) >= 60 ) && ( 25000 <= person.getValue(0) ) && ( person.getValue(0) <= 75000 ) ) )
			return false;
		return true;
	}

	public static boolean function3 (Tuple person) {
		if ( ( ( person.getValue(2) < 40 ) && ( 0 <= person.getValue(3) ) && ( person.getValue(3) <= 1 ) ) || 
				( ( 40 <= person.getValue(2) ) && ( person.getValue(2) < 60 ) && ( 1 <= person.getValue(3) ) && ( person.getValue(3) <= 3 ) ) ||
				( ( person.getValue(2) >= 60 ) && ( 2 <= person.getValue(3) ) && ( person.getValue(3) <= 4 ) ) )
			return false;
		return true;
	}

	public static boolean function4 (Tuple person) {
		if(
				((person.getValue(2) < 40) &&
						(((0 <= person.getValue(3)) && (person.getValue(3) <= 1) && (25000 <= person.getValue(0)) && (person.getValue(0) <= 75000)) ||
								((2 <= person.getValue(3)) && (50000 <= person.getValue(0)) && (person.getValue(0) <= 100000)))
				) ||
				((40 <= person.getValue(2)) && (person.getValue(2) < 60) &&
						(((1 <= person.getValue(3)) && (person.getValue(3) <= 3) && (50000 <= person.getValue(0)) && (person.getValue(0) <= 100000)) ||
								(((person.getValue(3)==0) || (person.getValue(3)==4))&& (75000 <= person.getValue(0)) && (person.getValue(0) <= 125000)))
				) ||
				((person.getValue(2) >= 60) &&
						(((2 <= person.getValue(3)) && (person.getValue(3) <= 4) && (50000 <= person.getValue(0)) && (person.getValue(0) <= 100000)) ||
								((person.getValue(3) < 2) && (25000 <= person.getValue(0)) && (person.getValue(0) <= 75000)))
				)
		)
			return false;
		return true;
	}

	public static boolean function5 (Tuple person) {
		if (
				((person.getValue(2) < 40) &&
						(((50000 <= person.getValue(0)) && (person.getValue(0) <= 100000) && (100000 <= person.getValue(8)) && (person.getValue(8) <= 200000)) ||
						((50000>person.getValue(0) || person.getValue(0)>100000) && (200000 <= person.getValue(8)) && (person.getValue(8) <= 400000)))
						) ||
						((40 <= person.getValue(2)) && (person.getValue(2) < 60) &&
						(((75000 <= person.getValue(0)) && (person.getValue(0) <= 125000) && (200000 <= person.getValue(8)) && (person.getValue(8) <= 400000)) ||
						(((person.getValue(0) < 75000) || (person.getValue(0)>125000))&& (300000 <= person.getValue(8)) && (person.getValue(8) <= 500000)))
						) ||
						((person.getValue(2) >= 60) &&
						(((25000 <= person.getValue(0)) && (person.getValue(0) <= 75000) && (300000 <= person.getValue(8)) && (person.getValue(8) <= 500000)) ||
						((25000>person.getValue(0) || person.getValue(0)>75000) && (100000 <= person.getValue(8)) && (person.getValue(8) <= 300000)))
						)
						)
			return false;
		return true;
	}

	public static boolean function6 (Tuple person) {
		if (
				((person.getValue(2) < 40) && (50000 <= person.getValue(0)+person.getValue(1)) && (person.getValue(0)+person.getValue(1) <= 100000)) ||
				((40 <= person.getValue(2)) && (person.getValue(2) < 60) && (75000 <= person.getValue(0)+person.getValue(1)) && (person.getValue(0)+person.getValue(1) <= 125000)) ||
				((person.getValue(2) >= 60) && (25000 <= person.getValue(0)+person.getValue(1)) && (person.getValue(0)+person.getValue(1) <= 75000))
				)
			return false;
		return true;
	}

	public static boolean function7 (Tuple person) {
		if ( ( 0.67 * ( person.getValue(0) + person.getValue(1) ) - 0.2 * person.getValue(8) - 20000 ) > 0 )
			return false;
		return true;
	}

	public static boolean function8 (Tuple person) {
		if ( ( 0.67 * ( person.getValue(0) + person.getValue(1) ) - 5000 * person.getValue(3) - 20000 ) > 0 )
			return false;
		return true;
	}

	public static boolean function9 (Tuple person) {
		if ( ( 0.67 * ( person.getValue(0) + person.getValue(1) ) - 5000 * person.getValue(3) - 0.2 * person.getValue(8) - 10000 ) > 0 )
			return false;
		return true;
	}

	public static boolean function10 (Tuple person) {
		double equity;
		if ( person.getValue(7) < 20 ) equity = 0;
		else equity = 0.1 * person.getValue(6) * ( person.getValue(7) - 20 );
		
		if ( ( 0.67 * ( person.getValue(0) + person.getValue(1) ) - 5000 * person.getValue(3) - 0.2 * equity - 10000 ) > 0 )
			return false;
		return true;
	}

}
