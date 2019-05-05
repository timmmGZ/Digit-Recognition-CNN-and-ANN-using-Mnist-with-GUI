package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class ProbabilityPanel extends JPanel {

	private static final long serialVersionUID = -8391378721284959216L;
	List<JProgressBar> probs = new ArrayList<>();
	MainFrame mainFrame;

	public ProbabilityPanel(MainFrame frame, int num) {
		setLayout(new BorderLayout());
		mainFrame = frame;
		for (int i = 0; i < num; i++)
			probs.add(new JProgressBar());
		setLeft();
		setRight();
	}

	public void setLeft() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		panel.setBackground(new Color(0, 206, 209));
		for (String word : mainFrame.info.getTrainWordNames())
			panel.add(new JLabel(word, SwingConstants.CENTER));
		add(panel, BorderLayout.WEST);
	}

	public void setRight() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		panel.setBackground(new Color(0, 206, 209));
		for (JProgressBar bar : probs) {
			bar.setStringPainted(true);
			panel.add(bar);
		}
		add(panel, BorderLayout.CENTER);
	}

	public void updateProbs(List<Double> outputs) {
		for (int i = 0; i < outputs.size(); i++) {
			double prob = outputs.get(i);
			probs.get(i).setForeground(new Color((int) (255 * (1 - prob)) / 1, (int) (255 * prob / 1), 0));
			probs.get(i).setValue((int) ((prob * 1000) / 10.0));
		}
	}
}
