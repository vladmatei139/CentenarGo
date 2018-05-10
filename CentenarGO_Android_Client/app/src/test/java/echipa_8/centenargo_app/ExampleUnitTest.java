package echipa_8.centenargo_app;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.Test;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void login_successful() throws Exception{
        final String email = "emi777@yahoo.com";
        final String password = "qq11qq11";

        URL url = new URL("http://192.168.43.169:8080/api/login");

        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.connect();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password", password);

        DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(jsonObject.toString());
        dos.flush();
        dos.close();

        Integer replyCode = httpURLConnection.getResponseCode();

        Assert.assertEquals((int)replyCode, 200);
    }

    @Test
    public void already_registered() throws Exception {
        URL url = new URL("http://127.0.0.1:8080/api/signup");

        final String email = "emi777@yahoo.com";
        final String password = "qq11qq11";

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.connect();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("username", "test");
        jsonObject.put("password", password);
        jsonObject.put("firstname", "test");
        jsonObject.put("lastname", "test");


        DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(jsonObject.toString());
        dos.flush();
        dos.close();

        Integer replyCode = httpURLConnection.getResponseCode();
        String replyMessage = httpURLConnection.getResponseMessage();

        Assert.assertEquals((int) replyCode, 400);
    }

    @Test
    public void validate_location() throws Exception{

        final LatLng ZERO_KM_BUCHAREST = new LatLng(44.4327025, 26.104049400000008);
        final LatLng FMI_UNIBUC = new LatLng(44.435459, 26.099698);
        float[] dist = new float[1];


        Location.distanceBetween(ZERO_KM_BUCHAREST.latitude, ZERO_KM_BUCHAREST.longitude,
                                        FMI_UNIBUC.latitude, FMI_UNIBUC.longitude, dist);

        Assert.assertTrue(dist[0]/1000 < 1);


    }


}