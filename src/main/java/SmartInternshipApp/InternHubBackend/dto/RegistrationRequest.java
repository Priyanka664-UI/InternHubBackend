package SmartInternshipApp.InternHubBackend.dto;

import SmartInternshipApp.InternHubBackend.entity.Student;
import java.time.LocalDate;

public class RegistrationRequest {
    private String fullName;
    private String email;
    private LocalDate birthDate;
    private Student.Gender gender;
    private String username;
    private String password;

    // Constructors
    public RegistrationRequest() {}

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Student.Gender getGender() { return gender; }
    public void setGender(Student.Gender gender) { this.gender = gender; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}