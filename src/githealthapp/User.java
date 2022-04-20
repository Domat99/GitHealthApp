/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package githealthapp;

/**
 *
 * @author domat
 */
public class User {
    private String userName;
    private String password;
    private String gender;
    private String dateOfBirth;
    private int height;
    private int weight;

    public User(String userName, String password, String gender, String dateOfBirth, int height, int weight) {
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return "User{" + "userName=" + userName + ", password=" + password + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", height=" + height + ", weight=" + weight + '}';
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    
    
}
