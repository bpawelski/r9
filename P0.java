import java.math.BigInteger;
import java.math.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;
import java.util.BitSet;
import java.util.*;
import com.googlecode.javaewah.EWAHCompressedBitmap;

public class P0 {

	public static void main(String[] args) throws CloneNotSupportedException {

		int[] arr1 = new int[]{1,2,2,2,2};
        int[] arr2 = new int[]{1,2,2,2};

		Cycle cycle1 = new Cycle(arr1);
        int[][] cycles1 = cycle1.cycles;
		int[] code1 = cycle1.code;

        Cycle cycle2 = new Cycle(arr2);
        int[][] cycles2 = cycle2.cycles;
		int[] code2 = cycle2.code;

		System.out.println("\nPermutation of B^n((1)(2)(2)(2)(2)) as orbits" + ":");
		System.out.println(Arrays.deepToString(cycle1.cycles));
        int[][] trimmed = trim(cycle1.cycles);
        System.out.println("\nNow, we take only pairs from the second row (corresponding with b,c from Lemma 13) -- the first pair is [128, 256] and the last pair is [255, 383]. Then, we subtract 128 from the first element of each pair, and 256 from the second:");
        System.out.println(Arrays.deepToString(trimmed));

        System.out.println("\nAnd the pairs describe which i-th bits in b,c are equal in each fixed point in D9 under permutation (1)(23)(45)(67)(89).");

        int[] bcArr = getElementsAtIndexOne(trimmed);
        System.out.println("\nIndeed, the second element of each subarray describes which bits in c have to be equal with the i-th bit in b:");
		System.out.println(Arrays.toString(bcArr));    
        System.out.println("\nWhich is equal to the permutation B^n((1)(23)(45)(67))" + ":");
		System.out.println(Arrays.toString(code2));

	}


    public static int[][] trim(int[][] array) {
        ArrayList<int[]> validSubArrays = new ArrayList<>();

        for (int[] subArray : array) {
            if (subArray.length == 2 && subArray[0] >= 128 && subArray[0] <= 255) {
                subArray[0] -= 128;
                subArray[1] -= 256;

                validSubArrays.add(subArray);
            }
        }

        // Konwersja ArrayList na tablicę
        int[][] result = new int[validSubArrays.size()][];
        result = validSubArrays.toArray(result);

        return result;
    }

    public static int[] getElementsAtIndexOne(int[][] array) {
        ArrayList<Integer> tempList = new ArrayList<>();

        for (int[] subArray : array) {
            if (subArray.length > 1) {
                tempList.add(subArray[1]);
            }
        }

        // Konwersja ArrayList na tablicę int[]
        int[] result = new int[tempList.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tempList.get(i);
        }

        return result;
    }
}
