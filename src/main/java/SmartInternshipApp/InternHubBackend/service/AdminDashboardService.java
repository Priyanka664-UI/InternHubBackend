package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.*;
import SmartInternshipApp.InternHubBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class AdminDashboardService {
    
    @Autowired private StudentRepository studentRepository;
    @Autowired private InternshipRepository internshipRepository;
    @Autowired private InternshipApplicationRepository applicationRepository;
    @Autowired private CertificateRepository certificateRepository;
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Basic counts
        stats.put("totalStudents", studentRepository.count());
        stats.put("totalInternships", internshipRepository.count());
        stats.put("totalApplications", applicationRepository.count());
        stats.put("totalCertificates", certificateRepository.countTotalCertificates());
        
        // Application status breakdown
        stats.put("pendingApplications", applicationRepository.countByStatus(InternshipApplication.ApplicationStatus.PENDING));
        stats.put("acceptedApplications", applicationRepository.countByStatus(InternshipApplication.ApplicationStatus.ACCEPTED));
        stats.put("completedApplications", applicationRepository.countByStatus(InternshipApplication.ApplicationStatus.COMPLETED));
        
        // Performance metrics
        stats.put("averagePerformanceRating", certificateRepository.getAveragePerformanceRating());
        
        return stats;
    }
    
    public List<Map<String, Object>> getCollegeWiseStats() {
        List<Student> students = studentRepository.findAll();
        Map<String, Integer> collegeCount = new HashMap<>();
        
        for (Student student : students) {
            collegeCount.merge(student.getCollege(), 1, Integer::sum);
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : collegeCount.entrySet()) {
            Map<String, Object> collegeData = new HashMap<>();
            collegeData.put("college", entry.getKey());
            collegeData.put("studentCount", entry.getValue());
            result.add(collegeData);
        }
        
        return result;
    }
}