package utils;

public class MathFunctions {
	public static double log2 (double X) {
		if ( X != 0 )
			return Math.log(X)/Math.log(2);
		else return 0;
	}

	public static long factorial(long n) {
		if ( n == 0 || n == 1 )
			return 1;
		long prod = 1;
		for ( int i = 2; i <= n; i++ )
			prod *= i;
		return prod;
	}

	public static double NchooseX (int N, int x) {
		return (double)factorial(N)/(factorial(x) * factorial(N-x));
	}
}
