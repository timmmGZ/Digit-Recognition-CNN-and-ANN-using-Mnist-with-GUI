package tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import neuralNetwork.Convolution;
import neuralNetwork.DataSet;

public class ReadFile {
	public static int i = 0;

	public static List<DataSet> readFromSingleCsv(String filename) throws IOException {
		long t = System.currentTimeMillis();
		Convolution conv = new Convolution();
		List<DataSet> trainingSets = new ArrayList<>();
		for (Scanner s = new Scanner(new FileInputStream(new File("dataset/" + filename))); s.hasNextLine();) {
			String[] line = s.nextLine().split(",");
			String word = String.valueOf(line[0]);
			double[][] trainSetInputs = new double[28][28];
			for (int y = 0; y < 28; y++)
				for (int x = 0; x < 28; x++)
					trainSetInputs[y][x] = Integer.parseInt(line[y * 28 + x + 1]);
			trainingSets.add(new DataSet(trainSetInputs, DataInfo.getTrueOutput(word), word, conv));
			i++;
		}
		System.out.println((System.currentTimeMillis() - t) / 1000.0 + " seconds");
		return trainingSets;
	}

	public static List<DataSet> readFromSingleTxt(String filename) throws IOException {
		long t = System.currentTimeMillis();
		Convolution conv = new Convolution();
		List<DataSet> trainingSets = new ArrayList<>();
		for (Scanner s = new Scanner(new FileInputStream(new File("dataset/" + filename))); s.hasNextLine();) {
			String[] line = s.nextLine().split("\\|");
			String word = String.valueOf(line[line.length - 1]);
			double[][] trainSetInputs = new double[28][28];
			for (int y = 0; y < 28; y++)
				for (int x = 0; x < 28; x++)
					trainSetInputs[x][y] = Integer.parseInt(line[y * 28 + x]) / 255.0;
			trainingSets.add(new DataSet(trainSetInputs, DataInfo.getTrueOutput(word), word, conv));
			i++;
		}
		System.out.println((System.currentTimeMillis() - t) / 1000.0 + " seconds");
		return trainingSets;
	}

}
