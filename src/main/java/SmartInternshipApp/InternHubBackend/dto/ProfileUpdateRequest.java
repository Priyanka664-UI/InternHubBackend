package SmartInternshipApp.InternHubBackend.dto;

import SmartInternshipApp.InternHubBackend.entity.Student;
import java.time.LocalDate;

public class ProfileUpdateRequest {
    private String fullName;
    private String email;
    private LocalDate birthDate;
    private Student.Gender gender;
    private String college;
    private String course;

    // Constructors
    public ProfileUpdateRequest() {}

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Student.Gender getGender() { return gender; }
    public void setGender(Student.Gender gender) { this.gender = gender; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
}