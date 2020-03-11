import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainFlightApp {
	/** components and frames for GUI*/
	private JFrame frame;
	private JTextField usernameInput;
	private JLabel lblUsername;
	private JLabel lblName;
	private JLabel lblEmailNewUser;
	private JLabel lblRegisterNewUser;
	private JTextField inputNewUserEmail;
	private JTextField inputNewUserName;
	private JLabel lblCardNumber;
	private JLabel editUserCreditCards;
	private JTextField inputCardNumber;
	private JLabel lblCurrentUser;
	private JLabel lblShowCurrentUser;
	private JButton btnShowFlights;
	private showFlightsJFrame showFlights = new showFlightsJFrame();
	private JButton btnAddNewCard;
	private JButton btnDeleteCard;
	private JButton btnShowBookings;
	private JButton btnRegister;
	private JButton loginLogoutButton;
	private JLabel lblRegisterError;
	private JLabel lblRegisterSuccessful;
	private JLabel lblNotLoggedIn;
	private JLabel lblcardRestrictions;
	private JLabel lblAddress;
	private JTextField inputAddress;
	private JLabel lblNeedAddress;
	private JButton btnModAddress;
	private JLabel lblAddCardSuccess;
	private JLabel lblCardNumberInvalid;
	private JLabel lblDelCardSuccess;
	private JLabel lblModSuccess;
	private JButton btnBookFlight;
	private JScrollPane scrollPaneBookings;
	private JTable bookingsTable;
	/** login variables */
	private final String url = "jdbc:postgresql://localhost/airlines";
    private final String user = "postgres";
    private final String password = "E11iotPostgres";//"<add your password>";
	/** variables */
	private String currentUser = "";
	private boolean showingFlights = false;
	private boolean showingBookings = false;
	private JTextField inputFlightNum;
	private JLabel lblBookingInstructions;
	private JLabel lblCardNumInst;
	private JTextField inputCardNum;
	private JLabel lblClassIns;
	private JLabel lblInvalidSeatType;
	private JLabel lblInvalidCard;
	private JTextField inputSeatType;
	private JLabel lblinvalidFlight;
	private JLabel lblBookingSuccess;
	private JLabel lblcancelBookingPrompt;
	private JTextField inputCancelBookingFlightNum;
	private JButton btnCancelBooking;
	private JLabel lblBookingCancelSuccess;
	private JLabel lblBookingNotFound;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFlightApp window = new MainFlightApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFlightApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1179, 624);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// Login/Logout button action
		loginLogoutButton = new JButton("Login");
		loginLogoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentUser.isEmpty()) {
					String userInput = usernameInput.getText();
					String getUsers = "SELECT * FROM Customer";
					try (Connection conn = DriverManager.getConnection(url, user, password);
				 		    Statement stmt = conn.createStatement();
				 			ResultSet rset = stmt.executeQuery(getUsers); ){
						while(rset.next()) {
							if (userInput.equals(rset.getString(1))) {
								currentUser = userInput;
								String currentUserName = rset.getString(2);
								lblShowCurrentUser.setText(currentUser + "          Hello " + currentUserName + "!");
								loginLogoutButton.setText("Logout");
								break;
							}
						}
						if (currentUser.isEmpty()) {
							lblShowCurrentUser.setText("user not found");
						}
			   	 	}catch (SQLException e1) {
//			   	 		System.out.println(e1.getMessage());
			   	 	}
				} 
				else { // logout
					currentUser = "";
					loginLogoutButton.setText("Login");
					usernameInput.setText("");
					lblShowCurrentUser.setText("Goodbye!");
					showingFlights = false;
					showFlights.setVisible(showingFlights);
					btnShowFlights.setText("Show Flights");		
					inputNewUserEmail.setText("");
					inputNewUserName.setText("");
					inputAddress.setText("");
					inputCardNumber.setText("");
					bookingsTable = new JTable();
					scrollPaneBookings.setViewportView(bookingsTable);
					inputFlightNum.setText("");
					inputCardNum.setText("");
					inputSeatType.setText("");
					lblBookingCancelSuccess.setVisible(false);
					lblBookingNotFound.setVisible(false);
					inputCancelBookingFlightNum.setText("");
				}
			}
		});
		loginLogoutButton.setBounds(188, 13, 97, 25);
		frame.getContentPane().add(loginLogoutButton);
		
		usernameInput = new JTextField();
		usernameInput.setBounds(55, 14, 116, 22);
		frame.getContentPane().add(usernameInput);
		usernameInput.setColumns(10);
		
		lblUsername = new JLabel("email:");
		lblUsername.setBounds(12, 13, 72, 25);
		frame.getContentPane().add(lblUsername);
		
		lblRegisterNewUser = new JLabel("register new user:");
		lblRegisterNewUser.setBounds(12, 54, 183, 16);
		frame.getContentPane().add(lblRegisterNewUser);
		
		lblEmailNewUser = new JLabel("email:");
		lblEmailNewUser.setBounds(12, 83, 36, 16);
		frame.getContentPane().add(lblEmailNewUser);
		
		inputNewUserEmail = new JTextField();
		inputNewUserEmail.setBounds(55, 80, 230, 22);
		frame.getContentPane().add(inputNewUserEmail);
		inputNewUserEmail.setColumns(10);
		
		lblName = new JLabel("name:");
		lblName.setBounds(310, 83, 44, 16);
		frame.getContentPane().add(lblName);
		
		inputNewUserName = new JTextField();
		inputNewUserName.setBounds(352, 80, 116, 22);
		frame.getContentPane().add(inputNewUserName);
		inputNewUserName.setColumns(10);
		
		editUserCreditCards = new JLabel("enter the card number of a credit card you want to delete or add to your account:");
		editUserCreditCards.setBounds(12, 115, 483, 16);
		frame.getContentPane().add(editUserCreditCards);
		
		inputCardNumber = new JTextField();
		inputCardNumber.setBounds(108, 144, 246, 22);
		frame.getContentPane().add(inputCardNumber);
		inputCardNumber.setColumns(10);
		
		lblCardNumber = new JLabel("card number: ");
		lblCardNumber.setBounds(12, 147, 89, 16);
		frame.getContentPane().add(lblCardNumber);
		
		lblCurrentUser = new JLabel("current user:");
		lblCurrentUser.setBounds(310, 17, 81, 16);
		frame.getContentPane().add(lblCurrentUser);
		
		lblShowCurrentUser = new JLabel("......");
		lblShowCurrentUser.setBounds(403, 17, 409, 16);
		frame.getContentPane().add(lblShowCurrentUser);
		
		btnShowFlights = new JButton("Show Flights");
		btnShowFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (showingFlights) { // if flight frame is already up
					showFlights.setVisible(showingFlights);
					btnShowFlights.setText("Hide Flights");
					showingFlights = false;
				}else { // flight frame is not up
					showFlights.setVisible(showingFlights);
					btnShowFlights.setText("Show Flights");
					showingFlights = true;
				}
				
			}
		});
		btnShowFlights.setBounds(932, 106, 146, 91);
		frame.getContentPane().add(btnShowFlights);
		
		btnAddNewCard = new JButton("add card");
		// adding a card
		btnAddNewCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNotLoggedIn.setVisible(false);
				lblNeedAddress.setVisible(false);
				lblAddCardSuccess.setVisible(false);
				lblCardNumberInvalid.setVisible(false);
				lblDelCardSuccess.setVisible(false);
				if (currentUser.isEmpty()) {
					lblNotLoggedIn.setVisible(true);
					return;
				} 
				else if (inputCardNumber.getText().isEmpty() || inputCardNumber.getText().length() != 6) {
					lblCardNumberInvalid.setVisible(true);
					return;
				}
				else if (inputAddress.getText().isEmpty()) {
					lblNeedAddress.setVisible(true);
					return;
				} 
				else {
					try {
						int x = Integer.parseInt(inputCardNumber.getText());
					}catch (Exception notNumber) {
						lblCardNumberInvalid.setVisible(true);
						return;
					}					
					String insertCardq = "INSERT INTO Credit_Address VALUES (" + inputCardNumber.getText() + ", '" + inputAddress.getText() + "', '" + currentUser + "');";
					try (Connection conn = DriverManager.getConnection(url, user, password);
				 		    Statement stmt = conn.createStatement();){
				 		conn.setAutoCommit(false);
				        stmt.executeUpdate(insertCardq);
				        conn.commit();
						lblAddCardSuccess.setVisible(true);
			       }catch (SQLException e1) {
			    	   lblCardNumberInvalid.setVisible(true);
			       }
				}
			}
		});
		btnAddNewCard.setBounds(366, 144, 117, 25);
		frame.getContentPane().add(btnAddNewCard);
		
		btnDeleteCard = new JButton("delete card");
		// deleting a card
		btnDeleteCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNotLoggedIn.setVisible(false);
				lblNeedAddress.setVisible(false);
				lblAddCardSuccess.setVisible(false);
				lblCardNumberInvalid.setVisible(false);
				lblDelCardSuccess.setVisible(false);
				if (currentUser.isEmpty()) {
					lblNotLoggedIn.setVisible(true);
					return;
				} 
				else if (inputCardNumber.getText().isEmpty() || inputCardNumber.getText().length() != 6) {
					lblCardNumberInvalid.setVisible(true);
					return;
				}
				else {
					try {
						int x = Integer.parseInt(inputCardNumber.getText());
					}catch (Exception notNumber) {
						lblCardNumberInvalid.setVisible(true);
						return;
					}
					String checkUserq = "SELECT email FROM Credit_Address WHERE credit_card_number = " + inputCardNumber.getText() + ";";
					try (Connection conn = DriverManager.getConnection(url, user, password);
							Statement stmt = conn.createStatement();
				 			ResultSet rset = stmt.executeQuery(checkUserq);){
						rset.next();
						System.out.println(currentUser);
						System.out.println(rset.getString("email"));
						
						if (!(rset.getString("email").equals(currentUser))) {
							lblCardNumberInvalid.setVisible(true);
							return;
						}
					}catch (SQLException e2) {
						lblCardNumberInvalid.setVisible(true);
						return;
					}
					String deleteCardq = "DELETE FROM Credit_Address WHERE credit_card_number = " + inputCardNumber.getText() + ";";
					try (Connection conn = DriverManager.getConnection(url, user, password);
				 		    Statement stmt = conn.createStatement();){
				 		conn.setAutoCommit(false);
				        stmt.executeUpdate(deleteCardq);
				        conn.commit();
				        lblDelCardSuccess.setVisible(true);
			       }catch (SQLException e1) {
			    	   lblCardNumberInvalid.setVisible(true);
			       }
				}
				inputCardNumber.setText("");
			}
		});
		btnDeleteCard.setBounds(495, 144, 115, 25);
		frame.getContentPane().add(btnDeleteCard);
		
		btnShowBookings = new JButton("Show/refresh My bookings");
		btnShowBookings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String queryBookings = "SELECT * FROM Booking NATURAL JOIN Flight WHERE email = '" + currentUser + "';";
				try (Connection conn = DriverManager.getConnection(url, user, password);
			 		    Statement stmt = conn.createStatement();
			 			ResultSet rset = stmt.executeQuery(queryBookings); ){
					bookingsTable = new JTable(buildTableModel(rset));
					scrollPaneBookings.setViewportView(bookingsTable);
		    	 }catch (SQLException e2) {
//		    		 System.out.println(e2.getMessage());
		    	 }
			}
		});
		btnShowBookings.setBounds(1003, 436, 146, 34);
		frame.getContentPane().add(btnShowBookings);
		
		// register new user
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblRegisterError.setVisible(false);
				lblRegisterSuccessful.setVisible(false);
				// rewrite conditions
				if (((inputNewUserEmail.getText() == null) || (inputNewUserEmail.getText().isEmpty())) || ((inputNewUserName.getText() == null) || (inputNewUserName.getText().isEmpty()))){
					lblRegisterError.setVisible(true);				 	
				} else {
					String registerNewUserq = "INSERT INTO Customer values ('" + inputNewUserEmail.getText() + "', '" + inputNewUserName.getText() + "');";
					try (Connection conn = DriverManager.getConnection(url, user, password);
				 		    Statement stmt = conn.createStatement();){
				 		conn.setAutoCommit(false);
				        stmt.executeUpdate(registerNewUserq);
				        conn.commit();
						lblRegisterSuccessful.setVisible(true);
			       }catch (SQLException regError) {
			    	   lblRegisterError.setVisible(true);
			       }
				}				
			}
		});
		btnRegister.setBounds(495, 79, 97, 25);
		frame.getContentPane().add(btnRegister);
		
		btnModAddress = new JButton("Modify Address");
		// modify address attached to card number
		btnModAddress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNotLoggedIn.setVisible(false);
				lblNeedAddress.setVisible(false);
				lblAddCardSuccess.setVisible(false);
				lblCardNumberInvalid.setVisible(false);
				lblDelCardSuccess.setVisible(false);
				if (currentUser.isEmpty()) {
					lblNotLoggedIn.setVisible(true);
					return;
				} 
				else if (inputCardNumber.getText().isEmpty() || inputCardNumber.getText().length() != 6) {
					lblCardNumberInvalid.setVisible(true);
					return;
				}
				else if (inputAddress.getText().isEmpty()) {
					lblNeedAddress.setVisible(true);
					return;
				} 
				else {
					try {
						int x = Integer.parseInt(inputCardNumber.getText());
					}catch (Exception notNumber) {
						lblCardNumberInvalid.setVisible(true);
						return;
					}
					String checkUserq = "SELECT email FROM Credit_Address WHERE credit_card_number = " + inputCardNumber.getText() + ";";
					try (Connection conn = DriverManager.getConnection(url, user, password);
							Statement stmt = conn.createStatement();
				 			ResultSet rset = stmt.executeQuery(checkUserq);){
						rset.next();						
						if (!(rset.getString("email").equals(currentUser))) {
							lblCardNumberInvalid.setVisible(true);
							return;
						}
					}catch (SQLException e2) {
						lblCardNumberInvalid.setVisible(true);
						return;
					}
					String modifyAddressq = "UPDATE Credit_Address SET address = '" + inputAddress.getText() + "' WHERE credit_card_number = " + inputCardNumber.getText() + ";";
					try (Connection conn = DriverManager.getConnection(url, user, password);
				 		    Statement stmt = conn.createStatement();){
				 		conn.setAutoCommit(false);
				        stmt.executeUpdate(modifyAddressq);
				        conn.commit();
				        lblModSuccess.setVisible(true);
			       }catch (SQLException e1) {
			    		 System.out.println(e1.getMessage());
			       }
				}
				
			}
		});
		btnModAddress.setBounds(366, 209, 129, 25);
		frame.getContentPane().add(btnModAddress);
		
		lblRegisterError = new JLabel("Please enter both an email and name");
		lblRegisterError.setBounds(622, 83, 260, 16);
		lblRegisterError.setVisible(false);
		frame.getContentPane().add(lblRegisterError);
		
		lblRegisterSuccessful = new JLabel("You are registered!");
		lblRegisterSuccessful.setBounds(622, 83, 122, 16);
		lblRegisterSuccessful.setVisible(false);
		frame.getContentPane().add(lblRegisterSuccessful);
		
		lblNotLoggedIn = new JLabel("please login before adding/deleting cards");
		lblNotLoggedIn.setBounds(108, 181, 246, 16);
		lblNotLoggedIn.setVisible(false);
		frame.getContentPane().add(lblNotLoggedIn);
		
		lblcardRestrictions = new JLabel("card number must be 6 digits");
		lblcardRestrictions.setBounds(622, 147, 190, 16);
		frame.getContentPane().add(lblcardRestrictions);
		
		lblAddress = new JLabel("billing address:");
		lblAddress.setBounds(12, 213, 89, 16);
		frame.getContentPane().add(lblAddress);
		
		inputAddress = new JTextField();
		inputAddress.setBounds(108, 210, 246, 22);
		frame.getContentPane().add(inputAddress);
		inputAddress.setColumns(10);
		
		lblNeedAddress = new JLabel("please enter an address");
		lblNeedAddress.setBounds(108, 249, 177, 16);
		lblNeedAddress.setVisible(false);
		frame.getContentPane().add(lblNeedAddress);
		
		lblAddCardSuccess = new JLabel("card successfully added");
		lblAddCardSuccess.setBounds(108, 181, 230, 16);
		lblAddCardSuccess.setVisible(false);
		frame.getContentPane().add(lblAddCardSuccess);
		
		lblCardNumberInvalid = new JLabel("card number invalid or already in use");
		lblCardNumberInvalid.setBounds(386, 181, 236, 16);
		lblCardNumberInvalid.setVisible(false);
		frame.getContentPane().add(lblCardNumberInvalid);
		
		lblDelCardSuccess = new JLabel("card successfully deleted");
		lblDelCardSuccess.setBounds(108, 181, 183, 16);
		lblDelCardSuccess.setVisible(false);
		frame.getContentPane().add(lblDelCardSuccess);
		
		lblModSuccess = new JLabel("Billing Address modified!");
		lblModSuccess.setBounds(506, 213, 158, 16);
		lblModSuccess.setVisible(false);
		frame.getContentPane().add(lblModSuccess);
		
		btnBookFlight = new JButton("book flight");
		// booking a flight
		btnBookFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentUser.isEmpty()) {
					return;
				}
				lblinvalidFlight.setVisible(false);
				lblInvalidCard.setVisible(false);
				lblInvalidSeatType.setVisible(false);
				lblBookingSuccess.setVisible(false);
				// check if valid flight number
				String flightNum = "";
				String getFlightNumsq = "SELECT flight_number FROM Flight;";
				try (Connection conn = DriverManager.getConnection(url, user, password);
			 		    Statement stmt = conn.createStatement();
			 			ResultSet rset = stmt.executeQuery(getFlightNumsq); ){
					while(rset.next()) {
						if (inputFlightNum.getText().equals(rset.getString(1))) {
							flightNum = inputFlightNum.getText();
							break;
						}
					}
					if (flightNum.isEmpty()) {
						lblinvalidFlight.setVisible(true);
						return;
					}
		   	 	}catch (SQLException e1) {
		   	 	}
				// check if valid card
				String cardNum = "";
				try {
					int x = Integer.parseInt(inputCardNum.getText());
				}catch (Exception notNumber) {
					lblInvalidCard.setVisible(true);
					return;
				}
				if (inputCardNum.getText().length() != 6) {
					lblInvalidCard.setVisible(true);
					return;
				}
				String getUserCardsq = "SELECT credit_card_number FROM Credit_Address WHERE email = '" + currentUser + "';";
				try (Connection conn = DriverManager.getConnection(url, user, password);
			 		    Statement stmt = conn.createStatement();
			 			ResultSet rset = stmt.executeQuery(getUserCardsq); ){
					while(rset.next()) {
						if (inputCardNum.getText().equals(rset.getString(1))) {
							cardNum = inputCardNum.getText();
							break;
						}
					}
					if (cardNum.isEmpty()) {
						lblInvalidCard.setVisible(true);
						return;
					}
		   	 	}catch (SQLException e1) {
		   	 	}
				
				// check if valid seat type
				String seatType = inputSeatType.getText();
				if (!(seatType.equals("FC") || seatType.equals("Econ"))) {
					lblInvalidSeatType.setVisible(true);
					return;
				}
				
				String addBookingq = "INSERT INTO Booking VALUES ('" + currentUser + "', " + cardNum + ", '" + seatType + "', " + flightNum + ");";
				try (Connection conn = DriverManager.getConnection(url, user, password);
			 		    Statement stmt = conn.createStatement();){
			 		conn.setAutoCommit(false);
			        stmt.executeUpdate(addBookingq);
			        conn.commit();
		       }catch (SQLException g) {
		       }
			lblBookingSuccess.setVisible(true);
			}
		});
		btnBookFlight.setBounds(833, 302, 116, 25);
		frame.getContentPane().add(btnBookFlight);
		
		scrollPaneBookings = new JScrollPane();
		scrollPaneBookings.setBounds(12, 405, 965, 99);
		frame.getContentPane().add(scrollPaneBookings);
		
		inputFlightNum = new JTextField();
		inputFlightNum.setBounds(476, 303, 116, 22);
		frame.getContentPane().add(inputFlightNum);
		inputFlightNum.setColumns(10);
		
		lblBookingInstructions = new JLabel("Please enter the flight number of the flight you want to book:");
		lblBookingInstructions.setBounds(12, 306, 438, 16);
		frame.getContentPane().add(lblBookingInstructions);
		
		lblCardNumInst = new JLabel("Please enter the credit card used:");
		lblCardNumInst.setBounds(12, 335, 298, 16);
		frame.getContentPane().add(lblCardNumInst);
		
		inputCardNum = new JTextField();
		inputCardNum.setBounds(476, 332, 116, 22);
		frame.getContentPane().add(inputCardNum);
		inputCardNum.setColumns(10);
		
		lblClassIns = new JLabel("Please enter either 'FC' or 'Econ':");
		lblClassIns.setBounds(12, 364, 342, 16);
		frame.getContentPane().add(lblClassIns);
		
		lblInvalidSeatType = new JLabel("not a valid seat choice");
		lblInvalidSeatType.setBounds(622, 364, 183, 16);
		lblInvalidSeatType.setVisible(false);
		frame.getContentPane().add(lblInvalidSeatType);
		
		lblInvalidCard = new JLabel("not a valid card");
		lblInvalidCard.setBounds(622, 335, 122, 16);
		lblInvalidCard.setVisible(false);
		frame.getContentPane().add(lblInvalidCard);
		
		inputSeatType = new JTextField();
		inputSeatType.setBounds(476, 361, 116, 22);
		frame.getContentPane().add(inputSeatType);
		inputSeatType.setColumns(10);
		
		lblinvalidFlight = new JLabel("not a valid flight");
		lblinvalidFlight.setBounds(622, 306, 97, 16);
		lblinvalidFlight.setVisible(false);
		frame.getContentPane().add(lblinvalidFlight);
		
		lblBookingSuccess = new JLabel("flight successfully booked");
		lblBookingSuccess.setBounds(965, 306, 167, 16);
		lblBookingSuccess.setVisible(false);
		frame.getContentPane().add(lblBookingSuccess);
		
		lblcancelBookingPrompt = new JLabel("Please enter the flight number of the booking you would like to cancel:");
		lblcancelBookingPrompt.setBounds(12, 521, 447, 16);
		frame.getContentPane().add(lblcancelBookingPrompt);
		
		inputCancelBookingFlightNum = new JTextField();
		inputCancelBookingFlightNum.setBounds(476, 518, 116, 22);
		frame.getContentPane().add(inputCancelBookingFlightNum);
		inputCancelBookingFlightNum.setColumns(10);
		
		btnCancelBooking = new JButton("cancel booking");
		btnCancelBooking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblBookingCancelSuccess.setVisible(false);
				lblBookingNotFound.setVisible(false);
				String flight2Cancel = "";
				if (inputCancelBookingFlightNum.getText().length() != 2) {
					return;
				}
				try {
					Integer.parseInt(inputCancelBookingFlightNum.getText());
				} catch (Exception ee) {
					return;
				}
				String checkIfInBookings = "SELECT flight_number FROM Booking WHERE email = '" + currentUser + "';";
				try (Connection conn = DriverManager.getConnection(url, user, password);
			 		    Statement stmt = conn.createStatement();
			 			ResultSet rset = stmt.executeQuery(checkIfInBookings); ){
			 		while(rset.next()) {
			 			if (inputCancelBookingFlightNum.getText().equals(rset.getString("flight_number"))) {
			 				flight2Cancel = inputCancelBookingFlightNum.getText();
			 				break;
			 			}
			 		}
			 		if (flight2Cancel.isEmpty()) {
			 			lblBookingNotFound.setVisible(true);
			 			return;
			 		}
		    	}catch (SQLException ee) {
		    	}
				String cancelBookingq = "DELETE FROM Booking WHERE flight_number = " + flight2Cancel + ";";
				try (Connection conn = DriverManager.getConnection(url, user, password);
			 		    Statement stmt = conn.createStatement();){
					conn.setAutoCommit(false);
			        stmt.executeUpdate(cancelBookingq);
			        conn.commit();
			        lblBookingCancelSuccess.setVisible(true);
		       }catch (SQLException e1) {
		    	   	lblBookingNotFound.setVisible(true);
		       }
			}
		});
		btnCancelBooking.setBounds(833, 517, 116, 25);
		frame.getContentPane().add(btnCancelBooking);
		
		lblBookingCancelSuccess = new JLabel("booking successfully cancelled");
		lblBookingCancelSuccess.setBounds(965, 521, 184, 16);
		lblBookingCancelSuccess.setVisible(false);
		frame.getContentPane().add(lblBookingCancelSuccess);
		
		lblBookingNotFound = new JLabel("booking not found");
		lblBookingNotFound.setBounds(622, 521, 158, 16);
		lblBookingNotFound.setVisible(false);
		frame.getContentPane().add(lblBookingNotFound);
		
	}
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
