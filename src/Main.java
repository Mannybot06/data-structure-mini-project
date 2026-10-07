import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EmployeeOperations manager = new EmployeeOperations();
        String filename = "employees.json";
        manager.loadFromFile(filename);

        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("Employee Management System");
        System.out.println("Available operations: \na -> ADD \np -> PRINT, \nl -> LOOKUP, \nm -> MODIFY, \ns -> SORT, \nr -> REMOVE, \nq -> QUIT");

        while (true) {
            System.out.print("\nEnter operation: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("q")) {
                manager.saveToFile(filename);
                break;
            }

            switch (input) {
                case "a":
                    System.out.println("Add employee:");
                    try {
                        System.out.print("Enter custom UID: ");
                        int uid = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter DOB (YYYY-MM-DD): ");
                        LocalDate dob = LocalDate.parse(scanner.nextLine(), formatter);
                        System.out.print("Enter Address: ");
                        String address = scanner.nextLine();
                        System.out.print("Enter Onboarding Date (YYYY-MM-DD): ");
                        LocalDate onboarding = LocalDate.parse(scanner.nextLine(), formatter);

                        if (manager.addEmployee(new Employee(uid, name, dob, address, onboarding))) {
                            System.out.println("Employee added");
                        } else {
                            System.out.println("UID already exists.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input format. Please try again.");
                    }
                    break;

                case "p":
                    System.out.println("All stored employees:");
                    manager.printAll();
                    break;

                case "l":
                    System.out.println("Lookup:");
                    try {
                        System.out.print("Enter UID to lookup employee: ");
                        int uid = Integer.parseInt(scanner.nextLine());
                        Employee emp = manager.lookupByUid(uid);
                        if (emp != null) {
                            System.out.println("Result: " + emp);
                        } else {
                            System.out.println("Employee not found. Try another UID, or press q to quit.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid UID format.");
                    }
                    break;

                case "m":
                    System.out.println("Modify employee data:");
                    try {
                        System.out.print("Enter UID of employee to modify: ");
                        int uid = Integer.parseInt(scanner.nextLine());
                        if (manager.lookupByUid(uid) == null) {
                            System.out.println("Employee not found. Try another UID, or press q to quit.");
                            break;
                        }
                        System.out.print("Enter new name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter new date of birth (YYYY-MM-DD): ");
                        LocalDate dob = LocalDate.parse(scanner.nextLine(), formatter);
                        System.out.print("Enter new address: ");
                        String address = scanner.nextLine();
                        System.out.print("Enter mew onboarding date (yyyy-mm-dd): ");
                        LocalDate onboarding = LocalDate.parse(scanner.nextLine(), formatter);

                        if (manager.updateEmployee(uid, name, dob, address, onboarding)) {
                            System.out.println("Employee info updated.");
                        } else {
                            System.out.println("Update failed. Try again.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input format.");
                    }
                    break;

                case "s":
                    System.out.println("Sorted Employees:");
                    for (Employee emp : manager.getSortedEmployees()) {
                        System.out.println(emp);
                    }
                    break;

                case "r":
                    System.out.println("Remove employee:");
                    try {
                        System.out.print("Enter UID to remove: ");
                        int uid = Integer.parseInt(scanner.nextLine());
                        if (manager.removeEmployee(uid)) {
                            System.out.println("Employee removed.");
                        } else {
                            System.out.println("Employee not found. Try another UID, or press q to quit.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid UID format.");
                    }
                    break;

                default:
                    System.out.println("Unknown operation: " + input + ". Try one of these: \\na -> ADD \np -> PRINT, \nl -> LOOKUP, \nm -> MODIFY, \ns -> SORT, \nr -> REMOVE, \nq -> QUIT");
                    break;
            }
        }
        System.out.println("Quitting.");
        scanner.close();
    }
}
