package studio.dreamys.network;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StatusReporter {
    public static void sendStatusUpdate(String uuid, String status, double tps, long ram, String macro) {
        try {
            URL url = new URL("http://localhost:3000/api/update");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String json = String.format(
                    "{\"uuid\":\"%s\",\"status\":\"%s\",\"tps\":%.2f,\"ram\":%d,\"macro\":\"%s\"}",
                    uuid, status, tps, ram, macro
            );

            OutputStream os = connection.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            connection.getResponseCode(); // Forces the request to go through
            connection.disconnect();
        } catch (Exception e) {
            System.err.println("[HyAuto] Failed to send status update: " + e.getMessage());
        }
    }
}
