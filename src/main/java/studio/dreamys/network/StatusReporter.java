package studio.dreamys.network;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StatusReporter {

    public static void sendStatusUpdate(String uuid, String status, double tps, long ram, String macro) {
        try {
            URL url = new URL("http://localhost:3000/api/update");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = String.format(
                    "{\"uuid\":\"%s\", \"status\":\"%s\", \"tps\":%.2f, \"ram\":%d, \"macro\":\"%s\"}",
                    uuid, status, tps, ram, macro
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            conn.getResponseCode(); // send request
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
