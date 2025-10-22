package org.example.servlets;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.gson.Gson;

public class StudentService {
    private ConcurrentMap<Integer, Student> students;
    private AtomicInteger key;

    public StudentService() {
        this.students = new ConcurrentHashMap<>();
        key = new AtomicInteger();

        this.addStudent(new Student("Ivan", 10, "M"));
        this.addStudent(new Student("Marja", 10, "A"));
        this.addStudent(new Student("Sergey", 10, "K"));
    }

    public String findAllStudent() {
        List<Student> list = new ArrayList<>(this.students.values());
        return this.toJson(list);
    }

    public boolean createStudent(String jsonPayload) {
        if (jsonPayload == null) return false;

        Gson gson = new Gson();
        try {


            if (!jsonPayload.trim().startsWith("[")) {
                jsonPayload = "[" + jsonPayload + "]";
            };


            Student[] studentsArray = gson.fromJson(jsonPayload, Student[].class);
            if (studentsArray != null && studentsArray.length>0) {
                boolean allSuccess = true;
                for (Student student : studentsArray) {
                    if (student != null) {
                        boolean success = this.addStudent(student);
                        if (!success) {
                            allSuccess = false;
                        }
                    }
                }
                return allSuccess;
            }




        } catch (Exception e) {
            // Логируем ошибку для отладки
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

        return false;
    }

    private String toJson(List<Student> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    private boolean addStudent(Student student) {
        System.out.println("Добавить студента");
        System.out.println(student.getName());
        if (student == null) return false;
        int id = key.incrementAndGet();
        student.setId(id);
        students.put(id, student);
        return true;
    }
}