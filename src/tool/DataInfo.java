package tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

import neuralNetwork.DataSet;

public class DataInfo {
	private List<String> trainWordNames = new ArrayList<>();
	private static Map<String, List<Integer>> trueOutputs;
	private static List<DataSet> trainSets, testSets;

	public DataInfo() throws IOException {
		trueOutputs = vectorsParser("dataset/standardOutput/trueOutput.txt");
		trainSets = ReadFile.readFromSingleTxt("mnist_digits_train.txt");
		testSets = ReadFile.readFromSingleTxt("mnist_digits_test.txt");
	}

	public static List<DataSet> getTrainSets() {
		return trainSets;
	}

	public static List<DataSet> getTestSets() {
		return testSets;
	}

	public List<String> getTrainWordNames() {
		return trainWordNames;
	}

	public String[] getTrainWordNamesArray() {
		return trainWordNames.toArray(new String[getTrainWordNames().size()]);
	}

	public static List<Integer> getTrueOutput(String letter) {
		return trueOutputs.get(letter);
	}

	public Map<String, List<Integer>> vectorsParser(String file) throws FileNotFoundException {
		Map<String, List<Integer>> map = new TreeMap<>();
		for (Scanner s = new Scanner(new FileInputStream(new File(file))); s.hasNextLine();) {
			String[] line = s.nextLine().split(",");
			trainWordNames.add(line[0]);
			map.put(line[0],
					Arrays.asList(line[1].split("")).stream().map(Integer::parseInt).collect(Collectors.toList()));
		}
		return map;
	}
}
