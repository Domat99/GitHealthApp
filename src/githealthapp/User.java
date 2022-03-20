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

    private String name;
    private String password;
    private String gender;
    private String dateOfBirth;
    private int height;
    private int weight;

    public User(String name, String password, String gender, String dateOfBirth, int height, int weight) {
        this.name = name;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public String getDateOfBirth() {
        return this.dateOfBirth;
    }
}
