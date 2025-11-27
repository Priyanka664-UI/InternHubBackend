package SmartInternshipApp.InternHubBackend.entity;

public enum CompanyCategory {
    TECHNOLOGY("Technology"),
    FINANCE("Finance"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    MANUFACTURING("Manufacturing"),
    RETAIL("Retail"),
    CONSULTING("Consulting"),
    MEDIA("Media & Entertainment"),
    GOVERNMENT("Government"),
    NON_PROFIT("Non-Profit"),
    STARTUP("Startup"),
    MULTINATIONAL("Multinational Corporation"),
    OTHER("Other");

    private final String displayName;

    CompanyCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}