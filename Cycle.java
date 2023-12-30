import java.util.*;
import java.util.stream.IntStream;
import java.io.*;

public class Cycle {
	int[] lengths;
	int size;
	int[] code;
	int[][] cycles;
	int[] smallestsInCycles;
	int[] bitsNumberInSmallest;

   public Cycle(int[] lengths) {
      this.lengths = lengths;
      this.size = IntStream.of(lengths).sum();
      this.code = modelIntoCode(generateModel(trueWord(lengths)), generateModel(permWord(lengths)));
      this.cycles = codeIntoCycle(code);
      this.smallestsInCycles = smallestRepresentantsOfCycles(cycles);
      this.bitsNumberInSmallest = countBits(smallestsInCycles);
   }

	private static String trueWord(int[] cycle) {
		int length = IntStream.of(cycle).sum();

		return "123456789ABCDEFGH".substring(0, length);
	}

	private static String permWord(int[] cycle) {

		String word = trueWord(cycle);

		String output = "";
		int position = 0;

		for (int i = 0; i < cycle.length; i++) {
			position = position + cycle[i] - 1;
			String part = "" + word.charAt(position);

			for (int j = 0; j < cycle[i]-1; j++) {
				part = part + word.charAt(position-cycle[i]+j+1);
			}
			position += 1;
			output = output + part;
			
		}

		return output;
	}

	private static String[] generateModel(String word){
        
        Stack<String> stack = new Stack<>();

        for (int i = word.length()-1; i >= 0; i--) {
            stack.push(Character.toString(word.charAt(i)));
        }

        String[] out = new String[1<<word.length()];

        out[0] = "";
        int lastPow = 1;

        for (int i = 1; i < out.length; i++) {
            if ((i & (i - 1)) == 0) {
                out[i] = stack.pop();
                lastPow = i;
            } else {
                out[i] = (out[(i-lastPow)]+out[lastPow]).chars().sorted().collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            }
        }
        return out;
    }

	private static int[] modelIntoCode(String[] permModel, String[] trueModel) {
		int[] output = new int[permModel.length];

		for (int i = 0; i < permModel.length; i++) {
			for (int j = 0; j < trueModel.length; j++) {
				if (permModel[i].equals(trueModel[j])) 	  {
					output[i] = j;
					continue;
				}
			}
		}

		return output;
	}

	private static int[][] codeIntoCycle(int[] code) {

		ArrayList<Integer> numbers = new ArrayList<>();

		for (int i = 0; i < code.length; i++) {
			numbers.add(i);
		}

		ArrayList<int[]> cycles = new ArrayList<>();

		int startElement;

		do {
			startElement = numbers.get(0).intValue();
			int[] currentCycle = determineCycleInArray(code, startElement);
			cycles.add(currentCycle);
			removeCycleFromArrayList(currentCycle, numbers);
		} while (numbers.size() != 0);

		int[][] output = new int[cycles.size()][];

		for (int i = 0; i < cycles.size(); i++) {
			output[i] = cycles.get(i);
		}

		//System.out.println("Number of cycles: " + output.length);

		return output;
	}

	private static int[] determineCycleInArray(int[] code, int startElement) {
		int[] output = new int[]{startElement};
		if (code[startElement] == startElement) return output;
		int currentElement = startElement;

		do {
		  output = addElementToIntArray(output, code[currentElement]);
		  currentElement = code[currentElement];
	  } while(code[currentElement] != startElement);

	  return output;
	}

	private static void removeCycleFromArrayList(int[] cycle, ArrayList<Integer> list){
		for (int i = 0; i < cycle.length; i++) {
			for (int j = 0; j < list.size(); j++) {
				if (cycle[i] == list.get(j)) {
					list.remove(j);
				}
			}
		}
	}

	private static int[] addElementToIntArray(int[] a, int e) {
        int[] output  = Arrays.copyOf(a, a.length + 1);
        output[a.length] = e;
        return output;
    }

    private static int[] smallestRepresentantsOfCycles(int[][] cycles) {
    	int[] output = new int[cycles.length];

    	for (int i = 0; i < cycles.length; i++) {
    		int currentSmallest = cycles[i][0];
    		for (int j = 0; j < cycles[i].length; j++) {
    			if (cycles[i][j] < currentSmallest) currentSmallest = cycles[i][j];
    		}
    		output[i] = currentSmallest;
    	}

    	return output;
    }

    private static int[] countBits(int[] numbers)
    {
    	int[] output = new int[numbers.length];

    	for (int i = 0; i < output.length; i++) {
    		output[i] = countBitsInSingleInt(numbers[i]);
    	}
        return output;
    }

    private static int countBitsInSingleInt(int n)
    {
        int count = 0;
        while (n > 0) {
            count += n & 1;
            n >>= 1;
        }
        return count;
    }

    private static int[][] reverseIntArray(int[][] array) {
    	ArrayList<int[]> list = new ArrayList<>();

    	for (int i = array.length-1; i >= 0; i--) {
    		list.add(array[i]);
    	}

    	int[][] output = new int[array.length][];
    	
    	for (int i = 0; i < list.size(); i++) {
    		output[i] = list.get(i);
    	}

    	return output;
    }

    int[][] buildCycleHasseRows() {
    	int[][] output = new int[size+1][0];
    	for (int i = 0; i < smallestsInCycles.length; i++) {
    		output[bitsNumberInSmallest[i]] = addElementToIntArray(output[bitsNumberInSmallest[i]], smallestsInCycles[i]);	
    	}
    	output = reverseIntArray(output);
    	return output;
    }

    int[][] getCycles() {
    	return cycles;
    }

}