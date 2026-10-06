import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Plant {
    String name;
    float temperature;
    float waterNeeds;
    float sunlight;

    public Plant(String name, float temperature, float waterNeeds, float sunlight) {
        this.name = name;
        this.temperature = temperature;
        this.waterNeeds = waterNeeds;
        this.sunlight = sunlight;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Temperature: " + temperature + ", Water Needs: " + waterNeeds + ", Sunlight: " + sunlight;
    }
}

public class backend {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load the MySQL JDBC driver
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/minor_outplants_dataset", "Root", "root"); // Replace "username" and "password" with your database credentials

            Scanner scanner = new Scanner(System.in);
 
            System.out.print("Enter the number of plants: ");
            int numPlants = scanner.nextInt();

            List<Plant> selectedPlants = new ArrayList<>();
            for (int i = 0; i < numPlants; i++) {
                System.out.print("Enter the name of plant " + (i + 1) + ": ");
                String name = scanner.next();
                selectedPlants.add(new Plant(name, 0, 0, 0)); // Placeholder values for now
            }

            System.out.println("Choose an option:");
            System.out.println("1. Sunlight");
            System.out.println("2. Water");
            System.out.println("3. Temperature");
            System.out.println("4. Info");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Retrieve and display data for sunlight
                    retrieveAndDisplayData(conn, selectedPlants, "sunlight");
                    break;
                case 2:
                    // Retrieve and display data for water
                    retrieveAndDisplayData(conn, selectedPlants, "water_needs");
                    break;
                case 3:
                    // Retrieve and display data for temperature
                    retrieveAndDisplayData(conn, selectedPlants, "temperature");
                    break;
                case 4:
                    // Display info for selected plants
                    for (Plant plant : selectedPlants) {
                        System.out.println(plant);
                    }
                    break;
                default:
                    System.err.println("Invalid choice.");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void retrieveAndDisplayData(Connection conn, List<Plant> selectedPlants, String columnName) {
        try {
            Statement stmt = conn.createStatement();
            StringBuilder query = new StringBuilder("SELECT name, " + columnName + " FROM plants WHERE name IN (");

            for (Plant plant : selectedPlants) {
                query.append("'").append(plant.name).append("',");
            }
            query.setLength(query.length() - 1); // Remove the trailing comma
            query.append(")");

            ResultSet resultSet = stmt.executeQuery(query.toString());

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                float value = resultSet.getFloat(columnName);
                for (Plant plant : selectedPlants) {
                    if (plant.name.equals(name)) {
                        if (columnName.equals("sunlight")) {
                            plant.sunlight = value;
                        } else if (columnName.equals("water_needs")) {
                            plant.waterNeeds = value;
                        } else if (columnName.equals("temperature")) {
                            plant.temperature = value;
                        }
                    }
                }
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}