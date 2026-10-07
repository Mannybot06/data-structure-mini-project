import java.time.LocalDate;

public class Employee {
    private int uid;
    private String name;
    private LocalDate dateOfBirth;
    private String address;
    private LocalDate onboardingDate;

    public Employee(int uid, String name, LocalDate dateOfBirth, String address, LocalDate onboardingDate) {
        this.uid = uid;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.onboardingDate = onboardingDate;
    }

    public int getUid() { return uid; }
    public String getName() { return name; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getAddress() { return address; }
    public LocalDate getOnboardingDate() { return onboardingDate; }

    public void setName(String name) { this.name = name; }
    public void setDateOfBirth(LocalDate dob) { this.dateOfBirth = dob; }
    public void setAddress(String address) { this.address = address; }
    public void setOnboardingDate(LocalDate date) { this.onboardingDate = date; }

    @Override
    public String toString() {
        return "Employee{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", dob=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", onboarding=" + onboardingDate +
                '}';
    }

    public String toJson() {
        return String.format(
            "{\"uid\": %d, \"name\": \"%s\", \"dob\": \"%s\", \"address\": \"%s\", \"onboarding\": \"%s\"}",
            uid, escape(name), dateOfBirth, escape(address), onboardingDate
        );
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static Employee fromJson(String json) {
        // Simple manual parsing for the specific format
        int uid = Integer.parseInt(extract(json, "\"uid\":", ","));
        String name = unescape(extract(json, "\"name\": \"", "\""));
        LocalDate dob = LocalDate.parse(extract(json, "\"dob\": \"", "\""));
        String address = unescape(extract(json, "\"address\": \"", "\""));
        LocalDate onboarding = LocalDate.parse(extract(json, "\"onboarding\": \"", "\""));
        return new Employee(uid, name, dob, address, onboarding);
    }

    private static String extract(String json, String key, String endDelim) {
        int start = json.indexOf(key) + key.length();
        int end = json.indexOf(endDelim, start);
        return json.substring(start, end).trim();
    }

    private static String unescape(String s) {
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
