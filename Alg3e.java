import java.math.BigInteger;
import java.math.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;
import java.util.BitSet;
import java.util.*;
import com.googlecode.javaewah.EWAHCompressedBitmap;

public class Alg3e {

	public static void main(String[] args) throws CloneNotSupportedException {

		int[] arr = new int[]{3,3};

		Cycle cycle = new Cycle(arr);

		Poset poset = new Poset(cycle);
		
		ArrayList<EWAHCompressedBitmap> list = poset.listOfSets();

		System.out.println("Number of fixed points (D(9) under permutation of input variables of cycle type (3,3,3) is: " + poset.cardinalityOf333(list).toString());

	}
}
