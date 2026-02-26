package SmartInternshipApp.InternHubBackend.dto;

import java.time.LocalDateTime;

public class NewsDTO {
    private Long id;
    private String title;
    private String content;
    private Long companyId;
    private String companyName;
    private LocalDateTime createdAt;

    public NewsDTO() {}

    public NewsDTO(Long id, String title, String content, Long companyId, String companyName, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.companyId = companyId;
        this.companyName = companyName;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
