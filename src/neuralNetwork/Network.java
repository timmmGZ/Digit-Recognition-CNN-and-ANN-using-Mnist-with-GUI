package neuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Network {
	private static final int numOfNeurons = 10;
	private List<Neuron> neurons = new ArrayList<>();

	public Network() {
		for (int i = 0; i < numOfNeurons; i++)
			neurons.add(new Neuron());
	}

	public void setAllNeuronsInputs(List<Double> inputvector) {
		neurons.forEach(n -> n.setInputs(inputvector));
	}

	public void modifyAllNeuronsWeights(List<Integer> label) {
		List<Double> p = getOutputs();
		neurons.forEach(n -> n.totalCost += n.modifyWeights(label.get(neurons.indexOf(n)), p.get(neurons.indexOf(n))));
	}

	public List<Neuron> getNeurons() {
		return neurons;
	}

	public List<Double> getOutputs() {
		List<Double> probs = neurons.stream().map(n -> Math.exp(n.sum())).collect(Collectors.toList());
		double totalProb = probs.stream().reduce(0.0, Double::sum);
		return probs.stream().map(p -> p /= totalProb).collect(Collectors.toList());
	}
}
//150 1.5, 50 1, 50 0.5, 50 0.25, 1 0.1
//100 2, 50 1.5, 50 1, 50 0.5, 25 0.25

