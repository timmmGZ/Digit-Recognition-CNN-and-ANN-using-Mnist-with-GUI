package neuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataSet {

	private int[][] inputsAsMatrix;
	private List<Integer> featureVector, inputVector;
	private List<Integer> trueOuput;
	private String label;

	public DataSet(int[][] in, List<Integer> out, String word, Convolution convolution) {
		inputsAsMatrix = in;
		trueOuput = out;
		label = word;
		convolution.setupAllLayers(inputsAsMatrix);
		featureVector = convolution.getFeatureVector();
		inputVector = Arrays.stream(inputsAsMatrix).map(Arrays::stream).map(e -> e.boxed()).flatMap(s -> s)
				.collect(Collectors.toList());
		// adjustToCentroid();
	}

	public int[][] getInputs() {
		return inputsAsMatrix;
	}

	public List<Integer> getInputsVector() {
		return inputVector;
	}// map ->List<int[]> ->List<IntStream> ->List<Stream<Integer>> ->Stream<Integer>

	public List<Integer> getFeatureVector() {
		return featureVector;
	}

	public List<Integer> getTrueOutput() {
		return trueOuput;
	}

	public void adjustToCentroid() {
		int lx = 28, rx = 0, upy = 28, downy = 0, cx, cy;
		for (int y = 0; y < 28; y++)
			for (int x = 0; x < 28; x++)
				if (inputsAsMatrix[x][y] != 0) {
					upy = y < upy ? y : upy;
					downy = y > downy ? y : downy;
					lx = x < lx ? x : lx;
					rx = x > rx ? x : rx;
				}
		cx = (int) (14 - (rx - lx + 1) / 2.0 - lx);
		cy = (int) (14 - (downy - upy + 1) / 2.0 - upy);
		List<Integer> newInput = new ArrayList<>();
		if (cx != 0 || cy != 0) {
			System.out.println(label + ":" + cx + "," + cy);
			for (int y = 0; y < 28; y++) {
				for (int x = 0; x < 28; x++)
					System.out.print(inputsAsMatrix[x][y] == 0 ? 0 : 1);
				System.out.println();
			}
			System.out.println();
			for (int y = 0; y < 28; y++)
				for (int x = 0; x < 28; x++) {
					int ccx = Math.min(Math.max((x - cy), 0), 27);
					int ccy = Math.min(Math.max((y - cx), 0), 27);
					newInput.add(inputsAsMatrix[ccy][ccx]);
				}
			for (int y = 0; y < 28; y++) {
				for (int x = 0; x < 28; x++)
					System.out.print(newInput.get(x * 28 + y) == 0 ? 0 : 1);
				System.out.println();
			}
		}
	}

	public String getLabel() {
		return label;
	}
}
