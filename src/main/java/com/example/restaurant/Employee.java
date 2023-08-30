package com.example.restaurant;

public class Employee {
    private int eid;
    private String ename;

    private double earned;

    private String role;

    public Employee(int eid, String ename, double earned, String role) {
        this.eid = eid;
        this.ename = ename;
        this.earned = earned;
        this.role = role;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public double getEarned() {
        return earned;
    }

    public void setEarned(double earned) {
        this.earned = earned;
    }

    public void addEarned(double increase){this.earned += increase;}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "eid=" + eid +
                ", ename='" + ename + '\'' +
                ", earned=" + earned +
                ", role='" + role + '\'' +
                '}';
    }
}
