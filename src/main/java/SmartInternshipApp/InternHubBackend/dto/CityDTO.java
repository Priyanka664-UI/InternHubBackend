package SmartInternshipApp.InternHubBackend.dto;

public class CityDTO {
    private Long id;
    private String name;
    private Long stateId;
    private String stateName;
    
    public CityDTO() {}
    
    public CityDTO(Long id, String name, Long stateId, String stateName) {
        this.id = id;
        this.name = name;
        this.stateId = stateId;
        this.stateName = stateName;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Long getStateId() { return stateId; }
    public void setStateId(Long stateId) { this.stateId = stateId; }
    
    public String getStateName() { return stateName; }
    public void setStateName(String stateName) { this.stateName = stateName; }
}