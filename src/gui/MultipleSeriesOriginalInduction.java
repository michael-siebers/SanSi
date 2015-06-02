package gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import series.NumberSeries;
import series.NumberSeriesDefinition;
import series.SimpleNumberSeriesDefinition;
import series.inducers.BetterSimpleNumberSeriesInducer;
import expressions.exceptions.NumberSeriesGenerationException;

public class MultipleSeriesOriginalInduction extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8975959433916448596L;
	
	private JTextField seriesField;
	private JTextPane resultPane;
	
	private BetterSimpleNumberSeriesInducer inducer = new BetterSimpleNumberSeriesInducer();
	private SimpleNumberSeriesDefinition inducedDefinition=null;
	private NumberSeries inducedSeries=null;
	private List<SimpleNumberSeriesDefinition> collectedSeries = new LinkedList<SimpleNumberSeriesDefinition>();
	
	private JSpinner predictionSpinner;
	private SpinnerNumberModel predictionSpinnerModel;
	
	
	private JList<String> listSeriesDefinitions;
	
	private final ActionListener induceActionListener = new ActionListener() {
		// Run when button is pressed!
		public void actionPerformed(ActionEvent arg0) {
			collectSeries();
		}
	};
	
	private int inductionSize;

	

	
	

	public MultipleSeriesOriginalInduction() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.setTitle("Semi-Analytic Number Series Inducer");
		this.setBounds(100, 100, 479, 438);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Number Series:");
		
		lblNewLabel.setLabelFor(getSeriesField());
		
		JButton btnInduce = new JButton("Induce");
		btnInduce.addActionListener(induceActionListener);
		
		
		JScrollPane resultScrollPane = new JScrollPane();
		resultScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JScrollPane seriesScrollPane = new JScrollPane();
		seriesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		listSeriesDefinitions = new JList<String>();
		listSeriesDefinitions.setVisibleRowCount(5);
		listSeriesDefinitions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSeriesDefinitions.setLayoutOrientation(JList.VERTICAL);
		listSeriesDefinitions.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				predictSeries();
				
			}
		});
		

		
		
		
		
		JButton btnPredict = new JButton("Predict");
		btnPredict.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				predictSeries();
				
			}
		});
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		
		GroupLayout groupLayout =  new GroupLayout(this.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblNewLabel)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(getSeriesField(), GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED))
								.addComponent(resultScrollPane, GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(seriesScrollPane, GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addContainerGap())
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnInduce, GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
							.addContainerGap())
						.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnPredict, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(getSeriesField(), GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnInduce)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(seriesScrollPane, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPredict)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(resultScrollPane, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JLabel lblNewLabel_1 = new JLabel("Show next");
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		
		lblNewLabel_1.setLabelFor(getPredictionSpinner());
		panel.add(getPredictionSpinner());
		
		
		JLabel lblNewLabel_2 = new JLabel("numbers!");
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setLabelFor(getPredictionSpinner());
		this.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{resultPane, lblNewLabel_1, lblNewLabel_2, seriesField, btnInduce, lblNewLabel, predictionSpinner}));
		resultScrollPane.setViewportView(getResultPane());
		seriesScrollPane.setViewportView(listSeriesDefinitions);
		this.getContentPane().setLayout(groupLayout);
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

	private void collectSeries() {
		try {
			final NumberSeries forInduction = NumberSeries.fromString(getSeriesField().getText());
			inductionSize = forInduction.size();
			getResultPane().setText("Starting number series induction ...");
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					collectedSeries.clear();
					List<SimpleNumberSeriesDefinition> defs = inducer.collectDefinitions(forInduction, true);
					
				
					if(defs.isEmpty()) {
						getResultPane().setText("Induction finished. No solution found!");
					} else {
						collectedSeries.addAll(defs);
						
						String[] data = new String[collectedSeries.size()];
						int pos =0;
						for(SimpleNumberSeriesDefinition def : collectedSeries) {
							StringBuilder value = new StringBuilder();
							NumberSeries initial = def.getInitial();
							
							for(int ipos = 0; ipos < initial.size(); ipos++)
								value.append(String.format("%s(%d) = %d; ", "x", pos, initial.get(ipos)));
							
							value.append("x").append("(n) = ").append(def.getExpression().formatExpression("x", 1));
							
							data[pos++] = value.toString();
						}
						listSeriesDefinitions.setListData(data);
						
						predictSeries();
						getResultPane().setCaretPosition(0);
					}
					
					
				}
			});
			
		} catch (NumberFormatException e) {
			getResultPane().setText("Could not parse number series: "+ e.getMessage());
		}
		
		
	}

	private void predictSeries() {
		int selected = listSeriesDefinitions.getSelectedIndex();
		if(selected == -1) {
			getResultPane().setText("No definition selected.");
			return;
		}
		

		try {
			
			
			NumberSeriesDefinition def = collectedSeries.get(selected);
			
			inducedSeries = def.produce(inductionSize+getPredictionSpinnerModel().getNumber().intValue());
			getResultPane().setText(inducedSeries.toString());
		} catch (NumberSeriesGenerationException nsgEx) {
			inducedSeries = null;
			getResultPane().setText(String.format("Could not show the %d following numbers: %s", 
					getPredictionSpinnerModel().getNumber().intValue(),
					nsgEx.getCause().getMessage()));
			}

	}
}
