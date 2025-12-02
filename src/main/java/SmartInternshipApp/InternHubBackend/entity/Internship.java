package SmartInternshipApp.InternHubBackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "internships")
public class Internship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String company;
    
    @Column(length = 1000)
    private String description;
    
    private String location;
    private String duration;
    private String stipend;
    
    @Column(name = "skills_required")
    private String skillsRequired;
    
    @Column(name = "state_id")
    private Long stateId;
    
    @Column(name = "city_id")
    private Long cityId;
    
    @Column(name = "work_type")
    private String workType; // Remote, On-site, Hybrid
    
    @Column(name = "company_id")
    private Long companyId;
    
    // Constructors
    public Internship() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    
    public String getStipend() { return stipend; }
    public void setStipend(String stipend) { this.stipend = stipend; }
    
    public String getSkillsRequired() { return skillsRequired; }
    public void setSkillsRequired(String skillsRequired) { this.skillsRequired = skillsRequired; }
    
    public Long getStateId() { return stateId; }
    public void setStateId(Long stateId) { this.stateId = stateId; }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    
    public String getWorkType() { return workType; }
    public void setWorkType(String workType) { this.workType = workType; }
    
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}