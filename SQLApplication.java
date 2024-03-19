
/*
Compile code:
javac -cp ".;lib/postgresql-42.7.3.jar" SQLApplication.java

Run code:
java -cp ".;lib/postgresql-42.7.3.jar" SQLApplication

Initialize database if necessary:
\i /path/to/scripts/dbInitializer.sql
\i C:/Users/nathaniel/Desktop/3005/A3Q1/scripts/dbInitializer.sql

*/
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;


public class SQLApplication {

    //specifics for connecting to a database
    static final String url = "jdbc:postgresql://localhost:5432/";
    static final String user = "postgres";
    static final String password = "hello";

    Statement statement;

    //prints all students in the database
    public void getAllStudents(){
        try{

            //runs query
            statement.executeQuery("SELECT * FROM students");
            ResultSet resultSet = statement.getResultSet();
            System.out.print("\n");

            //prints each value of each row, neatly
            while (resultSet.next()){
                for (int i = 1; i <= 5; i++)
                    System.out.print(resultSet.getString(i) + " ");
                System.out.print("\n");
            }
        }
        catch(Exception e){}
    }

    //adds a student with the given parameters into the database
    public void addStudent(String first_name, String last_name, String email, String enrollment_date){
        try{
            //runs INSERT query
            statement.executeUpdate("INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES ('"+first_name+"', '"+last_name+"', '"+email+"', '"+enrollment_date+"')");
        }
        catch(Exception e){System.out.println("Parameter error\n" + e);}
    }

    //updates email of given student_id
    public void updateStudentEmail(String student_id, String new_email){
        try{
            //runs UPDATE query
            int r = statement.executeUpdate("UPDATE students SET email = '"+new_email+"' WHERE student_id = "+student_id);

            //informs user if update was successful
            if(r==1){
                System.out.println("Student's email has been updated.");
            }
            else{
                System.out.println("Student ID not found.");
            }
        }
        catch(Exception e){System.out.println("Parameter error\n" + e);}
    }

    //deletes student with given student_id
    public void deleteStudent(String student_id){
        try{
            //runs DELETE query
            int r = statement.executeUpdate("DELETE FROM students WHERE student_id = "+student_id);
            if(r==1){
                System.out.println("Student's email has been deleted.");
            }
            else{
                System.out.println("Student ID not found.");
            }
        }
        catch(Exception e){System.out.println("Parameter error\n" + e);}
    }

    //creates a brand new database, initializes tables, and adds initial values
    public int initializeDatabase(String dbName){

        try{
            //connects to local postgresql server
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            
            if(connection!=null) System.out.println("PostgreSQL connection successful");
            else {System.out.println("PostgreSQL connection unsuccessful"); return 0;}

            //creates a new database under the given name
            statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE " + dbName + ";");

            //connects to the new database
            connection = DriverManager.getConnection(url + dbName, user, password);

            if(connection!=null) System.out.println("Database connection successful");
            else {System.out.println("Database connection unsuccessful"); return 0;}

            //initializes schema table
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students (student_id serial PRIMARY KEY, first_name VARCHAR(255) NOT NULL, last_name VARCHAR(255) NOT NULL, email VARCHAR(255) UNIQUE NOT NULL, enrollment_date DATE)");
            
            //inserts initial data/values
            statement.executeUpdate("INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES ('John', 'Doe', 'john.doe@example.com', '2023-09-01'), ('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'), ('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02')");
            System.out.println("Initialization of data successful\n");
            return 1;
        }

        catch(Exception e) {System.out.println("Initialization unsuccessful\n" + e); return 0;}
    }

    //connects to existing database, assumes table already exists
    //works when using the provided sql file to initialize values
    public int connectToExistingDatabase(String dbName){
        try{
            //connects to database of given name
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url + dbName, user, password);
            
            if(connection!=null) System.out.println("Database connection successful");
            else {System.out.println("Database connection unsuccessful"); return 0;}

            statement = connection.createStatement();

            return 1;
        }

        catch(Exception e) {System.out.println("Connection unsuccessful, database not found\n" + e); return 0;}
    }


    public static void main(String[] args) {
       
        SQLApplication app = new SQLApplication();
        
        Scanner scanner =  new Scanner(System.in);

        //asks user to either initialize a new database, or connect to an existing one
        while (true){
            System.out.println("\n\nOptions:\ninitialize - creates a new database with corresponding schema, and sets initial values\nconnect - connects to an existing database which already has the designated schema");
            String response = scanner.nextLine().toLowerCase();

            //initializes new database under given name
            if (response.equals("initialize")){
                System.out.println("\nEnter name of database:");
                String dbName = scanner.nextLine();
                if(app.initializeDatabase(dbName)==0) return;
                break;
            }

            //connects to existing database under given name
            if (response.equals("connect")){
                System.out.println("\nEnter name of database: (if you used provided SQL file, database is called a3database)");
                String dbName = scanner.nextLine();
                if(app.connectToExistingDatabase(dbName)==0) return;
                break;
            }

        }

        String choice = "tmp";

        //repeatedly prompts the user for input until exit
        while (!choice.equals("quit")){

            System.out.print("\n\nOptions:\ngetAllStudents\naddStudent <first_name> <last_name> <email> <enrollment_date>\nupdateStudentEmail <student_id> <new_email>\ndeleteStudent <student_id>\nquit\n  > ");
            
            //collects user input
            String[] params = scanner.nextLine().toLowerCase().split(" ");
            choice = params[0];
            System.out.println();

            switch (choice){

                //prints all students
                case "getallstudents":
                case "getall":
                    app.getAllStudents();
                    break;

                //adds a student under the given parameters
                case "addstudent":
                case "add":
                    if (params.length != 5){
                        System.out.println("This functions needs 4 parameters:\nfirst_name, last_name, email, enrollment_date");
                    }
                    else  app.addStudent(params[1], params[2], params[3], params[4]);
                    break;

                //updates a student email under the given parameters
                case "updatestudentemail":
                case "update":
                    if (params.length != 3){
                        System.out.println("This functions needs 2 parameters:\nstudent_id, new_email");
                    }
                    else  app.updateStudentEmail(params[1], params[2]);
                    break;

                //deletes a student email under the given parameters
                case "deletestudent":
                case "delete":
                    if (params.length != 2){
                        System.out.println("This functions needs 1 parameter:\nstudent_id");
                    }
                    else  app.deleteStudent(params[1]);
                    break;
            }
        }
    }
}
