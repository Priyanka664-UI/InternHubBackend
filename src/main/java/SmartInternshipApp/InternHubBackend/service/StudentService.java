package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Student;
import SmartInternshipApp.InternHubBackend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
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
            student.setName(updatedStudent.getName());
            student.setCollege(updatedStudent.getCollege());
            student.setCourse(updatedStudent.getCourse());
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
}