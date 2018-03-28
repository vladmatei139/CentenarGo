package echipa_8.centenargo_core.wrappers;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class Landmark {
    private Integer uuid;
    private String name;
    private String content;
    private Route route;
    private Double longitude;
    private Double latitude;
    private Integer routeOrder;
    private String imgPath;

    public Landmark() {
    }
    public Landmark(Integer uuid, String name, String content, Route route, Double longitude, Double latitude, Integer routeOrder, String imgPath) {
        this.uuid = uuid;
        this.name = name;
        this.content = content;
        this.route = route;
        this.longitude = longitude;
        this.latitude = latitude;
        this.routeOrder = routeOrder;
        this.imgPath = imgPath;
    }

    public Integer getUuid() {
        return uuid;
    }
    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Route getRoute() {
        return route;
    }
    public void setRoute(Route route) {
        this.route = route;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Integer getRouteOrder() {
        return routeOrder;
    }
    public void setRouteOrder(Integer routeOrder) {
        this.routeOrder = routeOrder;
    }

    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
