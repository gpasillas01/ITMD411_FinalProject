package javaapplication1;
import java.awt.GridLayout; //useful for layouts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;

//controls-label text fields, button
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.border.Border;



@SuppressWarnings("serial")
public class Login extends JFrame {

	Dao conn;
	boolean adminid;
	public Login() {

		super("IIT HELP DESK LOGIN");
		conn = new Dao();
		conn.createTables();
		setSize(400, 210);
		setLayout(new GridLayout(4, 2));
		
		//ADD 
		
		setLocationRelativeTo(null); // centers window.

		
		
		// SET UP CONTROLS
		JLabel lblRightHeading = new JLabel("Help Desk", JLabel.RIGHT);
		JLabel lblUsername = new JLabel("Enter Username", JLabel.LEFT);
		JLabel lblPassword = new JLabel("Enter Password", JLabel.LEFT);
		JLabel lblUserType = new JLabel("Select Type", JLabel.LEFT);
		JLabel lblStatus = new JLabel(" ", JLabel.CENTER);
		//JLabel lblSpacer = new JLabel("", JLabel.CENTER);
			
		
	
		
		
		lblRightHeading.setForeground(new Color(255, 255, 255)); 
		lblUsername.setForeground(new Color(0, 102, 255));
		lblPassword.setForeground(new Color(0, 102, 255));
		lblStatus.setForeground(new Color(0, 102, 255));
		lblUserType.setForeground(new Color(0, 102, 255));
		
		
		lblRightHeading.setFont(new Font("Arial", Font.BOLD, 25));
	    lblUsername.setFont(new Font("Georgia", Font.BOLD, 20));
	    lblPassword.setFont(new Font("Georgia", Font.BOLD, 20));
	    lblUserType.setFont(new Font("Georgia", Font.BOLD, 20));
		
		
		
		
		JTextField txtUname = new JTextField(10);

		JPasswordField txtPassword = new JPasswordField();
		JButton btn = new JButton("Submit");
		JButton btnExit = new JButton("Exit");

	
		btn.setFont(new Font("Geneva", Font.PLAIN, 19));
	    btnExit.setFont(new Font("Geneva", Font.PLAIN, 19));
	    btnExit.setBackground(new Color(102, 153, 153));
	    btnExit.setForeground(new Color(102, 153, 153));
	    btn.setBackground(new Color(255, 51, 51));
	    btn.setForeground(new Color(153, 204, 255));
		
		
		
	    lblStatus.setToolTipText("Contact help desk to unlock password");
		lblRightHeading.setHorizontalAlignment(JLabel.CENTER);
		lblUsername.setHorizontalAlignment(JLabel.CENTER);
		lblPassword.setHorizontalAlignment(JLabel.CENTER);
		lblUserType.setHorizontalAlignment(JLabel.CENTER);
 
		//Dropdown list choices
		String choices[]= { "Administrator", "Guest User" };
		JComboBox select = new JComboBox(choices);
		
		
		// ADD OBJECTS TO FRAME
		add(lblRightHeading);   // 1st row filler
		add(lblUsername);   // 2nd row
		add(txtUname);
		add(lblPassword);// 3rd row
		add(txtPassword);
		add(lblUserType);     // 4th row
		//add(txtType);	  
		add(select);
		add(btn);         // 5th row
		add(btnExit);
		add(lblStatus);   // 6th row
			
		btn.addActionListener(new ActionListener() {
		int count = 0; // count agent
		
		@SuppressWarnings("deprecation") //might need to trouble shoot from this line down
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean admin = false;
			count = count + 1;
			// verify credentials of user (MAKE SURE TO CHANGE TO YOUR TABLE NAME BELOW)

			String query = "SELECT * FROM farpas_users WHERE uname = ? and upass = ?;";
			String uname;
			String upass;
			try (PreparedStatement stmt = conn.getConnection().prepareStatement(query)) {
				stmt.setString(1, txtUname.getText());
				uname = txtUname.getText();//comeback to this
				stmt.setString(2, txtPassword.getText());
				upass = txtPassword.getText(); //comeback 2 this
				
				
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					admin = rs.getBoolean("admin"); // get table column value
					new Tickets(admin); //open Tickets file / GUI interface
					setVisible(false); // HIDE THE FRAME
					dispose(); // CLOSE OUT THE WINDOW
				} else
					lblStatus.setText("Try again! " + (3 - count) + " / 3 attempt(s) left");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		//getting the user. verifying administrator creidentials
		String queryOne = "SELECT admin FROM farpas_users WHERE uname = ? and upass = ?;";
		try (PreparedStatement stmt2 = conn.getConnection().prepareStatement(queryOne)) {
			stmt2.setString(1,txtUname.getText()); // uname
			stmt2.setString(2,txtPassword.getText());// upass);
			ResultSet rs = stmt2.executeQuery();
			if(rs.next()) {
			adminid = rs.getBoolean("admin");
			} else
				lblStatus.setText("Try again! " + (3 - count) + " / 3 attempts left");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		}
	});
	btnExit.addActionListener(e -> System.exit(0));

	setVisible(true); // SHOW THE FRAME
}

public static void main(String[] args) {

	System.out.println("[ Ticketing system made by Yaqub Farook and Griselda Pasillas ]\n");

	new Login();
		}
	}
		