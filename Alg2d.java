import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class Alg2d {

    private static long[] dn = {0, 1, 279, 1335, 4959, 6015, 32767, 65535, 65537, 65815, 66871, 70495, 71551, 98303, 131071, 18284823, 18285879, 18289503, 18290559, 18317311, 18350079, 87491895, 87496575, 87523327, 87556095, 324997983, 324999039, 325025791, 325058559, 394205055, 394231807, 394264575, 2147450879, 2147483647, 4294967295L};
    private static int dsize = dn.length;
    private static boolean[][] r = new boolean[dsize][dsize];
    private static long[][] re = new long[dsize][dsize];
    private static Map<Long, Integer> TabMap = new HashMap<>();
    private static BitSet[] cols = new BitSet[dsize];
    private static BigInteger result = new BigInteger("0");
    private static BigInteger[] aResults = new BigInteger[dsize];


    public static void main(String[] args) {

        for (int i = 0; i < dsize; i++) {
            TabMap.put(dn[i], i);
            cols[i] = new BitSet();
        }

        long start = System.currentTimeMillis();

        for (int x = 0; x < dsize; x++) {
        	aResults[x] = new BigInteger("0"); 
            for (int y = 0; y < dsize; y++) {
                if ((dn[x] | dn[y]) == dn[y]) {
                    r[x][y] = true;
                    cols[y].set(x);
                }
            }
        }

        BitSet tempCol;
        BitSet tempRow;
        for (int row = 0; row < dsize; row++) {
            BitSet currentRow = getRowBitSet(r, row);
            for (int col = row; col < dsize; col++) {
                if (!r[row][col]) continue;
                tempRow = (BitSet) currentRow.clone();
                tempCol = cols[col];
                tempRow.and(tempCol);
                re[row][col] = tempRow.cardinality();
            }
        }
        
        IntStream.range(0, dsize).parallel().forEach(
                a -> {
            for (int b = 0; b < dsize; b++) {
                for (int c = 0; c < dsize; c++) {
                    for (int d = 0; d < dsize; d++) {
                        for (int e = 0; e < dsize; e++) {
                            for (int f = 0; f < dsize; f++) {
                                aResults[a] = aResults[a].add(Hh(a, b, c, d, e, f, re));
                             }
                         }
                     }
                 }
             }
       });
        
        for (int i = 0; i < dsize; i++){
				result = result.add(BigInteger.valueOf(aResults[i].longValue()));
        }
	long end = System.currentTimeMillis();
        float time = (end - start) / 1000.0F;
        System.out.println("Execution time: " + time + "s");
        System.out.println("Result is: " + result.toString());

    }

    private static BitSet getRowBitSet(boolean[][] matrix, int row) {
        BitSet output = new BitSet();
        boolean[] array = matrix[row];
        for (int i = 0; i < array.length; i++) {
            if (array[i]) output.set(i);
        }
        return output;
    }

    private static BigInteger H(int a, int b, int c, int d, int e, int f, long[][] re) {
        long H = 0;
        int index1 = TabMap.get(dn[a] | dn[b] | dn[d]);
        int index2 = TabMap.get(dn[a] | dn[c] | dn[e]);
        int index3 = TabMap.get(dn[b] | dn[c] | dn[f]);
        int index4 = TabMap.get(dn[d] | dn[e] | dn[f]);

        for (int u = TabMap.get(dn[a] | dn[b] | dn[c] | dn[d] | dn[e] | dn[f]); u < dsize; u++) {
            H += re[index1][u]
                    * re[index2][u]
                    * re[index3][u]
                    * re[index4][u];
        }

        return BigInteger.valueOf(H);
    }

    private static BigInteger h(int a, int b, int c, int d, int e, int f, long[][] re) {
        long H = 0;
        int index1 = TabMap.get(dn[a] & dn[b] & dn[c]);
        int index2 = TabMap.get(dn[a] & dn[d] & dn[e]);
        int index3 = TabMap.get(dn[b] & dn[d] & dn[f]);
        int index4 = TabMap.get(dn[c] & dn[e] & dn[f]);

        for (int u = TabMap.get(dn[a] & dn[b] & dn[c] & dn[d] & dn[e] & dn[f]); u >=0; u--) {
            H += re[u][index1]
                    * re[u][index2]
                    * re[u][index3]
                    * re[u][index4];
        }

        return BigInteger.valueOf(H);
    }

    private static BigInteger Hh(int a, int b, int c, int d, int e, int f, long[][] re) {
        return H(a, b, c, d, e, f, re).multiply(h(a, b, c, d, e, f, re));
    }

}
