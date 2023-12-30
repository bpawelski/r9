import java.math.BigInteger;
import java.math.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;
import java.util.BitSet;
import java.util.*;
import com.googlecode.javaewah.EWAHCompressedBitmap;

public class Alg2b {

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

		Poset poset = new Poset(cycle);
		ArrayList<EWAHCompressedBitmap> list = poset.listOfSets();

		BigInteger result = poset.cardinalityOfLevelUp(list, 2);

		System.out.println("\nNumber of fixed points in D(" + (sum+2) + ") under permutation of cycle type" + Arrays.toString(arr) + ":");
		System.out.println(result.toString() + "\n");

		}
}
