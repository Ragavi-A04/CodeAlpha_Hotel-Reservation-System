# 🏨 HOTEL RESERVATION SYSTEM – Royal Grand Hotel

A complete **Java Desktop-based Hotel Reservation Management System** built using **Java Swing, SQLite (JDBC), and iText PDF**.

This system simulates a real-world hotel software solution with Admin and Customer roles, booking management, cancellation, and professional PDF receipt generation.

---

## 📌 Project Overview

The **Hotel Reservation System** is a role-based desktop application that allows:

- Admins to manage hotel rooms
- Customers to book and cancel rooms
- Automatic availability tracking
- Downloadable booking receipts in PDF format

This project demonstrates practical implementation of:

- Database connectivity (JDBC + SQLite)
- Role-based authentication
- GUI design using Swing
- File handling and PDF generation

---

## ✨ Features

### 🔐 Authentication System
- User Signup
- Role Selection (Admin / Customer)
- Secure Login
- Role-based access control

---

### 🛎 Admin Functionalities
- Add Room
- Set Room Category (Standard / Deluxe / Suite)
- Set Room Price
- View All Rooms
- Availability Tracking

---

### 🧑‍💼 Customer Functionalities
- Select Room Category
- Dynamic Room Number Loading
- Real-time Availability Status (Available / Unavailable)
- Reserve Room
- Cancel Booking
- Download Booking Receipt (PDF)

---

### 📄 PDF Receipt Includes
- Hotel Name
- Customer Name
- Room Number
- Room Category
- Check-in Date
- Check-out Date
- Amount Paid
- Booking Date

Receipt is saved automatically in:
```
Downloads Folder
```

---

## 🛠 Technologies Used

| Technology | Purpose |
|------------|----------|
| Java Swing | GUI Development |
| SQLite | Database |
| JDBC | Database Connectivity |
| iText PDF | PDF Receipt Generation |
| IntelliJ IDEA | Development IDE |

---

## 🗄 Database Tables

The system automatically creates the following tables:

- `users`
- `rooms`
- `bookings`

Database file:
```
hotel.db
```

---

## ⚙ How to Run the Project

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/Hotel-Reservation-System.git
```

---

### 2️⃣ Open in IntelliJ IDEA

- Open IntelliJ
- Click **Open**
- Select the project folder

---

### 3️⃣ Add Required Libraries

Go to:

```
File → Project Structure → Libraries → Add JAR
```

Add:

- sqlite-jdbc-3.42.0.0.jar
- itextpdf-5.5.13.3.jar

---

### 4️⃣ Run the Application

Run:

```
HotelSystem.java
```

The database file `hotel.db` will be created automatically.

---

# 📸 Screenshots

Create a folder named:

```
screenshots
```

Place your images inside it and rename properly.

---

## 🔐 Login Page
```markdown
![Login](screenshots/login.png)
```

## 📝 Signup & Role Selection
```markdown
![Signup + Select Role](screenshots/signup & rolechoose.png)
```

## 🏠 Dashboard
```markdown
![Dashboard](screenshots/dashboard.png)
```

## ➕ Admin Adding Room
```markdown
![Admin Add Room](screenshots/admin can addroom.png)
![Customer cannot Add Room-->Adim Only](screenshots/customercant add room.png)
```

## 🛏 Book Room Page
```markdown
![Book Room](screenshots/bookroom.png)
```

## ✅ Reservation Success
```markdown
![Reservation Success](screenshots/reservesuccess.png)
```

## ❌ Cancel Booking
```markdown
![Cancel Booking](screenshots/roomcancel.png)
![Cancel Booking](screenshots/roomcancelled.png)
```

## 📄 Downloaded Receipt
```markdown
![Downloaded PDF](screenshots/downloadedpdf.png)
```

## 📋 View Rooms
```markdown
![View Rooms](screenshots/viewrooms.png)
```

---

# 🎯 Project Highlights

✔ Real-time Room Availability  
✔ Role-Based Access Control  
✔ Structured Database Design  
✔ Clean GUI Layout  
✔ Professional PDF Generation  
✔ Internship-Level Production Style  

---

# 🔮 Future Enhancements

- Payment Gateway Integration
- Calendar Date Picker
- Booking History Page
- Admin Analytics Dashboard
- Email Receipt System
- Room Images Upload
- Reports & Revenue Tracking

---

# 💼 Internship Project

This project was developed as part of:

**Java Programming Internship**

It demonstrates:

- Practical Software Development
- Database Integration
- Object-Oriented Programming
- GUI Design Principles
- Business Logic Implementation

---

# 👩‍💻 Author

**Ragavi Arumugam**  
Java Programming Intern  
Passionate about building real-world software solutions.

---

⭐ If you like this project, feel free to star the repository!
