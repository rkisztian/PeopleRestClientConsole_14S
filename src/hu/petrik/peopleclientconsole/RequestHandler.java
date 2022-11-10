package hu.petrik.peopleclientconsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class RequestHandler {

    private RequestHandler(){

    }

    public static Response get(String url) throws IOException {
        URL urlOBJ = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlOBJ.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestProperty("Accept", "application/json");

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        /*for(String line = br.readLine(); line != null; line = br.readLine());*/

        String line = br.readLine();
        while (br != null){
            builder.append(line).append(System.lineSeparator());
            line = br.readLine();
        }
        br.close();
        is.close();
        String content = builder.toString().trim();
        return new Response(responseCode, content);

    }

}
