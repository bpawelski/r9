import java.util.*;
import java.util.stream.IntStream;
import java.io.*;
import java.math.BigInteger;
import com.googlecode.javaewah.EWAHCompressedBitmap;
import com.googlecode.javaewah.datastructure.BitSet;
import java.util.stream.IntStream;


public class Poset {
	Cycle cycle;
	int[][] hasseRows;
	int[] smallest;
	int[] hasseFirst;
	HashMap requirements;

   public Poset(Cycle cycle) {
   	this.cycle = cycle;
    this.hasseRows = cycle.buildCycleHasseRows();
    this.smallest = cycle.smallestsInCycles;
    this.hasseFirst = buildHasseFirst();
    this.requirements = buildRequirements();
   }

   private int[] buildHasseFirst() {
   	int[] output = new int[0];
   	int counter = 0;
   	for (int i = 0; i < hasseRows.length; i++) {
   		output = addElementToIntArray(output, counter);
   		counter += hasseRows[i].length;
   	}

   	return output;
   }

    private HashMap<Integer, int[]> buildRequirements() {

   	HashMap<Integer, int[]> output = new HashMap<>();

   	for (int i = 1; i < hasseRows.length; i++) {
   		for (int j = 0; j < hasseRows[i].length; j++) {
   			int[] r = new int[0];

   			for (int nex = 0; nex < hasseRows[i-1].length; nex++) {
   				if ((hasseRows[i][j] | hasseRows[i-1][nex]) == hasseRows[i-1][nex]) {
   					r = addElementToIntArray(r, hasseRows[i-1][nex]);
   					continue;
   				} else if (requirementsForWholeArrAreSatisfied(hasseRows[i][j], hasseRows[i-1][nex])) {
   					r = addElementToIntArray(r, hasseRows[i-1][nex]);
   				}
   			}
   			output.put(hasseRows[i][j], r);
   		}
   	}
   		return output;
   	}

   	private boolean requirementsForWholeArrAreSatisfied(int firstKey, int secondKey) {
   		int[] arr = findArrForKey(secondKey);

   		for (int i = 0; i < arr.length; i++) {
   			if ((firstKey | arr[i]) == arr[i]) return true;
   		}

   		return false;
   	}

   	private int[] findArrForKey(int key) {
   		for (int i = 0; i < cycle.cycles.length; i++) {
   			if (key == cycle.cycles[i][0]) return cycle.cycles[i];
   		}
   		return new int[]{};
   	}

   	private static int[] addElementToIntArray(int[] a, int e) {
        int[] output  = Arrays.copyOf(a, a.length + 1);
        output[a.length] = e;
        return output;
    }

   ArrayList<EWAHCompressedBitmap> listOfSets() throws CloneNotSupportedException {
    ArrayList<EWAHCompressedBitmap> output = new ArrayList<>();

    EWAHCompressedBitmap empty = new EWAHCompressedBitmap();
    EWAHCompressedBitmap first = new EWAHCompressedBitmap();
    first.set(hasseRows[0][0]);

    output.add(empty);
    output.add(first);

    for (int i = 1; i < hasseRows.length; i++) {
      for (int j = 0; j < hasseRows[i].length; j++) {
        int key = hasseRows[i][j];
        int currentSize = output.size();
        for (int x = 0; x < currentSize; x++) {
          if (requirementsAreSatisfied(key, output.get(x))) {
            EWAHCompressedBitmap c = (EWAHCompressedBitmap) output.get(x).clone();
            c.set(key);
            output.add(c);
          }
        }
      }
    }

    return output;
   }

  long cardinality() throws CloneNotSupportedException {
    ArrayList<EWAHCompressedBitmap> output = new ArrayList<>();
    long[] results = new long[hasseRows.length];

    EWAHCompressedBitmap empty = new EWAHCompressedBitmap();
    EWAHCompressedBitmap first = new EWAHCompressedBitmap();
    first.set(hasseRows[0][0]);

    output.add(empty);
    output.add(first);

    results[0] = 2;
    long result = 2;

    for (int i = 1; i < hasseRows.length; i++) {
      if (i > 2) {
        for (int k = 0; k < results[i-2]-10; k++) {
          output.remove(output.get(k));
        }
      }
      for (int j = 0; j < hasseRows[i].length; j++) {
        int key = hasseRows[i][j];
        int currentSize = output.size();
        for (int x = 0; x < currentSize; x++) {
          if (requirementsAreSatisfied(key, output.get(x))) {
            EWAHCompressedBitmap c = (EWAHCompressedBitmap) output.get(x).clone();
            c.set(key);
            output.add(c);
            results[i]++;
            result++;
          }
        }
      }
    }

    return result;
   }

    boolean requirementsAreSatisfied(int key, EWAHCompressedBitmap bitset) {
    int[] r = (int[])requirements.get(key);
    if (r == null) return false;
    if (r.length == 0) return true;
    int length = r.length;
    for (int i = 0; i < length; i++) {
      if (!bitset.get(r[i])) {
        return false;
      } 
    }
    return true;
   }

   long cardinalityOfArrayListOfArrayLists(ArrayList<ArrayList<EWAHCompressedBitmap>> input) {
    long output = 0;
      for (int i = 0; i < input.size(); i++) {
        output += input.get(i).size();
      }

      return output;
   }


   ArrayList<EWAHCompressedBitmap> getLevelUpSet(ArrayList<EWAHCompressedBitmap> set) {
      int size = set.size();
      int highest = hasseRows[0][0];

      ArrayList<EWAHCompressedBitmap> output = new ArrayList<>();

      for (int i = 0; i < set.size(); i++) {
        for (int j = i; j < set.size(); j++) {
          if (set.get(i).or(set.get(j)).equals(set.get(j))) {

            EWAHCompressedBitmap newBitmap = new EWAHCompressedBitmap();
            int[] arri = set.get(i).toArray();
            int[] arrj = set.get(j).toArray();

            int arr2length = highest*2;

            for (int x = 0; x < arrj.length; x++) {
              newBitmap.set(arrj[x]);
            }

            for (int x = 0; x < arri.length; x++) {
              newBitmap.set(arri[x]+highest+1);
            }

            output.add(newBitmap);

          }
        }
      }

      return output;
   }



   BigInteger cardinalityOfLevelUp(ArrayList<EWAHCompressedBitmap> set, int levelsUp) {

    int size = set.size();
    BigInteger output = new BigInteger("0");

    if (levelsUp == 1) {
      for (int i = 0; i < set.size(); i++) {
        for (int j = i; j < set.size(); j++) {
          if (set.get(i).or(set.get(j)).equals(set.get(j))) output = output.add(BigInteger.ONE);
        }
      }
    }

    //////////////////////////////////

    if (levelsUp == 2) {
      boolean[][] mat = new boolean[size][size];
      BitSet[] cols = new BitSet[size];

      for (int i = 0; i < size; i++) {
            cols[i] = new BitSet(size);
        }

      for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (set.get(i).or(set.get(j)).equals(set.get(j))) {
                    mat[i][j] = (set.get(i).or(set.get(j)).equals(set.get(j)));
                    cols[j].set(i);
                }
            }
        }

      for (int row = 0; row < size; row++) {
        BitSet currentRow = getRowBitSet(mat, row, size);
          for (int col = 0; col < size; col++) {
            if (!mat[row][col]) continue;
            BitSet tempCol_ = cols[col];
            long cardinality = currentRow.andcardinality(tempCol_);
            cardinality = cardinality*cardinality;
            output = output.add(BigInteger.valueOf(cardinality));
        }
      }
    }

    ////////////////////////////////

    if (levelsUp == 8) {
      boolean[][] mat = new boolean[size][size];
      long[][] mat1 = new long[size][size];
      long[][] mat2 = new long[size][size];
      BitSet[] cols = new BitSet[size];

      for (int i = 0; i < size; i++) {
        cols[i] = new BitSet(size);
      }

      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          mat[i][j] = set.get(i).or(set.get(j)).equals(set.get(j));
          if (mat[i][j]) cols[j].set(i);
        }
      }

      for (int i = 0; i < size; i++) {
        BitSet currentRow = getRowBitSet(mat, i, size);
        for (int j = 0; j < size; j++) {
          if (mat[i][j]) {
            mat1[i][j] = 1;
            mat2[i][j] = currentRow.andcardinality(cols[j]); 
            output = output.add(BigInteger.valueOf(mat2[i][j]));
          }
        }
      }
    }

    ////////////////////////////////

    if (levelsUp == 9) {
      boolean[][] mat = new boolean[size][size];
      long[][] mat1 = new long[size][size];
      long[][] mat2 = new long[size][size];
      long[][] mat3 = new long[size][size];
      BitSet[] cols = new BitSet[size];

      for (int i = 0; i < size; i++) {
        cols[i] = new BitSet(size);
      }

      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          mat[i][j] = set.get(i).or(set.get(j)).equals(set.get(j));
          if (mat[i][j]) cols[j].set(i);
        }
      }

      for (int i = 0; i < size; i++) {
        BitSet currentRow = getRowBitSet(mat, i, size);
        for (int j = 0; j < size; j++) {
          if (mat[i][j]) {
            mat1[i][j] = 1;
            mat2[i][j] = currentRow.andcardinality(cols[j]); 
          }
        }
      }

      for(int i = 0; i < size; i++) {
        for(int j = i; j < size; j++) {    
          for(int k = 0; k < size; k++) {      
            mat3[i][j] += mat1[i][k] * mat2[k][j];      
          }
          output = output.add(BigInteger.valueOf(mat3[i][j]));
        }
      }
    }

    return output;

   }

  BigInteger cardinalityOf2222(ArrayList<EWAHCompressedBitmap> set) throws CloneNotSupportedException {

    int size = set.size();
    BigInteger output = new BigInteger("0");

      BigInteger[] Dn = generateDn(6);
      BigInteger[] Bn = bijections2(Dn);

      long length = Dn.length;

      BigInteger[] ory = new BigInteger[Dn.length];
      BigInteger[] andy = new BigInteger[Dn.length];

      for (int i = 0; i < Dn.length; i++) {
        ory[i] = Dn[i].or(Bn[i]);
        andy[i] = Dn[i].and(Bn[i]);
      }

      long[] multipliersOrs = new long[Dn.length];
      long[] multipliersAnds = new long[Dn.length];
      long[] results = new long[(int)length];

      BigInteger[] sets = new BigInteger[set.size()];

      for (int i = 0; i < set.size(); i++) {
        sets[i] = BitSetIntoBigInteger(set.get(i));
      }

      HashMap<BigInteger, Integer> indexOfSetOrs = new HashMap<>();
      HashMap<BigInteger, Integer> indexOfSetAnds = new HashMap<>();

      for (int x = 0; x < length; x++) {
        System.out.println(x);
        indexOfSetOrs.put(ory[x], x);
        indexOfSetAnds.put(andy[x], x);
      }

      System.out.println(indexOfSetAnds.size());
      System.out.println(indexOfSetOrs.size());

      IntStream.range(0, Dn.length).parallel().forEach(
                i -> {

                    long counterOR = 0;
                    long counterAND = 0;

                    for (int j = 0; j < size; j++) {

                      if (andy[i].and(sets[j]).equals(sets[j])) counterAND++;
                      if (ory[i].or(sets[j]).equals(sets[j])) counterOR++;
                    }

                    multipliersAnds[i] = counterAND;
                    multipliersOrs[i] = counterOR;
                });

      for (int i = 0; i < length; i++) {
        output = output.add(BigInteger.valueOf(multipliersOrs[i]*multipliersAnds[i]));
      }
      
      return output;
   }

    private static BigInteger[] generateDn(int level) {
        if (level == 0) return new BigInteger[]{new BigInteger("0"),new BigInteger("1")};
        BigInteger[] prevDn  = generateDn(level-1);
        int pow = 1 << level-1;
        ArrayList<BigInteger> arr = new ArrayList<>();
        for (int i = 0; i < prevDn.length; i++){
            for (int j = i; j < prevDn.length; j++) {
                if (prevDn[i].or(prevDn[j]).equals(prevDn[j])) {
                    arr.add(prevDn[i].shiftLeft(pow).or(prevDn[j]));
                }
            }
        }
        BigInteger[] array = new BigInteger[arr.size()];
        for (int i = 0; i < arr.size(); i++) 
            array[i] = arr.get(i);
        return array;
    }

    private static BigInteger[] bijections2(BigInteger[] Dn) {

        BigInteger[] bijections = new BigInteger[Dn.length];
        
        //(2)(2)(2)
        int[] p = new int[]{0, 2, 1, 3, 8, 10, 9, 11, 4, 6, 5, 7, 12, 14, 13, 15, 32, 34, 33, 35, 40, 42, 41, 43, 36, 38, 37, 39, 44, 46, 45, 47, 16, 18, 17, 19, 24, 26, 25, 27, 20, 22, 21, 23, 28, 30, 29, 31, 48, 50, 49, 51, 56, 58, 57, 59, 52, 54, 53, 55, 60, 62, 61, 63};

        for (int i = 0; i < Dn.length; i++) {

            BigInteger out = new BigInteger("0");
            BigInteger one = new BigInteger("1");

            for (int j = 0; j < p.length; j++){
              if (((Dn[i].shiftRight(p[j]).and(one).equals(one)))) out = out.or(one.shiftLeft(j));
            }

            bijections[i] = out;
        }

        return bijections;
    }

        private static BigInteger[] bijections3(BigInteger[] Dn) {

        BigInteger[] bijections = new BigInteger[Dn.length];

        //(3)(3)
        int[] p = new int[]{0, 2, 4, 6, 1, 3, 5, 7, 8, 10, 12, 14, 9, 11, 13, 15, 16, 18, 20, 22, 17, 19, 21, 23, 24, 26, 28, 30, 25, 27, 29, 31, 32, 34, 36, 38, 33, 35, 37, 39, 40, 42, 44, 46, 41, 43, 45, 47, 48, 50, 52, 54, 49, 51, 53, 55, 56, 58, 60, 62, 57, 59, 61, 63};

        for (int i = 0; i < Dn.length; i++) {

            BigInteger out = new BigInteger("0");
            BigInteger one = new BigInteger("1");

            for (int j = 0; j < p.length; j++){
              if (((Dn[i].shiftRight(p[j]).and(one).equals(one)))) out = out.or(one.shiftLeft(j));
            }

            bijections[i] = out;
        }

        return bijections;
    }

    BigInteger cardinalityOf33(ArrayList<EWAHCompressedBitmap> set) throws CloneNotSupportedException {

      int size = set.size();
      BigInteger output = new BigInteger("0");

      BigInteger[] Dn = generateDn(6);
      BigInteger[] Bn = bijections3(Dn);
      BigInteger[] Cn = bijections3(Bn);

      long length = Dn.length;

      BigInteger[] orAB = new BigInteger[Dn.length];
      BigInteger[] orABC = new BigInteger[Dn.length];
      BigInteger[] andABC = new BigInteger[Dn.length];

      for (int i = 0; i < Dn.length; i++) {
        orAB[i] = Dn[i].or(Bn[i]);
        orABC[i] = Dn[i].or(Bn[i]).or(Cn[i]);
        andABC[i] = Dn[i].and(Bn[i]).and(Cn[i]);
      }

      System.out.println("or and ands done!");

      BigInteger[] sets = new BigInteger[set.size()];

      for (int i = 0; i < set.size(); i++) {
        sets[i] = BitSetIntoBigInteger(set.get(i));
      }

      System.out.println(Arrays.toString(sets));

      long[] intervalsDnUp = new long[Dn.length];
      long[] intervalsDnDown = new long[Dn.length];

      for (int i = 0; i < Dn.length; i++) {
        for (int j = 0; j < sets.length; j++) {
            if (orABC[i].or(sets[j]).equals(sets[j])) intervalsDnUp[i]++;
            if (sets[j].or(andABC[i]).equals(andABC[i])) intervalsDnDown[i]++;
        }
      }

      long[] results = new long[Dn.length];

      IntStream.range(0, Dn.length).parallel().forEach(
                i -> {

                  long result = 0;

                    for (int j = i; j < Dn.length; j++) {

                      if (orAB[i].or(Dn[j]).equals(Dn[j])) {

                        BigInteger x = Dn[j];
                        BigInteger y = Bn[j];
                        BigInteger z = Cn[j];

                        result = result + intervalsDnUp[j]*intervalsDnDown[i];

                    results[i] = result;
                }}});

      for (int i = 0; i < Dn.length; i++) {
        output = output.add(BigInteger.valueOf(results[i]));
      }
      
      return output;
   }

    private static boolean bitIsSet(long f, int bit1) {
         return (((f >> bit1) & 1) == 1);
    }

    private static BitSet getRowBitSet(boolean[][] matrix, int row, int size) {
        BitSet output = new BitSet(size);
        for (int i = row; i < matrix.length; i++) {
            if (matrix[row][i]) output.set(i);
        }
        return output;
    }

    private long BitSetIntoLong(EWAHCompressedBitmap input) {
        
        EWAHCompressedBitmap bitset = unpackBitSet(input);
        var listOfSetBits = bitset.toList();

        java.util.BitSet s = new java.util.BitSet();

        for (int i = 0; i < listOfSetBits.size(); i++) {
          s.set(listOfSetBits.get(i));
        }

        long output = 0;

        long bi = s.stream()
            .takeWhile(t -> t < Long.SIZE)
            .mapToLong(t -> 1L << t)
            .reduce(0, (a, b) -> a | b);

        bi = Long.reverse(bi);

        return bi;
    }

    public BigInteger BitSetIntoBigInteger(EWAHCompressedBitmap input) {
        
        EWAHCompressedBitmap bitset = unpackBitSet(input);
        var listOfSetBits = bitset.toList();

        //System.out.println(listOfSetBits);

        if (listOfSetBits.size() == 0) return new BigInteger("0");

        int length = listOfSetBits.get(listOfSetBits.size()-1);

        BigInteger output = new BigInteger("0");

        java.util.BitSet s = new java.util.BitSet();

        for (int i = 0; i < listOfSetBits.size(); i++) {
          output = output.setBit(length - listOfSetBits.get(i));
        }

        return output;
    }

    private EWAHCompressedBitmap unpackBitSet(EWAHCompressedBitmap input) {
      int[][] cycles = cycle.getCycles();

      EWAHCompressedBitmap output = new EWAHCompressedBitmap();

      for (int i = 0; i < cycles.length; i++) {
        if (input.get(cycles[i][0])) {
          for (int j = 0; j < cycles[i].length; j++) {
            output.set(cycles[i][j]);
          }
        }
      }

      return output;

    }

}
