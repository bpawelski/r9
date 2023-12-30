import java.math.BigInteger;
import java.math.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;
import java.util.BitSet;
import java.util.*;
import com.googlecode.javaewah.EWAHCompressedBitmap;

public class Alg1b {

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

		//ArrayList<EWAHCompressedBitmap> list = poset.getLevelUpSet(poset.listOfSets());

		//BigInteger result2 = poset.cardinalityOfLevelUp(poset.listOfSets(), 2);


		//System.out.println(poset.cardinality());

		System.out.println("\nNumber of fixed points in D(" + sum + ") under permutation of cycle type" + Arrays.toString(arr) + ":");
		System.out.println(list.size() + "\n");

		//Uncomment if you want to get a list of fixed points!
		System.out.println("List of fixed pints in D(" + sum + ") under permutation of cycle type" + Arrays.toString(arr) + ":");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}

		//System.out.println(result2.toString());
}

}