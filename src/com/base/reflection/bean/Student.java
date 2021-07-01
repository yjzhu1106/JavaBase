package com.base.reflection.bean;

public class Student extends Person {
    private String studentId;
    private String school;

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", school='" + school + '\'' +
                '}';
    }



    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Student(String studentId, String school) {
        this.studentId = studentId;
        this.school = school;
    }

    public Student() {
    }

//    public Student(String name, int age, String studentId, String school) {
//        super(name, age);
//        this.studentId = studentId;
//        this.school = school;
//    }
}
