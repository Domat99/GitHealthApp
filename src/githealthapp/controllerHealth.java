/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package githealthapp;

import DBConnection.DBConnectionProviderHealth;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author elias
 */
public class controllerHealth {

    DBConnectionProviderHealth connectionProvider = new DBConnectionProviderHealth();
    static String userName = "";

    @FXML
    private TextField txtFldUsername;
    @FXML
    private PasswordField txtFldPassword;
    @FXML
    private Label lblNotFound;

    @FXML
    private void logInClicked(ActionEvent event) throws IOException, SQLException {
        checkLogin();
    }

    @FXML
    public void adminBtnClicked(ActionEvent event) throws IOException {
        lblNotFound.setText("Button not working yet.");
        lblNotFound.setStyle("-fx-text-fill: purple");
    }

    public void signUpClicked() throws IOException {
        changeScenes("CreateAccountHealth.fxml", 750, 800);

    }

    public void forgotPasswordClicked() throws IOException {

    }

    private void checkLogin() throws IOException, SQLException {
        String test1 = txtFldUsername.getText().trim().toLowerCase();
        String test2 = txtFldPassword.getText().trim();

        if ((txtFldPassword.getText().trim().isEmpty()) || (txtFldUsername.getText().trim().isEmpty())) {
            lblNotFound.setText("Please type in both the username and password");
            lblNotFound.setStyle("-fx-text-fill: #D05F12");//Orange
        } else {
            boolean data = importData(test1, test2, lblNotFound);
            if (data == true) {
                userName = test1;
                changeScenes("MainHealth.fxml", 950, 1500);

            }
        }
    }

    //For the main page after sign in
    @FXML
    private LineChart<String, Number> graphSteps;

    @FXML
    private ProgressIndicator pieSleep;

    @FXML
    private ProgressBar barWater;

    @FXML
    private LineChart<String, Number> graphCalories;

    @FXML
    private LineChart<String, Number> graphHR;

    @FXML
    private LineChart<String, Number> graphOL;

    @FXML
    private Button btnInfoPage;
    @FXML
    private Button btnLoadGraphs;

    @FXML
    void stepsMouseClicked(MouseEvent event) throws IOException {
        //changeScenes("ViewInfoFXML.fxml", 750, 800);
    }

    @FXML
    void sleepMouseClicked(MouseEvent event) {

    }

    @FXML
    void waterMouseClicked(MouseEvent event) {

    }

    @FXML
    void caloriesMouseClicked(MouseEvent event) {

    }

    @FXML
    void heartRateMouseClicked(MouseEvent event) {

    }

    @FXML
    void oxygenMouseClicked(MouseEvent event) {

    }

    @FXML
    void infoBtnClicked(ActionEvent event) throws IOException {
        changeScenes("ViewInfoFXML.fxml", 750, 800);

    }

    @FXML
    void loadGraphsBtnClicked(ActionEvent event) throws IOException, SQLException {
        LocalDate today = LocalDate.of(2022, 03, 26);
        plotGraphDashboard(getGraphsData(userName, "Steps", today), "Steps");
        plotGraphDashboard(getGraphsData(userName, "Calories_In", today), "Calories_In");
        plotGraphDashboard(getGraphsData(userName, "Heart_Rate", today), "Heart_Rate");
        plotGraphDashboard(getGraphsData(userName, "Oxygen_Level", today), "Oxygen_Level");
        plotSleepGraphDashboard(getSleepData(userName, today));
        plotWaterGraphDashboard(getGraphsData(userName, "Water", today));

    }

    //Import user data from the user's table in database
    private int[] getGraphsData(String name, String column, LocalDate today) throws SQLException {

        String dateToday = today.toString();
        int[] results = new int[4];
        Connection connection = connectionProvider.getConnection();
        String query = "SELECT " + column + " FROM " + name + " WHERE Date = \"" + dateToday + "\"";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();
            int i = 0;
            int retrieveColumn;
            while (resultSet.next()) {
                retrieveColumn = resultSet.getInt(column);
                results[i] = retrieveColumn;
                i += 1;
            }
        } catch (SQLException ex) {
            System.out.println("An Error Has Occured With setGraphsImport Selecting: " + ex.getMessage());
        }
        connection.close();
        return results;

    }

    //Use the user's data to plot the graphs in the dashboard
    private void plotGraphDashboard(int[] results, String column) {
        LineChart<String, Number> chart = null;
        switch (column) {
            case "Steps":
                chart = graphSteps;
                break;
            case "Calories_In":
                chart = graphCalories;
                break;
            case "Heart_Rate":
                chart = graphHR;
                break;
            case "Oxygen_Level":
                chart = graphOL;
                break;
            default:
                break;
        }
        XYChart.Series series1 = new XYChart.Series();
        chart.getXAxis().setAnimated(false);
        chart.getYAxis().setAnimated(true);
        chart.setAnimated(true);
        series1.getData().add(new XYChart.Data<>("0-6", results[0]));
        series1.getData().add(new XYChart.Data<>("6-12", results[1]));
        series1.getData().add(new XYChart.Data<>("12-18", results[2]));
        series1.getData().add(new XYChart.Data<>("18-24", results[3]));

        chart.getData().addAll(series1);
    }

    private Double[] getSleepData(String name, LocalDate today) throws SQLException {

        Connection connection = connectionProvider.getConnection();

        String dateToday = today.toString();

        LocalDate yesterday = today.minusDays(1);
        String yesterdayString = yesterday.toString();

        Double[] results = new Double[4];
        String query = "SELECT Sleep FROM " + name + " WHERE Date = \"" + dateToday + "\" AND time = \"18-24\"";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            results[0] = resultSet.getDouble("Sleep");

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured With setGraphsImport Selecting: " + ex.getMessage());
        }
        String query2 = "SELECT Sleep FROM " + name + " WHERE Date = \"" + yesterdayString + "\" AND time = \"0-6\" OR Date = \"" + yesterdayString + "\" AND time = \"6-12\""
                + " OR Date = \"" + yesterdayString + "\" AND time = \"12-18\"";
        try {
            PreparedStatement stmt2 = connection.prepareStatement(query2);

            ResultSet resultSet = stmt2.executeQuery();
            int i = 1;
            Double retrieveColumn;
            while (resultSet.next()) {
                retrieveColumn = resultSet.getDouble("Sleep");
                results[i] = retrieveColumn;
                i += 1;
            }
        } catch (SQLException ex) {
            System.out.println("An Error Has Occured With setGraphsImport Selecting: " + ex.getMessage());
        }
        connection.close();

        return results;
    }

    private void plotSleepGraphDashboard(Double[] results) {
        Double sleepTime = 0.0;
        for (Double result : results) {
            sleepTime += result;
        }

        Double sleepPercentage = sleepTime / 24.0;
        pieSleep.setProgress(sleepPercentage);

    }

    private void plotWaterGraphDashboard(int[] results) {
        int waterAmount = 0;
        for (int j = 0; j < results.length; j++) {
            waterAmount += results[j];
        }
        if (waterAmount >= 10) {
            barWater.setProgress(1);
        } else {
            Double waterPercentage = waterAmount / 10.0;
            barWater.setProgress(waterPercentage);
        }
    }

    @FXML
    private void logOutClicked() throws IOException {
        userName = "";
        changeScenes("FXMLHealth.fxml", 525, 800);
    }

    @FXML
    private void deleteAccount2Clicked() throws IOException, SQLException {

        changeScenes("DeleteAccount.fxml", 525, 800);
    }

    //For sign up page
    @FXML
    private Label messageLabel;
    @FXML
    private TextField txtFldCreateUsername;
    @FXML
    private PasswordField txtFldCreatePassword;
    @FXML
    private PasswordField txtFldConfirmPassword;
    @FXML
    private RadioButton rbMale;
    @FXML
    private RadioButton rbFemale;
    @FXML
    private DatePicker birthDate;
    @FXML
    private TextField txtFldHeight;
    @FXML
    private TextField txtFldWeight;

    @FXML
    private void backClicked(ActionEvent event) {
        try {
            changeScenes("FXMLHealth.fxml", 500, 800);
        } catch (IOException ex) {
            Logger.getLogger(controllerHealth.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void createClicked(ActionEvent event) throws IOException, SQLException {
        checkValues();
    }

    private void checkValues() throws IOException, SQLException {

        LocalDate date1 = LocalDate.now().minusYears(16);
        LocalDate birthDate1 = birthDate.getValue();

        if ((txtFldCreateUsername.getText().trim().isEmpty())
                || (txtFldCreatePassword.getText().trim().isEmpty())
                || (txtFldConfirmPassword.getText().trim().isEmpty())
                || (!(rbMale.isSelected()) && !(rbFemale.isSelected()))
                || (birthDate.getValue() == null)
                || (txtFldHeight.getText().trim().isEmpty())
                || (txtFldWeight.getText().trim().isEmpty())) {
            messageLabel.setText("Please fill all the fields");
            messageLabel.setStyle("-fx-text-fill: #D05F12");//Orange
        } else if ((txtFldCreatePassword.getText().trim()).equals(txtFldConfirmPassword.getText().trim())) {
            if (birthDate1.isBefore(date1)) {

                if ((txtFldCreatePassword.getText().trim().chars().count()) >= (8.00)) {

                    messageLabel.setText("Success");
                    messageLabel.setStyle("-fx-text-fill: #00B050");//Green

                    checkUsername(txtFldCreateUsername, txtFldCreatePassword, rbMale,
                            rbFemale, birthDate, txtFldHeight, txtFldWeight, messageLabel);

                } else {
                    messageLabel.setText("Please use at least 8 Characters for the password");
                    messageLabel.setStyle("-fx-text-fill: #FF0000");//Red

                }
            } else {
                messageLabel.setText("You need to be at least 16 years old to use this program");
                messageLabel.setStyle("-fx-text-fill: #FF0000");//Red
            }
        } else if (!(txtFldCreatePassword.getText().trim()).equals(txtFldConfirmPassword.getText().trim())) {
            messageLabel.setText("Please make sure the password matches in both boxes");
            messageLabel.setStyle("-fx-text-fill: #D05F12");//Orange
        } else {
            messageLabel.setText("An Error has accured");
            messageLabel.setStyle("-fx-text-fill: #FF0000");//Red
        }
    }

    //NOT DONE YET!
    //Gets the users info to use for the sign in.
    private Boolean importData(String s1, String s2, Label lbl) throws IOException, SQLException {
        Connection connection = connectionProvider.getConnection();
        String query = "SELECT Password FROM Users WHERE UserName = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        try {
            stmt = connection.prepareStatement(query);

            stmt.setString(1, s1);

            resultSet = stmt.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                lbl.setText("User not found.");
                lbl.setStyle("-fx-text-fill: #FF0000");//Red
            } else {
                while (resultSet.next()) {
                    String retrievePassword = resultSet.getString("Password");
                    lbl.setText("Wrong Password");
                    lbl.setStyle("-fx-text-fill: #FF0000");//Red
                    if (retrievePassword.equals(s2)) {
                        lbl.setText("Success!!");
                        lbl.setStyle("-fx-text-fill: #00B050");//Green
                        connection.close();
                        return true;
                    }
                }

            }

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured With importData Selecting: " + ex.getMessage());
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
            }
        }

        connection.close();

        return false;

    }

    //Takes the user's info and creates an account.
    protected void save(TextField username, PasswordField password, DatePicker dp,
            TextField height, TextField weight, Label lbl1, String st1) throws SQLException {
        Connection connection = connectionProvider.getConnection();
        String query = "INSERT INTO Users (UserName, Password, Gender,DOB,Height,Weight) VALUES(?,?,?,?,?,?) ";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setString(1, username.getText().toLowerCase().trim());
            pstmt.setString(2, password.getText().trim());
            pstmt.setString(3, st1);
            pstmt.setString(4, dp.getValue().toString());
            pstmt.setString(5, height.getText().trim());
            pstmt.setString(6, weight.getText().trim());

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured With Users Update: " + ex.getMessage());
        }
        connection.close();
    }

    protected void createUserTable(TextField username) throws SQLException {
        Connection connection = connectionProvider.getConnection();

        String user = username.getText().toLowerCase().trim();

        String sql = "CREATE TABLE IF NOT EXISTS " + user + " (\n"
                + "	Date text,\n"
                + "	time text,\n"
                + "	Heart_Rate integer,\n"
                + "     Oxygen_Level integer,\n"
                + "     Calories_In integer,\n"
                + "     Steps integer,\n"
                + "     Calories_Out integer,\n"
                + "     Sleep real,\n"
                + "     Water integer\n);";

        try {

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured: " + ex.getMessage());
        }
        connection.close();
    }

    //Checks if the username is already taken. If not, it saves the new user's
    //info using the save() function.
    protected void checkUsername(TextField username2, PasswordField password2, RadioButton rb1, RadioButton rb2,
            DatePicker dp2, TextField height2, TextField weight2, Label lbl2) throws IOException, SQLException {

        Connection connection = connectionProvider.getConnection();
        String gender = "";

        String query = "SELECT * FROM Users WHERE UserName = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, username2.getText().toLowerCase().trim());

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.isBeforeFirst()) {
                lbl2.setText("The username is already taken, please choose another one");
                lbl2.setStyle("-fx-text-fill: #FF0000");//Red
            } else if (username2.getText().toLowerCase().trim().equals("users")) {
                lbl2.setText("This username is not available, please choose another one");
                lbl2.setStyle("-fx-text-fill: #FF0000");//Red
            } else {
                lbl2.setText("Success! Welcome to Sehtak Fitness!");
                lbl2.setStyle("-fx-text-fill: #00B050");//Green

                if (rb1.isSelected()) {
                    gender = "male";
                } else if (rb2.isSelected()) {
                    gender = "female";
                } else {
                    lbl2.setText("ERROR, please try again or contact us");
                    lbl2.setStyle("-fx-text-fill: #FF0000");//Red
                }

                save(username2, password2, dp2, height2, weight2, lbl2, gender);
                createUserTable(username2);
                changeScenes("FXMLHealth.fxml", 500, 800);
            }

        } catch (SQLException ex) {

        }
        connection.close();
    }

    @FXML
    private Button btnDeleteAccount;
    @FXML
    private Button btnConfrimDelete;
    @FXML
    private Button btnCancelDelete;
    @FXML
    private TextField txtFldUsernameDelete;
    @FXML
    private PasswordField txtFldPasswordDelete;
    @FXML
    private Label lblDeleteInfo;

    @FXML
    private void deleteClicked(ActionEvent event) throws IOException, SQLException {

        String name = txtFldUsernameDelete.getText().trim().toLowerCase();
        String password = txtFldPasswordDelete.getText().trim();

        if ((password.isEmpty()) || (name.isEmpty())) {
            lblDeleteInfo.setText("Please type in both the username and password");
            lblDeleteInfo.setStyle("-fx-text-fill: #D05F12");//Orange
        } else {
            boolean data = importData(name, password, lblDeleteInfo);
            if (data == true) {
                if (name != userName) {
                    btnConfrimDelete.setText("Please enter the user name that you have used to sign in");
                    btnConfrimDelete.setStyle("-fx-text-fill: #FF0000");//Red
                } else {

                    btnConfrimDelete.setText("Please confirm delete");
                    btnConfrimDelete.setStyle("-fx-text-fill: #FF0000");//Red
                    btnConfrimDelete.setVisible(true);
                    btnDeleteAccount.setVisible(false);
                    txtFldUsernameDelete.setEditable(false);
                    txtFldPasswordDelete.setEditable(false);
                }
            }
        }

    }

    @FXML
    private void confirmDeleteClicked(ActionEvent event) {
        Connection connection = connectionProvider.getConnection();
        String name = txtFldUsernameDelete.getText().trim().toLowerCase();

        String sql = "DELETE FROM Users WHERE UserName = ?";

        try {

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, name);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured while deleting the account from the users data: " + ex.getMessage());
        }
        String sql2 = "DROP TABLE " + name;

        try {

            PreparedStatement pstmt = connection.prepareStatement(sql2);
            pstmt.executeUpdate();

            lblDeleteInfo.setText("User has been deleted succesfully!");
            lblDeleteInfo.setStyle("-fx-text-fill: #D05F12");//Orange
            name = "";

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured while deleting the account table: " + ex.getMessage());
        }

    }

    //THIS FUNCTION IS NOT WORKING, YOU NEED TO FIX IT
    @FXML
    private void mouseEnteredCancel(ActionEvent event) {
        setTooltipButton(btnCancelDelete);
    }

    @FXML
    private void cancelDeleteClicked(ActionEvent event) throws IOException {
        changeScenes("MainHealth.fxml", 950, 1500);
    }

    protected void changeScenes(String sceneName, int h, int w) throws IOException {

        GitHealthApp m = new GitHealthApp();

        m.changeScene(sceneName);
        m.stg.setHeight(h);
        m.stg.setWidth(w);
        m.stg.centerOnScreen();
    }

    protected void setTooltipButton(Button btn1) {
        Tooltip tt1 = new Tooltip("Cancel and Sign out");
        btn1.setTooltip(tt1);
    }

    //New Scene (View and edit date)
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button btnSteps;

    @FXML
    private Button btnWater;

    @FXML
    private Button btnSleep;

    @FXML
    private Button btnCalories;

    @FXML
    private Button btnHeartRate;

    @FXML
    private Button btnOxygen;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnActivity;

    @FXML
    private Button btnFood;

    
    @FXML
    void stepsBtnClicked(ActionEvent event) {
        getPane("StepsFXML");
        controllerHealth object = new controllerHealth();
        Pane View1 = object.getPane("StepsFXML");
        mainPane.setCenter(View1);

    }

    @FXML
    void sleepBtnClicked(ActionEvent event) {
        getPane("SleepFXML");
        controllerHealth object = new controllerHealth();
        Pane View2 = object.getPane("SleepFXML");
        mainPane.setCenter(View2);
    }

    @FXML
    void waterBtnClicked(ActionEvent event) {
        getPane("WaterFXML");
        controllerHealth object = new controllerHealth();
        Pane View2 = object.getPane("WaterFXML");
        mainPane.setCenter(View2);
    }

    @FXML
    void caloriesBtnClicked(ActionEvent event) {
        getPane("CaloriesFXML");
        controllerHealth object = new controllerHealth();
        Pane View2 = object.getPane("CaloriesFXML");
        mainPane.setCenter(View2);
    }

    @FXML
    void heartRateBtnClicked(ActionEvent event) {
        getPane("HeartRateFXML");
        controllerHealth object = new controllerHealth();
        Pane View2 = object.getPane("HeartRateFXML");
        mainPane.setCenter(View2);

    }

    @FXML
    void oxygenBtnClicked(ActionEvent event) {
        getPane("OxygenFXML");
        controllerHealth object = new controllerHealth();
        Pane View2 = object.getPane("OxygenFXML");
        mainPane.setCenter(View2);
    }

    @FXML
    void activityBtnClicked(ActionEvent event) {
        getPane("ActivityFXML");
        controllerHealth object = new controllerHealth();
        Pane View2 = object.getPane("ActivityFXML");
        mainPane.setCenter(View2);
    }
    
    @FXML
    void foodBtnClicked(ActionEvent event) {
        getPane("FoodFXML");
        controllerHealth object = new controllerHealth();
        Pane View2 = object.getPane("FoodFXML");
        mainPane.setCenter(View2);
    }
    
    
    @FXML
    void backToDashboardBtnClicked(ActionEvent event) throws IOException {
        changeScenes("MainHealth.fxml", 950, 1500);
    }

    private Pane view;

    public Pane getPane(String fxmlFile) {
        try {
            URL fileUrl = GitHealthApp.class.getResource("/githealthapp/" + fxmlFile + ".fxml");
            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML file can not be found.");
            }
            view = new FXMLLoader().load(fileUrl);
        } catch (IOException e) {
            System.out.println("No Page " + fxmlFile + ". Please check file name.");
        }
        return view;
    }

    //
    //
    //Scenes in View Info
    //
    //
    //
    //Steps
    //
    @FXML
    private LineChart<String, Number> graphStepsInfo;

    @FXML
    private Button btnLoadSteps;

    @FXML
    void loadStepsBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Steps");
    }

    //
    //Sleep
    //
    @FXML
    private LineChart<String, Number> graphSleepInfo;

    @FXML
    private Button btnSleepLoad;

    @FXML
    void loadSleepBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Sleep");
    }

    //
    //Water
    //
    @FXML
    private LineChart<String, Number> graphWaterInfo;

    @FXML
    private Button btnLoadWater;

    @FXML
    void loadWaterBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Water");
    }

    //
    //Calories
    //
    @FXML
    private LineChart<String, Number> graphCaloriesInfo;

    @FXML
    private Button btnCaloriesLoad;

    @FXML
    void loadCaloriesBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Calories_In");
        plotGraphInfo("Calories_Out");
    }

    //
    //Heart Rate
    //
    @FXML
    private LineChart<String, Number> graphHRInfo;

    @FXML
    private Button btnHeartRateLoad;

    @FXML
    void loadHeartRateBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Heart_Rate");
    }

    //
    //Oxygen
    //
    @FXML
    private LineChart<String, Number> graphOLInfo;

    @FXML
    private Button btnOxygenLoad;

    @FXML
    void loadOxygenBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Oxygen_Level");
    }

    private void plotGraphInfo(String column) throws SQLException {
        LocalDate day = LocalDate.of(2022, 03, 26);

        if (column.equals("Sleep")) {
            plotSleepGraphInfo(day);
        } else {
            int[] totResults = new int[7];
            int[] dayResults = new int[4];
            int results;
            for (int i = 0; i < totResults.length; i++) {
                results = 0;
                dayResults = getGraphsData(userName, column, day);
                for (int j = 0; j < dayResults.length; j++) {
                    results += dayResults[j];
                }
                if (column.equals("Heart_Rate") || column.equals("Oxygen_Level")) {
                    results /= 4;
                }
                totResults[i] = results;
                day = day.minusDays(1);
            }

            LineChart<String, Number> chart = null;
            switch (column) {
                case "Steps":
                    chart = graphStepsInfo;
                    break;
                case "Water":
                    chart = graphWaterInfo;
                    break;
                case "Heart_Rate":
                    chart = graphHRInfo;
                    break;
                case "Oxygen_Level":
                    chart = graphOLInfo;
                    break;
                case "Calories_In":
                    chart = graphCaloriesInfo;
                    break;
                case "Calories_Out":
                    chart = graphCaloriesInfo;
                    break;
                default:
                    break;
            }
            XYChart.Series series1 = new XYChart.Series();
            if (column.equals("Calories_In")) {
                series1.setName("Calories In");
            }
            if (column.equals("Calories_Out")) {
                series1.setName("Calories Out");
            }
            chart.getXAxis().setAnimated(false);
            chart.getYAxis().setAnimated(true);
            chart.setAnimated(true);
            day = day.plusDays(1);
            for (int k = 6; k >= 0; k--) {
                series1.getData().add(new XYChart.Data<>(day.toString(), totResults[k]));
                day = day.plusDays(1);
            }
            chart.getData().addAll(series1);
        }
    }

    private void plotSleepGraphInfo(LocalDate day) throws SQLException {
        Double[] totResults = new Double[7];
        Double[] dayResults = new Double[4];
        Double results;
        for (int i = 0; i < totResults.length; i++) {
            results = 0.0;
            dayResults = getSleepData(userName, day);
            for (Double dayResult : dayResults) {
                results += dayResult;
            }
            totResults[i] = results;
            day = day.minusDays(1);
        }
        XYChart.Series series1 = new XYChart.Series();
        graphSleepInfo.getXAxis().setAnimated(false);
        graphSleepInfo.getYAxis().setAnimated(true);
        graphSleepInfo.setAnimated(true);
        day = day.plusDays(1);
        for (int k = 6; k >= 0; k--) {
            series1.getData().add(new XYChart.Data<>(day.toString(), totResults[k]));
            day = day.plusDays(1);
        }
        graphSleepInfo.getData().addAll(series1);

    }
    
    
    //
    //Activity
    //
    @FXML
    private Button btnActivityLoad;

    @FXML
    void loadActivityBtnClicked(ActionEvent event) {

    }
    
    
    //
    //Food
    //
    @FXML
    private Button btnFoodLoad;

    @FXML
    void loadFoodBtnClicked(ActionEvent event) {

    }
    
    
    
}
