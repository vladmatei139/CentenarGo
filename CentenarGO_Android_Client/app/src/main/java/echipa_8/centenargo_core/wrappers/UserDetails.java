package echipa_8.centenargo_core.wrappers;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class UserDetails {
    private Integer uuid;
    private String firstName;
    private String lastName;
    private Integer currentRouteId;
    private Integer currentRouteLandmarkID;

    public UserDetails() {
    }
    public UserDetails(Integer uuid, String firstName, String lastName, Integer currentRouteId, Integer currentRouteLandmarkID) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentRouteId = currentRouteId;
        this.currentRouteLandmarkID = currentRouteLandmarkID;
    }

    public Integer getUuid() {
        return uuid;
    }
    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getCurrentRouteId() {
        return currentRouteId;
    }
    public void setCurrentRouteId(Integer currentRouteId) {
        this.currentRouteId = currentRouteId;
    }

    public Integer getCurrentRouteLandmarkID() {
        return currentRouteLandmarkID;
    }
    public void setCurrentRouteLandmarkID(Integer currentRouteLandmarkID) {
        this.currentRouteLandmarkID = currentRouteLandmarkID;
    }
}
