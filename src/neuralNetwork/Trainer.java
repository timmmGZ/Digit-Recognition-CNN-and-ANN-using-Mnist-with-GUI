package neuralNetwork;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JTextArea;

import gui.DrawingPanel;
import gui.MainFrame;
import tool.DataInfo;

public class Trainer {
	public Network network;
	public List<DataSet> trainSets;
	public static int i = 0, epoch = 1;
	public JTextArea lossTextArea = new JTextArea();
	DecimalFormat df = new DecimalFormat("0.0000");
	DrawingPanel drawPanel;

	public Trainer(MainFrame mf) throws IOException {
		lossTextArea = mf.lossTextArea;
		network = mf.network;
		trainSets = DataInfo.getTrainSets();
		drawPanel = mf.drawPanel;
		train(120000, 1);
	}

	public void train(int count, int type) {
		for (i = 0; i < count; i++) {
			DataSet trainSet = trainSets.get((int) (Math.random() * trainSets.size()));
			network.setAllNeuronsInputs(type == 0 ? trainSet.getFeatureVector() : trainSet.getInputsVector());
			network.modifyAllNeuronsWeights(trainSet.getTrueOutput());
			if (i % 60000 == 59999)
				UpdateLossAreaAndWeightsDrawing(count);
		}
	}

	public void UpdateLossAreaAndWeightsDrawing(int c) {
		StringBuilder sb = new StringBuilder();
		network.getNeurons().forEach(n -> {
			int index = network.getNeurons().indexOf(n);
			if (n.totalCost > n.lastTotalC && Neuron.keepBestWeights) {
				n.weights = n.oldWeights;
				sb.append(index + " neuron loss: " + df.format((n.totalCost = n.lastTotalC) / 600) + "%(keep)  ");
			} else
				sb.append(index + "neuron loss: " + df.format((n.lastTotalC = n.totalCost) / 600) + "%               ");
			sb.append((index % 2 == 0 ? "" : "\n"));
			Neuron.totalCAll += n.totalCost;
			n.totalCost = 0;
		});
		sb.append(
				"Epoch " + epoch++ + " total loss: " + (int) (Neuron.totalCAll / 6) / 100.0 + "%, " + Neuron.totalCAll);
		Neuron.totalCAll = 0;
		lossTextArea.setText(sb.toString());
		try {
			lossTextArea.update(lossTextArea.getGraphics());
			if (drawPanel.convPanel.showWeights)
				drawPanel.convPanel.update(drawPanel.convPanel.getGraphics());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
