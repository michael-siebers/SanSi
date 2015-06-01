package gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import series.NumberSeries;
import series.SimpleNumberSeriesDefinition;
import series.inducers.BetterSimpleNumberSeriesInducer;
import expressions.exceptions.NumberSeriesGenerationException;

import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

public class SingleSeriesOriginalInduction extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8975959433916448596L;
	
	private JTextField seriesField;
	private JTextPane resultPane;
	
	private BetterSimpleNumberSeriesInducer inducer = new BetterSimpleNumberSeriesInducer();
	private SimpleNumberSeriesDefinition inducedDefinition=null;
	private NumberSeries inducedSeries=null;
	private JSpinner predictionSpinner;
	private SpinnerNumberModel predictionSpinnerModel;
	private final ActionListener induceActionListener = new ActionListener() {
		// Run when button is pressed!
		public void actionPerformed(ActionEvent arg0) {
			induceSeries();
		}
	};
	private int inductionSize;
	

	public SingleSeriesOriginalInduction() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setTitle("Semi-Analytic Number Series Inducer");
		this.setBounds(100, 100, 400, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Number Series:");
		
		lblNewLabel.setLabelFor(getSeriesField());
		
		JLabel lblNewLabel_1 = new JLabel("Show next");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		
		lblNewLabel_1.setLabelFor(getPredictionSpinner());
		
		
		JLabel lblNewLabel_2 = new JLabel("numbers!");
		lblNewLabel_2.setLabelFor(getPredictionSpinner());
		
		JButton btnNewButton = new JButton("Induce & Predict");
		btnNewButton.addActionListener(induceActionListener);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(getResultPane());
		
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(getPredictionSpinner(), GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
									.addGap(1)
									.addComponent(lblNewLabel_2))
								.addComponent(getSeriesField(), GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
							.addGap(6))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnNewButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
							.addGap(6)))
					.addGap(0))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(getSeriesField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(getPredictionSpinner(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1)
						.addComponent(lblNewLabel_2))
					.addGap(10)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
					.addContainerGap())
		);
		this.getContentPane().setLayout(groupLayout);
		this.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{resultPane, lblNewLabel_1, lblNewLabel_2, seriesField, btnNewButton, lblNewLabel, predictionSpinner}));
	}

	private JTextField getSeriesField() {
		if(seriesField==null) {
			seriesField = new JTextField();
			seriesField.addActionListener(induceActionListener);
			seriesField.getDocument().addDocumentListener(new DocumentListener() {
				  public void changedUpdate(DocumentEvent e) {
					  inducedDefinition = null;
				  }
				  public void removeUpdate(DocumentEvent e) {
					  inducedDefinition = null;
				  }
				  public void insertUpdate(DocumentEvent e) {
					  inducedDefinition = null;
				  }
				});
			seriesField.setToolTipText("Provide a series of numbers separated by commas (,). Please note that number series used for the KI2012 paper had to have at least 12 numbers.");
			seriesField.setText("1,2,3,4,5,6,7,8,9,10,11,12");
			seriesField.setColumns(10);
		}
		return seriesField;
	}

	private JTextPane getResultPane() {
		if(resultPane==null) {
			resultPane = new JTextPane();
			resultPane.setEditable(false);
		}
		return resultPane;
	}

	private JSpinner getPredictionSpinner() {
		if(predictionSpinner==null) {
			predictionSpinner = new JSpinner();
			predictionSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if(inducedDefinition!=null)
						predictSeries();
					
				}
			});
			predictionSpinner.setModel(getPredictionSpinnerModel());
		}
		return predictionSpinner;
	}

	private SpinnerNumberModel getPredictionSpinnerModel() {
		if(predictionSpinnerModel==null)
			predictionSpinnerModel = new SpinnerNumberModel(new Integer(3), new Integer(1), null, new Integer(1));
		return predictionSpinnerModel;
	}

	private void induceSeries() {
		try {
			final NumberSeries forInduction = NumberSeries.fromString(getSeriesField().getText());
			inductionSize = forInduction.size();
			getResultPane().setText("Starting number series induction ...");
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					inducedDefinition = inducer.induce(forInduction, true);
				
				
					if(inducedDefinition == null) {
						getResultPane().setText("Induction finished. No solution found!");
					} else {
						predictSeries();
						getResultPane().setCaretPosition(0);
					}
					
					
				}
			});
			
		} catch (NumberFormatException e) {
			// ToDo: show text in resultRane!	
			getResultPane().setText("Could not parse number series: "+ e.getMessage());
			inducedDefinition=null;
		}
		
		
	}

	private void predictSeries() {
		String numbers=null;
		try {
			inducedSeries = inducedDefinition.produce(inductionSize+getPredictionSpinnerModel().getNumber().intValue());
			numbers = inducedSeries.toString();
		} catch (NumberSeriesGenerationException nsgEx) {
			inducedSeries = null;
			numbers = String.format("Could not show the %d following numbers: %s", 
					getPredictionSpinnerModel().getNumber().intValue(),
					nsgEx.getCause().getMessage());
		}
		
		
		getResultPane().setText(String.format("%s\n\n%s", inducedDefinition.toString(),numbers));
	}
}
