package echipa_8.centenargo_core.wrappers;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class Route {
    private Integer uuid;
    private String name;
    private ArrayList<Landmark> landmarks;
    private Integer currentLandmarkIndex;
    private Boolean isCompleted;

    public Route() {
    }
    public Route(Integer uuid, String name, ArrayList<Landmark> landmarks, Integer currentLandmarkIndex, Boolean isCompleted) {
        this.uuid = uuid;
        this.name = name;
        this.landmarks = landmarks;
        this.currentLandmarkIndex = currentLandmarkIndex;
        this.isCompleted = isCompleted;
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

    /**
     *
     * @return true if route ended
     *         false is not finished
     */
    public Boolean setNextLandmark(){
        if(currentLandmarkIndex + 1 < landmarks.size()){
            currentLandmarkIndex++;
            isCompleted = false;
            return false;
        } else {
            isCompleted = true;
        }

        return true;
    }
    public Landmark getCurrentLandmark(){
        if(landmarks == null){
            return null;
        }

        if(currentLandmarkIndex < 0 || currentLandmarkIndex > landmarks.size()){
            return null;
        }

        return landmarks.get(currentLandmarkIndex);

    }
    public ArrayList<Landmark> getAvailableLandmarks(){
        ArrayList<Landmark> available = new ArrayList<>();
        for (int i = 0; i <= currentLandmarkIndex ; i++) {
            available.add(landmarks.get(i));
        }
        return available;
    }

    public Boolean checkIfCompleted() {
        return isCompleted;
    }

    public static Route generateMockup(){
        Random random = new Random();
        Integer id = random.nextInt() % 4983;
        String title = "Ruta numarul" + String.valueOf(id);
        Boolean isComp = random.nextBoolean();

        return new Route(id, title, new ArrayList<Landmark>(), 0, isComp);
    }
}
