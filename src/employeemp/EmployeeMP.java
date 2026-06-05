
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package employeemp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Taukir
 */
public class EmployeeMP extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}=======
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
    static List<String[]> employees = new ArrayList<>(); // name, id, dept, designation, salary
    static List<String[]> departments = new ArrayList<>(); // name, task, description

    // static leave & salary rows
    static List<String[]> leaves = new ArrayList<>(Arrays.asList(
            new String[] { "Taukir", "ICT", "Sick", "3", "Approved" },
            new String[] { "Mahi", "Training", "Annual", "5", "Rejected" },
            new String[] { "Shishir", "Administration", "Sick", "2", "Approved" },
            new String[] { "Ashiq", "Mechanical", "Casual", "1", "Pending" }));
    static List<String[]> salaries = new ArrayList<>(Arrays.asList(
            new String[] { "Taukir", "ICT", "08047", "23432" },
            new String[] { "Mahi", "Training", "08010", "2343" },
            new String[] { "Shishir", "Administration", "08025", "2452" },
            new String[] { "Ashiq", "Database", "08027", "2345" }));

    static {
        departments.add(new String[] { "ICT", "Software Development", "Handles all IT systems" });
        departments.add(new String[] { "Training", "Employee Training", "Manages training programs" });
        departments.add(new String[] { "Administration", "Office Management", "Administrative tasks" });
        departments.add(new String[] { "CEO", "Executive Decisions", "Top-level management" });

        employees.add(new String[] { "Taukir", "08047", "Accounting", "Senior Officer", "$7433" });
        employees.add(new String[] { "Mahi", "08010", "Training", "Senior Officer", "$2343" });
        employees.add(new String[] { "Shishir", "08025", "Admin", "Officer", "$2452" });
        employees.add(new String[] { "Ashiq", "08027", "CEO", "Manager", "$2345" });
    }

    // ── UI roots ──────────────────────────────────────────────────────────────
    BorderPane root;
    Sidebar sidebar;

    // ── Colors ────────────────────────────────────────────────────────────────
    static final String TEAL = "#009688";
    static final String LT_TEAL = "#80CBC4";
    static final String DARK_RED = "#8B2020";
    static final String BG = "#F0F0F0";
    static final String GOLD = "#C8A000";
    static final String WHITE = "#FFFFFF";

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
    // SIDEBAR
    // ══════════════════════════════════════════════════════════════════════════
    class Sidebar extends VBox {
        Map<String, Label> items = new LinkedHashMap<>();

        Sidebar() {
            setPrefWidth(220);
            setStyle("-fx-background-color:" + TEAL + "; -fx-padding:20 14;");
            setSpacing(4);

            String[][] nav = {
                    { "⊞", "Dashboard" },
                    { "👤", "Employees" },
                    { "⊟", "Departments" },
                    { "◎", "Leaves" },
                    { "⊛", "Salary" }
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
    // NAVIGATION
    // ══════════════════════════════════════════════════════════════════════════
    void navigate(String section) {
        sidebar.highlight(section);
        switch (section) {
            case "Dashboard" -> showDashboard();
            case "Employees" -> showEmployees();
            case "Departments" -> showDepartments();
            case "Leaves" -> showLeaves(null);
            case "Salary" -> showSalary();
        }
    }

    void setContent(javafx.scene.Node node) {
        ScrollPane sp = new ScrollPane(node);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:" + BG + "; -fx-background:" + BG + ";");
        root.setCenter(sp);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // DASHBOARD
    // ══════════════════════════════════════════════════════════════════════════
    void showDashboard() {
        VBox page = page();

        Label title = title("Dashboard Overview");
        title.setUnderline(true);

        // Stat cards
        long totalPay = 0;
        for (String[] e : employees)
            totalPay += parseSalary(e[4]);

        HBox cards = new HBox(16,
                statCard("👥", "Total\nEmployee", String.valueOf(employees.size()), "#F5C842"),
                statCard("🏢", "Total\nDepartment", String.valueOf(departments.size()), "#E57373"),
                statCard("💰", "Monthly\nPay", "$" + totalPay, "#A5D6A7"));

        Label leavesTitle = title("Leaves Details");
        leavesTitle.setUnderline(true);

        long approved = leaves.stream().filter(l -> l[4].equals("Approved")).count();
        long pending = leaves.stream().filter(l -> l[4].equals("Pending")).count();
        long rejected = leaves.stream().filter(l -> l[4].equals("Rejected")).count();

        VBox leaveRows = new VBox(12,
                leaveRow("📄", "Leave Applied", leaves.size(), "#26A69A"),
                leaveRow("🕐", "Leave Pending", (int) pending, "#5D3A1A"),
                leaveRow("✔", "Leave Approved", (int) approved, "#2E7D32"),
                leaveRow("✖", "Leave Rejected", (int) rejected, "#C62828"));

        page.getChildren().addAll(title, cards, leavesTitle, leaveRows);
        setContent(page);
    }

    HBox statCard(String icon, String label, String value, String color) {
        HBox card = new HBox(10);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setStyle("-fx-background-color:" + color + "; -fx-background-radius:8;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label ic = new Label(icon);
        ic.setFont(Font.font(26));
        Label tx = new Label(label + "\n" + value);
        tx.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        card.getChildren().addAll(ic, tx);
        return card;
    }

    HBox leaveRow(String icon, String label, int count, String color) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        Label ic = new Label(icon);
        ic.setMinSize(44, 44);
        ic.setMaxSize(44, 44);
        ic.setAlignment(Pos.CENTER);
        ic.setFont(Font.font(20));
        ic.setStyle("-fx-background-color:" + color + "; -fx-background-radius:4; -fx-text-fill:white;");

        VBox txt = new VBox(2);
        Label n = new Label(label);
        n.setFont(Font.font("Arial", 16));
        n.setTextFill(Color.web(color));
        Label c = new Label(String.valueOf(count));
        c.setFont(Font.font("Arial", 13));
        txt.getChildren().addAll(n, c);

        row.getChildren().addAll(ic, txt);
        return row;

    }

    // ══════════════════════════════════════════════════════════════════════════
    // DEPARTMENTS LIST
    // ══════════════════════════════════════════════════════════════════════════
    void showEmployees() {
        VBox page = page();

        Label title = title("Department Details");
        Button addBtn = redButton("Add Department");
        addBtn.setOnAction(e -> showDepartmentForm());
        HBox btnRow = new HBox(addBtn);
        btnRow.setAlignment(Pos.CENTER);

        VBox table = new VBox(2);
        table.getChildren().add(tableRow(true, "Name", "Employee ID", "Department", "Designation", "Salary"));
        for (String[] emp : employees) {
            table.getChildren().add(tableRow(false, emp[0], emp[1], emp[2], emp[3], emp[4]));
        }

        page.getChildren().addAll(title, btnRow, table);
        setContent(page);
    }

    HBox deptRow(boolean bold, String sl, String name, Button action) {
        HBox row = new HBox();
        row.setPadding(new Insets(5, 0, 5, 0));
        Label l1 = cell(sl, bold, 70);
        Label l2 = cell(name, bold, -1);
        HBox.setHgrow(l2, Priority.ALWAYS);
        row.getChildren().addAll(l1, l2);
        if (action != null)
            row.getChildren().add(action);
        return row;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // EMPLOYEES LIST
    // ══════════════════════════════════════════════════════════════════════════
    void showEmployees() {
        VBox page = page();

        Label title = title("Employees Details");
        Button addBtn = redButton("Add Employee");
        addBtn.setOnAction(e -> showEmployeeForm());

        HBox btnRow = new HBox(addBtn);
        btnRow.setAlignment(Pos.CENTER);

        VBox table = new VBox(2);
        table.getChildren().add(tableRow(true, "Name", "Employee ID", "Department", "Designation", "Salary"));
        for (String[] emp : employees) {
            table.getChildren().add(tableRow(false, emp[0], emp[1], emp[2], emp[3], emp[4]));
        }

        page.getChildren().addAll(title, btnRow, table);
        setContent(page);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // EMPLOYEE FORM
    // ══════════════════════════════════════════════════════════════════════════
    void showEmployeeForm() {
        sidebar.highlight("Employees");
        VBox page = page();

        Label title = title("Employees Form");

        TextField nameF = field("Name");
        TextField emailF = field("Email");
        TextField dobF = field("DD/MM/YYYY");
        TextField deptF = field("Department");
        TextField salF = field("Salary");
        TextField passF = field("Password");

        nameF.setMaxWidth(560);

        HBox row1 = new HBox(16, vbox("Email", emailF), vbox("Date of Birth", dobF));
        HBox row2 = new HBox(16, vbox("Department", deptF), vbox("Salary", salF));
        for (HBox r : new HBox[] { row1, row2 }) {
            for (var c : r.getChildren())
                HBox.setHgrow(c, Priority.ALWAYS);
        }

        passF.setMaxWidth(300);

        Button submit = redButton("Submit");
        submit.setOnAction(e -> {
            String name = nameF.getText().trim();
            String dept = deptF.getText().trim();
            String sal = salF.getText().trim();
            if (name.isEmpty() || dept.isEmpty()) {
                alert("Name and Department are required.");
                return;
            }
            String id = String.format("%05d", 8000 + employees.size() + 1);
            employees.add(new String[] { name, id, dept, "Officer", "$" + sal });
            alert("Employee added successfully!");
            showEmployees();
        });

        page.getChildren().addAll(
                title,
                vbox("Name", nameF),
                row1, row2,
                vbox("Password", passF),
                submit);
        setContent(page);
    }// Shishir

    // ══════════════════════════════════════════════════════════════════════════
    // DEPARTMENT FORM
    // ══════════════════════════════════════════════════════════════════════════
    void showEmployeeForm() {
        sidebar.highlight("Employees");
        VBox page = page();

        Label title = title("Employees Form");

        TextField nameF = field("Name");
        TextField emailF = field("Email");
        TextField dobF = field("DD/MM/YYYY");
        TextField deptF = field("Department");
        TextField salF = field("Salary");
        TextField passF = field("Password");

        nameF.setMaxWidth(560);

        HBox row1 = new HBox(16, vbox("Email", emailF), vbox("Date of Birth", dobF));
        HBox row2 = new HBox(16, vbox("Department", deptF), vbox("Salary", salF));
        for (HBox r : new HBox[] { row1, row2 }) {
            for (var c : r.getChildren())
                HBox.setHgrow(c, Priority.ALWAYS);
        }

        passF.setMaxWidth(300);

        Button submit = redButton("Submit");
        submit.setOnAction(e -> {
            String name = nameF.getText().trim();
            if (name.isEmpty()) {
                alert("Department name is required.");
                return;
            }
            departments.add(new String[] { name, taskF.getText().trim(), descF.getText().trim() });
            alert("Department added!");
            showDepartments();
        });

        page.getChildren().addAll(
                title,
                vbox("Department Name", nameF),
                vbox("Task of the Department", taskF),
                vbox("Description", descF),
                addBtn);
        setContent(page);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // LEAVES (static data, filter buttons)- Shishir2
    // ══════════════════════════════════════════════════════════════════════════
    void showLeaves(String filter) {
        VBox page = page();

        Label title = title("Leaves Details");

        Button appr = redButton("Approved");
        Button pend = redButton("Pending");
        Button rej = redButton("Rejected");
        appr.setOnAction(e -> showLeaves("Approved"));
        pend.setOnAction(e -> showLeaves("Pending"));
        rej.setOnAction(e -> showLeaves("Rejected"));

        HBox filterRow = new HBox(8, appr, pend, rej);

        VBox table = new VBox(4);
        table.getChildren().add(tableRow(true, "SL No", "Name", "Dept", "Leave Type", "Days", "Status"));

        int i = 1;
        for (String[] l : leaves) {
            if (filter == null || l[4].equals(filter)) {
                table.getChildren().add(tableRow(false,
                        String.valueOf(i++), l[0], l[1], l[2], l[3], l[4]));
            }
        }

        page.getChildren().addAll(title, filterRow, table);
        setContent(page);
    }
    // Mahi you will make salary Section

    // ══════════════════════════════════════════════════════════════════════════
    // SALARY (static data, edit button)-Mahi02
    // ══════════════════════════════════════════════════════════════════════════
    void showSalary() {
        VBox page = page();

        Label title = title("Salary Details");

        VBox table = new VBox(4);
        table.getChildren().add(tableRow(true, "SL No", "Name", "Dept", "Employee ID", "$Salary", "Action"));

        int i = 1;
        for (String[] s : salaries) {
            final String[] row = s;
            Button editBtn = smallRedButton("EDIT");
            editBtn.setOnAction(e -> {
                TextInputDialog dlg = new TextInputDialog(row[3]);
                dlg.setTitle("Edit Salary");
                dlg.setHeaderText("Update salary for " + row[0]);
                dlg.setContentText("New Salary:");
                dlg.showAndWait().ifPresent(val -> {
                    try {
                        Double.parseDouble(val);
                        row[3] = val;
                        showSalary();
                    } catch (NumberFormatException ignored) {
                    }
                });
            });

            HBox tableRow = new HBox();
            tableRow.setPadding(new Insets(5, 0, 5, 0));
            String[] vals = { String.valueOf(i++), row[0], row[1], row[2], row[3] };
            for (String v : vals) {
                Label lbl = cell(v, false, 120);
                HBox.setHgrow(lbl, Priority.ALWAYS);
                tableRow.getChildren().add(lbl);
            }
            tableRow.getChildren().add(editBtn);
            table.getChildren().add(tableRow);
        }

        page.getChildren().addAll(title, table);
        setContent(page);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HELPERS-Taukir-2
    // ══════════════════════════════════════════════════════════════════════════
    VBox page() {
        VBox v = new VBox(20);
        v.setPadding(new Insets(30, 50, 30, 50));
        v.setStyle("-fx-background-color:" + BG + ";");
        return v;
    }

    Label title(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        return l;
    }

    TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color:white; -fx-border-color:#333; " +
                "-fx-border-radius:20; -fx-background-radius:20; -fx-padding:6 12;");
        return tf;
    }

    Button redButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + DARK_RED + "; -fx-text-fill:white; " +
                "-fx-background-radius:6; -fx-font-size:13; -fx-padding:6 16;");
        return b;
    }

    Button smallRedButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + DARK_RED + "; -fx-text-fill:white; " +
                "-fx-background-radius:6; -fx-font-size:11; -fx-padding:3 10;");
        return b;
    }

    VBox vbox(String labelText, javafx.scene.Node field) {
        Label l = new Label(labelText);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        VBox v = new VBox(4, l, field);
        HBox.setHgrow(v, Priority.ALWAYS);
        return v;
    }

    HBox tableRow(boolean bold, String... cols) {
        HBox row = new HBox();
        row.setPadding(new Insets(5, 0, 5, 0));
        for (String v : cols) {
            Label lbl = cell(v, bold, 120);
            HBox.setHgrow(lbl, Priority.ALWAYS);
            row.getChildren().add(lbl);
        }
        return row;
    }

    Label cell(String text, boolean bold, double width) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, 13));
        if (width > 0)
            l.setPrefWidth(width);
        l.setMaxWidth(Double.MAX_VALUE);
        return l;
    }

    long parseSalary(String s) {
        try {
            return (long) Double.parseDouble(s.replace("$", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
