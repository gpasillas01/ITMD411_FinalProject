package javaapplication1;

import java.awt.Color;
//import java.awt.Image; comeback to thius
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.sql.SQLException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date; 
//might delete lines 4 lines above

//import javax.swing.ImageIcon; comeback to this
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuHaltTicket; 
	JMenuItem mnuItemReturned;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;

	public Tickets(Boolean isAdmin) {

		chkIfAdmin = isAdmin;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);
//-----------------------------------------------
		// initialization for 3rd sub menu items for main menu of admin
		mnuItemReturned = new JMenuItem("See Resolved Tickets");
		//  main menu item--adding to admin
		mnuAdmin.add(mnuItemReturned);
		
		// initialization for  sub menu items for main menu of admin
		mnuHaltTicket = new JMenuItem("Close Ticket");
		// main menu item--adding to ticket 
		mnuAdmin.add(mnuHaltTicket);

		// initialization for main menu of Tickets  (1st sub menu item) 
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// main menu item--adding to ticket 
		mnuTickets.add(mnuItemOpenTicket);

		//// initialization for main menu of Tickets  (2nd sub menu item) 
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// main menu item--adding to ticket 
		mnuTickets.add(mnuItemViewTicket);
//---------------------------------------------------------		

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemReturned.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
		mnuHaltTicket.addActionListener(this);

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above*
		 */
	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY); //we might need to keep change
		setLocationRelativeTo(null);
		setVisible(true);
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
			String initialdate = JOptionPane.showInputDialog("Create booking for the following date: ");
			
			// insert ticket information to database

			int id = dao.insertRecords(ticketName, ticketDesc, LocalDate.parse(initialdate));

			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else
				System.out.println("Ticket cannot be created!!!");
		}

		else if (e.getSource() == mnuItemViewTicket) { 

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJtable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				//getContentPane().setBackground(new Color(10, 44, 92)); this is what they had. Take it out
				getContentPane().setBackground(Color.DARK_GRAY);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException sqe) {
				sqe.printStackTrace(); //we might've changed e to e in the Dao class
			}
		}
		
		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */
//----------------------------------------------------------------------------------------------		
			//when the user clicks on the delete button
			else if (e.getSource() == mnuItemDelete) {		

				try {
					String idval = JOptionPane.showInputDialog(null, "Out of all the ticket_id 's which one do you want to delete?");
					int id = Integer.parseInt(idval);
					dao.deleteRecords(id);

				} catch (SQLException sqe) {
					sqe.printStackTrace();
				}
			}
		
			//when the user clicks on the closing ticket button
			else if (e.getSource() == mnuHaltTicket) {		

				try {
					String idval = JOptionPane.showInputDialog(null, "Out of all the ticket_id 's which one do you want to close?");
					int id = Integer.parseInt(idval);
					LocalDate initialdate = null;
					dao.endingRecords(id, initialdate);//come back to this at the end 

				} catch (SQLException sqe) {
					sqe.printStackTrace();
				}
			}
		
			//When the user clicks on the updates button
			else if (e.getSource() == mnuItemUpdate) {

				try {
					String idval = JOptionPane.showInputDialog(null, "Out of all the ticket_id 's which one do you want to update?");
					int id = Integer.parseInt(idval);
					String ticketDesc = JOptionPane.showInputDialog(null, "Changing the following ticket description to: ");
					dao.updateRecords(id, ticketDesc);
//e
				} catch (SQLException sqe) {
					sqe.printStackTrace();
				}
			}
		
			//When the user clicks on the resolved button
			else if (e.getSource() == mnuItemReturned) {
				// collecting all of returned tickets so that the details can be viewed in the JTable
				try {
					// Using the JTable to make a table and display the model off the results
					JTable jt = new JTable(ticketsJtable.buildTableModel(dao.readReturned()));
					jt.setBounds(25, 35, 150, 350);
					JScrollPane sp = new JScrollPane(jt);
					add(sp);
					setVisible(true); // refreshes or repaints frame on screen

				} catch (SQLException sqe) {
					sqe.printStackTrace();
				}
				
			}

	}}