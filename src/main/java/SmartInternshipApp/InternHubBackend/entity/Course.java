package SmartInternshipApp.InternHubBackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    private String description;
    
    @Column(name = "duration_months")
    private Integer durationMonths;
    
    @ManyToMany(mappedBy = "courses")
    @JsonBackReference
    private Set<CourseCategory> categories = new HashSet<>();
    
    public Course() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }
    
    public Set<CourseCategory> getCategories() { return categories; }
    public void setCategories(Set<CourseCategory> categories) { this.categories = categories; }
}
