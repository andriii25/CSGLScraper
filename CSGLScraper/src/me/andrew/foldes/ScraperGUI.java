package me.andrew.foldes;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class ScraperGUI extends JFrame
{
	

	public JTable table;
	public DefaultTableModel model;
	private JTextField urlTextField;
	private CSGLScraper scraper = new CSGLScraper();
	private JButton scrapeButton;
	private JTextField keyTextField;
	private boolean bShouldContain = true;
	private ArrayList<ArrayList<String>> trades;
	final JLabel workingLabel;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public ScraperGUI()
	{
		setTitle("CS:GO Lounge Scraper and Filter v1.0");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 125, 700, 400);
		try
		{
			setIconImage(ImageIO.read(new File("res/iconImage.png")));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		
		urlTextField = new JTextField();
		urlTextField.setBounds(10, 33, 567, 25);
		urlTextField.setColumns(10);
		
		JLabel urlLabel = new JLabel("CSGO Lounge Tradesearch Link (If results have multiple pages it must be #2)");
		urlLabel.setBounds(10, 10, 700, 25);
		urlLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		
		JLabel keyLabel = new JLabel("No. of keys");
		keyLabel.setBounds(530, 66, 80, 25);
		keyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		
		keyTextField = new JTextField();
		keyTextField.setBounds(10, 89, 567, 25);
		keyTextField.setColumns(10);
		
		final JToggleButton shouldContain = new JToggleButton("Contains");
		shouldContain.setBounds(614, 89, 96, 25);
		shouldContain.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				if (shouldContain.isSelected())
				{
					bShouldContain = false;
					shouldContain.setText("Not contains");
				} else
				{
					bShouldContain = true;
					shouldContain.setText("Contains");
				}
				
			}
		});
		
		final JSpinner keyNoSpinner = new JSpinner();
		keyNoSpinner.setBounds(580, 89, 30, 25);
		keyNoSpinner.addChangeListener(new ChangeListener() 
		{
			public void stateChanged(ChangeEvent arg0) 
			{
				if ((Integer)keyNoSpinner.getValue() > 0)
				{
					keyTextField.setEditable(false);
					//shouldContain.disable();
				}
				else if ((Integer)keyNoSpinner.getValue() == 0) 
				{
					keyTextField.setEditable(true);
				}
			}
		});
		keyNoSpinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		
		JLabel keysTextField = new JLabel("Words that should be (not) contained. Separate them with spaces");
		keysTextField.setBounds(10, 66, 460, 25);
		keysTextField.setFont(new Font("Arial", Font.PLAIN, 16));
		
		workingLabel = new JLabel("");
		workingLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		workingLabel.setBounds(256, 536, 200, 35);
		getContentPane().add(workingLabel);
		
		model = new DefaultTableModel()
		{
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return false;
				
			}
		};
		model.addColumn("Tradelinks");
		model.addColumn("Trade Context");
		scrapeButton = new JButton("Get trades");
		scrapeButton.setBounds(580, 33, 130, 25);
		scrapeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				//workingLabel.setText("Working...");
				
				String url = urlTextField.getText();
				String keyListS = keyTextField.getText();
				scrapeButton.setText("Working...");
				StringTokenizer st = new StringTokenizer(keyListS);
				ArrayList<String> keyList = new ArrayList<String>();
				int keyNo = (Integer) keyNoSpinner.getValue();
				if (keyNo > 0)
				{
					keyList.add(keyNo + "k");
					keyList.add(keyNo + " k");
				}
				else 
				{
					while (st.hasMoreTokens())
					{
						keyList.add(st.nextToken());
					}
				}
				
				trades = scraper.Scrape(url, keyList, bShouldContain);
				
				for (int i = model.getRowCount() - 1; i >= 0; i--)
				{
					model.removeRow(i);
				}
				for (int i = 0; i < trades.get(0).size(); i++)
				{
					model.addRow(new Object[]{trades.get(0).get(i), trades.get(1).get(i)});
				}
				workingLabel.setText("Finished!");
				//scrapeButton.setText("Get trades");
			}
		});
		
		table = new JTable(model);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				int row = table.getSelectedRow();
				int col = table.getSelectedColumn();
				if (col == 0)
				{
					String url = "http://" + trades.get(0).get(row);
					try 
					{
						Desktop.getDesktop().browse(new URL(url).toURI());
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);

					}
				}
			}
		});
		/*table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Tradelink", "Trade context"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});*/
		table.getColumnModel().getColumn(0).setPreferredWidth(225);
		table.getColumnModel().getColumn(0).setMinWidth(225);
		table.getColumnModel().getColumn(0).setMaxWidth(225);
		table.getColumnModel().getColumn(1).setPreferredWidth(475);
		table.getColumnModel().getColumn(1).setMinWidth(475);
		table.getColumnModel().getColumn(1).setMaxWidth(475);
		scrollPane.setViewportView(table);
		getContentPane().setLayout(null);
		getContentPane().add(keyLabel);
		getContentPane().add(keyNoSpinner);
		getContentPane().add(urlTextField);
		getContentPane().add(urlLabel);
		getContentPane().add(scrapeButton);
		getContentPane().add(keysTextField);
		getContentPane().add(keyTextField);
		getContentPane().add(shouldContain);
		getContentPane().add(scrollPane);
		
		JButton openLinkButton = new JButton("Open all links in browser");
		openLinkButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				for (int i = 0; i < trades.get(0).size(); i++)
				{
					try 
					{
						String url = "http://" + trades.get(0).get(i);
						Desktop.getDesktop().browse(new URL(url).toURI());
					} catch (Exception e)
					{
						JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		openLinkButton.setBounds(10, 536, 200, 35);
		getContentPane().add(openLinkButton);
		
		JButton aboutButton = new JButton("About");
		aboutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				JOptionPane.showMessageDialog(null, "Made by andriii25\nVersion 1.0\nhttps://github.com/andriii25/CSGLScraper", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		aboutButton.setBounds(510, 536, 200, 35);
		getContentPane().add(aboutButton);
		

		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		//frame = new JFrame();
		setBounds(100, 100, 736, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
