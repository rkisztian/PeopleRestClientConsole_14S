package hu.petrik.peopleclientconsole;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

public final class RequestHandler {

    private RequestHandler(){

    }

    public static Response get(String url) throws IOException {
        HttpURLConnection connection = setUpConnection(url);

        connection.setRequestMethod("GET");

        return getResponse(connection);

    }

    public static Response post(String url, String data) throws IOException {
        HttpURLConnection connection = setUpConnection(url);

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type","application/json");
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
        writer.write(data);
        writer.flush();
        writer.close();
        os.close();

        return getResponse(connection);
    }

    private static Response getResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        InputStream is = null;
        if (responseCode < 400){
            is = connection.getInputStream();
        }else{
            is = connection.getErrorStream();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            builder.append(line).append(System.lineSeparator());
            line = br.readLine();
        }
        br.close();
        is.close();
        String content = builder.toString().trim();
        return new Response(responseCode, content);
    }

    private static HttpURLConnection setUpConnection(String url) throws IOException {
        URL urlOBJ = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlOBJ.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestProperty("Accept", "application/json");
        return connection;
    }

}
