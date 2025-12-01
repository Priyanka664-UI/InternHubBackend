package SmartInternshipApp.InternHubBackend.dto;

import SmartInternshipApp.InternHubBackend.entity.Student;
import java.time.LocalDate;

public class RegistrationRequest {
    private String name;
    private String fullName;
    private String email;
    private LocalDate birthDate;
    private Student.Gender gender;
    private String password;
    private String college;
    private String course;

    public RegistrationRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Student.Gender getGender() { return gender; }
    public void setGender(Student.Gender gender) { this.gender = gender; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
}