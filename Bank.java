package kartikjava;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.*;
 class Info {

    private String accountNo;
    private int pin;

     public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountPass(int pass) {
        this.pin = pass;
    }

     public int getAccountPass() {
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
		while(!name.matches("(([A-Z][a-z]+(\\s[A-Z][a-z]+)*)|([a-z]+(\\s[a-z]+)*))")){
			System.out.println("\tInvalid Name.\n \tTry Again\n");
			System.out.print("\t| Enter the name: ");
			this.name = sc.nextLine();
		}
		System.out.print("\t| Enter the address: ");
		this.address = sc.nextLine();
        System.out.print("\t| Enter the contact number: ");
        this.contact = sc.next();
		while(!contact.matches("^[6-9][0-9]{9}$")){
			System.out.println("\tInvalid Contact Number.\n\tTry Again");
			System.out.print("\t| Enter the contact number: ");
			this.contact = sc.next();
		}
        System.out.print("\t| Account type (Saving or Current). : ");
        this.account_type = sc.next();
	   
        account_no();
       this.current_money=0;
    }
    String getPhoneNo() {
        return contact;
    }
    double getmoney(){
        return current_money;
    }
    
    String getName(){
        return this.name;
    }
	String getAccType(){
		return this.account_type;
	}
	String getAddress(){
		return this.address;
	}
	
    void deposit_money(double pass_money){
        this.current_money+=pass_money;
    }
     void withdraw_money(double update){
        this.current_money-=update;
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
        System.out.println("\tAccount NUmber: " + getAccountNo());
        System.out.println("\tAccount type: " + account_type.toUpperCase());
		
    }
	
}


	public class Bank {
		
		static PreparedStatement ps;
		static ResultSet rs;
		static LocalDate date;
		static LocalTime time;
    public static void main(String[] args) {
			
		String url = "jdbc:mysql://127.0.0.1:3306/Bank_Project?user=root";
        String user = "root";
		String password = "kartik@23";
		try(Connection con = DriverManager.getConnection(url, user, password))
		{
			System.out.println("\tConnected to database successfully!\n");
			
			System.out.println("\n\t-----Wel Come to Your Bank------\n");
			Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println(
                    "\n\t1.Create Acoount.\t2.Your Profile.\n" +
                    "\t3.Have a Account.\t4.Set the PIN.\n" +
                    "\t5.Withdraw Money.\t6.Check Balance.\n" +
                    "\t7.Add Money.\t\t8.Update Profile\n" + 
					"\t9.Reset PIN.\t\t10.Transcation History.\n"+
					"\t11.Send Money. \t\t12.For Exit.\n"
            );

            
			System.out.print("\tEnter the Choice: ");
			int	ch1=sc.nextInt();
			sc.nextLine();
            switch (ch1) {
			
    // 1. Create Account
    case 1: {
        System.out.print("\tFill out the all Details: ");
        //int count = sc.nextInt();

        System.out.println("\n\tMinimum balance is 500 Rs.\n");

        //for (int i = 0; i < count; i++) {
           Details obj = new Details();
			obj.createAccount(sc);
            while (true) {
                System.out.print("\t| Enter the starting Amount: ");
                double pass = sc.nextDouble();
				
				System.out.println("  --------------------------------");

                if (pass < 500) {
                    System.out.println("\tInsufficient amount. Try again.");
					
                } else {
					
                    obj.deposit_money(pass);
					String insert = "INSERT INTO BankInfo(Acct_No, Cust_name, Cust_phone, Cust_Address, Acct_type, Acct_balance, Acct_PIN) VALUES (?, ?, ?, ?, ?, ?,?)";
					try{
					
					ps = con.prepareStatement(insert);
					ps.setString(1,obj.getAccountNo());
					ps.setString(2,obj.getName());
					ps.setString(3,obj.getPhoneNo());
					ps.setString(4,obj.getAddress());
					ps.setString(5,obj.getAccType());
					ps.setDouble(6,obj.getmoney());
					ps.setNull(7,java.sql.Types.INTEGER);
					ps.executeUpdate();
					System.out.println("\tAccount created successfully.\n");
				
                    break;
				}catch(SQLException es){
					System.out.println("Error: "+es.getMessage());
					es.printStackTrace();
				}
              }
           }
	
    }
		break;
    // 2. Show Profile
    case 2: {
				System.out.print("\tEnter your Phone Number: ");
				String phone = sc.next();
				while(!phone.matches("^[6-9][0-9]{9}$")){
					System.out.println("\tInvalid Contact Number.\n\tTry Again");
					System.out.print("\t| Enter the contact number: ");
					phone = sc.next();
				}
				boolean found=false;
				String phn="SELECT Acct_No, Cust_name, Cust_phone, Cust_Address, Acct_type FROM BankInfo WHERE Cust_phone =?";
				try{
				
				ps=con.prepareStatement(phn);
				ps.setString(1,phone);
				rs=ps.executeQuery();
				if(rs.next()){
					do{
						System.out.println(
							"\tAccount No : " + rs.getString("Acct_No") + " | " +
							"Name : " + rs.getString("Cust_name") + " | " +
							"Phone : " + rs.getString("Cust_phone") + " | " +
							"Address : " + rs.getString("Cust_Address") + " | " +
							"Account Type : " + rs.getString("Acct_Type") + "\n"
						);
					}while(rs.next());
				}else{
					System.out.println("\n\tNo account is associated with this phone number.");
				}
					//else{System.out.println("\n\tAccount not found!.");}
				}catch(SQLException es){
					System.out.println("Error:"+es.getMessage());
				}	
			break;
	}
			// 3. Have an Account
    case 3: {
				System.out.print("\tEnter your Account Number: ");
				String accNo = sc.next();
				while(!accNo.matches("KM2325[0-9]{6}$")){
					System.out.println("\tInvalid Account No.\n\t Try Again.");
					System.out.println("\tEnter your Account Number:");
					 accNo=sc.next();
				}
				System.out.print("\tEnter the PIN: ");
				int pass = sc.nextInt();

				boolean found = false;
				String acct="SELECT * FROM BankInfo WHERE Acct_No=? && Acct_PIN = ?";
				ps=con.prepareStatement(acct);
				ps.setString(1,accNo);
				ps.setInt(2,pass);
				rs=ps.executeQuery();
				if(rs.next()){
					System.out.println("\n\tAccount is Exist.");
					found=true;
				}
				
				if (!found) {
					System.out.println("\tAccount is not Exist.");
				}
				break;
			}

			// 4. Set Password
    case 4: {
				System.out.print("\tEnter your Account Number: ");
				String accNo = sc.next();
				while(!accNo.matches("KM2325[0-9]{6}$")){
					System.out.println("\tInvalid Account No.\n\t Try Again.");
					System.out.println("\tEnter your Account Number:");
					 accNo=sc.next();
				}
				
				
				try{
					String checkpass="SELECT Acct_PIN FROM BankInfo WHERE Acct_PIN IS NOT NULL AND Acct_No=?";
					ps=con.prepareStatement(checkpass);
					ps.setString(1,accNo);
					rs=ps.executeQuery();
						if(rs.next()){System.out.println("\n\tPIN already set.");}
						else{
							rs.close();
							while(true){
							System.out.print("\tSet PIN: ");
							int p1 = sc.nextInt();
							System.out.print("\tConfirm PIN: ");
							int p2 = sc.nextInt();
							if (p1 == p2) {
								String setpass="UPDATE  BankInfo SET Acct_PIN=? WHERE Acct_No=?";
								ps=con.prepareStatement(setpass);
								ps.setInt(1,p2);
								ps.setString(2,accNo);
									
									if(ps.executeUpdate() > 0)
									{
										//obj.setAccountPass(p1);
										System.out.println("\tPIN set successfully.");
										break;
									}else{
										System.out.println("\n\tAccount not found!.");break;
										}
								}else{
										System.out.println("\n\tPassword not match!. \n\tTry Again.");
									}
							}
						}
					}catch(SQLException e){System.out.println("ERROR: "+e.getMessage());}
				break;
			}

    // 5. Withdraw Money
    case 5: {
				System.out.print("\tEnter Account Number: ");
				String accNo = sc.next();
				while(!accNo.matches("KM2325[0-9]{6}$")){
					System.out.println("\tInvalid Account No.\n\t Try Again.");
					System.out.println("\tEnter your Account Number:");
					 accNo=sc.next();
				}
				System.out.print("\tEnter PIN: ");
				int pass = sc.nextInt();
				date=LocalDate.now();
				time=LocalTime.now();
				try{
					con.setAutoCommit(false);
					String check="SELECT * FROM BankInfo WHERE Acct_No=? AND Acct_PIN = ?";
					ps=con.prepareStatement(check);
					ps.setString(1,accNo);
					ps.setInt(2,pass);
					rs=ps.executeQuery();
						if(rs.next()){
							double current_balance=rs.getDouble("Acct_balance");
							System.out.print("\n\tEnter the Withdraw Amount:");
							double amt=sc.nextDouble();
							if(amt<=current_balance){
								
									try{
									String update_amt="UPDATE BankInfo SET Acct_balance  = Acct_balance - ? WHERE Acct_No=?";
									ps=con.prepareStatement(update_amt);
									ps.setDouble(1,amt);
									ps.setString(2,accNo);
									int row=ps.executeUpdate();
									ps.close();
									String trans="INSERT INTO Transactions (Acct_No,Trans_name,Trans_date,Trans_time,Trans_type,Amount) VALUES (?,?,?,?,?,?)";
									ps=con.prepareStatement(trans);
									ps.setString(1,accNo);
									ps.setString(2,"BOI");
									ps.setDate(3,java.sql.Date.valueOf(date));
									ps.setTime(4,java.sql.Time.valueOf(time));
									ps.setString(5,"Debited");
									ps.setDouble(6,amt);
									ps.executeUpdate();
									ps.close();
									if(row>0){
										con.commit();
										System.out.println("\n\tWithdrawal Successful!");
										
										}
									else{con.rollback();System.out.println("\n\tSomething went Wrong!");}
									}catch(SQLException es){System.out.println("Error: "+es.getMessage());}
								}
								else{System.out.println("\n\tInsufficient Balance.");}
							}
							else{System.out.println("\n\tAccount Not Found (check Account No. & PIN).");}
						
						
				}catch (SQLException e) {
					try {
						con.rollback(); 
					} catch (SQLException ex) {
						System.out.println("Database Error: " + ex.getMessage());
						
					}
					System.out.println("Database Error: " + e.getMessage());
					
				} finally {
					try {
						con.setAutoCommit(true); 
					} catch (SQLException ex) {
						System.out.println("Database Error: " + ex.getMessage());
						
					}
				}
				break;
			}

    // 6. Check Balance
    case 6: {
				System.out.print("\tEnter Account Number: ");
				String accNo = sc.next();
				while(!accNo.matches("KM2325[0-9]{6}$")){
					System.out.println("\tInvalid Account No.\n\t Try Again.");
					System.out.println("\tEnter your Account Number:");
					 accNo=sc.next();
				}
				System.out.print("\tEnter PIN: ");
				int pass = sc.nextInt();
				
					try{
						String check = "SELECT Acct_balance FROM BankInfo WHERE Acct_No=? AND Acct_PIN=?";
						ps=con.prepareStatement(check);
						ps.setString(1, accNo);
						ps.setInt(2, pass);
						
						rs = ps.executeQuery();
						if (rs.next()) {
							double balance = rs.getDouble("Acct_balance");
							System.out.println("\n\tCurrent Balance: " + balance);
						} else {
							System.out.println("\n\tAccount Not Found or Invalid PIN.");
						}
						rs.close(); 
					} catch (SQLException e) {
						System.out.println("Database Error: " + e.getMessage()+"\n");
						e.printStackTrace();
					}
				break;
			}

    // 7. Add Money
    case 7: {	
				System.out.print("\tEnter Account Number: ");
				String accNo = sc.next();
				while(!accNo.matches("KM2325[0-9]{6}$")){
					System.out.println("\tInvalid Account No.\n\t Try Again.");
					System.out.println("\tEnter your Account Number:");
					 accNo=sc.next();
				}
				System.out.print("\tEnter PIN: ");
				int pass = sc.nextInt();
				time=LocalTime.now();
				date=LocalDate.now();
				String name=null;
				
				try{
					con.setAutoCommit(false);
					System.out.print("\n\tEnter Deposit the Amount: ");
					double amt=sc.nextDouble();
					String check="UPDATE  BankInfo SET Acct_balance= Acct_balance + ? WHERE Acct_No=? AND Acct_PIN=?";
					ps=con.prepareStatement(check);
					ps.setDouble(1,amt);
					ps.setString(2,accNo);
					ps.setInt(3,pass);
					int count=ps.executeUpdate();
					if(count>0){
						ps.close();
						String getname="SELECT Cust_name FROM BankInfo WHERE Acct_No=?";
						ps=con.prepareStatement(getname);
						ps.setString(1,accNo);
						rs=ps.executeQuery();
						if(rs.next()){
							 name=rs.getString("Cust_name");
						}
						
						ps.close();
						String trans="INSERT INTO Transactions (Acct_No,Acct_hold_name,Trans_method,Trans_date,Trans_time,Trans_type,Amount) VALUES (?,?,?,?,?,?,?)";
						ps=con.prepareStatement(trans);
						ps.setString(1,accNo);
						ps.setString(2,name);
						ps.setString(3,"SELF");
						ps.setDate(4,java.sql.Date.valueOf(date));
						ps.setTime(5,java.sql.Time.valueOf(time));
						ps.setString(6,"Credited");
						ps.setDouble(7,amt);
						ps.executeUpdate();
						ps.close();
						con.commit();
						System.out.println("\n\tSuccessfully Deposit.");
					}else{con.rollback(); System.out.println("Something went Wrong!check acc no or pin");}
				}catch(SQLException e){
					try{
						con.rollback();
					}catch(SQLException es){
						System.out.println("Error: "+es.getMessage());
						es.printStackTrace();
					}
					System.out.println("Error: "+e.getMessage());
					e.printStackTrace();
				}finally{
					try{
						con.setAutoCommit(true);
					}catch(SQLException ex){
						System.out.println("Error : "+ ex.getMessage());
						ex.printStackTrace();
					}
				}
				break;
		}
				
			
			//8.Update your profile.
			case 8: {	
						boolean found=false;
						System.out.print("\tEnter your Account Number to Update:");
						String accNo=sc.next();
						while(!accNo.matches("KM[0-9]{10}$")){
							System.out.println("\tInvalid Account No.\n\t Try Again.");
							System.out.println("\tEnter your Account Number:");
							accNo=sc.next();
						}
						try{
							String check="SELECT * FROM BankInfo WHERE Acct_No=?";
							ps=con.prepareStatement(check);
							ps.setString(1,accNo);
							 rs=ps.executeQuery();
							if(rs.next()){
								sc.nextLine();
								System.out.println("\n\tUpdate the Details.\n" + "\tYou can only update (Name , Phone, Address, Type)\n");
								 System.out.println("  --------------------------------");
								System.out.print("\t| Enter the name: ");                      
								String upname = sc.nextLine();
								
								while(!upname.matches("(([A-Z][a-z]+(\\s[A-Z][a-z]+)*)|([a-z]+(\\s[a-z]+)*))")){
									System.out.println("\tInvalid Name.\n \tTry Again\n");
									System.out.print("\t| Enter the name: ");
									upname = sc.nextLine();
								}
							
								System.out.print("\t| Enter the contact number: ");
								String upcontact = sc.next();
								while(!upcontact.matches("^[6-9][0-9]{9}$")){
									System.out.println("\tInvalid Contact Number.\n\tTry Again");
									System.out.print("\t| Enter the contact number: ");
									 upcontact = sc.next();
								}
								sc.nextLine();
								System.out.print("\t| Enter the address: ");
								String upaddress = sc.nextLine();
								System.out.print("\t| Account type (Saving or Current). : ");
								String acct_type = sc.next();
								String insert="UPDATE BankInfo SET Cust_name=? , Cust_phone=?, Cust_address=?, Acct_type=? WHERE Acct_No=?";
								ps=con.prepareStatement(insert);
								ps.setString(1,upname);
								ps.setString(2,upcontact);
								ps.setString(3,upaddress);
								ps.setString(4,acct_type);
								ps.setString(5,accNo);
								ps.executeUpdate();
								ps.close();
							}else{
								System.out.println("\tAccount not Found!.");
							}
						}catch(SQLException es){System.out.println("Error:"+es.getMessage());}
						
						break;
					}

				//9.reset password.
			case 9:  {
						System.out.println("\n\t--Reset your PIN.--");
						System.out.print("\tEnter your Account Number: ");
						String accNo = sc.next();
						while(!accNo.matches("KM[0-9]{10}$")){
							System.out.println("\tInvalid Account No.\n\t Try Again.");
							System.out.println("\tEnter your Account Number:");
					 		accNo=sc.next();
						}
						System.out.print("\tEnter Previous PIN: ");
						int pass = sc.nextInt();
						String check="SELECT * FROM BankInfo WHERE Acct_No=? AND Acct_PIN=?";
						try{
							ps=con.prepareStatement(check);
							ps.setString(1,accNo);
							ps.setInt(2,pass);
							rs=ps.executeQuery();
							if(rs.next()){
								System.out.print("\tEnter new PIN:");
								int new_pass1=sc.nextInt();
								System.out.print("\tConform new PIN:");
								int new_pass2=sc.nextInt();
								if(new_pass1 == new_pass2){
									String insertPass="UPDATE BankInfo SET Acct_PIN=? WHERE Acct_No=?";
									ps=con.prepareStatement(insertPass);
									ps.setInt(1,new_pass1);
									ps.setString(2,accNo);
									ps.executeUpdate();
									ps.close();
									System.out.println("\n\tReset Password Successfully.");
								}
								else{System.out.println("\n\tPassword do not match.");}
								
							}else{System.out.println("\n\tIncorrect PIN or Account Number.");}
						}
						catch(SQLException es){
							System.out.println("Error:"+es.getMessage()+ "\n");
							es.printStackTrace();
						}
					break;
				}


			
	    case 10: {
					System.out.print("\n\tEnter Account Number: ");
					String acc = sc.next();

					while (!acc.matches("KM[0-9]{10}$")) {
						System.out.println("\tInvalid Account Number. Try Again.");
						acc = sc.next();
					}

					String query = "SELECT * FROM Transactions WHERE Acct_No=? ORDER BY Trans_date DESC, Trans_time DESC";
					ps = con.prepareStatement(query);
					ps.setString(1, acc);
					rs = ps.executeQuery();

					System.out.println("\n\n\t---------------------------------------------------------------------------------------------------------------");

						System.out.printf("\t%-6s %-20s %-15s %-12s %-12s %-10s %-12s %-10s\n",
						"ID", "Name", "Account No", "Method", "Date", "Time", "Type", "Amount");

						System.out.println("\t---------------------------------------------------------------------------------------------------------------");

						boolean found = false;

						while (rs.next()) {

							found = true;

							int id = rs.getInt("Trans_ID");
							String name = rs.getString("Acct_hold_name");
							String accNo = rs.getString("Acct_No");
							String method = rs.getString("Trans_method");
							java.sql.Date date = rs.getDate("Trans_date");
							Time time = rs.getTime("Trans_time");
							String type = rs.getString("Trans_type");
							double amount = rs.getDouble("Amount");

							System.out.printf("\t%-6d %-20s %-15s %-12s %-12s %-10s %-12s %-10.2f\n",
									id, name, accNo, method, date, time, type, amount);
						}

						System.out.println("\t---------------------------------------------------------------------------------------------------------------");


					if (!found) {
						System.out.println("\n\tNo Transactions Found.");
					}
					
				break;
				}

		case 11: {

					try {

						date = LocalDate.now();
						time = LocalTime.now();

						System.out.print("\n\tEnter Receiver Account No: ");
						String receiverAcc = sc.next();

						while (!receiverAcc.matches("KM[0-9]{10}$")) {
							System.out.println("\tInvalid Account No. Try Again.");
							receiverAcc = sc.next();
						}

						System.out.print("\tEnter Receiver Phone No: ");
						String receiverPhone = sc.next();

						while (!receiverPhone.matches("^[6-9][0-9]{9}$")) {
							System.out.println("\tInvalid Contact Number. Try Again.");
							receiverPhone = sc.next();
						}

						con.setAutoCommit(false);

						
						String receiverQuery = "SELECT * FROM BankInfo WHERE Acct_No=? AND Cust_phone=?";
						ps = con.prepareStatement(receiverQuery);
						ps.setString(1, receiverAcc);
						ps.setString(2, receiverPhone);
						rs = ps.executeQuery();

						if (!rs.next()) {
							System.out.println("\n\tReceiver account not found.");
							con.rollback();
							break;
						}

						String receiverName = rs.getString("Cust_name");
						ps.close();

						System.out.print("\n\tEnter Your Account No: ");
						String senderAcc = sc.next();

						while (!senderAcc.matches("KM[0-9]{10}$")) {
							System.out.println("\tInvalid Account No. Try Again.");
							senderAcc = sc.next();
						}

						if (senderAcc.equals(receiverAcc)) {
							System.out.println("\n\tCannot transfer to same account.");
							con.rollback();
							break;
						}

						System.out.print("\tEnter Your PIN: ");
						int senderPin = sc.nextInt();

						String senderQuery = "SELECT * FROM BankInfo WHERE Acct_No=? AND Acct_PIN=?";
						ps = con.prepareStatement(senderQuery);
						ps.setString(1, senderAcc);
						ps.setInt(2, senderPin);
						rs = ps.executeQuery();

						if (!rs.next()) {
							System.out.println("\n\tIncorrect Account No or PIN.");
							con.rollback();
							break;
						}

						double senderBalance = rs.getDouble("Acct_balance");
						String senderName = rs.getString("Cust_name");
						ps.close();

						
						System.out.print("\tEnter Amount to Transfer: ");
						double amount = sc.nextDouble();

						if (senderBalance < amount) {
							System.out.println("\n\tInsufficient Balance.");
							con.rollback();
							break;
						}

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

						try {
							con.rollback();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}

						System.out.println("\nError: " + e.getMessage());
					}

					break;
				}
				    // 12. Exit
		case 12: {
					System.out.println("\n\tThanks for visiting...");
					System.exit(0);
					break;
				}

		default:
			System.out.println("\n\tInvalid Choice.");
		}
			System.out.println();
           System.out.print("\tPress (ENTER) to contiue..");
			sc.nextLine();
			sc.nextLine();
		
			}
		}
		catch(SQLException e){
			 System.out.println("\tDatabase Error:"+e.getMessage());
		}
		}
		
	}

