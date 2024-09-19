Project Overview: 
The Retail Business Management System (RBMS) is designed to streamline retail operations, manage employee and customer data, and track sales efficiently. This project was developed using Oracle PL/SQL for the database management and Java with JDBC for the user interface.

My Contributions
1. Database Design and Setup
	Table Creation: Designed and implemented six key tables: Employees, Customers, Products, Prod_Discnt, Purchases, and Logs. Developed SQL scripts for creating these tables in Oracle.
	Data Integrity: Ensured that all tables maintain proper relationships and data integrity through appropriate data types and constraints.
2. PL/SQL Package Development
	Procedures and Functions: Created a PL/SQL package that encapsulates all necessary procedures and functions, including:
	Sequence Generation: Developed sequences for automatic generation of unique identifiers for pur# and log#.
	Data Retrieval: Implemented procedures to display data from each table, facilitating debugging and data management.
	Sales Reporting: Designed a procedure to report monthly sales activities by employee, summarizing sales counts, quantities sold, and total revenue.
	Employee Management: Developed a procedure to add new employee records to the Employees table, including automatic logging of the operation.
	Purchasing Logic: Created a procedure for adding purchases, including validations for stock levels, payment calculations, and updating customer visit records.
3. Trigger Implementation
	Automatic Logging: Implemented triggers to automatically log database operations (inserts, updates) in the Logs table, enhancing traceability and auditing.
	Inventory Management: Created triggers to manage inventory levels, ensuring that stock quantities are updated accordingly and triggering alerts for low stock situations.
4. Java Interface Development
	User-Friendly Interface: Developed a Java application with a menu-driven interface, allowing users to interact with the RBMS easily.
	JDBC Integration: Utilized JDBC to connect the Java application to the Oracle database, enabling data retrieval and manipulation through the PL/SQL procedures.
5. Documentation and Collaboration
	Code Documentation: Provided in-line comments and documentation for all procedures and functions within the PL/SQL package for clarity and ease of understanding.
	Team Collaboration: Actively participated in team meetings, contributed to the planning and execution of project tasks, and collaborated with teammates to ensure project milestones            were met.
Conclusion
This project enhanced my understanding of database management, PL/SQL programming, and Java application development. I gained valuable experience in designing a system from the ground up and working collaboratively in a team environment.

