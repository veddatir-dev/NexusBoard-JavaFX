package com.nexus.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ImageGenService {
    private static final String API_KEY = "f44be770-e77a-46f8-8e88-9bb2501588e9"; // Replace with your DeepAI key

    public static String generateImage(String textPrompt) {
        try {
            URL url = new URI("https://api.deepai.org/api/text2img").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("api-key", API_KEY);
            conn.setDoOutput(true);

            String data = "text=" + textPrompt;
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            // The response contains “output_url”: https://...
            // Quick extraction of the URL string
            String res = response.toString();
            return res.substring(res.indexOf("https://"), res.lastIndexOf("\""));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
