import java.text.*;  //Provides classes for formatting and parsing dates and 
import java.sql.*;  //Contains classes and interfaces for JDBC (Java Database Connectivity), which is used to interact with databases.
import oracle.jdbc.*; // Specific classes for working with Oracle databases, extending JDBC functionality.
import java.math.*; // Contains classes for performing arbitrary-precision arithmetic 
import java.io.*; //Provides classes for input/output operations, such as reading from and writing to files.
import java.awt.*; //Abstract Window Toolkit, used for creating graphical user interfaces in Java.
import oracle.jdbc.pool.OracleDataSource; //A class for managing a connection pool for Oracle databases.

public class Driver {
   public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
   public static String formattedDate;
   public static void main (String args []) throws SQLException {
	   boolean exit = false;
    try
    {
		//oracle database connection
    	OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource(); //creating an instance
        ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
        Connection conn = ds.getConnection("pchaluv1", "Sreedevi789");
        
	        
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
   catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
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
			while (op.next()) {
				System.out.println(op.getString(1) + "\t" + op.getString(2) + "\t" + op.getString(3) +  "\t" + op.getString(4));
			}
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
			while (op.next()) {
				formattedDate = dateFormat.format(op.getDate(6)).toUpperCase();
				System.out.println(String.format("%-10s", op.getString(1))  + String.format("%-25s", op.getString(2)) + String.format("%-20s", op.getString(3))  + String.format("%-20s", op.getString(4))  + String.format("%-25s", op.getString(5)) + String.format("%-15s", formattedDate));
			}
	
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
			CallableStatement stdinfo = conn.prepareCall("begin ? := project2.show_products(); end;");  //creating a callable statement
			stdinfo.registerOutParameter(1, OracleTypes.CURSOR);   //register the first parameter of the CallableStatement as an output parameter 
			
			// execute and retrieve the result set
			stdinfo.execute();
			ResultSet op = (ResultSet)((OracleCallableStatement)stdinfo).getCursor(1);
	
			// print the results
			System.out.println(String.format("%-10s", "pid")  + String.format("%-15s", "name") + String.format("%-10s", "qoh")  + String.format("%-20s", "qoh_threshold")  + String.format("%-15s", "orig_price") + String.format("%-15s", "discnt_category"));
			System.out.println(String.format("%-10s", "---")  + String.format("%-15s", "----") + String.format("%-10s", "---")  + String.format("%-20s", "-------------")  + String.format("%-15s", "----------") + String.format("%-15s", "---------------"));
			while (op.next()) {
				System.out.println(String.format("%-10s", op.getString(1))  + String.format("%-15s", op.getString(2)) + String.format("%-10s", op.getString(3))  + String.format("%-20s", op.getString(4))  + String.format("%-15s", op.getString(5)) + String.format("%-15s", op.getString(6)));
			}
	
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
			while (op.next()) {
				System.out.println(String.format("%-25s", op.getString(1))  + String.format("%-15s", op.getString(2)));
			}
	
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
			while (op.next()) {
				formattedDate = dateFormat.format(op.getDate(5)).toUpperCase();
				System.out.println(String.format("%-10s", op.getString(1))  + String.format("%-10s", op.getString(2)) + String.format("%-10s", op.getString(3))  + String.format("%-10s", op.getString(4))  + String.format("%-15s", formattedDate) + String.format("%-10s", op.getString(6)) + String.format("%-15s", op.getString(7)) + String.format("%-10s", op.getString(8)) + String.format("%-10s", op.getString(9)));
			}
	
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
			while (op.next()) {
				System.out.println(String.format("%-10s", op.getString(1))  + String.format("%-25s", op.getString(2)) + String.format("%-25s", op.getString(3))  + String.format("%-35s", op.getString(4))  + String.format("%-25s", op.getString(5)) + String.format("%-10s", op.getString(6)));
			}
	
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
			while (rs.next()) {
				System.out.println(String.format("%-12s", rs.getString(1))  + String.format("%-12s", rs.getString(2)) + String.format("%-12s", rs.getString(3))  + String.format("%-12s", rs.getString(4))  + String.format("%-12s", rs.getInt(5)) + String.format("%-15s", rs.getInt(6)) + String.format("%-12s", rs.getDouble(7)));
            }
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

        //close the statement
        cs.close();

    }
    catch (SQLException ex) { System.out.println ("\n** SQLException caught **\n" + ex.getMessage());}
    catch (Exception e) {System.out.println ("\n** other Exception caught **\n");}

}

public static void add_purchase(String e_id, String p_id, String c_id, String pur_qty, String pur_unit_price, Connection conn){
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
			if (message!=null)
			System.out.println(message);
        
		if(status.equals("1"))
          System.out.println("Insufficient quantity in stock.");
        else if(status.equals("0"))
          System.out.println("Add purchase sucessfully.");
        else if(status.equals("2"))
          System.out.println("Invalid eid.");
        else if(status.equals("3"))
          System.out.println("Invalid cid.");
        else if(status.equals("4"))
          System.out.println("Invalid pid.");

		/*else if(status.equals("5"))
          System.out.println("Invalid purchase unit price.");
        /*else
        {
          int firstArg = 0;
          try {
          firstArg = Integer.parseInt(status);
            } catch (NumberFormatException e) {
            System.err.println("Argument" + status + " must be an integer.");
            System.exit(1);
            }
		  System.out.println("Add purchase sucessfully.");
          System.out.println("The current qoh of the product is below the required threshold and new supply is required.");
          System.out.println("The new value of qoh of the product after new supply: " + firstArg);

        }*/
            //close the result set
			cs.close();


//close the statement
//cs.close();
}
catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
  catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n");}
}

}