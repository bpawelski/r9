import java.math.BigInteger;
import java.math.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;
import java.util.BitSet;
import java.util.*;
import com.googlecode.javaewah.EWAHCompressedBitmap;

public class P1 {

	public static void main(String[] args) throws CloneNotSupportedException {

		int[] arr1 = new int[]{3,3,3};
        int[] arr2 = new int[]{3,3};

		Cycle cycle1 = new Cycle(arr1);
        int[][] cycles1 = cycle1.cycles;
		int[] code1 = cycle1.code;

        Cycle cycle2 = new Cycle(arr2);
        int[][] cycles2 = cycle2.cycles;
		int[] code2 = cycle2.code;

		System.out.println("\nPermutation of B^n((3)(3)(3)) as orbits" + ":");
		System.out.println(Arrays.deepToString(cycle1.cycles));
        int[][] trimmed333 = trim(cycle1.cycles);
        System.out.println("\nNow, we take only triplets from the second row (corresponding with b,c,d from Lemma 16) -- the first triplet is [64, 128, 256] and the last triplet is [127, 191, 319]. Then, we subtract 64 from the first element of each triplet, 128 from the second, and 256 from the third:");
        System.out.println(Arrays.deepToString(trimmed333));

        System.out.println("\nAnd the triplets describe which i-th bits in b,c,d are equal in each fixed point in D9 under permutation (123)(456)(789).");

        int[] bcArr = getElementsAtIndexOne(trimmed333);
        System.out.println("\nIndeed, the second element of each subarray describes which bits in c have to be equal with the i-th bit in b:");
		System.out.println(Arrays.toString(bcArr));    
        System.out.println("\nWhich is equal to the permutation B^n((3)(3))" + ":");
		System.out.println(Arrays.toString(code2));

	}


    public static int[][] trim(int[][] array) {
        ArrayList<int[]> validSubArrays = new ArrayList<>();

        for (int[] subArray : array) {
            if (subArray.length == 3 && subArray[0] >= 64 && subArray[0] <= 127) {
                subArray[0] -= 64;
                subArray[1] -= 128;
                subArray[2] -= 256;

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
