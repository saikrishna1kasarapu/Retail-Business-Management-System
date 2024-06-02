
import java.sql.*;
import java.text.*;
import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Driver {
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    public static String formattedDate;
	public static JFrame frame = new JFrame("Oracle Database Viewer");
	public static JTextArea messageArea = new JTextArea(18, 60);

   public static void main (String args []) throws SQLException {
	   boolean exit = false;
	   Console console = System.console();
           	if (console == null) {
            	 System.err.println("No console.");
            	 System.exit(1);
		}
    try
    {
		//oracle database connection
    	OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
        ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
	Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
	char[] passwordArray = console.readPassword("Enter password: ");
        String password = new String(passwordArray);
        Connection conn = ds.getConnection(username, password);
		// JFrame frame = new JFrame("Oracle Database Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
		messageArea.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(4, 1));
		JButton displaytuples = new JButton("Display all tuples");
		JButton monsalact = new JButton("Monthly Sale Activites of Employees");
		JButton addemp = new JButton("Add Employee");
		JButton addpur = new JButton("Add Purchase");
		JButton exitt = new JButton("Exit");

        JButton employeesButton = new JButton("Show Employees");
        JButton customersButton = new JButton("Show Customers");
        JButton productsButton = new JButton("Show Products");
		JButton proddiscnt = new JButton("Show Products Discount");
		JButton purchasesButton = new JButton("Show Purchases");
		JButton logs = new JButton("Show Logs");

		panel.add(displaytuples);
        panel.add(monsalact);
        panel.add(addemp);
		panel.add(addpur);
		panel.add(exitt);

        // panel.add(employeesButton);
        // panel.add(customersButton);
        // panel.add(productsButton);
		// panel.add(proddiscnt);
		// panel.add(purchasesButton);
		// panel.add(logs);

		displaytuples.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                show_tuples(conn);
            }
        });

        monsalact.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 18));
				UIManager.put("OptionPane.messageForeground", Color.WHITE);
				UIManager.put("Panel.background", Color.LIGHT_GRAY);
				String eid = JOptionPane.showInputDialog(null, "Enter eid:", "Employee ID", JOptionPane.PLAIN_MESSAGE);
                find_monthlysaleactivities(eid, conn); 
            }
        });

        addemp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 18));
				UIManager.put("OptionPane.messageForeground", Color.WHITE);
				UIManager.put("Panel.background", Color.LIGHT_GRAY);
				JTextField eidField = new JTextField(10);
				JTextField nameField = new JTextField(10);
				JTextField telephoneField = new JTextField(10);
				JTextField emailField = new JTextField(10);

				JPanel panel2 = new JPanel(new GridLayout(0, 1));
				panel2.add(new JLabel("Employee ID:"));
				panel2.add(eidField);
				panel2.add(new JLabel("Name:"));
				panel2.add(nameField);
				panel2.add(new JLabel("Telephone:"));
				panel2.add(telephoneField);
				panel2.add(new JLabel("Email:"));
				panel2.add(emailField);

				int result = JOptionPane.showConfirmDialog(null, panel2, "Add Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					String eid = eidField.getText();
					String name = nameField.getText();
					String telephone = telephoneField.getText();
					String email = emailField.getText();
					add_employees(eid, name, telephone, email, conn);
				}
				// String eid = JOptionPane.showInputDialog(null, "Enter eid:", "Employee ID", JOptionPane.PLAIN_MESSAGE);
				// String name = JOptionPane.showInputDialog(null, "Enter name:", "name", JOptionPane.PLAIN_MESSAGE);
				// String telephone = JOptionPane.showInputDialog(null, "Enter telephone in format ***-***-****:", "telephone", JOptionPane.PLAIN_MESSAGE);
				// String email = JOptionPane.showInputDialog(null, "Enter email:", "email", JOptionPane.PLAIN_MESSAGE);
                // add_employees(eid, name, telephone, email, conn);
            }
        });

        addpur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 18));
				UIManager.put("OptionPane.messageForeground", Color.WHITE);
				UIManager.put("Panel.background", Color.LIGHT_GRAY);
				JTextField eidField = new JTextField(10);
				JTextField pidField = new JTextField(10);
				JTextField cidField = new JTextField(10);
				JTextField purqtyField = new JTextField(10);
				JTextField purunitprice = new JTextField(10);

				JPanel panel3 = new JPanel(new GridLayout(0, 1));
				panel3.add(new JLabel("Employee ID:"));
				panel3.add(eidField);
				panel3.add(new JLabel("Pid:"));
				panel3.add(pidField);
				panel3.add(new JLabel("Cid:"));
				panel3.add(cidField);
				panel3.add(new JLabel("Purchase Quantity:"));
				panel3.add(purqtyField);
				panel3.add(new JLabel("Purchase Quantity:"));
				panel3.add(purunitprice);

				int result = JOptionPane.showConfirmDialog(null, panel3, "Add Purchase", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					String eid = eidField.getText();
					String pid = pidField.getText();
					String cid = cidField.getText();
					String pur_qty = purqtyField.getText();
					String pur_unit_prrice = purunitprice.getText();
					add_purchase(eid, pid, cid, pur_qty, pur_unit_prrice, conn);
				}
            }
        });

		exitt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?", "Exit Program Confirmation", JOptionPane.YES_NO_OPTION);
				if (confirmed == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
            }
        });

		// employeesButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         show_employees(conn);
        //     }
        // });

        // customersButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         show_customers(conn);
        //     }
        // });

        // productsButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         show_products(conn);
        //     }
        // });

        // purchasesButton.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         show_purchases(conn);
        //     }
        // });

		// proddiscnt.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         show_proddiscnt(conn);
        //     }
        // });

        // logs.addActionListener(new ActionListener() {
        //     public void actionPerformed(ActionEvent e) {
        //         show_logs(conn);
        //     }
        // });

		frame.getContentPane().add(panel);
		frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
	        
		while(!exit) {
			System.out.println();
			String input;
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println("Select anyone from the following:");
			System.out.println("1.Display all tuples");
			System.out.println("2.Monthly Sale Activites of Employees");
			System.out.println("3.Add Employee");
			System.out.println("4.Add Purchase");
			System.out.println("5.exit");
			
			input = userInput.readLine();
			
			boolean exit1 = false;
			
			if(input.equals("1")) {
				while(!exit1) {
					System.out.println();
					String tableselected;
					
					System.out.println("choose a table to display:");
					System.out.println("1.employees");
					System.out.println("2.customers");
					System.out.println("3.products");
					System.out.println("4.products discount");
					System.out.println("5.purchases");
					System.out.println("6.logs");
					
					tableselected = userInput.readLine();
					
					switch(tableselected){
						case "1":
								 show_employees(conn);
								 exit1=true;
								 break;
						case "2":
								 show_customers(conn);
								 exit1=true;
								 break;
						case "3":
								 show_products(conn);
								 exit1=true;
								 break;
						case "4":
								 show_proddiscnt(conn);
								 exit1 = true;
								 break;
						case "5":
								 show_purchases(conn);
								 exit1 = true;
								 break; 
						case "6":
								 show_logs(conn);
								 exit1 = true;
								 break;
						default:
								System.out.println("Invalid input!Please select correct option");
								break;

					}
				}
				
			}
			else if(input.equals("2")) {
				String eid;
				System.out.println("Enter eid of employee requested: ");
				eid = userInput.readLine();
				find_monthlysaleactivities(eid, conn); 
			}
			else if(input.equals("3")) {
				String eid,name,telephone,email;
				System.out.println("Enter eid of employee : ");
				eid = userInput.readLine();
				System.out.println("Enter employee name :");
				name = userInput.readLine();
				System.out.println("Enter telephone number :");
				telephone = userInput.readLine();
				System.out.println("Enter email :");
				email= userInput.readLine();
				add_employees(eid, name, telephone, email, conn); 
			}
			else if(input.equals("4")) {
				String eid,pid,cid,pur_qty,pur_unit_price;
				System.out.println("Enter eid: ");
				eid = userInput.readLine();
				System.out.println("Enter pid: ");
				pid = userInput.readLine();
				System.out.println("Enter cid: ");
				cid = userInput.readLine();
				System.out.println("Enter pur_qty: ");
				pur_qty = userInput.readLine();
				System.out.println("Enter pur_unit_price: ");
				pur_unit_price = userInput.readLine();
				add_purchase(eid, pid, cid, pur_qty, pur_unit_price, conn); 
			}
			else if(input.equals("5")) {
				System.out.println("Exiting from the program!");
				break;
			}
			else {
				System.out.println("Invalid option selection, please choose any of the above options from 1 to 5");
			}
			
		}

        //closes the database connection
	   System.out.println("Connection Closed!");
       conn.close();
   }
   catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
   catch (Exception e) {System.out.println ("\n*** other Exception caught***\n");e.printStackTrace();}
//    catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
  }	
   
  	public static void show_tuples(Connection conn){
		frame.setVisible(false);
		JFrame frame1 = new JFrame("Oracle Database Viewer");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setSize(700, 700);

        JPanel panel1 = new JPanel(new GridLayout(4, 1));

        JButton employeesButton = new JButton("Show Employees");
        JButton customersButton = new JButton("Show Customers");
        JButton productsButton = new JButton("Show Products");
		JButton proddiscnt = new JButton("Show Products Discount");
		JButton purchasesButton = new JButton("Show Purchases");
		JButton logs = new JButton("Show Logs");
		JButton back = new JButton("Back");


        panel1.add(employeesButton);
        panel1.add(customersButton);
        panel1.add(productsButton);
		panel1.add(proddiscnt);
		panel1.add(purchasesButton);
		panel1.add(logs);
		panel1.add(back);

		employeesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                show_employees(conn);
            }
        });

        customersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                show_customers(conn);
            }
        });

        productsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                show_products(conn);
            }
        });

        purchasesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                show_purchases(conn);
            }
        });

		proddiscnt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                show_proddiscnt(conn);
            }
        });

        logs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                show_logs(conn);
            }
        });

		back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
				frame1.setVisible(false);
				frame1.getContentPane().remove(panel1);
				frame1.getContentPane().revalidate(); 
				return;
            }
        });

        frame1.getContentPane().add(panel1);
		frame1.setLocationRelativeTo(null);
        frame1.setVisible(true);
	}

    public static void show_employees(Connection conn) {
		try {
			//call to stored procedure
			CallableStatement stdinfo = conn.prepareCall("begin ? := project2.show_employees(); end;");
			stdinfo.registerOutParameter(1, OracleTypes.CURSOR);
			// execute and retrieve the result set
			stdinfo.execute();
			ResultSet op = (ResultSet)((OracleCallableStatement)stdinfo).getCursor(1);
			System.out.println("eid" + "\t" + "name" + "\t" + "telephone#" +  "\t" + "email");
			System.out.println("---" + "\t" + "----" + "\t" + "----------" +  "\t" + "-----");
			messageArea.setText("");
			messageArea.append("eid" + "\t" + "name" + "\t" + "telephone#" +  "\t" + "email\n");
			messageArea.append("---" + "\t" + "----" + "\t" + "----------" +  "\t" + "-----\n");
			while (op.next()) {
				System.out.println(op.getString(1) + "\t" + op.getString(2) + "\t" + op.getString(3) +  "\t" + op.getString(4));
				messageArea.append(op.getString(1) + "\t" + op.getString(2) + "\t" + op.getString(3) +  "\t" + op.getString(4)+"\n");
			}
			JOptionPane.showMessageDialog(null, new JScrollPane(messageArea), "Messages", JOptionPane.PLAIN_MESSAGE);
		}
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}		
	}
	public static void show_customers(Connection conn) {
		try{
	
			//call to stored procedure
			CallableStatement stdinfo = conn.prepareCall("begin ? := project2.show_customers(); end;");
			stdinfo.registerOutParameter(1, OracleTypes.CURSOR);
			
			// execute and retrieve the result set
			stdinfo.execute();
			ResultSet op = (ResultSet)((OracleCallableStatement)stdinfo).getCursor(1);
	
			// print the results
			System.out.println(String.format("%-10s", "cid")  + String.format("%-25s", "first_name") + String.format("%-20s", "last_name")  + String.format("%-20s", "phone#")  + String.format("%-25s", "vists_made") + String.format("%-15s", "last_visit_date"));
			System.out.println(String.format("%-10s", "---")  + String.format("%-25s", "----------") + String.format("%-20s", "---------")  + String.format("%-20s", "------")  + String.format("%-25s", "----------") + String.format("%-15s", "---------------"));
			messageArea.setText("");
			messageArea.append(String.format("%-10s", "cid")  + String.format("%-25s", "first_name") + String.format("%-20s", "last_name")  + String.format("%-20s", "phone#")  + String.format("%-25s", "vists_made") + String.format("%-15s", "last_visit_date")+"\n");
			messageArea.append(String.format("%-10s", "---")  + String.format("%-25s", "----------") + String.format("%-20s", "---------")  + String.format("%-20s", "------")  + String.format("%-25s", "----------") + String.format("%-15s", "---------------")+"\n");
			while (op.next()) {
				formattedDate = dateFormat.format(op.getDate(6)).toUpperCase();
				System.out.println(String.format("%-10s", op.getString(1))  + String.format("%-25s", op.getString(2)) + String.format("%-20s", op.getString(3))  + String.format("%-20s", op.getString(4))  + String.format("%-25s", op.getString(5)) + String.format("%-15s", formattedDate));
				messageArea.append(String.format("%-10s", op.getString(1))  + String.format("%-25s", op.getString(2)) + String.format("%-20s", op.getString(3))  + String.format("%-20s", op.getString(4))  + String.format("%-25s", op.getString(5)) + String.format("%-15s", formattedDate)+"\n");
			}
			JOptionPane.showMessageDialog(null, new JScrollPane(messageArea), "Messages", JOptionPane.PLAIN_MESSAGE);
	
			//close the result set, statement
			op.close();
			op.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
	}
	public static void show_products(Connection conn){
	
		try{
	
			//call to stored procedure
			CallableStatement stdinfo = conn.prepareCall("begin ? := project2.show_products(); end;");
			stdinfo.registerOutParameter(1, OracleTypes.CURSOR);
			
			// execute and retrieve the result set
			stdinfo.execute();
			ResultSet op = (ResultSet)((OracleCallableStatement)stdinfo).getCursor(1);
	
			// print the results
			System.out.println(String.format("%-10s", "pid")  + String.format("%-15s", "name") + String.format("%-10s", "qoh")  + String.format("%-20s", "qoh_threshold")  + String.format("%-15s", "orig_price") + String.format("%-15s", "discnt_category"));
			System.out.println(String.format("%-10s", "---")  + String.format("%-15s", "----") + String.format("%-10s", "---")  + String.format("%-20s", "-------------")  + String.format("%-15s", "----------") + String.format("%-15s", "---------------"));
			messageArea.setText("");
			messageArea.append(String.format("%-10s", "pid")  + String.format("%-15s", "name") + String.format("%-10s", "qoh")  + String.format("%-20s", "qoh_threshold")  + String.format("%-15s", "orig_price") + String.format("%-15s", "discnt_category")+"\n");
			messageArea.append(String.format("%-10s", "---")  + String.format("%-15s", "----") + String.format("%-10s", "---")  + String.format("%-20s", "-------------")  + String.format("%-15s", "----------") + String.format("%-15s", "---------------")+"\n");
			while (op.next()) {
				System.out.println(String.format("%-10s", op.getString(1))  + String.format("%-15s", op.getString(2)) + String.format("%-10s", op.getString(3))  + String.format("%-20s", op.getString(4))  + String.format("%-15s", op.getString(5)) + String.format("%-15s", op.getString(6)));
				messageArea.append(String.format("%-10s", op.getString(1))  + String.format("%-15s", op.getString(2)) + String.format("%-10s", op.getString(3))  + String.format("%-20s", op.getString(4))  + String.format("%-15s", op.getString(5)) + String.format("%-15s", op.getString(6))+"\n");
			}
			JOptionPane.showMessageDialog(null, new JScrollPane(messageArea), "Messages", JOptionPane.PLAIN_MESSAGE);
	
			//close the result set, statement
			op.close();
			stdinfo.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
	
	}
	public static void show_proddiscnt(Connection conn){
	
		try{
	
			//call to stored procedure
			CallableStatement stdinfo = conn.prepareCall("begin ? := project2.show_proddiscnt(); end;");
			stdinfo.registerOutParameter(1, OracleTypes.CURSOR);
			
			// execute and retrieve the result set
			stdinfo.execute();
			ResultSet op = (ResultSet)((OracleCallableStatement)stdinfo).getCursor(1);
	
			// print the results
			System.out.println(String.format("%-25s", "discnt_category")  + String.format("%-15s", "discnt_rate"));
			System.out.println(String.format("%-25s", "---------------")  + String.format("%-15s", "-----------"));
			messageArea.setText("");
			messageArea.append(String.format("%-25s", "discnt_category")  + String.format("%-15s", "discnt_rate")+"\n");
			messageArea.append(String.format("%-25s", "---------------")  + String.format("%-15s", "-----------")+"\n");
			while (op.next()) {
				System.out.println(String.format("%-25s", op.getString(1))  + String.format("%-15s", op.getString(2)));
				messageArea.append(String.format("%-25s", op.getString(1))  + String.format("%-15s", op.getString(2))+"\n");
			}
			JOptionPane.showMessageDialog(null, new JScrollPane(messageArea), "Messages", JOptionPane.PLAIN_MESSAGE);
	
			//close the result set, statement
			op.close();
			stdinfo.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
	
	}
	public static void show_purchases(Connection conn){
	
		try{
	
			//call to stored procedure
			CallableStatement stdinfo = conn.prepareCall("begin ? := project2.show_purchases(); end;");
			stdinfo.registerOutParameter(1, OracleTypes.CURSOR);
			
			// execute and retrieve the result set
			stdinfo.execute();
			ResultSet op = (ResultSet)((OracleCallableStatement)stdinfo).getCursor(1);
	
			// print the results
			System.out.println(String.format("%-10s", "pur#")  + String.format("%-10s", "eid") + String.format("%-10s", "pid")  + String.format("%-10s", "cid")  + String.format("%-15s", "pur_time") + String.format("%-10s", "quantity") + String.format("%-15s", "unit_price") + String.format("%-10s", "payment") + String.format("%-10s", "saving"));
			System.out.println(String.format("%-10s", "----")  + String.format("%-10s", "---") + String.format("%-10s", "----")  + String.format("%-10s", "----")  + String.format("%-15s", "----------") + String.format("%-10s", "--------") + String.format("%-15s", "-----------") + String.format("%-10s", "-------") + String.format("%-10s", "------"));
			messageArea.setText("");
			messageArea.append(String.format("%-10s", "pur#")  + String.format("%-10s", "eid") + String.format("%-10s", "pid")  + String.format("%-10s", "cid")  + String.format("%-15s", "pur_time") + String.format("%-10s", "quantity") + String.format("%-15s", "unit_price") + String.format("%-10s", "payment") + String.format("%-10s", "saving")+"\n");
			messageArea.append(String.format("%-10s", "----")  + String.format("%-10s", "---") + String.format("%-10s", "----")  + String.format("%-10s", "----")  + String.format("%-15s", "----------") + String.format("%-10s", "--------") + String.format("%-15s", "-----------") + String.format("%-10s", "-------") + String.format("%-10s", "------")+"\n");
			while (op.next()) {
				formattedDate = dateFormat.format(op.getDate(5)).toUpperCase();
				System.out.println(String.format("%-10s", op.getString(1))  + String.format("%-10s", op.getString(2)) + String.format("%-10s", op.getString(3))  + String.format("%-10s", op.getString(4))  + String.format("%-15s", formattedDate) + String.format("%-10s", op.getString(6)) + String.format("%-15s", op.getString(7)) + String.format("%-10s", op.getString(8)) + String.format("%-10s", op.getString(9)));
				messageArea.append(String.format("%-10s", op.getString(1))  + String.format("%-10s", op.getString(2)) + String.format("%-10s", op.getString(3))  + String.format("%-10s", op.getString(4))  + String.format("%-15s", formattedDate) + String.format("%-10s", op.getString(6)) + String.format("%-15s", op.getString(7)) + String.format("%-10s", op.getString(8)) + String.format("%-10s", op.getString(9))+"\n");
			}
			JOptionPane.showMessageDialog(null, new JScrollPane(messageArea), "Messages", JOptionPane.PLAIN_MESSAGE);
	
			//close the result set, statement
			op.close();
			stdinfo.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
	
	}

	public static void show_logs(Connection conn){
	
		try{
	
			//call to stored procedure
			CallableStatement stdinfo = conn.prepareCall("begin ? := project2.show_logs(); end;");
			stdinfo.registerOutParameter(1, OracleTypes.CURSOR);
			
			// execute and retrieve the result set
			stdinfo.execute();
			ResultSet op = (ResultSet)((OracleCallableStatement)stdinfo).getCursor(1);
	
	
			// print the results
			System.out.println(String.format("%-10s", "log#")  + String.format("%-25s", "user_name") + String.format("%-25s", "operation")  + String.format("%-35s", "operation_time")  + String.format("%-25s", "table_name") + String.format("%-10s", "tuple_pkey"));
			System.out.println(String.format("%-10s", "----")  + String.format("%-25s", "---------") + String.format("%-25s", "---------")  + String.format("%-35s", "--------------")  + String.format("%-25s", "----------") + String.format("%-10s", "----------"));
			messageArea.setText("");
			messageArea.append(String.format("%-10s", "log#")  + String.format("%-25s", "user_name") + String.format("%-25s", "operation")  + String.format("%-35s", "operation_time")  + String.format("%-25s", "table_name") + String.format("%-10s", "tuple_pkey")+"\n");
			messageArea.append(String.format("%-10s", "----")  + String.format("%-25s", "---------") + String.format("%-25s", "---------")  + String.format("%-35s", "--------------")  + String.format("%-25s", "----------") + String.format("%-10s", "----------")+"\n");
			while (op.next()) {
				System.out.println(String.format("%-10s", op.getString(1))  + String.format("%-25s", op.getString(2)) + String.format("%-25s", op.getString(3))  + String.format("%-35s", op.getString(4))  + String.format("%-25s", op.getString(5)) + String.format("%-10s", op.getString(6)));
				messageArea.append(String.format("%-10s", op.getString(1))  + String.format("%-25s", op.getString(2)) + String.format("%-25s", op.getString(3))  + String.format("%-35s", op.getString(4))  + String.format("%-25s", op.getString(5)) + String.format("%-10s", op.getString(6))+"\n");
			}
			JOptionPane.showMessageDialog(null, new JScrollPane(messageArea), "Messages", JOptionPane.PLAIN_MESSAGE);
	
			//close the result set, statement
			op.close();
			stdinfo.close();
		} 
		catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
		catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n" + e.toString());}
	
	}

	//find_monthlysaleactivities method
  public static void find_monthlysaleactivities(String e_id, Connection conn){
    try{
        //call to stored procedure
        CallableStatement cs = conn.prepareCall("begin project2.find_monthlysaleactivities(:1,:2,:3); end;");
        //set parameters
			cs.setString(1, e_id);
            cs.registerOutParameter(2, OracleTypes.CURSOR);
			cs.registerOutParameter(3, OracleTypes.NUMBER);
            // execute and retrieve the result set
			cs.execute();
        
        ResultSet rs = (ResultSet)((OracleCallableStatement)cs).getObject(2);
        // print the results
			System.out.println(String.format("%-12s", "eid")  + String.format("%-12s", "name") + String.format("%-12s", "Month")  + String.format("%-12s", "Year")  + String.format("%-12s", "total_sales") + String.format("%-15s", "total_quantity") + String.format("%-12s", "total_amount"));
			System.out.println(String.format("%-12s", "---")  + String.format("%-12s", "----") + String.format("%-12s", "-----")  + String.format("%-12s", "----")  + String.format("%-12s", "-----------") + String.format("%-15s", "--------------")  + String.format("%-12s", "------------"));
			messageArea.setText("");
			messageArea.append(String.format("%-12s", "eid")  + String.format("%-12s", "name") + String.format("%-12s", "Month")  + String.format("%-12s", "Year")  + String.format("%-12s", "total_sales") + String.format("%-15s", "total_quantity") + String.format("%-12s", "total_amount")+"\n");
			messageArea.append(String.format("%-12s", "---")  + String.format("%-12s", "----") + String.format("%-12s", "-----")  + String.format("%-12s", "----")  + String.format("%-12s", "-----------") + String.format("%-15s", "--------------")  + String.format("%-12s", "------------")+"\n");
			while (rs.next()) {
				System.out.println(String.format("%-12s", rs.getString(1))  + String.format("%-12s", rs.getString(2)) + String.format("%-12s", rs.getString(3))  + String.format("%-12s", rs.getString(4))  + String.format("%-12s", rs.getInt(5)) + String.format("%-15s", rs.getInt(6)) + String.format("%-12s", rs.getDouble(7)));
				messageArea.append(String.format("%-12s", rs.getString(1))  + String.format("%-12s", rs.getString(2)) + String.format("%-12s", rs.getString(3))  + String.format("%-12s", rs.getString(4))  + String.format("%-12s", rs.getInt(5)) + String.format("%-15s", rs.getInt(6)) + String.format("%-12s", rs.getDouble(7))+"\n");
            }
			JOptionPane.showMessageDialog(null, new JScrollPane(messageArea), "Messages", JOptionPane.PLAIN_MESSAGE);
            //close the result set
			rs.close();


//close the statement
//cs.close();
}
catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
  catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
}

public static void add_employees( String eid, String name, String telephone, String email, Connection conn){

    try{

        //call to stored procedure
        CallableStatement cs = conn.prepareCall("begin project2.add_employees(?, ?, ?, ?, ?); end;");

        //set parameters
        cs.setString(1, eid);
        cs.setString(2, name);
        cs.setString(3, telephone);
        cs.setString(4, email);
        cs.registerOutParameter(5, Types.VARCHAR);

        // execute and retrieve the result set
        cs.execute();
        
        //get the out parameter result.
        String message = cs.getString(5);
        System.out.println(message);
		messageArea.setText("");
		messageArea.append(message+"\n");
		JOptionPane.showMessageDialog(null, new JScrollPane(messageArea), "Messages", JOptionPane.PLAIN_MESSAGE);

        //close the statement
        cs.close();

    }
    catch (SQLException ex) { System.out.println ("\n** SQLException caught **\n" + ex.getMessage());}
    catch (Exception e) {System.out.println ("\n** other Exception caught **\n");}

}

public static void add_purchase(String e_id, String p_id, String c_id, String pur_qty, String pur_unit_price ,Connection conn){
    try{
        //call to stored procedure
        CallableStatement cs = conn.prepareCall("begin project2.add_purchase(:1,:2,:3,:4,:5,:6,:7); end;");
        //set parameters
			cs.setString(1, e_id);
			cs.setString(2, p_id);
			cs.setString(3, c_id);
			cs.setString(4, pur_qty);
			cs.setString(5, pur_unit_price);
            cs.registerOutParameter(6, OracleTypes.DOUBLE);
			cs.registerOutParameter(7, OracleTypes.VARCHAR);
            // execute and retrieve the result set
			cs.execute();

			String status = cs.getString(6);

			String message = cs.getString(7);
        
		if(status.equals("1"))
          System.out.println("Insufficient quantity in stock.");
        else if(status.equals("0"))
          {
			System.out.println("Add purchase sucessfully.");
			messageArea.setText("");
			messageArea.append("Add purchase sucessfully."+"\n");
			JOptionPane.showMessageDialog(null, new JScrollPane(messageArea), "Messages", JOptionPane.PLAIN_MESSAGE);
		}
        else if(status.equals("2"))
          System.out.println("Invalid eid.");
        else if(status.equals("3"))
          System.out.println("Invalid cid.");
        else if(status.equals("4"))
          System.out.println("Invalid pid.");
        // else
        // {
        //   int firstArg = 0;
        //   try {
        //   firstArg = Integer.parseInt(status);
        //     } catch (NumberFormatException e) {
        //     System.err.println("Argument" + status + " must be an integer.");
        //     System.exit(1);
        //     }
        //   System.out.println("The current qoh of the product is below the required threshold and new supply is required.");
        //   System.out.println("The new value of qoh of the product after new supply: " + firstArg);

        // }
            //close the result set
			cs.close();


//close the statement
//cs.close();
}
catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
  catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
}

}
