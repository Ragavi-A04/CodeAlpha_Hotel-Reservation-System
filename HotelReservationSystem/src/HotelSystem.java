import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.io.*;
import java.time.LocalDate;
import java.awt.Desktop;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;


public class HotelSystem extends JFrame {

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:hotel.db");
    }

    CardLayout layout = new CardLayout();
    JPanel container = new JPanel(layout);

    String currentUser = "";
    String currentRole = "";

    public HotelSystem() {

        setTitle("Hotel Reservation System");
        setSize(1150,720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createTables();

        container.add(loginPage(),"LOGIN");
        container.add(signupPage(),"SIGNUP");
        container.add(dashboardPage(),"DASH");
        container.add(bookRoomPage(),"BOOK");

        add(container);
        layout.show(container,"LOGIN");

        setVisible(true);
    }

    // ================= DATABASE =================
    private void createTables(){
        try(Connection con=getConnection();
            Statement stmt=con.createStatement()){

            stmt.execute("CREATE TABLE IF NOT EXISTS users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "password TEXT," +
                    "role TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS rooms(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "room_number INTEGER UNIQUE," +
                    "category TEXT," +
                    "price REAL," +
                    "available INTEGER)");

            stmt.execute("CREATE TABLE IF NOT EXISTS bookings(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "room_number INTEGER," +
                    "checkin TEXT," +
                    "checkout TEXT)");

        }catch(Exception e){ e.printStackTrace(); }
    }

    // ================= HEADER =================
    private JPanel header(String text){
        JPanel panel=new JPanel(new BorderLayout());
        panel.setBackground(new Color(69, 4, 80));

        JLabel title=new JLabel(
                "HOTEL RESERVATION SYSTEM – Royal Grand Hotel",
                SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,24));

        JLabel sub=new JLabel(text,SwingConstants.CENTER);
        sub.setForeground(Color.WHITE);

        panel.add(title,BorderLayout.CENTER);
        panel.add(sub,BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        return panel;
    }

    private JPanel wrap(JPanel center,String text){
        JPanel panel=new JPanel(new BorderLayout());
        panel.add(header(text),BorderLayout.NORTH);
        panel.add(center,BorderLayout.CENTER);
        return panel;
    }

    private void styleButton(JButton b,Color c){
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,16));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(250,60));
    }

    // ================= LOGIN =================
    private JPanel loginPage(){
        JPanel form=new JPanel(new GridLayout(4,2,15,15));
        form.setBorder(BorderFactory.createEmptyBorder(150,350,150,350));

        JTextField user=new JTextField();
        JPasswordField pass=new JPasswordField();

        JButton login=new JButton("Login");
        JButton signup=new JButton("Signup");

        styleButton(login,new Color(14, 122, 6));
        styleButton(signup,new Color(7, 21, 129));

        form.add(new JLabel("Username:")); form.add(user);
        form.add(new JLabel("Password:")); form.add(pass);
        form.add(login); form.add(signup);

        login.addActionListener(e->{
            try(Connection con=getConnection();
                PreparedStatement ps=con.prepareStatement(
                        "SELECT * FROM users WHERE username=? AND password=?")){

                ps.setString(1,user.getText());
                ps.setString(2,String.valueOf(pass.getPassword()));
                ResultSet rs=ps.executeQuery();

                if(rs.next()){
                    currentUser=user.getText();
                    currentRole=rs.getString("role");
                    layout.show(container,"DASH");
                }else{
                    JOptionPane.showMessageDialog(this,"Invalid Login");
                }

            }catch(Exception ex){ ex.printStackTrace(); }
        });

        signup.addActionListener(e->layout.show(container,"SIGNUP"));

        return wrap(form,"Welcome to Luxury Comfort");
    }

    // ================= SIGNUP =================
    private JPanel signupPage(){
        JPanel form=new JPanel(new GridLayout(5,2,15,15));
        form.setBorder(BorderFactory.createEmptyBorder(150,350,150,350));

        JTextField user=new JTextField();
        JPasswordField pass=new JPasswordField();
        JComboBox<String> role=
                new JComboBox<>(new String[]{"Admin","Customer"});

        JButton create=new JButton("Create Account");
        JButton back=new JButton("Back");

        styleButton(create,new Color(3, 23, 177));
        styleButton(back,Color.GRAY);

        form.add(new JLabel("Username:")); form.add(user);
        form.add(new JLabel("Password:")); form.add(pass);
        form.add(new JLabel("Role:")); form.add(role);
        form.add(create); form.add(back);

        create.addActionListener(e->{
            try(Connection con=getConnection();
                PreparedStatement ps=con.prepareStatement(
                        "INSERT INTO users(username,password,role) VALUES(?,?,?)")){

                ps.setString(1,user.getText());
                ps.setString(2,String.valueOf(pass.getPassword()));
                ps.setString(3,role.getSelectedItem().toString());
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Account Created");
                layout.show(container,"LOGIN");

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"User Exists");
            }
        });

        back.addActionListener(e->layout.show(container,"LOGIN"));

        return wrap(form,"Create New Account");
    }

    // ================= DASHBOARD =================
    private JPanel dashboardPage() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 80, 0));
        centerPanel.setOpaque(false);

        JButton addRoom = new JButton("ADD ROOM");
        JButton viewRoom = new JButton("VIEW ROOMS");
        JButton bookRoom = new JButton("BOOK ROOM");
        JButton cancel = new JButton("CANCEL BOOKING");
        JButton logout = new JButton("LOGOUT");

        Color mainColor = new Color(31, 112, 4);

        styleButton(addRoom, mainColor);
        styleButton(viewRoom, mainColor);
        styleButton(bookRoom, mainColor);
        styleButton(cancel, mainColor);
        styleButton(logout, new Color(200, 0, 0));

        Dimension btnSize = new Dimension(280, 60);

        addRoom.setMaximumSize(btnSize);
        viewRoom.setMaximumSize(btnSize);
        bookRoom.setMaximumSize(btnSize);
        cancel.setMaximumSize(btnSize);
        logout.setMaximumSize(new Dimension(200, 55));

        addRoom.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewRoom.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookRoom.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(addRoom);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(viewRoom);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(bookRoom);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(cancel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(logout);

        addRoom.addActionListener(e -> {
            if (currentRole.equals("Admin"))
                addRoomDialog();
            else
                JOptionPane.showMessageDialog(this, "Admin Only");
        });

        viewRoom.addActionListener(e -> viewRoomsDialog());
        bookRoom.addActionListener(e -> layout.show(container, "BOOK"));
        cancel.addActionListener(e -> cancelBookingDialog());
        logout.addActionListener(e -> layout.show(container, "LOGIN"));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        return wrap(mainPanel, "Welcome " + currentUser + " — Enjoy Your Stay");
    }

    // ================= ADD ROOM =================
    private void addRoomDialog(){
        JTextField number=new JTextField();
        JTextField price=new JTextField();
        JComboBox<String> category=
                new JComboBox<>(new String[]{"Standard","Deluxe","Suite"});

        JPanel panel=new JPanel(new GridLayout(3,2));
        panel.add(new JLabel("Room Number:")); panel.add(number);
        panel.add(new JLabel("Category:")); panel.add(category);
        panel.add(new JLabel("Price:")); panel.add(price);

        if(JOptionPane.showConfirmDialog(this,panel,
                "Add Room",JOptionPane.OK_CANCEL_OPTION)
                ==JOptionPane.OK_OPTION){

            try(Connection con=getConnection();
                PreparedStatement ps=con.prepareStatement(
                        "INSERT INTO rooms(room_number,category,price,available) VALUES(?,?,?,1)")){

                ps.setInt(1,Integer.parseInt(number.getText()));
                ps.setString(2,category.getSelectedItem().toString());
                ps.setDouble(3,Double.parseDouble(price.getText()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Room Added");

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Room Exists");
            }
        }
    }

    // ================= VIEW ROOMS =================
    private void viewRoomsDialog(){

        DefaultTableModel model=new DefaultTableModel(
                new String[]{"Room","Category","Price","Available"},0);

        JTable table=new JTable(model);

        try(Connection con=getConnection();
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT * FROM rooms")){

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("room_number"),
                        rs.getString("category"),
                        "₹ "+rs.getDouble("price"),
                        rs.getInt("available")==1?"Available":"Unavailable"
                });
            }

        }catch(Exception e){}

        JOptionPane.showMessageDialog(this,new JScrollPane(table),
                "Rooms",JOptionPane.PLAIN_MESSAGE);
    }

    // ================= CANCEL =================
    private void cancelBookingDialog(){

        String roomStr=JOptionPane.showInputDialog(this,"Enter Room Number:");
        if(roomStr==null) return;

        try(Connection con=getConnection()){

            int room=Integer.parseInt(roomStr);

            PreparedStatement del=con.prepareStatement(
                    "DELETE FROM bookings WHERE room_number=?");
            del.setInt(1,room);
            del.executeUpdate();

            PreparedStatement up=con.prepareStatement(
                    "UPDATE rooms SET available=1 WHERE room_number=?");
            up.setInt(1,room);
            up.executeUpdate();

            JOptionPane.showMessageDialog(this,"Booking Cancelled");

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Cancellation Failed");
        }
    }

    // ================= BOOK PAGE =================
    private JPanel bookRoomPage() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> categoryBox =
                new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite"});

        JComboBox<Integer> roomBox = new JComboBox<>();
        JLabel statusLabel = new JLabel("Select Room");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField checkin = new JTextField();
        JTextField checkout = new JTextField();

        JButton reserve = new JButton("RESERVE ROOM");
        JButton download = new JButton("DOWNLOAD RECEIPT");
        JButton back = new JButton("BACK");

        styleButton(reserve, new Color(40, 195, 11));
        styleButton(download, new Color(7, 21, 129));
        styleButton(back, Color.GRAY);

        reserve.setPreferredSize(new Dimension(220, 50));
        download.setPreferredSize(new Dimension(220, 50));
        back.setPreferredSize(new Dimension(150, 45));

        // ===== ROW 1
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        panel.add(categoryBox, gbc);

        // ===== ROW 2
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Room Number:"), gbc);

        gbc.gridx = 1;
        panel.add(roomBox, gbc);

        // ===== ROW 3
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Availability:"), gbc);

        gbc.gridx = 1;
        panel.add(statusLabel, gbc);

        // ===== ROW 4
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Check-in (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        panel.add(checkin, gbc);

        // ===== ROW 5
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Check-out (YYYY-MM-DD):"), gbc);

        gbc.gridx = 1;
        panel.add(checkout, gbc);

        // ===== ROW 6 BUTTONS
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(reserve, gbc);

        gbc.gridx = 1;
        panel.add(download, gbc);

        // ===== ROW 7 BACK
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(back, gbc);

        // ================= LOAD ROOMS =================
        categoryBox.addActionListener(e -> {
            roomBox.removeAllItems();
            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "SELECT room_number FROM rooms WHERE category=?")) {

                ps.setString(1, categoryBox.getSelectedItem().toString());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    roomBox.addItem(rs.getInt("room_number"));
                }

            } catch (Exception ignored) {}
        });

        // ================= SHOW STATUS =================
        roomBox.addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "SELECT available FROM rooms WHERE room_number=?")) {

                if (roomBox.getSelectedItem() == null) return;

                ps.setInt(1, (Integer) roomBox.getSelectedItem());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    if (rs.getInt("available") == 1) {
                        statusLabel.setText("Available");
                        statusLabel.setForeground(new Color(47, 170, 7));
                    } else {
                        statusLabel.setText("Unavailable");
                        statusLabel.setForeground(Color.RED);
                    }
                }

            } catch (Exception ignored) {}
        });

        // ================= RESERVE ONLY =================
        reserve.addActionListener(e -> {
            try (Connection con = getConnection()) {

                if (roomBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Select Room");
                    return;
                }

                int room = (Integer) roomBox.getSelectedItem();

                PreparedStatement check =
                        con.prepareStatement("SELECT price, available FROM rooms WHERE room_number=?");
                check.setInt(1, room);
                ResultSet rs = check.executeQuery();

                if (!rs.next() || rs.getInt("available") == 0) {
                    JOptionPane.showMessageDialog(this, "Room Not Available");
                    return;
                }

                PreparedStatement book =
                        con.prepareStatement(
                                "INSERT INTO bookings(username,room_number,checkin,checkout) VALUES(?,?,?,?)");

                book.setString(1, currentUser);
                book.setInt(2, room);
                book.setString(3, checkin.getText());
                book.setString(4, checkout.getText());
                book.executeUpdate();

                PreparedStatement update =
                        con.prepareStatement("UPDATE rooms SET available=0 WHERE room_number=?");
                update.setInt(1, room);
                update.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Room Reserved Successfully!\nNow click DOWNLOAD RECEIPT to get PDF.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Booking Failed");
            }
        });

        // ================= DOWNLOAD ONLY =================
        download.addActionListener(e -> {
            try (Connection con = getConnection()) {

                if (roomBox.getSelectedItem() == null) return;

                int room = (Integer) roomBox.getSelectedItem();

                PreparedStatement ps =
                        con.prepareStatement("SELECT price FROM rooms WHERE room_number=?");
                ps.setInt(1, room);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    double price = rs.getDouble("price");
                    generateReceipt(room, price,
                            checkin.getText(), checkout.getText());
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Download Failed");
            }
        });

        back.addActionListener(e -> layout.show(container, "DASH"));

        return wrap(panel, "Book Your Luxury Stay");
    }


    // ================= RECEIPT =================
    private void generateReceipt(int room,double amount,String in,String out){

        try{
            String path=System.getProperty("user.home")+
                    "/Downloads/Receipt_"+room+".pdf";

            Document doc=new Document();
            PdfWriter.getInstance(doc,new FileOutputStream(path));
            doc.open();

            Paragraph title=new Paragraph(
                    "ROYAL GRAND HOTEL\nBOOKING RECEIPT\n\n");
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            PdfPTable table=new PdfPTable(2);

            table.addCell("Customer"); table.addCell(currentUser);
            table.addCell("Room Number"); table.addCell(String.valueOf(room));
            table.addCell("Check-in"); table.addCell(in);
            table.addCell("Check-out"); table.addCell(out);
            table.addCell("Amount Paid"); table.addCell("₹ "+amount);
            table.addCell("Date"); table.addCell(LocalDate.now().toString());

            doc.add(table);
            doc.close();

            Desktop.getDesktop().open(new File(path));

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"PDF Error");
        }
    }

    public static void main(String[] args){
        new HotelSystem();
    }
}
