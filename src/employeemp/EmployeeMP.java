import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.collections.*;

import java.util.*;

public class EmployeeMP extends Application {

    // ── Data ──────────────────────────────────────────────────────────────────
    static List<String[]> employees   = new ArrayList<>();  // name, id, dept, designation, salary
    static List<String[]> departments = new ArrayList<>();  // name, task, description

    // static leave & salary rows
    static List<String[]> leaves = new ArrayList<>(Arrays.asList(
        new String[]{"Taukir",  "ICT",           "Sick",   "3", "Approved"},
        new String[]{"Mahi",    "Training",       "Annual", "5", "Rejected"},
        new String[]{"Shishir", "Administration", "Sick",   "2", "Approved"},
        new String[]{"Ashiq",   "Mechanical",     "Casual", "1", "Pending"}
    ));
    static List<String[]> salaries = new ArrayList<>(Arrays.asList(
        new String[]{"Taukir",  "ICT",           "08047", "23432"},
        new String[]{"Mahi",    "Training",       "08010", "2343"},
        new String[]{"Shishir", "Administration", "08025", "2452"},
        new String[]{"Ashiq",   "Database",       "08027", "2345"}
    ));

    static {
        departments.add(new String[]{"ICT",            "Software Development", "Handles all IT systems"});
        departments.add(new String[]{"Training",       "Employee Training",    "Manages training programs"});
        departments.add(new String[]{"Administration", "Office Management",    "Administrative tasks"});
        departments.add(new String[]{"CEO",            "Executive Decisions",  "Top-level management"});

        employees.add(new String[]{"Taukir",  "08047", "Accounting", "Senior Officer", "$7433"});
        employees.add(new String[]{"Mahi",    "08010", "Training",   "Senior Officer", "$2343"});
        employees.add(new String[]{"Shishir", "08025", "Admin",      "Officer",        "$2452"});
        employees.add(new String[]{"Ashiq",   "08027", "CEO",        "Manager",        "$2345"});
    }

    // ── UI roots ──────────────────────────────────────────────────────────────
    BorderPane root;
    Sidebar sidebar;

    // ── Colors ────────────────────────────────────────────────────────────────
    static final String TEAL      = "#009688";
    static final String LT_TEAL   = "#80CBC4";
    static final String DARK_RED  = "#8B2020";
    static final String BG        = "#F0F0F0";
    static final String GOLD      = "#C8A000";
    static final String WHITE     = "#FFFFFF";

    // ──────────────────────────────────────────────────────────────────────────
    @Override
    public void start(Stage stage) {
        root = new BorderPane();

        // Top bar
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color:" + LT_TEAL + "; -fx-padding:14 20;");
        topBar.setPrefHeight(58);
        Label logo = new Label("Logo");
        logo.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        topBar.getChildren().add(logo);
        root.setTop(topBar);

        // Sidebar
        sidebar = new Sidebar();
        root.setLeft(sidebar);

        showDashboard();

        Scene scene = new Scene(root, 1050, 680);
        stage.setTitle("Employee Management System");
        stage.setScene(scene);
        stage.show();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SIDEBAR
    // ══════════════════════════════════════════════════════════════════════════
    class Sidebar extends VBox {
        Map<String, Label> items = new LinkedHashMap<>();

        Sidebar() {
            setPrefWidth(220);
            setStyle("-fx-background-color:" + TEAL + "; -fx-padding:20 14;");
            setSpacing(4);

            String[][] nav = {
                {"⊞", "Dashboard"},
                {"👤","Employees"},
                {"⊟","Departments"},
                {"◎","Leaves"},
                {"⊛","Salary"}
            };

            for (String[] n : nav) {
                Label lbl = new Label(n[0] + "  " + n[1]);
                lbl.setFont(Font.font("Arial", FontWeight.NORMAL, 17));
                lbl.setTextFill(Color.WHITE);
                lbl.setPadding(new Insets(10, 8, 10, 8));
                lbl.setMaxWidth(Double.MAX_VALUE);
                String section = n[1];
                lbl.setOnMouseClicked(e -> navigate(section));
                items.put(n[1], lbl);
                getChildren().add(lbl);
            }

            highlight("Dashboard");
        }

        void highlight(String active) {
            for (var entry : items.entrySet()) {
                if (entry.getKey().equals(active)) {
                    entry.getValue().setTextFill(Color.web(GOLD));
                    entry.getValue().setFont(Font.font("Arial", FontWeight.BOLD, 17));
                } else {
                    entry.getValue().setTextFill(Color.WHITE);
                    entry.getValue().setFont(Font.font("Arial", FontWeight.NORMAL, 17));
                }
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  NAVIGATION
    // ══════════════════════════════════════════════════════════════════════════
    void navigate(String section) {
        sidebar.highlight(section);
        switch (section) {
            case "Dashboard"  -> showDashboard();
            case "Employees"  -> showEmployees();
            case "Departments"-> showDepartments();
            case "Leaves"     -> showLeaves(null);
            case "Salary"     -> showSalary();
        }
    }

    void setContent(javafx.scene.Node node) {
        ScrollPane sp = new ScrollPane(node);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:" + BG + "; -fx-background:" + BG + ";");
        root.setCenter(sp);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DASHBOARD
    // ══════════════════════════════════════════════════════════════════════════
    void showDashboard() {
        VBox page = page();

        Label title = title("Dashboard Overview");
        title.setUnderline(true);

        // Stat cards
        long totalPay = 0;
        for (String[] e : employees) totalPay += parseSalary(e[4]);

        HBox cards = new HBox(16,
            statCard("👥", "Total\nEmployee",   String.valueOf(employees.size()),   "#F5C842"),
            statCard("🏢", "Total\nDepartment", String.valueOf(departments.size()), "#E57373"),
            statCard("💰", "Monthly\nPay",      "$" + totalPay,                     "#A5D6A7")
        );

        Label leavesTitle = title("Leaves Details");
        leavesTitle.setUnderline(true);

        long approved = leaves.stream().filter(l -> l[4].equals("Approved")).count();
        long pending  = leaves.stream().filter(l -> l[4].equals("Pending")).count();
        long rejected = leaves.stream().filter(l -> l[4].equals("Rejected")).count();

        VBox leaveRows = new VBox(12,
            leaveRow("📄", "Leave Applied", leaves.size(),  "#26A69A"),
            leaveRow("🕐", "Leave Pending", (int) pending,  "#5D3A1A"),
            leaveRow("✔",  "Leave Approved",(int) approved, "#2E7D32"),
            leaveRow("✖",  "Leave Rejected",(int) rejected, "#C62828")
        );

        page.getChildren().addAll(title, cards, leavesTitle, leaveRows);
        setContent(page);
    }

    HBox statCard(String icon, String label, String value, String color) {
        HBox card = new HBox(10);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setStyle("-fx-background-color:" + color + "; -fx-background-radius:8;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label ic = new Label(icon); ic.setFont(Font.font(26));
        Label tx = new Label(label + "\n" + value);
        tx.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        card.getChildren().addAll(ic, tx);
        return card;
    }

    HBox leaveRow(String icon, String label, int count, String color) {
        HBox row = new HBox(12); row.setAlignment(Pos.CENTER_LEFT);

        Label ic = new Label(icon);
        ic.setMinSize(44, 44); ic.setMaxSize(44, 44);
        ic.setAlignment(Pos.CENTER); ic.setFont(Font.font(20));
        ic.setStyle("-fx-background-color:" + color + "; -fx-background-radius:4; -fx-text-fill:white;");

        VBox txt = new VBox(2);
        Label n = new Label(label); n.setFont(Font.font("Arial", 16)); n.setTextFill(Color.web(color));
        Label c = new Label(String.valueOf(count)); c.setFont(Font.font("Arial", 13));
        txt.getChildren().addAll(n, c);

        row.getChildren().addAll(ic, txt);
        return row;
        
        
    }
     // ══════════════════════════════════════════════════════════════════════════
    //  EMPLOYEES LIST
    // ══════════════════════════════════════════════════════════════════════════
    void showEmployees() {
        VBox page = page();

        Label title = title("Employees Details");
        Button addBtn = redButton("Add Employee");
        addBtn.setOnAction(e -> showEmployeeForm());

        HBox btnRow = new HBox(addBtn); btnRow.setAlignment(Pos.CENTER);

        VBox table = new VBox(2);
        table.getChildren().add(tableRow(true, "Name", "Employee ID", "Department", "Designation", "Salary"));
        for (String[] emp : employees) {
            table.getChildren().add(tableRow(false, emp[0], emp[1], emp[2], emp[3], emp[4]));
        }

        page.getChildren().addAll(title, btnRow, table);
        setContent(page);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  EMPLOYEE FORM
    // ══════════════════════════════════════════════════════════════════════════
    void showEmployeeForm() {
        sidebar.highlight("Employees");
        VBox page = page();

        Label title = title("Employees Form");

        TextField nameF  = field("Name");
        TextField emailF = field("Email");
        TextField dobF   = field("DD/MM/YYYY");
        TextField deptF  = field("Department");
        TextField salF   = field("Salary");
        TextField passF  = field("Password");

        nameF.setMaxWidth(560);

        HBox row1 = new HBox(16, vbox("Email", emailF), vbox("Date of Birth", dobF));
        HBox row2 = new HBox(16, vbox("Department", deptF), vbox("Salary", salF));
        for (HBox r : new HBox[]{row1, row2}) {
            for (var c : r.getChildren()) HBox.setHgrow(c, Priority.ALWAYS);
        }

        passF.setMaxWidth(300);

        Button submit = redButton("Submit");
        submit.setOnAction(e -> {
            String name = nameF.getText().trim();
            String dept = deptF.getText().trim();
            String sal  = salF.getText().trim();
            if (name.isEmpty() || dept.isEmpty()) {
                alert("Name and Department are required.");
                return;
            }
            String id = String.format("%05d", 8000 + employees.size() + 1);
            employees.add(new String[]{name, id, dept, "Officer", "$" + sal});
            alert("Employee added successfully!");
            showEmployees();
        });

        page.getChildren().addAll(
            title,
            vbox("Name", nameF),
            row1, row2,
            vbox("Password", passF),
            submit
        );
        setContent(page);
    }


   

    public static void main(String[] args) { 
        launch(args); 
    }
}