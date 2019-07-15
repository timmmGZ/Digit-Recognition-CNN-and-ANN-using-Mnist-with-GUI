package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import neuralNetwork.Convolution;
import neuralNetwork.DataSet;
import tool.DataInfo;

public class DrawingPanel extends JPanel implements MouseMotionListener, MouseListener {

	private static final long serialVersionUID = 2699483160369518329L;
	public int pixelW = 13, pixelH = 13, resolution = 28;
	protected double[][] map, mapDrawing = new double[28][28];
	public ConvolutionPanel convPanel;
	MainFrame mainFrame;
	Convolution conv = new Convolution();

	public DrawingPanel(ConvolutionPanel cP, MainFrame m) throws IOException {
		super();
		map = new double[resolution][resolution];
		convPanel = cP;
		setPreferredSize(new Dimension(364, 364));
		setBackground(Color.WHITE);
		addMouseMotionListener(this);
		addMouseListener(this);
		mainFrame = m;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int y = 0; y < resolution; y++)
			for (int x = 0; x < resolution; x++) {
				int grey = 255 - (int) (map[y][x] * 255);
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

	private void setPixel(MouseEvent e) {
		try {
			mapDrawing[e.getY() / pixelH][e.getX() / pixelW] = SwingUtilities.isLeftMouseButton(e) ? 0.8 : 0;
			double[][] a = conv.createDrawing(mapDrawing);
			for (int y = 0; y < map.length; y++)
				for (int x = 0; x < map[0].length; x++)
					a[y][x] = 1 / (1 + Math.exp(-a[y][x] * 100) * 255);
			map = a;
			convPanel.setCurrentWordsConvDrawing(new DataSet(map, conv));
			repaint();
			mainFrame.trainer.network.setAllNeuronsInputs(convPanel.getVector());
			List<Double> outputs = mainFrame.trainer.network.getOutputs();
			mainFrame.probPanel.updateProbs(outputs);
			String prediction = mainFrame.info.getTrainWordNames().get(outputs.indexOf(Collections.max(outputs)));
			mainFrame.predict.setText("Prediction: " + prediction);
			mainFrame.accuracy.setText("Prediction of your drawing doesn't have true label");
		} catch (Exception exc) {
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		setPixel(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		setPixel(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public void clear() {
		map = new double[28][28];
		mapDrawing = new double[28][28];
		repaint();
		convPanel.setCurrentWordsConvDrawing(new DataSet(map, conv));
	}

}
