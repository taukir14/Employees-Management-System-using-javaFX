import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;

public class HRDashboard extends Application {

    private static final String TEAL_LIGHT  = "#4DC9D6";
    private static final String TEAL_HEADER = "#5DD5DC";
    private static final String BG_MAIN     = "#E8F4F5";
    private static final String YELLOW_CARD = "#F5C842";
    private static final String PINK_CARD   = "#F47C7C";
    private static final String GREEN_CARD  = "#A8E063";
    private static final String WHITE       = "#FFFFFF";
    private static final String TEXT_DARK   = "#1A1A2E";
    private static final String SIDEBAR_BG  = "#008C9E";

    @Override
    public void start(Stage primaryStage) {
        // ── Root Layout ──────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_MAIN + ";");

        // ── Top Header Bar ───────────────────────────────────────────
        HBox header = buildHeader();
        root.setTop(header);

        // ── Sidebar ──────────────────────────────────────────────────
        VBox sidebar = buildSidebar();
        root.setLeft(sidebar);

        // ── Main Content ─────────────────────────────────────────────
        ScrollPane scrollPane = new ScrollPane(buildMainContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setCenter(scrollPane);

        // ── Scene ────────────────────────────────────────────────────
        Scene scene = new Scene(root, 1100, 720);
        primaryStage.setTitle("HR Management Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    // ─────────────────────────────────────────────────────────────────
    //  HEADER
    // ─────────────────────────────────────────────────────────────────
    private HBox buildHeader() { 
        HBox header = new HBox();
        header.setPrefHeight(65);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 24, 0, 24));
        header.setStyle("-fx-background-color: " + TEAL_HEADER + ";");

        // Drop shadow under header
        DropShadow headerShadow = new DropShadow();
        headerShadow.setColor(Color.web("#00000033"));
        headerShadow.setOffsetY(3);
        headerShadow.setRadius(8);
        header.setEffect(headerShadow);

        // Logo text
        Label logo = new Label("Logo");
        logo.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
        logo.setTextFill(Color.web(TEXT_DARK));

        // Spacer
        //Region spacer = new Region();
        //HBox.setHgrow(spacer, Priority.ALWAYS);
/*
        // User avatar circle
        StackPane avatar = new StackPane();
        Circle circle = new Circle(22);
        circle.setFill(Color.web(TEXT_DARK));
        circle.setStroke(Color.web(WHITE));
        circle.setStrokeWidth(2);
        Label avatarIcon = new Label("👤");
        avatarIcon.setFont(Font.font(20));
        avatar.getChildren().addAll(circle, avatarIcon);
        avatar.setCursor(Cursor.HAND);
*/

        header.getChildren().addAll(logo /*, spacer /*,avatar*/);
        return header;
    }

    // ─────────────────────────────────────────────────────────────────
    //  SIDEBAR
    // ─────────────────────────────────────────────────────────────────
    private VBox buildSidebar() {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(230);
        sidebar.setPadding(new Insets(30, 0, 30, 0));
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");

        String[][] navItems = {
            {"⊞",  "Dashboard",   "active"},
            {"👤", "Employees",   ""},
            {"🏢", "Departments", ""},
            {"🌿", "Leaves",      ""},
            {"💰", "Salary",      ""}
        };

        for (String[] item : navItems) {
            HBox navRow = buildNavItem(item[0], item[1], item[2].equals("active"));
            sidebar.getChildren().add(navRow);
        }

        return sidebar;
    }

    private HBox buildNavItem(String icon, String label, boolean active) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 20, 12, 24));
        row.setCursor(Cursor.HAND);

        if (active) {
            row.setStyle("-fx-background-color: rgba(255,255,255,0.15); " +
                         "-fx-border-color: transparent transparent transparent #F5C842; " +
                         "-fx-border-width: 0 0 0 4;");
        }

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        iconLabel.setTextFill(active ? Color.web(YELLOW_CARD) : Color.web("#DDEFEF"));

        Label textLabel = new Label(label);
        textLabel.setFont(Font.font("Verdana", active ? FontWeight.BOLD : FontWeight.NORMAL, 15));
        textLabel.setTextFill(active ? Color.web(WHITE) : Color.web("#CDEAEA"));

        row.getChildren().addAll(iconLabel, textLabel);

        // Hover effect
        row.setOnMouseEntered(e -> {
            if (!active)
                row.setStyle("-fx-background-color: rgba(255,255,255,0.08);");
        });
        row.setOnMouseExited(e -> {
            if (!active)
                row.setStyle("-fx-background-color: transparent;");
        });

        return row;
    }

    // ─────────────────────────────────────────────────────────────────
    //  MAIN CONTENT
    // ─────────────────────────────────────────────────────────────────
    private VBox buildMainContent() {
        VBox content = new VBox(36);
        content.setPadding(new Insets(36, 40, 40, 40));
        content.setStyle("-fx-background-color: " + BG_MAIN + ";");

        // ── Section: Dashboard Overview ──────────────────────────────
        Label overviewTitle = sectionTitle("Dashboard Overview");

        HBox statsRow = new HBox(24);
        statsRow.setAlignment(Pos.CENTER_LEFT);
        statsRow.getChildren().addAll(
            buildStatCard("👥", "Total\nEmployees", "10",  YELLOW_CARD),
            buildStatCard("🏢", "Total\nDepartments", "5", PINK_CARD),
            buildStatCard("💵", "Monthly\nPay", "5,648,576,348$", GREEN_CARD)
        );

        // ── Section: Leaves Details ──────────────────────────────────
        Label leavesTitle = sectionTitle("Leaves Details");

        VBox leavesList = new VBox(16);
        leavesList.getChildren().addAll(
            buildLeaveCard("📄", "Leave Applied",  "3", "#1DA8B8", "#B8E8ED"),
            buildLeaveCard("🕐", "Leave Pending",  "3", "#8B3A0F", "#F0D5C8"),
            buildLeaveCard("✅", "Leave Approved", "3", "#2E7D32", "#C8EBC9"),
            buildLeaveCard("❌", "Leave Rejected", "3", "#C62828", "#FBCDD0")
        );

        content.getChildren().addAll(
            overviewTitle, statsRow,
            leavesTitle,   leavesList
        );

        return content;
    }

    // ─────────────────────────────────────────────────────────────────
    //  STAT CARD
    // ─────────────────────────────────────────────────────────────────
    private HBox buildStatCard(String emoji, String title, String value, String bgColor) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18, 24, 18, 18));
        card.setPrefWidth(250);
        card.setPrefHeight(90);
        card.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: hand;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#00000022"));
        shadow.setOffsetY(4);
        shadow.setRadius(12);
        card.setEffect(shadow);

        // Icon box
        StackPane iconBox = new StackPane();
        Rectangle iconBg = new Rectangle(52, 52);
        iconBg.setArcWidth(12);
        iconBg.setArcHeight(12);
        iconBg.setFill(Color.web("#00000015"));
        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font(24));
        iconBox.getChildren().addAll(iconBg, emojiLabel);

        // Text
        VBox textBox = new VBox(2);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
        titleLabel.setTextFill(Color.web(TEXT_DARK));
        titleLabel.setWrapText(true);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        valueLabel.setTextFill(Color.web(TEXT_DARK));
        valueLabel.setWrapText(true);
        valueLabel.setMaxWidth(150);

        textBox.getChildren().addAll(titleLabel, valueLabel);
        card.getChildren().addAll(iconBox, textBox);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        // Hover lift
        card.setOnMouseEntered(e -> {
            DropShadow hoverShadow = new DropShadow();
            hoverShadow.setColor(Color.web("#00000033"));
            hoverShadow.setOffsetY(8);
            hoverShadow.setRadius(18);
            card.setEffect(hoverShadow);
            card.setTranslateY(-2);
        });
        card.setOnMouseExited(e -> {
            card.setEffect(shadow);
            card.setTranslateY(0);
        });

        return card;
    }

    // ─────────────────────────────────────────────────────────────────
    //  LEAVE CARD
    // ─────────────────────────────────────────────────────────────────
    private HBox buildLeaveCard(String emoji, String label, String count,
                                String iconBgColor, String cardBgColor) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18, 28, 18, 18));
        card.setPrefHeight(80);
        card.setMaxWidth(560);
        card.setStyle(
            "-fx-background-color: " + WHITE + ";" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: hand;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#00000018"));
        shadow.setOffsetY(3);
        shadow.setRadius(10);
        card.setEffect(shadow);

        // Colored icon square
        StackPane iconBox = new StackPane();
        Rectangle iconBg = new Rectangle(58, 58);
        iconBg.setArcWidth(12);
        iconBg.setArcHeight(12);
        iconBg.setFill(Color.web(iconBgColor));
        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font(26));
        iconBox.getChildren().addAll(iconBg, emojiLabel);

        // Text
        VBox textBox = new VBox(4);
        Label nameLabel = new Label(label);
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web(iconBgColor));

        Label countLabel = new Label(count);
        countLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        countLabel.setTextFill(Color.web("#555555"));

        textBox.getChildren().addAll(nameLabel, countLabel);

        // Colored accent strip on left
        Rectangle strip = new Rectangle(5, 60);
        strip.setArcWidth(4);
        strip.setArcHeight(4);
        strip.setFill(Color.web(iconBgColor));

        card.getChildren().addAll(strip, iconBox, textBox);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: " + cardBgColor + ";" +
                "-fx-background-radius: 14;" +
                "-fx-cursor: hand;"
            );
            card.setTranslateX(4);
        });
        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: " + WHITE + ";" +
                "-fx-background-radius: 14;" +
                "-fx-cursor: hand;"
            );
            card.setTranslateX(0);
        });

        return card;
    }

    // ─────────────────────────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────────────────────────
    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        label.setTextFill(Color.web(TEXT_DARK));
        label.setPadding(new Insets(0, 0, 6, 0));

        // Underline via border on a wrapper — we return just the label;
        // decorate with an underline box below
        label.setStyle("-fx-underline: true;");
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
