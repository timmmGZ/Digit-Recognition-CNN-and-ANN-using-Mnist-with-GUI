package neuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Network {
	private static final int numOfNeurons = 10;
	private List<Neuron> neurons = new ArrayList<>();
	public double totalC = 0;

	public Network() {
		for (int i = 0; i < numOfNeurons; i++)
			neurons.add(new Neuron());
	}

	public void setAllNeuronsInputs(List<Integer> inputvector) {
		neurons.forEach(n -> n.setInputs(inputvector));
	}

	public void modifyAllNeuronsWeights(List<Integer> trueOutput) {
		neurons.forEach(n -> n.totalCost += n.modifyWeights(trueOutput.get(neurons.indexOf(n)), n.perdictedOutput()));

	}

	public List<Neuron> getNeurons() {
		return neurons;
	}

	public List<Double> getOutputs() {
		return neurons.stream().map(n -> n.perdictedOutput()).collect(Collectors.toList());
	}
}
