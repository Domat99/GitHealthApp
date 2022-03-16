/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package githealthapp;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Domat
 * @author Elias
 */
public class GitHealthApp extends Application {
    
    
    public static Stage stg;
    
    @Override
    public void start(Stage primaryStage) throws IOException {

        stg = primaryStage;
        stg.setResizable(true);
        stg.setMaxHeight(900);
        stg.setMaxWidth(1500);
        stg.centerOnScreen();
        
        Parent root1 = FXMLLoader.load(getClass().getResource("FXMLHealth.fxml"));
        
        Scene scene1 = new Scene(root1);
        
        primaryStage.setTitle("Your Health App");
        primaryStage.setScene(scene1);
        primaryStage.show();
    }
    
    public void changeScene(String fxml) throws IOException{
        Parent pane =  FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        stg.setResizable(true);
        //stg.setMaxHeight(900);
        //stg.setMaxWidth(1500);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
