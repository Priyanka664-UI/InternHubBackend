package SmartInternshipApp.InternHubBackend.service;

import SmartInternshipApp.InternHubBackend.entity.Internship;
import SmartInternshipApp.InternHubBackend.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DataInitService implements CommandLineRunner {
    
    @Autowired
    private InternshipRepository internshipRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (internshipRepository.count() == 0) {
            initializeInternships();
        }
    }
    
    private void initializeInternships() {
        Internship internship1 = new Internship();
        internship1.setTitle("Software Developer Intern");
        internship1.setCompany("Tech Corp");
        internship1.setDescription("Develop web applications using Java and Spring Boot");
        internship1.setLocation("Bangalore");
        internship1.setDuration("3 months");
        internship1.setStipend("15000");
        internship1.setSkillsRequired("Java, Spring Boot, MySQL");
        
        Internship internship2 = new Internship();
        internship2.setTitle("Data Science Intern");
        internship2.setCompany("Analytics Inc");
        internship2.setDescription("Work on machine learning projects");
        internship2.setLocation("Mumbai");
        internship2.setDuration("6 months");
        internship2.setStipend("20000");
        internship2.setSkillsRequired("Python, Machine Learning, SQL");
        
        Internship internship3 = new Internship();
        internship3.setTitle("Frontend Developer Intern");
        internship3.setCompany("Web Solutions");
        internship3.setDescription("Build responsive web interfaces using React");
        internship3.setLocation("Delhi");
        internship3.setDuration("4 months");
        internship3.setStipend("12000");
        internship3.setSkillsRequired("React, JavaScript, CSS");
        
        Internship internship4 = new Internship();
        internship4.setTitle("Mobile App Developer Intern");
        internship4.setCompany("Mobile Tech");
        internship4.setDescription("Develop Android applications using Java/Kotlin");
        internship4.setLocation("Pune");
        internship4.setDuration("5 months");
        internship4.setStipend("18000");
        internship4.setSkillsRequired("Android, Java, Kotlin");
        
        Internship internship5 = new Internship();
        internship5.setTitle("DevOps Intern");
        internship5.setCompany("Cloud Systems");
        internship5.setDescription("Learn cloud deployment and CI/CD pipelines");
        internship5.setLocation("Hyderabad");
        internship5.setDuration("3 months");
        internship5.setStipend("16000");
        internship5.setSkillsRequired("AWS, Docker, Jenkins");
        
        internshipRepository.save(internship1);
        internshipRepository.save(internship2);
        internshipRepository.save(internship3);
        internshipRepository.save(internship4);
        internshipRepository.save(internship5);
    }
}