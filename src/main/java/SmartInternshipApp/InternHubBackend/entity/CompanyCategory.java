package SmartInternshipApp.InternHubBackend.entity;

public enum CompanyCategory {
    SOFTWARE_DEVELOPMENT("Software Development"),
    AI_ML("Artificial Intelligence / Machine Learning"),
    DATA_SCIENCE("Data Science & Big Data Analytics"),
    CLOUD_COMPUTING("Cloud Computing"),
    CYBERSECURITY("Cybersecurity"),
    EMBEDDED_IOT("Embedded Systems & IoT"),
    ELECTRONICS_SEMICONDUCTOR("Electronics & Semiconductor"),
    POWER_ELECTRICAL("Power & Electrical Systems"),
    ROBOTICS_AUTOMATION("Robotics & Automation"),
    AUTOMOTIVE_MANUFACTURING("Automotive & Manufacturing"),
    CONSTRUCTION_INFRASTRUCTURE("Construction & Infrastructure"),
    STRUCTURAL_CIVIL("Structural & Civil Engineering"),
    TELECOMMUNICATIONS("Telecommunications & Networking");

    private final String displayName;

    CompanyCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}