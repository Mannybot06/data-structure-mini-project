import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EmployeeOperations {

    private HashMap<Integer, Employee> employees = new HashMap<>();
    private TreeMap<String, Integer> nameIndex = new TreeMap<>();

    public boolean addEmployee(Employee emp) {
        if (employees.containsKey(emp.getUid())) {
            return false;
        }
        employees.put(emp.getUid(), emp);
        nameIndex.put(emp.getName(), emp.getUid());
        return true;
    }

    public boolean removeEmployee(int uid) {
        Employee removed = employees.remove(uid);
        if (removed != null) {
            nameIndex.remove(removed.getName());
            return true;
        }
        return false;
    }

    public boolean updateEmployee(int uid, String newName, LocalDate newDob,
                                  String newAddress, LocalDate newOnboarding) {

        Employee emp = employees.get(uid);
        if (emp == null) return false;

        if (!emp.getName().equals(newName)) {
            nameIndex.remove(emp.getName());
            nameIndex.put(newName, uid);
        }

        emp.setName(newName);
        emp.setDateOfBirth(newDob);
        emp.setAddress(newAddress);
        emp.setOnboardingDate(newOnboarding);

        return true;
    }

    public Employee lookupByUid(int uid) {
        return employees.get(uid);
    }

    public Employee lookupByName(String name) {
        Integer uid = nameIndex.get(name);
        if (uid == null) return null;
        return employees.get(uid);
    }

    public List<Employee> getSortedEmployees() {
        List<Employee> sorted = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (Integer uid : nameIndex.values()) {
            sorted.add(employees.get(uid));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Sorted in " + (endTime - startTime) + "ms");
        return sorted;
    }

    public void printAll() {
        if (employees.isEmpty()) {
            System.out.println("No employees stored. Add employees with [a].");
            return;
        }
        long startTime = System.currentTimeMillis();
        for (Employee emp : employees.values()) {
            System.out.println(emp);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Printed in " + (endTime - startTime) + "ms");
    }

    public void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println("[");
            List<Employee> list = new ArrayList<>(employees.values());
            for (int i = 0; i < list.size(); i++) {
                out.print("  " + list.get(i).toJson());
                if (i < list.size() - 1) {
                    out.println(",");
                } else {
                    out.println();
                }
            }
            out.println("]");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) return;

        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            content = content.trim();
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1).trim();
                if (content.isEmpty()) return;

                String[] parts = content.split("\\},\\s*");
                for (String part : parts) {
                    String json = part.trim();
                    if (!json.endsWith("}")) json += "}";
                    try {
                        addEmployee(Employee.fromJson(json));
                    } catch (Exception e) {
                        System.out.println("Error parsing employee: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
