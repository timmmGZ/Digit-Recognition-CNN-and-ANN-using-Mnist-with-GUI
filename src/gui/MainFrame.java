package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import neuralNetwork.DataSet;
import neuralNetwork.Network;
import neuralNetwork.Neuron;
import neuralNetwork.Trainer;
import tool.DataInfo;
import tool.ReadFile;

public class MainFrame extends JFrame {
	/**
	 * timmmGZ
	 */
	private static final long serialVersionUID = 1L;
	Trainer trainer;
	DataInfo info;
	ProbabilityPanel probPanel;
	public DrawingPanel drawPanel;
	public Network network = new Network();
	private JComboBox<String> falseWordsBox;
	public JTextArea lossTextArea = new JTextArea();
	private JTextField trainingTimes = new JTextField("K epoches"), testingTimes = new JFormattedTextField("K times"),
			lRate = new JTextField("0.008");
	public JLabel accuracy = new JLabel("?", SwingConstants.CENTER), predict = new JLabel("?", SwingConstants.CENTER);
	private JButton clear = new JButton("Clear to be default"), train = new JButton("Train train sets"),
			showLines = new JButton("Show lines for CNN"), test = new JButton("Test random drawing"),
			showFalse = new JButton("Show false prediction of"), lR = new JButton("Learning rate"),
			clearDrawing = new JButton("Clear Drawing");
	private JCheckBox testSet = new JCheckBox("Test test sets"), trainSet = new JCheckBox("Test train sets"),
			cnnOrAnn = new JCheckBox("use CNN model"), testEachOnce = new JCheckBox("Test each data only once"),
			keepBestWeights = new JCheckBox("Keep best weights each epoch"), bP = new JCheckBox("Use back Propagation"),
			drawWeights = new JCheckBox("Draw weights on window");
	private static Map<String, List<DataSet>> falseSet = new TreeMap<>();
	private Color darkBlue = new Color(0, 206, 209), lightBlue = new Color(156, 206, 209);

	public static void main(String[] args) throws IOException, InterruptedException {
		new MainFrame();
	}

	public MainFrame() throws IOException {
		super("TimmmGZ¡ª¡ªHand Writing Recognization (CNN and ANN)");
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setReadFilePanel();
		info = new DataInfo();
		info.getTrainWordNames().forEach(w -> falseSet.put(w, new ArrayList<DataSet>()));
		drawPanel = new DrawingPanel(new ConvolutionPanel(network), this);
		trainer = new Trainer(this);
		probPanel = new ProbabilityPanel(this, 10);
		falseWordsBox = new JComboBox<>(info.getTrainWordNamesArray());
		setBottuns();
		setCheckBoxs();
		setLeft();
		setRight();
		pack();
		setLocationRelativeTo(null);
	}

	private void setReadFilePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JProgressBar jpb = new JProgressBar();
		jpb.setStringPainted(true);
		JLabel label = new JLabel("Creating 60000 train sets...                                     (1/3)");
		panel.add(jpb);
		addWithColor(panel, label, darkBlue, BorderLayout.NORTH);
		addWithColor(this, panel, darkBlue, null);
		pack();
		setLocationRelativeTo(null);
		Thread t2 = new Thread(() -> {
			while (Trainer.i < 119999) {
				while (Trainer.i > 0 && Trainer.i < 119999)
					jpb.setValue(Trainer.i / 1200);
				jpb.repaint();
			}
			remove(panel);
		});
		Thread t1 = new Thread(() -> {
			while (ReadFile.i < 70000) {
				while (ReadFile.i > 60000 && ReadFile.i < 70000)
					jpb.setValue((ReadFile.i - 60000) / 100);
				jpb.repaint();
			}
			label.setText("Training 100000 times...                                     (3/3)");
			t2.start();
		});
		new Thread(() -> {
			while (ReadFile.i < 60000) {
				jpb.setValue(ReadFile.i / 600);
				jpb.repaint();
			}
			label.setText("Creating 10000 test sets...                                  (2/3)");
			t1.start();
		}).start();
	}

	private void setLeft() {
		JPanel panel = new JPanel(new BorderLayout());
		setLeftUp(panel);
		setLeftDown(panel);
		lossTextArea.setEditable(false);
		lossTextArea.setBounds(125, 575, 350, 110);
		addWithColor(this, lossTextArea, lightBlue, null);
		add(panel);
	}

	private void setLeftUp(JPanel jPanel) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(33, 100, 33, 0));
		panel.add(drawPanel, BorderLayout.EAST);
		panel.add(drawPanel.convPanel, BorderLayout.WEST);
		addWithColor(jPanel, panel, lightBlue, BorderLayout.NORTH);
	}

	private void setLeftDown(JPanel jPanel) {
		JPanel panel = new JPanel(new GridLayout(0, 6, 3, 0));
		JPanel labels = new JPanel(new GridLayout(0, 1, 0, 0));
		predict.setFont(new Font("MS Song", Font.BOLD, 60));
		accuracy.setFont(new Font("MS Song", Font.BOLD, 30));
		labels.add(predict);
		labels.add(accuracy);
		panel.add(train);
		panel.add(trainingTimes);
		panel.add(test);
		panel.add(testingTimes);
		panel.add(lR);
		panel.add(lRate);
		panel.add(showFalse);
		panel.add(falseWordsBox);
		panel.add(showLines);
		panel.add(clearDrawing);
		panel.add(clear);
		addWithColor(panel, drawWeights, darkBlue, null);
		addWithColor(panel, cnnOrAnn, darkBlue, null);
		addWithColor(panel, testSet, darkBlue, null);
		addWithColor(panel, trainSet, darkBlue, null);
		addWithColor(panel, testEachOnce, darkBlue, null);
		addWithColor(panel, keepBestWeights, darkBlue, null);
		addWithColor(panel, bP, darkBlue, null);
		addWithColor(jPanel, labels, darkBlue, BorderLayout.CENTER);
		addWithColor(jPanel, panel, darkBlue, BorderLayout.SOUTH);
	}

	private void setRight() {
		add(probPanel, BorderLayout.EAST);
	}

	private void setCheckBoxs() {
		testSet.setSelected(true);
		testSet.addActionListener(e -> trainSet.setSelected(true));
		trainSet.addActionListener(e -> testSet.setSelected(true));
		bP.addActionListener(e -> Neuron.useBP = bP.isSelected() ? true : false);
		testEachOnce.addActionListener(e -> {
			testingTimes.setText("1");
			testingTimes.setEditable(testEachOnce.isSelected() ? false : true);
		});
		keepBestWeights.addActionListener(e -> Neuron.keepBestWeights = keepBestWeights.isSelected() ? true : false);
		drawWeights.addActionListener(e -> {
			drawPanel.convPanel.showWeights = drawWeights.isSelected() ? true : false;
			drawPanel.convPanel.repaint();
		});
		cnnOrAnn.addActionListener(e -> {
			drawPanel.convPanel.useCNN = cnnOrAnn.isSelected() ? true : false;
			Neuron.normalNN = cnnOrAnn.isSelected() ? false : true;
			drawPanel.convPanel.repaint();
			JOptionPane.showMessageDialog(null, "Remember to click Clear and Train again weights", "Attention",
					JOptionPane.WARNING_MESSAGE);
		});
	}

	private void setBottuns() {
		MyActionListener m = new MyActionListener();
		lR.setActionCommand("rate");
		lR.addActionListener(m);
		test.setActionCommand("test");
		test.addActionListener(m);
		clear.setActionCommand("clear");
		clear.addActionListener(m);
		train.setActionCommand("train");
		train.addActionListener(m);
		showLines.setActionCommand("line");
		showLines.addActionListener(m);
		showFalse.setActionCommand("showFalse");
		showFalse.addActionListener(m);
		clearDrawing.setActionCommand("clearDrawing");
		clearDrawing.addActionListener(m);
	}

	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("line")) {
				drawPanel.convPanel.showLines = drawPanel.convPanel.showLines ? false : true;
				drawPanel.convPanel.repaint();
				return;
			} else if (e.getActionCommand().equals("clear")) {
				Trainer.epoch = 1;
				falseSet.entrySet().forEach(w -> w.getValue().clear());
				lossTextArea.setText("");
				network.getNeurons().forEach(n -> {
					n.weights = new double[785];
					n.oldWeights = new double[785];
				});
				repaint();
			} else if (e.getActionCommand().equals("clearDrawing")) {
				drawPanel.clear();
			}
			try {
				if (e.getActionCommand().equals("train")) {
					trainer.train(Integer.parseInt(trainingTimes.getText()) * 60000, cnnOrAnn.isSelected() ? 0 : 1);
				} else if (e.getActionCommand().equals("rate")) {
					Neuron.learningRate = Double.parseDouble(lRate.getText());
				} else if (e.getActionCommand().equals("test")) {
					int number = Integer.parseInt(testingTimes.getText());
					falseSet.entrySet().forEach(w -> w.getValue().clear());
					number = testEachOnce.isSelected() ? (testSet.isSelected() ? (trainSet.isSelected() ? 70000 : 10000) //
							: 60000) : number;
					int testCount = 0;
					for (int i = 0; i < number; i++) {
						DataSet randomWord = drawPanel.drawRandomWord(testSet.isSelected() ? //
								(trainSet.isSelected() ? 2 : 0) : 1, testEachOnce.isSelected() ? i : -1);
						network.setAllNeuronsInputs(drawPanel.convPanel.getVector());
						List<Double> outputs = network.getOutputs();
						probPanel.updateProbs(outputs);
						String prediction = info.getTrainWordNames().get(outputs.indexOf(Collections.max(outputs)));
						testCount += prediction.equals(randomWord.getLabel()) ? 1 : 0;
						predict.setText(prediction + ", " + String.valueOf(prediction.equals(randomWord.getLabel())));
						accuracy.setText("Accuracy: " + ((int) ((double) testCount / number * 10000)) / 100.0 + "% ("
								+ testCount + "/" + number + ")");
						if (!prediction.equals(randomWord.getLabel()))
							falseSet.get(randomWord.getLabel()).add(randomWord);
					}
				} else if (e.getActionCommand().equals("showFalse")) {
					DataSet randomWord = drawPanel.drawFalseWord(falseSet.get(falseWordsBox.getSelectedItem()));
					network.setAllNeuronsInputs(drawPanel.convPanel.getVector());
					List<Double> outputs = network.getOutputs();
					probPanel.updateProbs(outputs);
					String prediction = info.getTrainWordNames().get(outputs.indexOf(Collections.max(outputs)));
					int index = DataInfo.getTestSets().indexOf(randomWord);
					predict.setText("Prediction: " + prediction + ", index in " + (index == -1 ? "train.txt: "//
							+ (DataInfo.getTrainSets().indexOf(randomWord) + 1) : "test.txt: " + (index + 1)));
					accuracy.setText("True Lable: " + randomWord.getLabel() + ", number of false of this word: "
							+ falseSet.get(randomWord.getLabel()).size());
				}
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(null, "Input or something wrong!", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void addWithColor(Container container, JComponent jC, Color color, Object constraints) {
		container.add(jC, constraints);
		jC.setBackground(color);
	}
}

// Neuron.d.stream().collect(Collectors.groupingBy(q -> q,
// Collectors.counting())).entrySet().stream()
// .sorted(Comparator.comparing(Map.Entry<Double,
// Long>::getKey)).forEach(System.out::println);