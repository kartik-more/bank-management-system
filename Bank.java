package Bank_Managment;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

class Info {

    private String accountNo;
    private String pin;

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountPass(String pass) {
        this.pin = pass;
    }

    public String getAccountPass() {
        return pin;
    }
}

class Details extends Info {

    private String contact;
    private String name;
    private String address;
    private String account_type;
    private double current_money;

    void createAccount(Scanner sc) {
        System.out.println("  --------------------------------");
        System.out.print("\t| Enter the name: ");
        this.name = sc.nextLine();
        while (!name.matches("(([A-Z][a-z]+(\\s[A-Z][a-z]+)*)|([a-z]+(\\s[a-z]+)*))")) {
            System.out.println("\tInvalid Name.\n \tTry Again\n");
            System.out.print("\t| Enter the name: ");
            this.name = sc.nextLine();
        }
        System.out.print("\t| Enter the address: ");
        this.address = sc.nextLine();
        System.out.print("\t| Enter the contact number: ");
        this.contact = sc.next();
        while (!contact.matches("^[6-9][0-9]{9}$")) {
            System.out.println("\tInvalid Contact Number.\n\tTry Again");
            System.out.print("\t| Enter the contact number: ");
            this.contact = sc.next();
        }
        System.out.print("\t| Account type (Saving or Current): ");
        this.account_type = sc.next();
        while (!account_type.equalsIgnoreCase("Saving") && !account_type.equalsIgnoreCase("Current")) {
            System.out.println("\tInvalid Account Type. Choose either 'Saving' or 'Current'.");
            System.out.print("\t| Account type (Saving or Current): ");
            account_type = sc.next();
        }
       
        account_no();
        this.current_money = 0;
    }

    String getPhoneNo() {
        return contact;
    }

    double getmoney() {
        return current_money;
    }

    String getName() {
        return this.name;
    }

    String getAccType() {
        return this.account_type;
    }

    String getAddress() {
        return this.address;
    }

    void deposit_money(double pass_money) {
        this.current_money += pass_money;
    }

    void withdraw_money(double update) {
        this.current_money -= update;
    }

    void account_no() {
        int rand = (int) (Math.random() * 1_000_000);
        String pass = "KM2325" + String.format("%06d", rand);
        setAccountNo(pass);
    }

    void display() {
        System.out.println("\n\tName: " + name);
        System.out.println("\tAddress: " + address);
        System.out.println("\tContact: " + contact);
        System.out.println("\tAccount Number: " + getAccountNo());
        System.out.println("\tAccount type: " + account_type.toUpperCase());
    }
}

public class BankPro {

    static PreparedStatement ps;
    static ResultSet rs;
    static LocalDate date;
    static LocalTime time;
    static Details obj;

    public static void main(String[] args) {

        String url = "jdbc:mysql://127.0.0.1:3306/Bank_Project?user=root";
        String user = "root";
        String password = "kartik@23";

        try (Connection con = DriverManager.getConnection(url, user, password)) {

            System.out.println("\n\tConnected to database successfully!\n");
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println(
                        "\n\t1.Create Account.\t2.Login.\n" +
                        "\t3.Exit.\n"
                );

                System.out.print("\tEnter the Choice: ");
                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1 -> createAccount(con, sc);
                    case 2 -> {
                        String sessionAcc = login(con, sc);
                        if (sessionAcc != null) sessionDashboard(con, sc, sessionAcc);
                    }
                    case 3 -> {
                        System.out.println("\n\tThanks for visiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("\n\tInvalid Choice.");
                }
            }

        } catch (SQLException e) {
            System.out.println("\tDatabase Error:" + e.getMessage());
        }
    }

    // CREATE ACCOUNT 
    static void createAccount(Connection con, Scanner sc) {
        System.out.println("\n\tFill out all Details: ");
        System.out.println("\n\tMinimum balance is 500 Rs.\n");
        obj = new Details();
        obj.createAccount(sc);

        while (true) {
            System.out.print("\t| Enter the starting Amount: ");
            double pass = sc.nextDouble();
            System.out.println("  --------------------------------");

            if (pass < 500) {
                System.out.println("\tInsufficient amount. Try again.");
            } else {
                obj.deposit_money(pass);
                String pin;
                while(true) {
	        	        System.out.print("\n\n\tCreate the PIN: ");
	        	        pin = sc.next();
		        	        while (!pin.matches("\\d{4}")) {
		        	            System.out.print("\tPIN must be 4 digits! Enter Again: ");
		        	            pin = sc.next();
		        	        }
	        	        System.out.print("\n\n\tConform the PIN: ");
	        	        String pin2=sc.next();
	        	        if(pin.equalsIgnoreCase(pin2)) {
	        	        	  obj.setAccountPass(pin);
	        	        	  break;
	        	        }
	        	        else {System.out.println("\n\tPIN do not match.\n\tTry Again.");}
                }
                String insert = "INSERT INTO BankInfo(Acct_No, Cust_name, Cust_phone, Cust_Address, Acct_type, Acct_balance, Acct_PIN) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try {
                	
                    ps = con.prepareStatement(insert);
                    ps.setString(1, obj.getAccountNo());
                    ps.setString(2, obj.getName());
                    ps.setString(3, obj.getPhoneNo());
                    ps.setString(4, obj.getAddress());
                    ps.setString(5, obj.getAccType());
                    ps.setDouble(6, obj.getmoney());
                    ps.setString(7, pin);
                    ps.executeUpdate();
                   
                    System.out.println("\n\t-------------------------------");
                    System.out.println("\t| Account Number:"+ obj.getAccountNo()+ " |");
                    System.out.println("\t-------------------------------");
                    System.out.println("\tAccount created successfully.\n");
                    break;
                } catch (SQLException es) {
                    System.out.println("Error: " + es.getMessage());
                    es.printStackTrace();
                }
            }
        }
    }

    // LOGIN 
    static String login(Connection con, Scanner sc) throws SQLException {
        System.out.print("\tEnter Account Number: ");
        String accNo = sc.next();
        while (!accNo.matches("KM2325[0-9]{6}$")) {
            System.out.println("\tInvalid Account No. Try Again.");
            accNo = sc.next();
        }

        System.out.print("\tEnter 4 digit PIN: ");
        String pin = sc.next();
        while (!pin.matches("\\d{4}")) {
            System.out.println("\tPIN must be 4 digits!");
            pin = sc.next();
        }

        String query = "SELECT * FROM BankInfo WHERE Acct_No=? AND Acct_PIN=?";
        ps = con.prepareStatement(query);
        ps.setString(1, accNo);
        ps.setString(2, pin);
        rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("\n\tLogin Successful!");
            return accNo;
        } else {
            System.out.println("\n\tInvalid Account or PIN.");
            return null;
        }
    }

    // SESSION DASHBOARD
    static void sessionDashboard(Connection con, Scanner sc, String sessionAcc) throws SQLException {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println(
                    "\n\t------ USER DASHBOARD ------\n" +
                    "\t1.Check Balance\n" +
                    "\t2.Add Money\n" +
                    "\t3.Withdraw Money\n" +
                    "\t4.Send Money\n" +
                    "\t5.Transaction History\n" +
                    "\t6.Update Profile\n" +
                    "\t7.Reset PIN\n" +
                    "\t8.Logout\n"
            );
            System.out.print("\tEnter Choice: ");
            int choice = sc.nextInt();
            //sc.nextLine();
            
            switch (choice) {
                case 1 -> checkBalance(con, sessionAcc);
                case 2 -> addMoney(con, sessionAcc, sc);
                case 3 -> withdrawMoney(con, sessionAcc, sc);
                case 4 -> transferMoney(con, sessionAcc, sc);
                case 5 -> transactionHistory(con, sessionAcc);
                case 6 -> updateProfile(con, sessionAcc, sc);
                case 7 -> resetPIN(con, sessionAcc, sc);
                case 8 -> {
                    System.out.println("\n\tLogged Out Successfully.");
                    loggedIn = false;
                }
                default -> System.out.println("\n\tInvalid Choice.");
            }
            System.out.println();
            sc.nextLine();
            System.out.print("\tPress (ENTER) to continue...");
            sc.nextLine();
        }
    }

    // OPERATIONS 
    static void checkBalance(Connection con, String accNo) throws SQLException {
        String query = "SELECT Acct_balance FROM BankInfo WHERE Acct_No=?";
        ps = con.prepareStatement(query);
        ps.setString(1, accNo);
        rs = ps.executeQuery();
        if (rs.next()) System.out.println("\n\tCurrent Balance: " + rs.getDouble("Acct_balance"));
    }
    // DEPOSIT MONEY
    static void addMoney(Connection con, String accNo, Scanner sc) throws SQLException {
        System.out.print("\tEnter Amount to Deposit: ");
        double amt = sc.nextDouble();
        while (amt <= 0) {
            System.out.print("\tAmount must be Greater than 0.\n\t Enter Again: ");
            amt = sc.nextDouble();
        }
        sc.nextLine();
        date = LocalDate.now();
        time = LocalTime.now();
        con.setAutoCommit(false);
        try {
            String update = "UPDATE BankInfo SET Acct_balance= Acct_balance + ? WHERE Acct_No=?";
            ps = con.prepareStatement(update);
            ps.setDouble(1, amt);
            ps.setString(2, accNo);
            ps.executeUpdate();
            ps.close();

            String getname = "SELECT Cust_name FROM BankInfo WHERE Acct_No=?";
            ps = con.prepareStatement(getname);
            ps.setString(1, accNo);
            rs = ps.executeQuery();
            String name = null;
            if (rs.next()) name = rs.getString("Cust_name");
            ps.close();

            String trans = "INSERT INTO Transactions (Acct_No,Acct_hold_name,Trans_method,Trans_date,Trans_time,Trans_type,Amount) VALUES (?,?,?,?,?,?,?)";
            ps = con.prepareStatement(trans);
            ps.setString(1, accNo);
            ps.setString(2, name);
            ps.setString(3, "SELF");
            ps.setDate(4, java.sql.Date.valueOf(date));
            ps.setTime(5, java.sql.Time.valueOf(time));
            ps.setString(6, "Credited");
            ps.setDouble(7, amt);
            ps.executeUpdate();
            ps.close();
            con.commit();
            System.out.println("\n\tDeposit Successful!");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            con.setAutoCommit(true);
        }
    }
    
 //  CASE 3: Withdraw Money 
    static void withdrawMoney(Connection con, String accNo, Scanner sc) throws SQLException {
        System.out.print("\tEnter 4 digit PIN: ");
        String pin = sc.next();
        while (!pin.matches("\\d{4}")) {
            System.out.print("\tPIN must be 4 digits! Enter Again: ");
            pin = sc.next();
        }

        date = LocalDate.now();
        time = LocalTime.now();
        con.setAutoCommit(false);
        try {
            String check = "SELECT * FROM BankInfo WHERE Acct_No=? AND Acct_PIN=?";
            ps = con.prepareStatement(check);
            ps.setString(1, accNo);
            ps.setString(2, pin);
            rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Cust_name");
                double current_balance = rs.getDouble("Acct_balance");
                System.out.print("\n\tEnter Withdraw Amount: ");
                double amt = sc.nextDouble();
                while (amt <= 0) {
                    System.out.print("\tAmount must be greater than 0. Enter Again: ");
                    amt = sc.nextDouble();
                }
                sc.nextLine();
                if (amt <= current_balance) {
                    String update_amt = "UPDATE BankInfo SET Acct_balance = Acct_balance - ? WHERE Acct_No=?";
                    ps = con.prepareStatement(update_amt);
                    ps.setDouble(1, amt);
                    ps.setString(2, accNo);
                    ps.executeUpdate();
                    ps.close();

                    String trans = "INSERT INTO Transactions (Acct_No,Acct_hold_name,Trans_method,Trans_date,Trans_time,Trans_type,Amount) VALUES (?,?,?,?,?,?,?)";
                    ps = con.prepareStatement(trans);
                    ps.setString(1, accNo);
                    ps.setString(2, name);
                    ps.setString(3, "SELF");
                    ps.setDate(4, java.sql.Date.valueOf(date));
                    ps.setTime(5, java.sql.Time.valueOf(time));
                    ps.setString(6, "Debited");
                    ps.setDouble(7, amt);
                    ps.executeUpdate();
                    ps.close();

                    con.commit();
                    System.out.println("\n\tWithdrawal Successful!");
                } else {
                    System.out.println("\n\tInsufficient Balance.");
                    con.rollback();
                }
            } else {
                System.out.println("\n\tAccount Not Found or Invalid PIN.");
            }
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Database Error: " + e.getMessage());
        } finally {
            con.setAutoCommit(true);
        }
    }

    //CASE 4: Transfer Money 
    static void transferMoney(Connection con, String senderAcc, Scanner sc) throws SQLException {
        date = LocalDate.now();
        time = LocalTime.now();

        System.out.print("\tEnter Receiver Account No: ");
        String receiverAcc = sc.next();
        while (!receiverAcc.matches("KM2325[0-9]{6}$")) {
            System.out.print("\tInvalid Account No. Enter Again: ");
            receiverAcc = sc.next();
        }

        System.out.print("\tEnter Receiver Phone No: ");
        String receiverPhone = sc.next();
        while (!receiverPhone.matches("^[6-9][0-9]{9}$")) {
            System.out.print("\tInvalid Phone. Enter Again: ");
            receiverPhone = sc.next();
        }

        if (senderAcc.equals(receiverAcc)) {
            System.out.println("\n\tCannot transfer to same account.");
            return;
        }

        System.out.print("\tEnter Your PIN: ");
        String senderPin = sc.next();
        while (!senderPin.matches("\\d{4}")) {
            System.out.print("\tPIN must be 4 digits! Enter Again: ");
            senderPin = sc.next();
        }
        sc.nextLine();
        try {
            con.setAutoCommit(false);

            // Verify receiver
            String receiverQuery = "SELECT * FROM BankInfo WHERE Acct_No=? AND Cust_phone=?";
            ps = con.prepareStatement(receiverQuery);
            ps.setString(1, receiverAcc);
            ps.setString(2, receiverPhone);
            rs = ps.executeQuery();
            if (!rs.next()) {
                System.out.println("\n\tReceiver not found.");
                con.rollback();
                return;
            }
            String receiverName = rs.getString("Cust_name");
            ps.close();

            // Verify sender
            String senderQuery = "SELECT * FROM BankInfo WHERE Acct_No=? AND Acct_PIN=?";
            ps = con.prepareStatement(senderQuery);
            ps.setString(1, senderAcc);
            ps.setString(2, senderPin);
            rs = ps.executeQuery();
            if (!rs.next()) {
                System.out.println("\n\tIncorrect Account No or PIN.");
                con.rollback();
                return;
            }
            double senderBalance = rs.getDouble("Acct_balance");
            String senderName = rs.getString("Cust_name");
            ps.close();

            System.out.print("\tEnter Amount to Transfer: ");
            double amount = sc.nextDouble();
            while (amount <= 0) {
                System.out.print("\tAmount must be > 0. Enter Again: ");
                amount = sc.nextDouble();
            }

            if (senderBalance < amount) {
                System.out.println("\n\tInsufficient Balance.");
                con.rollback();
                return;
            }

            // Update balances
            String credit = "UPDATE BankInfo SET Acct_balance = Acct_balance + ? WHERE Acct_No=?";
            ps = con.prepareStatement(credit);
            ps.setDouble(1, amount);
            ps.setString(2, receiverAcc);
            ps.executeUpdate();
            ps.close();

            String debit = "UPDATE BankInfo SET Acct_balance = Acct_balance - ? WHERE Acct_No=?";
            ps = con.prepareStatement(debit);
            ps.setDouble(1, amount);
            ps.setString(2, senderAcc);
            ps.executeUpdate();
            ps.close();

            // Transaction records
            String senderTrans = "INSERT INTO Transactions (Acct_hold_name, Acct_No, Trans_method, Trans_date, Trans_time, Trans_type, Amount) VALUES (?,?,?,?,?,?,?)";
            ps = con.prepareStatement(senderTrans);
            ps.setString(1, receiverName);
            ps.setString(2, senderAcc);
            ps.setString(3, "Transfer");
            ps.setDate(4, java.sql.Date.valueOf(date));
            ps.setTime(5, java.sql.Time.valueOf(time));
            ps.setString(6, "Debited");
            ps.setDouble(7, amount);
            ps.executeUpdate();
            ps.close();

            String receiverTrans = "INSERT INTO Transactions (Acct_hold_name, Acct_No, Trans_method, Trans_date, Trans_time, Trans_type, Amount) VALUES (?,?,?,?,?,?,?)";
            ps = con.prepareStatement(receiverTrans);
            ps.setString(1, senderName);
            ps.setString(2, receiverAcc);
            ps.setString(3, "Transfer");
            ps.setDate(4, java.sql.Date.valueOf(date));
            ps.setTime(5, java.sql.Time.valueOf(time));
            ps.setString(6, "Credited");
            ps.setDouble(7, amount);
            ps.executeUpdate();
            ps.close();

            con.commit();
            System.out.println("\n\tTransfer Successful.");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Error: " + e.getMessage());
        } finally {
            con.setAutoCommit(true);
        }
    }

    //CASE 5: Transaction History 
    static void transactionHistory(Connection con, String accNo) throws SQLException {
        String query = "SELECT * FROM Transactions WHERE Acct_No=? ORDER BY Trans_date DESC, Trans_time DESC";
        ps = con.prepareStatement(query);
        ps.setString(1, accNo);
        rs = ps.executeQuery();

        System.out.println("\n\t---------------------------------------------------------------------------------------------------------------");
        System.out.printf("\t%-6s %-20s %-15s %-12s %-12s %-10s %-12s %-10s\n", "ID", "Name", "Account No", "Method", "Date", "Time", "Type", "Amount");
        System.out.println("\t---------------------------------------------------------------------------------------------------------------");

        boolean found = false;
        while (rs.next()) {
            found = true;
            int id = rs.getInt("Trans_ID");
            String name = rs.getString("Acct_hold_name");
            String account = rs.getString("Acct_No");
            String method = rs.getString("Trans_method");
            java.sql.Date date = rs.getDate("Trans_date");
            Time time = rs.getTime("Trans_time");
            String type = rs.getString("Trans_type");
            double amount = rs.getDouble("Amount");
            System.out.printf("\t%-6d %-20s %-15s %-12s %-12s %-10s %-12s %-10.2f\n", id, name, account, method, date, time, type, amount);
        }
        System.out.println("\t---------------------------------------------------------------------------------------------------------------");
        if (!found) System.out.println("\n\tNo Transactions Found.");
    }

    // CASE 6: Update Profile
    static void updateProfile(Connection con, String accNo, Scanner sc) throws SQLException {
        String check = "SELECT * FROM BankInfo WHERE Acct_No=?";
        ps = con.prepareStatement(check);
        ps.setString(1, accNo);
        rs = ps.executeQuery();
       
        if (rs.next()) {
            sc.nextLine();
            System.out.println("\n\tUpdate Details (Name, Phone, Address, Type)\n");
            System.out.print("\t| Enter new name: ");
            String upname = sc.nextLine();
            while (!upname.matches("(([A-Z][a-z]+(\\s[A-Z][a-z]+)*)|([a-z]+(\\s[a-z]+)*))")) {
                System.out.print("\tInvalid Name. Enter Again: ");
                upname = sc.nextLine();
            }
            System.out.print("\t| Enter new contact: ");
            String upcontact = sc.next();
            while (!upcontact.matches("^[6-9][0-9]{9}$")) {
                System.out.print("\tInvalid Phone. Enter Again: ");
                upcontact = sc.next();
            }
            sc.nextLine();
            System.out.print("\t| Enter new address: ");
            String upaddress = sc.nextLine();
            System.out.print("\t| Account type (Saving or Current): ");
            String acct_type = sc.next();
            String insert = "UPDATE BankInfo SET Cust_name=?, Cust_phone=?, Cust_address=?, Acct_type=? WHERE Acct_No=?";
            ps = con.prepareStatement(insert);
            ps.setString(1, upname);
            ps.setString(2, upcontact);
            ps.setString(3, upaddress);
            ps.setString(4, acct_type);
            ps.setString(5, accNo);
            ps.executeUpdate();
            ps.close();
            System.out.println("\tProfile Updated Successfully!");
            sc.nextLine();
        } else {
            System.out.println("\tAccount not Found!.");
        }
    }

   //CASE 7: Reset PIN
    static void resetPIN(Connection con, String accNo, Scanner sc) throws SQLException {
        System.out.print("\tEnter Previous PIN: ");
        String pin = sc.next();
        while (!pin.matches("\\d{4}")) {
            System.out.print("\tPIN must be 4 digits! Enter Again: ");
            pin = sc.next();
        }

        String check = "SELECT * FROM BankInfo WHERE Acct_No=? AND Acct_PIN=?";
        ps = con.prepareStatement(check);
        ps.setString(1, accNo);
        ps.setString(2, pin);
        rs = ps.executeQuery();

        if (rs.next()) {
            String newPIN1, newPIN2;
            while (true) {
                System.out.print("\tEnter new PIN: ");
                newPIN1 = sc.next();
                if (!newPIN1.matches("\\d{4}")) {
                    System.out.println("\tPIN must be 4 digits! Try Again.");
                    continue;
                }
                System.out.print("\tConfirm new PIN: ");
                newPIN2 = sc.next();
                if (newPIN1.equals(newPIN2)) {
                    String update = "UPDATE BankInfo SET Acct_PIN=? WHERE Acct_No=?";
                    ps = con.prepareStatement(update);
                    ps.setString(1, newPIN1);
                    ps.setString(2, accNo);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("\n\tPIN Reset Successfully!");
                   
                    break;
                } else {
                    System.out.println("\tPIN does not match! Try Again.");
                }
            }
        } else {
            System.out.println("\n\tIncorrect Previous PIN.");
        }
        sc.nextLine();
    }
}
