import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An example showing how to send HTTP GET and read the response from the server
 */
public class SendRequest {

    public static void main(String[] args) {
        SendRequest example = new SendRequest("104.248.47.74", 80);
        example.doExampleGet();
        example.post2RandomNumbers();



        //get 1

    }


    public void authenticate() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "trudehs@ntnu.no");
        jsonObject.put("phone", "90972254");
        sendPost("dkrest/auth", jsonObject);
    }


    public void post2RandomNumbers() {
        int a = (int) Math.round(Math.random() * 100);
        int b = (int) Math.round(Math.random() * 100);

        JSONObject json = new JSONObject();
        json.put("a", a);
        json.put("b", b);
        System.out.println("Posting this JSON data to server");
        System.out.println(json.toString());
        // TODO: change path to something correct
        sendPost("dkrest/test/post ", json);
    }

    private String BASE_URL; // Base URL (address) of the server

    /**
     * Create an HTTP GET example
     *
     * @param host Will send request to this host: IP address or domain
     * @param port Will use this port
     */
    public SendRequest(String host, int port) {
        BASE_URL = "http://" + host + ":" + port + "/";
    }

    /**
     * Send an HTTP GET to a specific path on the web server
     */
    public void doExampleGet() {
        // TODO: change path to something correct
        sendGet("dkrest/test/get2");
    }

    /**
     * Send HTTP GET
     *
     * @param path     Relative path in the API.
     */
    private void sendGet(String path) {
        try {
            String url = BASE_URL + path;
            URL urlObj = new URL(url);
            System.out.println("Sending HTTP GET to " + url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server reached");
                // Response was OK, read the body (data)
                InputStream stream = con.getInputStream();
                String responseBody = convertStreamToString(stream);
                stream.close();
                System.out.println("Response from the server:");
                System.out.println(responseBody);
                JSONParser(responseBody);
            } else {
                String responseDescription = con.getResponseMessage();
                System.out.println("Request failed, response code: " + responseCode + " (" + responseDescription + ")");
            }
        } catch (ProtocolException e) {
            System.out.println("Protocol not supported by the server");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void JSONParser(String stuff) {
        try {
            JSONObject jsonObject = new JSONObject(stuff);
            System.out.println(jsonObject.toString());
        }
        catch (JSONException e) {
            System.out.println("Got exception in JSON parsing: " + e.getMessage());
        }
    }

    /**
     * Read the whole content from an InputStream, return it as a string
     * @param is Inputstream to read the body from
     * @return The whole body as a string
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append('\n');
            }
        } catch (IOException ex) {
            System.out.println("Could not read the data from HTTP response: " + ex.getMessage());
        }
        return response.toString();
    }


    /**
     * Send HTTP POST
     *
     * @param path     Relative path in the API.
     * @param jsonData The data in JSON format that will be posted to the server
     */
    private void sendPost(String path, JSONObject jsonData) {
        try {
            String url = BASE_URL + path;
            URL urlObj = new URL(url);
            System.out.println("Sending HTTP POST to " + url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(jsonData.toString().getBytes());
            os.flush();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server reached");

                // Response was OK, read the body (data)
                InputStream stream = con.getInputStream();
                String responseBody = convertStreamToString(stream);
                stream.close();
                System.out.println("Response from the server:");
                System.out.println(responseBody);
            } else {
                String responseDescription = con.getResponseMessage();
                System.out.println("Request failed, response code: " + responseCode + " (" + responseDescription + ")");
            }
        } catch (ProtocolException e) {
            System.out.println("Protocol nto supported by the server");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
