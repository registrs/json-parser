import com.google.gson.Gson;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class JSONParser {
    //int userId;
    //int id;
    private String title;
    //String body;

    public JSONParser[] returnParsedGson(String s) {
        Gson gson = new Gson();
        //if only one object
        //SomeShitFromHttp crap = gson.fromJson(s, SomeShitFromHttp.class);
        return gson.fromJson(s, JSONParser[].class);
    }

    static String getJSONByHttp() throws IOException {

        String USER_AGENT = "Mozilla/5.0";
        URL obj = new URL("https", "jsonplaceholder.typicode.com", 443, "/posts");
        //URL obj = new URL("https", "jsonplaceholder.typicode.com", 443, "/posts/1");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            //StringBuffer response = new StringBuffer();
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();

        } else {
            System.out.println("GET request failed");
            return "";
        }
    }

    static void parseResponseWithGson() throws IOException {

        JSONParser[] responseToJson = new JSONParser().returnParsedGson(getJSONByHttp());

        int arrSize = responseToJson.length;

        Arrays.stream(responseToJson).forEach(s -> System.out.println("object list title: " + s.title));
        List<JSONParser> respToList = Arrays.asList(responseToJson);
        String[] stringList = new String[arrSize];
        List<String> titleList = respToList.stream().map(JSONParser::getTitle).collect(Collectors.toList());


        //titleList.forEach(title -> System.out.println("title: " + title));
        //stringList = titleList.toArray(stringList);
        //System.out.println(">>>>>>" + stringList[1] + "<<<<<<" + stringList[2]);

        //JsonObject[] jobj = new Gson().fromJson(response, JsonObject[].class);

        //String result = jobj[1].get("title").toString();
        //String[] anotherArr = new String[responseToJson.length];
        //for (int i = 0; i < responseToJson.length; i++) {
        //    anotherArr[i] = jobj[i].get("title (basic): ").toString();
            //anotherArr = Arrays.asList(jobj).forEach(c -> c.get("title"))
        //}
    }

    //with javax
    static void responseWithJavax() throws IOException {
        URL url = new URL("https", "jsonplaceholder.typicode.com", 443, "/posts");
        try (InputStream is = url.openStream();
             JsonReader jsonReader = Json.createReader(is)) {

            JsonArray array = jsonReader.readArray();

            // just examples on how to get/print values
            System.out.print("First title:" + array.getJsonObject(0).get("title"));
            String[] stringArr = toStringArray(array);
            for (String aStringArr : stringArr) {
                System.out.println("Title: " + aStringArr);
            }
        }
    }


    // put parsed titles in array
    public static String[] toStringArray(JsonArray array) {
        if (array == null)
            return null;

        String[] arr = new String[array.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = array.getJsonObject(i).get("title").toString();
        }
        return arr;
    }

    String getTitle() {
        return title;
    }
}
