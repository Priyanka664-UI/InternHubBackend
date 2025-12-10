package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import SmartInternshipApp.InternHubBackend.dto.PasswordChangeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    public Optional<Student> getProfile(Long id) {
        return studentRepository.findById(id);
    }
    
    public Student updateProfile(Long id, Student updatedStudent) {
        Optional<Student> existing = studentRepository.findById(id);
        if (existing.isPresent()) {
            Student student = existing.get();
            if (updatedStudent.getFullName() != null) student.setFullName(updatedStudent.getFullName());
            if (updatedStudent.getEmail() != null) student.setEmail(updatedStudent.getEmail());
            if (updatedStudent.getBirthDate() != null) student.setBirthDate(updatedStudent.getBirthDate());
            if (updatedStudent.getGender() != null) student.setGender(updatedStudent.getGender());
            if (updatedStudent.getCollege() != null) student.setCollege(updatedStudent.getCollege());
            if (updatedStudent.getCourse() != null) student.setCourse(updatedStudent.getCourse());
            return studentRepository.save(student);
        }
        return null;
    }
    
    public boolean deleteProfile(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "id"));
    }
    
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }
    
    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        return studentRepository.findByKeyword(keyword.trim());
    }
    
    public boolean changePassword(Long id, PasswordChangeRequest request) {
        Optional<Student> existing = studentRepository.findById(id);
        if (existing.isPresent()) {
            Student student = existing.get();
            if (student.getPassword().equals(request.getCurrentPassword())) {
                student.setPassword(request.getNewPassword());
                studentRepository.save(student);
                return true;
            }
        }
        return false;
    }
}