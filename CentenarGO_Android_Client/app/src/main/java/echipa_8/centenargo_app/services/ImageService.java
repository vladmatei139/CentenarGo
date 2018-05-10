package echipa_8.centenargo_app.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import echipa_8.centenargo_app.utilities.Image;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

public class ImageService {

    private RequestQueue requestQueue;
    private Boolean liked;

    public ImageService(Context context, Integer imageId) {
        requestQueue = Volley.newRequestQueue(context);
        setLiked(imageId);
    }

    private void genericRequest(String url, Response.Listener<JSONObject> callback, String errorString) {
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.putOpt("token", SharedPreferencesUtility.getToken());
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    requestObject,
                    callback,
                    error -> Log.println(Log.ERROR, "ImageServiceError", errorString + ": " + error.getMessage())
            );
            requestQueue.add(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addLike(Integer imageId, Response.Listener<JSONObject> callback) {
        genericRequest(
                "http://10.0.2.2:8080/api/gallery/" + imageId + "/addLike/",
                callback,
                "Volley request for /api/gallery/image/addLike failed");
    }

    private void removeLike(Integer imageId, Response.Listener<JSONObject> callback) {
        genericRequest(
                "http://10.0.2.2:8080/api/gallery/" + imageId + "/removeLike/",
                callback,
                "Volley request for /api/gallery/image/removeLike failed");
    }

    private void checkLike(Integer imageId, Response.Listener<JSONObject> callback) {
        genericRequest(
                "http://10.0.2.2:8080/api/gallery/" + imageId + "/checkLiked/",
                callback,
                "Volley request for /api/gallery/image/checkLike failed");
    }

    public void like(Integer imageId, Response.Listener<JSONObject> callback) {
        if (liked == null) {
            return;
        }
        if (liked) {
            removeLike(imageId, callback);
        }
        else {
            addLike(imageId, callback);
        }
        liked = !liked;
    }

    public void getLikes(Integer imageId, Response.Listener<JSONObject> callback) {
        genericRequest(
                "http://10.0.2.2:8080/api/gallery/" + imageId + "/likes/",
                callback,
                "Volley request for /api/gallery/image/likes failed");
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Integer imageId) {
        this.requestQueue.cancelAll(r -> true);
        this.liked = null;
        checkLike(imageId, obj -> liked = obj.optBoolean("correct"));
    }
}
