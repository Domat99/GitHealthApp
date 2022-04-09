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
    Connection connection = connectionProvider.getConnection();

    @FXML
    private TextField txtFldUsername;
    @FXML
    private PasswordField txtFldPassword;
    @FXML
    private Label lblNotFound;
    
    @FXML
    private void logInClicked(ActionEvent event) throws IOException {
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

    private void checkLogin() throws IOException {
        String test1 = txtFldUsername.getText().trim().toLowerCase();
        String test2 = txtFldPassword.getText().trim();

        if ((txtFldPassword.getText().trim().isEmpty()) || (txtFldUsername.getText().trim().isEmpty())) {
            lblNotFound.setText("Please type in both the username and password");
            lblNotFound.setStyle("-fx-text-fill: #D05F12");//Orange
        } else {
            importData(test1, test2, lblNotFound);
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

    
    //Import user data from the user's table in database
    private void importGraphsDataDashboard(String userName, String column) throws SQLException{
        
        LocalDate today = LocalDate.now();
        String dateToday = today.toString();
        int[] results = new int[4];
        
        String query = "SELECT " + column + " FROM " + userName + " WHERE Date = \"" + "2022-03-19" + "\"";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();
            int i = 0;
            int retrieveColumn;
            while (resultSet.next()){
                retrieveColumn = resultSet.getInt(column);
                results[i] = retrieveColumn;
                i += 1;
            }  
        }catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("An Error Has Occured With setGraphsImport Selecting: " + ex.getMessage());
        }
        
        plotGraphDashboard(results, column);

    }
    
        
    //Use the user's data to plot the graphs in the dashboard
    private void plotGraphDashboard(int[] results, String column){
        LineChart<String, Number> chart = null;
        if(column.equals("Steps")){
            chart = graphSteps;
        }
        if(column.equals("Calories_In")){
            chart = graphCalories;
        }
        if(column.equals("Heart_Rate")){
            chart = graphHR;
        }
        if(column.equals("Oxygen_Level")){
            chart = graphOL;
        }
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Series 1");
        series1.getData().add(new XYChart.Data<>("0-6", results[0]));
        series1.getData().add(new XYChart.Data<>("6-12", results[1]));
        series1.getData().add(new XYChart.Data<>("12-18", results[2]));
        series1.getData().add(new XYChart.Data<>("18-24", results[3]));

        chart.getData().addAll(series1);
    }
    
    @FXML
    private void logOutClicked() throws IOException {
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
    private void createClicked(ActionEvent event) throws IOException {
        checkValues();
    }

    private void checkValues() throws IOException {

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
            if (birthDate1.isBefore(date1)){                      

                if ((txtFldCreatePassword.getText().trim().chars().count()) >= (8.00)) {

                    messageLabel.setText("Success");
                    messageLabel.setStyle("-fx-text-fill: #00B050");//Green

                    checkUsername(txtFldCreateUsername, txtFldCreatePassword, rbMale,
                            rbFemale, birthDate, txtFldHeight, txtFldWeight, messageLabel);

//                    txtFldCreateUsername.clear();
//                    txtFldCreatePassword.clear();
//                    txtFldConfirmPassword.clear();
//                    rbMale.setSelected(false);
//                    rbFemale.setSelected(false);
//                    birthDate.getEditor().clear();
//                    txtFldHeight.clear();
//                    txtFldWeight.clear();
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
    private void importData(String s1, String s2, Label lbl) throws IOException {

        String query = "SELECT Password FROM Users WHERE UserName = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, s1);

            ResultSet resultSet = stmt.executeQuery();

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
                        //System.out.println("Username: " + s1 + ", password: " + retrievePassword);
                        
                        changeScenes("MainHealth.fxml", 950, 1500);
                        
                      //importGraphsDataDashboard("ee", "Steps");
                      //importGraphsDataDashboard(s1, "Calories_In");
                      //importGraphsDataDashboard(s1, "Heart_Rate");
                      //importGraphsDataDashboard(s1, "Oxygen_Level");
                    }
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("An Error Has Occured With PhysycsValues Selecting: " + ex.getMessage());
        }

    }

    //Takes the user's info and creates an account.
    protected void save(TextField username, PasswordField password, DatePicker dp,
            TextField height, TextField weight, Label lbl1, String st1) {

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
    }
    
    protected void createUserTable(TextField username){
        
        String user = username.getText().toLowerCase().trim();
        
        String sql = "CREATE TABLE IF NOT EXISTS " + user + " (\n"
                + "	Date text,\n"
                + "	time text,\n"
                + "	Heart_Rate integer,\n"
                + "     Oxygen_Level integer,\n"
                + "     Calories_In integer,\n"
                + "     Steps integer,\n"
                + "     Calories_Out integer,\n"
                + "     Sleep real\n);";
        
        try {
            
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            pstmt.executeUpdate();
           
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("An Error Has Occured: " + ex.getMessage());
        }
    }

    //Checks if the username is already taken. If not, it saves the new user's
    //info using the save() function.
    protected void checkUsername(TextField username2, PasswordField password2, RadioButton rb1, RadioButton rb2,
            DatePicker dp2, TextField height2, TextField weight2, Label lbl2) throws IOException {

        String gender = "";

        String query = "SELECT * FROM Users WHERE UserName = ?;";

        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, username2.getText().toLowerCase().trim());

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.isBeforeFirst()) {
                lbl2.setText("The username is already taken, please choose another one");
                lbl2.setStyle("-fx-text-fill: #FF0000");//Red
            } else if(username2.getText().toLowerCase().trim().equals("users")){
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
    private void deleteClicked(ActionEvent event) throws IOException{
//        String userToDelete = txtFldUsernameDelete.getText().toLowerCase().trim();
//        String passwordToDelete = txtFldPasswordDelete.getText().toLowerCase().trim();
//        importDeleteAccount(userToDelete, passwordToDelete, lblDeleteInfo);
        
        btnConfrimDelete.setVisible(true);
        btnDeleteAccount.setVisible(false);
    }

    @FXML
    private void confirmDeleteClicked(ActionEvent event) {
    }

    //THIS FUNCTION IS NOT WORKING, YOU NEED TO FIX IT
    @FXML
    private void mouseEnteredCancel(ActionEvent event) {
        setTooltipButton(btnCancelDelete);
    }

    @FXML
    private void cancelDeleteClicked(ActionEvent event) {
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
    
//    private Boolean importDeleteAccount(String s1, String s2, Label lbl) throws IOException {
//
//        String query = "SELECT Password FROM Users WHERE UserName = ?";
//
//        try {
//            PreparedStatement stmt = connection.prepareStatement(query);
//
//            stmt.setString(1, s1);
//
//            ResultSet resultSet = stmt.executeQuery();
//
//            if (!resultSet.isBeforeFirst()) {
//                lbl.setText("User not found.");
//                lbl.setStyle("-fx-text-fill: #FF0000");//Red
//            } else {
//                while (resultSet.next()) {
//                    String retrievePassword = resultSet.getString("Password");
//                    lbl.setText("Wrong Password");
//                    lbl.setStyle("-fx-text-fill: #FF0000");//Red
//
//                    if (retrievePassword.equals(s2)) {
//                        lbl.setText("Success!!");
//                        lbl.setStyle("-fx-text-fill: #00B050");//Green
//
//                    }
//                }
//
//            }
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            System.out.println("An Error Has Occured With PhysycsValues Selecting: " + ex.getMessage());
//        }
//        return null;
//
//    }

    
    
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
    void backToDashboardBtnClicked(ActionEvent event) throws IOException {
        changeScenes("MainHealth.fxml", 950, 1500);
    }

    private Pane view;
    
    public Pane getPane(String fxmlFile){
    try{
        URL fileUrl = GitHealthApp.class.getResource("/githealthapp/" + fxmlFile + ".fxml");
        if(fileUrl == null){
            throw new java.io.FileNotFoundException("FXML file can not be found.");
    }
    view = new FXMLLoader().load(fileUrl);
    }catch (Exception e){
        System.out.println("No Page " + fxmlFile + ". Please check file name.");
    }
        return view;
    }
    
}
