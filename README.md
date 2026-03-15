# Bank Management System

A **Java-based Bank Management System** application built using **Java**, **JDBC**, and **MySQL**.  
This system simulates real-world banking operations, allowing users to manage accounts, deposit/withdraw money, transfer funds, and track transactions securely.

---

# Features #

# Account Creation with validation 

- Name (alphabet only)
- Contact number (10-digit Indian mobile number)
- Address of Customers
- Account type (Saving / Current)
- Minimum balance check (500 Rs)
  
  # Login & Session Management 
- Users stay logged in for seamless operations
- Account number stored in session for easier access
- PIN Management:
- Set, reset, and validate 4-digit PIN
- Secure PIN entry for all operations

# Deposit & Withdrawal 
- Real-time balance updates
- Transaction history maintained
- Minimum and maximum balance validations

# Money Transfer 
- Transfer funds securely between accounts
- Verifies account number and contact number of receiver
- Transaction recorded for both sender and receiver

# Profile Management 
- Update name, phone number, address, and account type

# Transaction History 
- Displays all transactions with details: date, time, type, and amount

# Database Integration 
- Uses MySQL to store account and transaction data
- JDBC prepared statements ensure security against SQL injection
  
---

## 🛠 Technologies Used

- **Java** – Core programming language.
- **JDBC** – Database connectivity.
- **MySQL** – Relational database for accounts and transactions.
- **Git & GitHub** – Version control and project hosting.
- **IDE** – Eclipse, IntelliJ IDEA, or NetBeans (optional).

---

# Validations Implemented 
- Name: Alphabets only, supports multiple words
- Contact: 10-digit Indian mobile number starting with 6–9
- Account Type: Must be Saving or Current
- PIN: Must be 4 digits
- Amounts: Cannot be negative or zero; withdrawal limited by balance
  
---

# Skills Demonstrated 

Technical Skills:

- Java OOP (classes, inheritance, methods)
- JDBC & MySQL CRUD operations
- SQL queries and transactions
- Input validation with Regex (phone, email, account number)
- Exception handling
- Console-based UI design
- Git & GitHub version control

---
# Soft Skills 

- Problem-solving
- Attention to detail
- Project organization & management
- Database design thinking
- Professional documentation

---

# Future Improvements 

- OTP Integration: Add SMS/email OTP for critical operations like money transfer.
- GUI Interface: Convert console-based app to a graphical interface.
- Encryption: Encrypt PIN and sensitive user data in the database.
- Multi-user Support: Handle multiple sessions simultaneously.

  ---
