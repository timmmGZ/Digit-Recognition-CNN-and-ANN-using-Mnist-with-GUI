package neuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class Neuron {
	ArrayList<Integer> inputs = new ArrayList<>();
	public static double learningRate = 0.05, totalCAll = 0;
	public double weights[] = new double[785], oldWeights[] = new double[785];
	public static boolean useBP = false, normalNN = true, keepBestWeights = false;

	public double totalCost = 0, lastTotalC = 100000000;

	public double modifyWeights(double trueOutput, double perdictedOutput) {
		double error = trueOutput - perdictedOutput;
		double backPropagation = 0;
		if (keepBestWeights) {
			System.arraycopy(weights, 0, oldWeights, 0, weights.length);
			backPropagation = useBP ? (-0.2 * error * perdictedOutput * (1 - perdictedOutput)) : 0;
		}
		for (int i = 0; i < inputs.size(); i++)
			weights[i] += (learningRate * inputs.get(i) * (error - backPropagation));
		weights[weights.length - 1] += learningRate * (error - backPropagation);
		return error * error;
	}

	public double perdictedOutput() {
		double sum = 0;
		for (int i = 0; i < inputs.size(); i++)
			sum += inputs.get(i) * weights[i];
		sum += weights[weights.length - 1];
		return normalNN ? sigmoid(sum / 1500000) : 1 / (1 + Math.exp(-sum / 1.2E8) / 1.8);
	}

	public void setInputs(List<Integer> list) {
		inputs = new ArrayList<>(list);
	}

	public static double sigmoid(Double sum) {
		return 1 / (1 + Math.exp(-sum));
	}

}
