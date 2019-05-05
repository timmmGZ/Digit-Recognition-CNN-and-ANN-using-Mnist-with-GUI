package gui;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import neuralNetwork.DataSet;
import tool.DataInfo;

public class DrawingPanel extends JPanel {

	private static final long serialVersionUID = 2699483160369518329L;
	public int pixelW = 13, pixelH = 13, resolution = 28;
	protected int[][] map;
	public ConvolutionPanel convPanel;

	public DrawingPanel(ConvolutionPanel cP) throws IOException {
		super();
		map = new int[resolution][resolution];
		convPanel = cP;
		setPreferredSize(new Dimension(364, 364));
		setBackground(Color.WHITE);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int y = 0; y < resolution; y++)
			for (int x = 0; x < resolution; x++) {
				int grey = 255 - map[y][x];
				g.setColor(new Color(grey, grey, grey));
				g.fillRect(x * pixelW, y * pixelH, pixelW, pixelH);
			}
	}

	public DataSet drawFalseWord(List<DataSet> falseSet) {
		DataSet random = falseSet.get((int) (Math.random() * falseSet.size()));
		map = random.getInputs();
		convPanel.setCurrentWordsConvDrawing(random);
		repaint();
		return random;
	}

	public DataSet drawRandomWord(int type, int i) {// i=0->testSet, i=1->trainSet, i=2->both
		Random r = new Random();
		DataSet random = i == -1 ? (type == 0 ? DataInfo.getTestSets().get(r.nextInt(10000))//
				: (type == 1 ? DataInfo.getTrainSets().get(r.nextInt(60000))
						: r.nextDouble() > 0.5 ? DataInfo.getTestSets().get(r.nextInt(10000))
								: DataInfo.getTrainSets().get(r.nextInt(60000))))
				: (type == 0 ? DataInfo.getTestSets().get(i)
						: (type == 1 ? DataInfo.getTrainSets().get(i)
								: i < 60000 ? DataInfo.getTrainSets().get(i) : DataInfo.getTestSets().get(i - 60000)));
		map = random.getInputs();
		convPanel.setCurrentWordsConvDrawing(random);
		repaint();
		return random;
	}

}
