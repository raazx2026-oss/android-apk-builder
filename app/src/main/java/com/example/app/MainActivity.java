package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    // APNE TELEGRAM BOT DETAILS YAHA DAALEIN
    private static final String BOT_TOKEN = "YOUR_BOT_TOKEN_HERE"; 
    private static final String CHAT_ID = "YOUR_CHAT_ID_HERE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView text = new TextView(this);
        text.setText("Telegram Bot App 🚀\nSending notification...");
        text.setTextSize(22);
        setContentView(text);

        // App start hote hi Telegram par message bhejna
        sendTelegramMessage("✅ App has been opened successfully on the device!");
    }

    private void sendTelegramMessage(String messageText) {
        // Network calls background thread par hone chahiye
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // JSON payload ban banana
                String jsonPayload = "{\"chat_id\": \"" + CHAT_ID + "\", \"text\": \"" + messageText + "\"}";

                // Request bhejna
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                
                // UI thread par Toast dikhana
                runOnUiThread(() -> {
                    if (responseCode == 200) {
                        Toast.makeText(MainActivity.this, "Message sent to Telegram!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to send: " + responseCode, Toast.LENGTH_SHORT).show();
                    }
                });

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
}
