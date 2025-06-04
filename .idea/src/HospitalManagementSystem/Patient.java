package HospitalManagementSystem;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        try {
            scanner.nextLine(); // clear leftover newline
            System.out.print("Enter Patient Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Patient Age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // consume newline after nextInt

            System.out.print("Enter Patient Gender: ");
            String gender = scanner.nextLine();

            String query = "INSERT INTO patients(name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Patient Added Successfully!");
            } else {
                System.out.println("❌ Failed to Add Patient.");
            }

        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid input! Please enter the correct data type.");
            scanner.nextLine(); // clear invalid input
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query = "SELECT * FROM patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Patients:");
            System.out.println("+------------+--------------------+----------+------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+------------+--------------------+----------+------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                System.out.printf("| %-10d | %-18s | %-8d | %-10s |\n", id, name, age, gender);
            }

            System.out.println("+------------+--------------------+----------+------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // returns true if a row is found
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
