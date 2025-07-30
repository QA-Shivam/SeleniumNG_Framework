package utils;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SlackIntegration {

    private static final String SLACK_BOT_TOKEN = "";
    private static final String CHANNEL_ID = ""; // Your Slack channel ID
    private static final OkHttpClient client = new OkHttpClient();

    public static void sendSlackMessage(String messageText, String reportPath) {
        sendMessageToSlack(messageText);

        File file = new File(reportPath);
        if (!file.exists()) {
            System.err.println("❌ File not found at: " + reportPath);
            return;
        }

        String fileName = file.getName();
        long fileSize = file.length();

        try {
            JSONObject uploadURLResponse = getUploadURL(fileName, fileSize);

            if (!uploadURLResponse.getBoolean("ok")) {
                System.err.println("❌ Failed to get upload URL: " + uploadURLResponse);
                return;
            }

            String uploadUrl = uploadURLResponse.getString("upload_url");
            String fileId = uploadURLResponse.getString("file_id");

            boolean uploaded = uploadFileToPresignedUrl(uploadUrl, file);
            if (!uploaded) {
                System.err.println("❌ Upload to pre-signed URL failed");
                return;
            }

            boolean complete = completeUpload(fileId, fileName, CHANNEL_ID);
            if (complete) {
                System.out.println("✅ Slack file upload completed and posted to channel.");
            } else {
                System.err.println("❌ Slack upload finalization failed.");
            }

        } catch (Exception e) {
            System.err.println("❌ Upload failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void sendMessageToSlack(String messageText) {
        JSONObject json = new JSONObject()
                .put("channel", CHANNEL_ID)
                .put("text", messageText);

        Request request = new Request.Builder()
                .url("https://slack.com/api/chat.postMessage")
                .addHeader("Authorization", "Bearer " + SLACK_BOT_TOKEN)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json.toString(), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            JSONObject res = new JSONObject(response.body().string());
            if (res.getBoolean("ok")) {
                System.out.println("✅ Slack message sent.");
            } else {
                System.err.println("❌ Slack message failed: " + res);
            }
        } catch (IOException e) {
            System.err.println("❌ Error sending message: " + e.getMessage());
        }
    }

    private static JSONObject getUploadURL(String filename, long length) throws IOException {
        HttpUrl url = HttpUrl.parse("https://slack.com/api/files.getUploadURLExternal").newBuilder()
                .addQueryParameter("filename", filename)
                .addQueryParameter("length", String.valueOf(length))
                .addQueryParameter("pretty", "1")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + SLACK_BOT_TOKEN)
                .get()
                .build();

        System.out.println("➡️ Sending GET request to: " + url);

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            System.out.println("📥 Slack Response from getUploadURL:\n" + body);
            return new JSONObject(body);
        }
    }

    private static boolean uploadFileToPresignedUrl(String uploadUrl, File file) throws IOException {
        OkHttpClient client = new OkHttpClient();

        byte[] fileBytes = Files.readAllBytes(file.toPath());

        RequestBody body = RequestBody.create(fileBytes, MediaType.parse("text/html"));
        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(body)
                .build();

        System.out.println("⬆️ Uploading file to pre-signed URL...");
        System.out.println("🔹 Request Method: " + request.method());
        System.out.println("🔹 Request URL: " + request.url());
        System.out.println("🔹 Request Body Length: " + fileBytes.length + " bytes");

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "null";

            System.out.println("🔸 Response Code: " + response.code());
            System.out.println("🔸 Response Message: " + response.message());
            System.out.println("🔸 Response Body: " + responseBody);

            if (response.isSuccessful()) {
                System.out.println("✅ File successfully uploaded to pre-signed URL.");
                return true;
            } else {
                System.err.println("❌ Upload failed.");
                return false;
            }
        }
    }


    private static boolean completeUpload(String fileId, String filename, String channelId) throws IOException {
        JSONObject fileEntry = new JSONObject()
                .put("id", fileId)
                .put("title", filename);

        JSONObject payload = new JSONObject()
                .put("files", new JSONArray().put(fileEntry))
                .put("channel_id", channelId)
                .put("initial_comment", "📎 *Attached HTML Report:* `" + filename + "`");

        Request request = new Request.Builder()
                .url("https://slack.com/api/files.completeUploadExternal")
                .addHeader("Authorization", "Bearer " + SLACK_BOT_TOKEN)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(payload.toString(), MediaType.parse("application/json")))
                .build();

        System.out.println("➡️ Sending POST request to completeUploadExternal");
        System.out.println("📝 Request Payload:\n" + payload.toString(2));

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            System.out.println("📥 Slack Response from completeUploadExternal:\n" + body);

            JSONObject res = new JSONObject(body);
            if (!res.getBoolean("ok")) {
                System.err.println("❌ completeUploadExternal failed: " + res);
                return false;
            }
            System.out.println("✅ File finalized and visible in the channel.");
            return true;
        }
    }
}
