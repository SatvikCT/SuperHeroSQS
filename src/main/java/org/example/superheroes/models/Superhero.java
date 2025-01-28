package org.example.superheroes.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "superheroes")
public class Superhero {
    @Id
    private String id;
    private String name;
    private String power;
    private String gender;
    private int age;
    private String universe;
    private boolean marked = false; // New field

    // Constructors
    public Superhero() {}

    public Superhero(String name, String power, String gender, int age, String universe) {
        this.name = name;
        this.power = power;
        this.gender = gender;
        this.age = age;
        this.universe = universe;
    }

    // Getters and Setters
    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUniverse() {
        return universe;
    }

    public void setUniverse(String universe) {
        this.universe = universe;
    }

    // Other getters and setters remain the same
}
