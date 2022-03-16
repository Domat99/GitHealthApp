/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package githealthapp;

import java.io.IOException;
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
import javafx.scene.text.Text;

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
    private void logInClicked(ActionEvent event) throws IOException{
        checkLogin();
    }
    @FXML
    public void adminBtnClicked (ActionEvent event) throws IOException{
        lblNotFound.setText("Button not working yet.");
        lblNotFound.setStyle("-fx-text-fill: purple");
    }
    
    public void signUpClicked() throws IOException{
        changeScenes("CreateAccountHealth.fxml",750,800); 

    }


    public void forgotPasswordClicked() throws IOException{
        
    }
    
    
        
    private void checkLogin() throws IOException{
        String test1=txtFldUsername.getText().trim().toLowerCase();
        String test2=txtFldPassword.getText().trim();
        
        if ((txtFldPassword.getText().trim().isEmpty()) || (txtFldUsername.getText().trim().isEmpty())) {
            lblNotFound.setText("Please type in both the username and password");
            lblNotFound.setStyle("-fx-text-fill: #D05F12");//Orange
        }
        else{
                importData(test1, test2, lblNotFound);
        }
    }
    

    //For the main page after sign in
    @FXML
    private void logOutClicked() throws IOException{
        changeScenes("FXMLHealth.fxml", 525, 800);
    }
    @FXML
    private void deleteAccount2Clicked() throws IOException{
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
            changeScenes("FXMLHealth.fxml",500,800);
        } catch (IOException ex) {
            Logger.getLogger(controllerHealth.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    @FXML
    private void createClicked(ActionEvent event) throws IOException {
        checkValues();
    }
    
    private void checkValues() throws IOException {

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
            if (birthDate.getValue().getYear() <= 2005) {  //Change this to age <18 after you learn how to find age///////////////////////                       

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
            
            if (!resultSet.isBeforeFirst()){
                lbl.setText("User not found.");
                lbl.setStyle("-fx-text-fill: #FF0000");//Red
            }
            else{
                while(resultSet.next()){
                    String retrievePassword = resultSet.getString("Password");
                     lbl.setText("Wrong Password");
                     lbl.setStyle("-fx-text-fill: #FF0000");//Red
                     
                    if(retrievePassword.equals(s2)){
                        lbl.setText("Success!!");
                        lbl.setStyle("-fx-text-fill: #00B050");//Green
                        //System.out.println("Username: " + s1 + ", password: " + retrievePassword);
                        changeScenes("MainHealth.fxml", 950, 1500);

                    }
                }
                
            }
            
        } catch (SQLException ex){
                    ex.printStackTrace();
            System.out.println("An Error Has Occured With PhysycsValues Selecting: " + ex.getMessage());
        }

    }
    
        //Takes the user's info and creates an account.
        protected void save(TextField username, PasswordField password,DatePicker dp,
                TextField height, TextField weight, Label lbl1,String st1) {
        
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
        
        //Checks if the username is already taken. If not, it saves the new user's
        //info using the save() function.
        protected void checkUsername(TextField username2, PasswordField password2, RadioButton rb1, RadioButton rb2,
                DatePicker dp2, TextField height2, TextField weight2, Label lbl2) throws IOException{
            
            String gender = "";
            
            String query = "SELECT * FROM Users WHERE UserName = ?;";
            
            try{
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, username2.getText().toLowerCase().trim());
            
            ResultSet resultSet = stmt.executeQuery();
            
            if(resultSet.isBeforeFirst()){
                lbl2.setText("The username is already taken, please choose another one");
                lbl2.setStyle("-fx-text-fill: #FF0000");//Red
            }
            else{
                lbl2.setText("Success! Welcome to Sehtak Fitness!");
                lbl2.setStyle("-fx-text-fill: #00B050");//Green
                
            if(rb1.isSelected()){
                gender = "male";
            }
            else if (rb2.isSelected()){
                gender = "female";
            }
            else{
                lbl2.setText("ERROR, please try again or contact us");
                lbl2.setStyle("-fx-text-fill: #FF0000");//Red
            }
                
                
                save(username2,password2,dp2,height2,weight2,lbl2,gender);
                changeScenes("FXMLHealth.fxml",500,800);
            }
            
            
            } catch (SQLException ex) {
            
        }
        
        }

        
        protected void changeScenes (String sceneName, int h, int w) throws IOException{
            
            GitHealthApp m = new GitHealthApp();
            
            m.changeScene(sceneName);
            m.stg.setHeight(h);
            m.stg.setWidth(w);
            m.stg.centerOnScreen();
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
    private void deleteClicked(ActionEvent event) {
        btnConfrimDelete.setVisible(true);
        btnDeleteAccount.setVisible(false);
    }

    @FXML
    private void confirmDeleteClicked(ActionEvent event) {
    }

    //THIS FUNCTION IS NOT WORKING, YOU NEED TO FIX IT
    @FXML
    private void mouseEnteredCancel(ActionEvent event){
        setTooltipButton(btnCancelDelete);
    }
    @FXML
    private void cancelDeleteClicked(ActionEvent event) {
    }

    protected void setTooltipButton (Button btn1){
        Tooltip tt1=new Tooltip("Cancel and Sign out");
        btn1.setTooltip(tt1);
    }
    
    
} 
