import java.math.BigInteger;
import java.math.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;
import java.util.BitSet;
import java.util.*;
import com.googlecode.javaewah.EWAHCompressedBitmap;

public class Alg1a {

	public static void main(String[] args) throws CloneNotSupportedException {

		int[] arr = new int[args.length];
		int sum = 0;

		for (int i = 0; i < args.length; i++) {
			arr[i] = Integer.valueOf(args[i]);
			sum += arr[i];
		}

		Cycle cycle = new Cycle(arr);

		int[][] cycles = cycle.cycles;

		int[] code = cycle.code;

		System.out.println("\nCycle type " + Arrays.toString(arr) + ":");

		System.out.println("\nPermutation of B^n" + ":");
		System.out.println(Arrays.toString(code));

		System.out.println("\nPermutation of B^n as orbits" + ":");
		System.out.println(Arrays.deepToString(cycle.cycles));

		System.out.println("\nPermutation of B^n as rows of Hasse diagram of orbits" + ":");
		System.out.println(Arrays.deepToString(cycle.buildCycleHasseRows()));

		Poset poset = new Poset(cycle);
		
		ArrayList<EWAHCompressedBitmap> list = poset.listOfSets();

		System.out.println("\nNumber of fixed points in D(" + sum + ") under permutation of cycle type" + Arrays.toString(arr) + ":");
		System.out.println(list.size() + "\n");
}

}