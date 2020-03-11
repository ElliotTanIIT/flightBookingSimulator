import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JCheckBox;

public class showFlightsJFrame extends JFrame {
	/** components and frames for GUI*/
	private JPanel contentPane;
	private JLabel lblFlights;
	private JLabel lblSearchByTime;
	private JLabel lblMaxPrice;
	private JButton btnSearch;

	private JCheckBox cbReturnFlight;
	private JCheckBox cbEcon;
	private JCheckBox cbOrderByPrice;
	private JCheckBox cbOrderByTime;

	private JTextField timeInput;
	private JTextField maxPriceInput;
	private JTextField destinationIATAInput;
	private JTextField dateInput;
	private JTextField departureIATAInput;
	
	private JLabel lblDepartureIATA;
	private JLabel lblDate;
	private JLabel lblDestinationIATA;
	private JLabel lblTimeInvalid;
	private JLabel lblMaxPriceInvalid;
	private JLabel lblDateInstruction;
	private JLabel lblDateInvalid;
	private JTable tableFlightsOut;
	private JTable tableFlightsReturn;
	private JScrollPane scrollPaneOut;
	private JScrollPane scrollPaneReturn;
	
	/** login variables */
	private final String url = "jdbc:postgresql://localhost/airlines";
    private final String user = "postgres";
    private final String password = "E11iotPostgres";//"<add your password>";
	
	/** variables */
	private int flightTime = -1; 	// duration of flight
	private double maxPrice = -1;	// maximum price of ticket
	private String destinationIATA;	// destination location
	private String departureIATA;	// departure location
	private String date;			// date
	private JLabel lblflights1;
	private JLabel lblflights2;
	private JButton btnBookFlight;
	private boolean showBookingWindow = false;
	

	//private boolean returnFlight = false; // boolean if user wants a return flight





	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					showFlightsJFrame frame = new showFlightsJFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public showFlightsJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1182, 745);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblFlights = new JLabel("Flights:");
		lblFlights.setFont(new Font("Verdana", Font.PLAIN, 17));
		lblFlights.setBounds(12, 13, 66, 44);
		contentPane.add(lblFlights);
		
		lblSearchByTime = new JLabel("flight duration:");
		lblSearchByTime.setBounds(12, 68, 98, 16);
		contentPane.add(lblSearchByTime);
		
		lblMaxPrice = new JLabel("maximum price: ");
		lblMaxPrice.setBounds(12, 97, 98, 16);
		contentPane.add(lblMaxPrice);
		
		btnSearch = new JButton("search/refresh");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblTimeInvalid.setVisible(false);
				lblMaxPriceInvalid.setVisible(false);
				lblDateInvalid.setVisible(false);
				// time of flight
				if (!timeInput.getText().isEmpty()) {
					try {
						flightTime = Integer.parseInt(timeInput.getText());
					} catch (Exception e) {
						lblTimeInvalid.setVisible(true);
						flightTime = -1;
					}
				}
				else {
					flightTime = -1;
				}
				// max price
				if (!maxPriceInput.getText().isEmpty()) {
					try {
						maxPrice = Integer.parseInt(maxPriceInput.getText());
					} catch (Exception e) {
						lblMaxPriceInvalid.setVisible(true);
						maxPrice = -1;
					}
				}
				else {
					maxPrice = -1;
				}
				// departure
				if (!departureIATAInput.getText().isEmpty()) {
					departureIATA = departureIATAInput.getText();
				} 
				else {
					departureIATA = null;
				}
				// destnation
				if (!destinationIATAInput.getText().isEmpty()) {
					destinationIATA = destinationIATAInput.getText();
				} 
				else {
					destinationIATA = null;
				}
				// date
				if (!dateInput.getText().isEmpty()) {
					String[] parseDate = dateInput.getText().split("/", 3);
					if (parseDate[0].length() != 2 || parseDate[1].length() != 2 || parseDate[2].length() != 4) {
						lblDateInvalid.setVisible(true);
						return;
					}
					int checkDay;
					int checkMth;
					int checkYr;
					try {
						checkDay = Integer.parseInt(parseDate[0]);
						checkMth = Integer.parseInt(parseDate[1]);
						checkYr = Integer.parseInt(parseDate[2]);
					} catch (Exception e){
						lblDateInvalid.setVisible(true);
						return;
					}
					if (checkDay > 31 || checkDay < 0 || checkMth > 12 || checkMth < 0 || checkYr < 0) {
						lblDateInvalid.setVisible(true);
						return;
					}
					if (checkDay > 30) {
						if (checkMth != 1
								|| checkMth != 3
								|| checkMth != 5
								|| checkMth != 7
								|| checkMth != 8
								|| checkMth != 10
								|| checkMth != 12) {
							lblDateInvalid.setVisible(true);
							return;
						}
					}
					else if (checkDay > 29) {
						if (checkMth == 2) {
							lblDateInvalid.setVisible(true);
							return;
						}
					}
					date = dateInput.getText();
				} 
				else {
					date = null;
				}
				// making the query
				String queryFlights = makeFlightsQuery(flightTime, maxPrice, departureIATA, destinationIATA, date, cbEcon.isSelected(), cbOrderByPrice.isSelected(), cbOrderByTime.isSelected()); 
				
				// populate tableFlightsOut
				try (Connection conn = DriverManager.getConnection(url, user, password);
			 		    Statement stmt = conn.createStatement();
			 			ResultSet rset = stmt.executeQuery(queryFlights); ){
					tableFlightsOut = new JTable(buildTableModel(rset));
					scrollPaneOut.setViewportView(tableFlightsOut);
					// how to display table
		    	 }catch (SQLException e) {
		    		 System.out.println(e.getMessage());
		    	 }
				
				if (cbReturnFlight.isSelected()) {
					// if need return flights, just swap destination and departure IATAs
					String queryReturnFlights = makeFlightsQuery(flightTime, maxPrice, destinationIATA, departureIATA, date, cbEcon.isSelected(), cbOrderByPrice.isSelected(), cbOrderByTime.isSelected());
					
					try (Connection conn = DriverManager.getConnection(url, user, password);
				 		    Statement stmt = conn.createStatement();
				 			ResultSet rset = stmt.executeQuery(queryReturnFlights); ){
						tableFlightsReturn = new JTable(buildTableModel(rset));
						scrollPaneReturn.setViewportView(tableFlightsReturn);
						// how to display table
			    	 }catch (SQLException e) {
			    		 System.out.println(e.getMessage());
			    	}
				}
				
				
			}
		});
		btnSearch.setBounds(1009, 21, 126, 32);
		contentPane.add(btnSearch);
		
		timeInput = new JTextField();
		timeInput.setBounds(122, 65, 116, 22);
		contentPane.add(timeInput);
		timeInput.setColumns(10);
		
		maxPriceInput = new JTextField();
		maxPriceInput.setBounds(122, 94, 116, 22);
		contentPane.add(maxPriceInput);
		maxPriceInput.setColumns(10);
		
		cbReturnFlight = new JCheckBox("return flight?", false);
		cbReturnFlight.setBounds(541, 122, 113, 25);
		contentPane.add(cbReturnFlight);
		// use if cbReturnFlight.isSelected() to return boolean
		
		cbEcon = new JCheckBox("economy?", false);
		cbEcon.setBounds(424, 93, 113, 25);
		contentPane.add(cbEcon);
		
		lblDepartureIATA = new JLabel("departure IATA:");
		lblDepartureIATA.setBounds(12, 126, 98, 16);
		contentPane.add(lblDepartureIATA);
		
		departureIATAInput = new JTextField();
		departureIATAInput.setBounds(122, 123, 116, 22);
		contentPane.add(departureIATAInput);
		departureIATAInput.setColumns(10);
		
		lblDestinationIATA = new JLabel("destination IATA:");
		lblDestinationIATA.setBounds(12, 155, 108, 16);
		contentPane.add(lblDestinationIATA);
		
		destinationIATAInput = new JTextField();
		destinationIATAInput.setBounds(122, 152, 116, 22);
		contentPane.add(destinationIATAInput);
		destinationIATAInput.setColumns(10);
		
		lblDate = new JLabel("date: ");
		lblDate.setBounds(12, 184, 56, 16);
		contentPane.add(lblDate);
		
		dateInput = new JTextField();
		dateInput.setBounds(122, 181, 116, 22);
		contentPane.add(dateInput);
		dateInput.setColumns(10);
		
		lblTimeInvalid = new JLabel("time is invalid!");
		lblTimeInvalid.setBounds(282, 68, 89, 16);
		contentPane.add(lblTimeInvalid);
		lblTimeInvalid.setVisible(false);
		
		lblMaxPriceInvalid = new JLabel("price is invalid!");
		lblMaxPriceInvalid.setBounds(282, 97, 89, 16);
		contentPane.add(lblMaxPriceInvalid);
		lblMaxPriceInvalid.setVisible(false);
		
		lblDateInstruction = new JLabel("please enter date with format: DD/MM/YYYY");
		lblDateInstruction.setBounds(282, 184, 255, 16);
		contentPane.add(lblDateInstruction);
		
		lblDateInvalid = new JLabel("date is invalid!");
		lblDateInvalid.setBounds(122, 212, 98, 16);
		contentPane.add(lblDateInvalid);
		lblDateInvalid.setVisible(false);
		
		cbOrderByPrice = new JCheckBox("order by price (higher priority)");
		cbOrderByPrice.setBounds(541, 93, 214, 25);
		contentPane.add(cbOrderByPrice);
		
		cbOrderByTime = new JCheckBox("order by time");
		cbOrderByTime.setBounds(541, 64, 113, 25);
		contentPane.add(cbOrderByTime);
		
		lblflights1 = new JLabel("Outgoing flights");
		lblflights1.setBounds(22, 238, 108, 16);
		contentPane.add(lblflights1);
		
		lblflights2 = new JLabel("Return flights");
		lblflights2.setBounds(22, 464, 88, 16);
		contentPane.add(lblflights2);
		
		scrollPaneOut = new JScrollPane();
		scrollPaneOut.setBounds(22, 261, 1113, 190);
		contentPane.add(scrollPaneOut);
		
		scrollPaneReturn = new JScrollPane();
		scrollPaneReturn.setBounds(22, 493, 1113, 190);
		contentPane.add(scrollPaneReturn);
		
	}
	
	/** make flight query */
	public static String makeFlightsQuery(int ft, double maxPrice, String depIATA, String destIATA, String dt, boolean cbEconSelected, boolean cbOrderByPriceSelected, boolean cbOrderByTimeSelected) {
		String queryFlights = "SELECT * FROM Flight NATURAL JOIN Price"; 
		String flightTimeq = "";
		String maxPriceq = "";
		String departureIATAq = "";
		String destinationIATAq = "";
		String dateq = "";
		
		if (ft >= 0) {
			flightTimeq = "time_diff < " + ft + " ";
		}
		if (maxPrice >= 0) {
			if(cbEconSelected) {
				maxPriceq = "price_econ <= " + maxPrice + " ";
			}else {
				maxPriceq = "price_FC <= " + maxPrice + " ";
			}
		}
		if (depIATA != null) {
			departureIATAq = "departure_IATA = '" + depIATA + "' ";
		}
		if (destIATA != null) {
			destinationIATAq = "destination_IATA = '" + destIATA + "' ";
		}
		if (dt != null) {
			dateq = "date_of_flight = '" + dt + "' ";
		}
		// check which to attach to query first
		int attach = 0;
		if (!flightTimeq.isEmpty()) {
			queryFlights += " WHERE " + flightTimeq;
			attach = 1;
		}else if (!maxPriceq.isEmpty()){
			queryFlights += " WHERE " +maxPriceq;
			attach = 2;
		}else if (!departureIATAq.isEmpty()){
			queryFlights += " WHERE " + departureIATAq;
			attach = 3;
		}else if (!destinationIATAq.isEmpty()) {
			queryFlights += " WHERE " + destinationIATAq;
			attach = 4;
		}else if (!dateq.isEmpty()) {
			queryFlights += " WHERE " + dateq;
		}
		// check which condition was added first, then start from next condition and add if necessary
		if (attach == 1) {
			if (!maxPriceq.isEmpty()) {
				queryFlights += " AND " + maxPriceq;
			}
			if (!departureIATAq.isEmpty()) {
				queryFlights += " AND " + departureIATAq;
			}
			if (!destinationIATAq.isEmpty()) {
				queryFlights += " AND " + destinationIATAq;
			}
			if (!dateq.isEmpty()) {
				queryFlights += " AND " + dateq;
			}
		} else if (attach == 2) {
			if (!departureIATAq.isEmpty()) {
				queryFlights += " AND " + departureIATAq;
			}
			if (!destinationIATAq.isEmpty()) {
				queryFlights += " AND " + destinationIATAq;
			}
			if (!dateq.isEmpty()) {
				queryFlights += " AND " + dateq;
			}
		} else if (attach == 3) {
			if (!destinationIATAq.isEmpty()) {
				queryFlights += " AND " + destinationIATAq;
			}
			if (!dateq.isEmpty()) {
				queryFlights += " AND " + dateq;
			}
		} else if (attach == 4) {
			if (!dateq.isEmpty()) {
				queryFlights += " AND " + dateq;
			}
		}
		if (cbOrderByPriceSelected) {
			if (cbEconSelected) {
				queryFlights += " ORDER BY price_econ";
			}else {
				queryFlights += " ORDER BY price_FC";
			}
		}else if (cbOrderByTimeSelected) {
			queryFlights += " ORDER BY time_diff";
		}
		queryFlights += ";";
		return queryFlights;
	}
	
	/** method poulates a Jtable with sql result set */
	public static DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
}
