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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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

    static Double MET = 0.0;
    static String timeSlot = "";
    static User user;
    static Double CPG = 0.0;

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
                user = getUserObject(test1);
                changeScenes("MainHealth.fxml", 950, 1500);

            }
        }
    }

    private User getUserObject(String userName) throws SQLException {
        Connection connection = connectionProvider.getConnection();

        User u1 = null;
        String query = "SELECT Password, Gender, DOB, Height, Weight FROM Users WHERE UserName = \"" + userName + "\"";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            String password = resultSet.getString("Password");
            String gender = resultSet.getString("Gender");
            String DOB = resultSet.getString("DOB");
            int height = resultSet.getInt("Height");
            int weight = resultSet.getInt("Weight");

            u1 = new User(userName, password, gender, DOB, height, weight);

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured With setGraphsImport Selecting: " + ex.getMessage());
        }
        connection.close();
        return u1;

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
        plotGraphDashboard(getGraphsData(user.getUserName(), "Steps", today), "Steps");
        plotGraphDashboard(getGraphsData(user.getUserName(), "Calories_In", today), "Calories_In");
        plotGraphDashboard(getGraphsData(user.getUserName(), "Heart_Rate", today), "Heart_Rate");
        plotGraphDashboard(getGraphsData(user.getUserName(), "Oxygen_Level", today), "Oxygen_Level");
        plotSleepGraphDashboard(getSleepData(user.getUserName(), today));
        plotWaterGraphDashboard(getGraphsData(user.getUserName(), "Water", today));

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
        chart.getData().clear();
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
        user = null;
        timeSlot = "";
        MET = 0.0;
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
                if (name != user.getUserName()) {
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
            user = null;
            timeSlot = "";
            MET = 0.0;

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
    private Button btnAddSteps;

    @FXML
    private DatePicker stepsDatePicker;

    @FXML
    private MenuButton menuTimePeriodSteps;

    @FXML
    private MenuItem zeroToSixAmStepsTime;

    @FXML
    private MenuItem sixToTwelveAmStepsTime;

    @FXML
    private MenuItem zeroToSixPmStepsTime;

    @FXML
    private MenuItem sixToTwelvePmStepsTime;

    @FXML
    private TextField TBoxSteps;

    @FXML
    private Label lblStepsAdded;

    @FXML
    void getZeroToSixAmStepsTime(ActionEvent event) {
        timeSlot = "0-6";
        menuTimePeriodSteps.setText("0:00-5:59");
    }

    @FXML
    void getsixToTwelveAmStepsTime(ActionEvent event) {
        timeSlot = "6-12";
        menuTimePeriodSteps.setText("6:00-11:59");
    }

    @FXML
    void getzeroToSixPmStepsTime(ActionEvent event) {
        timeSlot = "12-18";
        menuTimePeriodSteps.setText("12:00-17:59");
    }

    @FXML
    void getsixToTwelvePmStepsTime(ActionEvent event) {
        timeSlot = "18-24";
        menuTimePeriodSteps.setText("18:00-23:59");
    }

    @FXML
    void loadStepsBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Steps");
    }

    @FXML
    void addStepsBtnClicked(ActionEvent event) throws SQLException {
        if (TBoxSteps.getText() != "" || stepsDatePicker.getValue() != null
                || timeSlot != "") {
            String date = stepsDatePicker.getValue().toString();
            Double distance = Double.parseDouble(TBoxSteps.getText());
            int time = (int) ((distance / 4.9) * 60);
            int steps = distanceToSteps(distance);
            MET = 3.5;
            int calBurned = caloriesBurnedActivity(time);
            MET = 0.0;
            String slot = timeSlot;
            addDataFromInfo("Steps", date, timeSlot, steps);
            addDataFromInfo("Calories_Out", date, slot, calBurned);
            lblStepsAdded.setText("Steps value has been updated Successfully!");
            menuTimePeriodSteps.setText("Time");
            TBoxSteps.setText("");
        }
    }

    //
    //Sleep
    //
    @FXML
    private LineChart<String, Number> graphSleepInfo;

    @FXML
    private Button btnSleepLoad;

    @FXML
    private Button btnAddSleep;

    @FXML
    private DatePicker sleepDatePicker;

    @FXML
    private MenuButton menuTimePeriodSleep;

    @FXML
    private MenuItem zeroToSixAmSleepTime;

    @FXML
    private MenuItem sixToTwelveAmSleepTime;

    @FXML
    private MenuItem zeroToSixPmSleepTime;

    @FXML
    private MenuItem sixToTwelvePmSleepTime;

    @FXML
    private TextField TBoxSleep;

    @FXML
    private Label lblSleepAdded;

    @FXML
    void getZeroToSixAmSleepTime(ActionEvent event) {
        timeSlot = "0-6";
        menuTimePeriodSleep.setText("0:00-5:59");
    }

    @FXML
    void getsixToTwelveAmSleepTime(ActionEvent event) {
        timeSlot = "6-12";
        menuTimePeriodSleep.setText("6:00-11:59");
    }

    @FXML
    void getzeroToSixPmSleepTime(ActionEvent event) {
        timeSlot = "12-18";
        menuTimePeriodSleep.setText("12:00-17:59");
    }

    @FXML
    void getsixToTwelvePmSleepTime(ActionEvent event) {
        timeSlot = "18-24";
        menuTimePeriodSleep.setText("18:00-23:59");
    }

    @FXML
    void loadSleepBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Sleep");
    }

    @FXML
    void addSleepBtnClicked(ActionEvent event) throws SQLException {
        if (TBoxSleep.getText() != "" || sleepDatePicker.getValue() != null
                || timeSlot != "") {
            String date = sleepDatePicker.getValue().toString();
            Double newSleep = Double.parseDouble(TBoxSleep.getText());
            Double tot = addSleepFromInfo(date, timeSlot, newSleep);
            if (tot <= 6.0) {
                lblSleepAdded.setText("Sleep hours has been updated Successfully!");
            } else {
                tot -= newSleep;
                lblSleepAdded.setText("Sleep hours Cannot be updated since you already\n"
                        + "have slept for " + tot + "hours");
            }
            menuTimePeriodSleep.setText("Time");
            TBoxSleep.setText("");

        }
    }

    //
    //Water
    //
    @FXML
    private LineChart<String, Number> graphWaterInfo;

    @FXML
    private Button btnLoadWater;

    @FXML
    private Button btnAddWater;

    @FXML
    private DatePicker waterDatePicker;

    @FXML
    private MenuButton menuTimePeriodWater;

    @FXML
    private MenuItem zeroToSixAmWaterTime;

    @FXML
    private MenuItem sixToTwelveAmWaterTime;

    @FXML
    private MenuItem zeroToSixPmWaterTime;

    @FXML
    private MenuItem sixToTwelvePmWaterTime;

    @FXML
    private TextField TBoxWater;

    @FXML
    private Label lblWaterAdded;

    @FXML
    void getZeroToSixAmWaterTime(ActionEvent event) {
        timeSlot = "0-6";
        menuTimePeriodWater.setText("0:00-5:59");
    }

    @FXML
    void getsixToTwelveAmWaterTime(ActionEvent event) {
        timeSlot = "6-12";
        menuTimePeriodWater.setText("6:00-11:59");
    }

    @FXML
    void getzeroToSixPmWaterTime(ActionEvent event) {
        timeSlot = "12-18";
        menuTimePeriodWater.setText("12:00-17:59");
    }

    @FXML
    void getsixToTwelvePmWaterTime(ActionEvent event) {
        timeSlot = "18-24";
        menuTimePeriodWater.setText("18:00-23:59");
    }

    @FXML
    void loadWaterBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Water");
    }

    @FXML
    void addWaterBtnClicked(ActionEvent event) throws SQLException {
        if (TBoxWater.getText() != "" || waterDatePicker.getValue() != null
                || timeSlot != "") {
            String date = waterDatePicker.getValue().toString();
            int newWater = Integer.parseInt(TBoxWater.getText());
            addDataFromInfo("Water", date, timeSlot, newWater);
            lblWaterAdded.setText("Water value has been updated Successfully!");
            menuTimePeriodWater.setText("Time");
            TBoxWater.setText("");
        }
    }

    //
    //Calories
    //
    @FXML
    private LineChart<String, Number> graphCaloriesInfo;

    @FXML
    private Button btnCaloriesLoad;

    @FXML
    private Button btnAddCalories;
    @FXML
    private RadioButton rbCaloriesIn;
    @FXML
    private RadioButton rbCaloriesOut;

    @FXML
    void loadCaloriesBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Calories_In");
        plotGraphInfo("Calories_Out");
    }

    @FXML
    void addCaloriesBtnClicked(ActionEvent event) {

    }

    //
    //Heart Rate
    //
    @FXML
    private LineChart<String, Number> graphHRInfo;

    @FXML
    private Button btnHeartRateLoad;

    @FXML
    private Button btnAddHeart;

    @FXML
    private DatePicker HRDatePicker;

    @FXML
    private MenuButton menuTimePeriodHR;

    @FXML
    private MenuItem zeroToSixAmHRTime;

    @FXML
    private MenuItem sixToTwelveAmHRTime;

    @FXML
    private MenuItem zeroToSixPmHRTime;

    @FXML
    private MenuItem sixToTwelvePmHRTime;

    @FXML
    private TextField TBoxHR;

    @FXML
    private Label lblHRAdded;

    @FXML
    void getZeroToSixAmHRTime(ActionEvent event) {
        timeSlot = "0-6";
        menuTimePeriodHR.setText("0:00-5:59");
    }

    @FXML
    void getsixToTwelveAmHRTime(ActionEvent event) {
        timeSlot = "6-12";
        menuTimePeriodHR.setText("6:00-11:59");
    }

    @FXML
    void getzeroToSixPmHRTime(ActionEvent event) {
        timeSlot = "12-18";
        menuTimePeriodHR.setText("12:00-17:59");
    }

    @FXML
    void getsixToTwelvePmHRTime(ActionEvent event) {
        timeSlot = "18-24";
        menuTimePeriodHR.setText("18:00-23:59");
    }

    @FXML
    void loadHeartRateBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Heart_Rate");
    }

    @FXML
    void addHeartBtnClicked(ActionEvent event) throws SQLException {
        if (TBoxHR.getText() != "" || HRDatePicker.getValue() != null
                || timeSlot != "") {
            String date = HRDatePicker.getValue().toString();
            int newHR = Integer.parseInt(TBoxHR.getText());
            addDataFromInfo("Heart_Rate", date, timeSlot, newHR);
            lblHRAdded.setText("Heart Rate value has been updated Successfully!");
            menuTimePeriodOL.setText("Time");
            TBoxHR.setText("");
        }
    }

    //
    //Oxygen
    //
    @FXML
    private LineChart<String, Number> graphOLInfo;

    @FXML
    private Button btnOxygenLoad;

    @FXML
    private Button btnAddOxygen;

    @FXML
    private DatePicker OLDatePicker;

    @FXML
    private MenuButton menuTimePeriodOL;

    @FXML
    private MenuItem zeroToSixAmOLTime;

    @FXML
    private MenuItem sixToTwelveAmOLTime;

    @FXML
    private MenuItem zeroToSixPmOLTime;

    @FXML
    private MenuItem sixToTwelvePmOLTime;

    @FXML
    private TextField TBoxOL;

    @FXML
    private Label lblOLAdded;

    @FXML
    void getZeroToSixAmOLTime(ActionEvent event) {
        timeSlot = "0-6";
        menuTimePeriodOL.setText("0:00-5:59");
    }

    @FXML
    void getsixToTwelveAmOLTime(ActionEvent event) {
        timeSlot = "6-12";
        menuTimePeriodOL.setText("6:00-11:59");
    }

    @FXML
    void getzeroToSixPmOLTime(ActionEvent event) {
        timeSlot = "12-18";
        menuTimePeriodOL.setText("12:00-17:59");
    }

    @FXML
    void getsixToTwelvePmOLTime(ActionEvent event) {
        timeSlot = "18-24";
        menuTimePeriodOL.setText("18:00-23:59");
    }

    @FXML
    void loadOxygenBtnClicked(ActionEvent event) throws SQLException {
        plotGraphInfo("Oxygen_Level");
    }

    @FXML
    void addOxygenBtnClicked(ActionEvent event) throws SQLException {
        if (TBoxOL.getText() != "" || OLDatePicker.getValue() != null
                || timeSlot != "") {
            String date = OLDatePicker.getValue().toString();
            int newOL = Integer.parseInt(TBoxOL.getText());
            addDataFromInfo("Oxygen_Level", date, timeSlot, newOL);
            lblOLAdded.setText("Oxygen Level value has been updated Successfully!");
            menuTimePeriodOL.setText("Time");
            TBoxOL.setText("");
        }
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
                dayResults = getGraphsData(user.getUserName(), column, day);
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
            if (!column.equals("Calories_Out")) {
                chart.getData().clear();
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
            dayResults = getSleepData(user.getUserName(), day);
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
        graphSleepInfo.getData().clear();
        graphSleepInfo.getData().addAll(series1);

    }

    private int distanceToSteps(Double distance) {
        int steps = 0;
        if (user.getGender().equals("male")) {
            steps = (int) ((int) (distance * 1000.0) / 0.762);
        } else {
            steps = (int) ((int) (distance * 1000.0) / 0.6604);
        }
        return steps;
    }

    //
    //Activity
    //
    @FXML
    private DatePicker activityDatePicker;

    @FXML
    private MenuButton menuTimePeriodActivity;

    @FXML
    private MenuItem zeroToSixAmActTime;

    @FXML
    private MenuItem sixToTwelveAmActTime;

    @FXML
    private MenuItem zeroToSixPmActTime;

    @FXML
    private MenuItem sixToTwelvePmActTime;

    @FXML
    private Button btnActivityLoad;

    @FXML
    private Button btnAddActivity;

    @FXML
    private MenuButton menuActivity;

    @FXML
    private MenuItem runningActivity;

    @FXML
    private MenuItem soccerActivity;

    @FXML
    private MenuItem basketballActivity;

    @FXML
    private MenuItem walkingActivity;

    @FXML
    private TextField TBoxActivityTime;

    @FXML
    private Label lblActivityAdded;

    @FXML
    void loadActivityBtnClicked(ActionEvent event) {

    }

    @FXML
    void addActivityBtnClicked(ActionEvent event) throws SQLException {
        if (TBoxActivityTime.getText() != "" || activityDatePicker.getValue() != null || MET != 0.0
                || timeSlot != "") {
            String date = activityDatePicker.getValue().toString();
            int time = Integer.parseInt(TBoxActivityTime.getText());
            int calBurned = caloriesBurnedActivity(time);
            addDataFromInfo("Calories_Out", date, timeSlot, calBurned);
            lblActivityAdded.setText("Activity has been added Successfully!");
            MET = 0.0;
            menuTimePeriodActivity.setText("Time");
            TBoxActivityTime.setText("");
        }
    }

    @FXML
    void getBasketballValue(ActionEvent event) {
        MET = 8.0;
        menuActivity.setText("Basketball");
    }

    @FXML
    void getRunningValue(ActionEvent event) {
        MET = 13.5;
        menuActivity.setText("Running");
    }

    @FXML
    void getSoccerValue(ActionEvent event) {
        MET = 7.0;
        menuActivity.setText("Soccer");
    }

    @FXML
    void getWalkingValue(ActionEvent event) {
        MET = 3.5;
        menuActivity.setText("Walking");
    }

    @FXML
    void getZeroToSixAmActTime(ActionEvent event) {
        timeSlot = "0-6";
        menuTimePeriodActivity.setText("0:00-5:59");
    }

    @FXML
    void getsixToTwelveAmActTime(ActionEvent event) {
        timeSlot = "6-12";
        menuTimePeriodActivity.setText("6:00-11:59");
    }

    @FXML
    void getzeroToSixPmActTime(ActionEvent event) {
        timeSlot = "12-18";
        menuTimePeriodActivity.setText("12:00-17:59");
    }

    @FXML
    void getsixToTwelvePmActTime(ActionEvent event) {
        timeSlot = "18-24";
        menuTimePeriodActivity.setText("18:00-23:59");
    }

    //
    //Food
    //
    @FXML
    private MenuButton menuTimePeriodFood;

    @FXML
    private MenuItem zeroToSixAmFoodTime;

    @FXML
    private MenuItem sixToTwelveAmFoodTime;

    @FXML
    private MenuItem zeroToSixPmFoodTime;

    @FXML
    private MenuItem sixToTwelvePmFoodTime;

    @FXML
    private MenuButton menuFood;

    @FXML
    private MenuItem appleFood;

    @FXML
    private MenuItem bananaFood;

    @FXML
    private MenuItem saladeFood;

    @FXML
    private MenuItem chickenFood;

    @FXML
    private MenuItem beefFood;

    @FXML
    private MenuItem fishFood;

    @FXML
    private TextField TBoxFood;

    @FXML
    private Label lblFoodAdded;

    @FXML
    private DatePicker foodDatePicker;

    @FXML
    private Button btnFoodLoad;

    @FXML
    private Button btnAddFood;

    @FXML
    void getAppleValue(ActionEvent event) {
        CPG = 0.52;
        menuFood.setText("Apple");
    }

    @FXML
    void getBananaValue(ActionEvent event) {
        CPG = 0.89;
        menuFood.setText("Banana");
    }

    @FXML
    void getBeefValue(ActionEvent event) {
        CPG = 2.5;
        menuFood.setText("Beef");
    }

    @FXML
    void getChickenValue(ActionEvent event) {
        CPG = 2.39;
        menuFood.setText("Chicken");
    }

    @FXML
    void getFishValue(ActionEvent event) {
        CPG = 2.06;
        menuFood.setText("Fish");
    }

    @FXML
    void getSaladeValue(ActionEvent event) {
        CPG = 0.64;
        menuFood.setText("Home Salade");
    }

    @FXML
    void getZeroToSixAmFoodTime(ActionEvent event) {
        timeSlot = "0-6";
        menuTimePeriodFood.setText("0:00-5:59");
    }

    @FXML
    void getsixToTwelveAmFoodTime(ActionEvent event) {
        timeSlot = "6-12";
        menuTimePeriodFood.setText("6:00-11:59");
    }
    
    @FXML
    void getzeroToSixPmFoodTime(ActionEvent event) {
        timeSlot = "12-18";
        menuTimePeriodFood.setText("12:00-17:59");
    }
    
    @FXML
    void getsixToTwelvePmFoodTime(ActionEvent event) {
        timeSlot = "18-24";
        menuTimePeriodFood.setText("18:00-23:59");
    }

    @FXML
    void loadFoodBtnClicked(ActionEvent event) {

    }

    @FXML
    void addFoodBtnClicked(ActionEvent event) throws SQLException {
        if (TBoxFood.getText() != "" || foodDatePicker.getValue() != null || CPG != 0.0
                || timeSlot != "") {
            String date = foodDatePicker.getValue().toString();
            int grams = Integer.parseInt(TBoxFood.getText());
            int calEaten = (int) (grams * CPG);
            addDataFromInfo("Calories_In", date, timeSlot, calEaten);
            lblFoodAdded.setText("Food has been added Successfully!");
            CPG = 0.0;
            menuTimePeriodFood.setText("Time");
            TBoxFood.setText("");
        }
    }

    private int caloriesBurnedActivity(int time) {
        int cal = (int) (time * ((MET * 3.5 * user.getWeight()) / 200));
        return cal;
    }

    private void addDataFromInfo(String col, String date, String time, int difference) throws SQLException {
        Connection connection = connectionProvider.getConnection();
        int newResult = 0;
        String query = "SELECT " + col + " FROM " + user.getUserName() + " WHERE Date = \"" + date + "\" AND time = \"" + time + "\"";

        int oldResult = 0;
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            oldResult = resultSet.getInt(col);

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured With addDataFromInfo Selecting: " + ex.getMessage());
        }

        if (col.equals("Calories_In") || col.equals("Calories_Out") || col.equals("Steps") || col.equals("Water")) {
            newResult = oldResult + difference;
        } else {
            newResult = (oldResult + difference) / 2;
        }

        String query2 = "UPDATE " + user.getUserName() + " SET " + col + " = " + newResult + " WHERE Date = \"" + date + "\" AND time = \"" + time + "\"";

        try {
            PreparedStatement stmt = connection.prepareStatement(query2);

            stmt.executeUpdate();

            timeSlot = "";

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured With addDataFromInfo Updating: " + ex.getMessage());
        }
        connection.close();
    }

    private Double addSleepFromInfo(String date, String time, Double difference) throws SQLException {
        Connection connection = connectionProvider.getConnection();
        Double newResult = 0.0;
        String query = "SELECT Sleep FROM " + user.getUserName() + " WHERE Date = \"" + date + "\" AND time = \"" + time + "\"";

        Double oldResult = 0.0;
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            oldResult = resultSet.getDouble("Sleep");

        } catch (SQLException ex) {
            System.out.println("An Error Has Occured With addSleepFromInfo Selecting: " + ex.getMessage());
        }

        connection.close();
        newResult = oldResult + difference;

        if (newResult > 6.0) {
            timeSlot = "";
            return newResult;
        } else {
            Connection connection2 = connectionProvider.getConnection();
            String query2 = "UPDATE " + user.getUserName() + " SET Sleep = " + newResult + " WHERE Date = \"" + date + "\" AND time = \"" + time + "\"";

            try {
                PreparedStatement stmt = connection2.prepareStatement(query2);

                stmt.executeUpdate();

                timeSlot = "";

            } catch (SQLException ex) {
                System.out.println("An Error Has Occured With addSleepFromInfo Updating: " + ex.getMessage());
            }
            connection2.close();
            return newResult;
        }
    }

}
