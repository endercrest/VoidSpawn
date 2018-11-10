package com.endercrest.voidspawn.api;

import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class SupportAPIRequest {

    private static final String API_ENDPOINT = "https://support-api.endercrest.com";

    private String email;
    private String message;
    private List<File> files;

    public SupportAPIRequest(String email, String message, List<File> files) {
        this.email = email;
        this.message = message;
        this.files = files;
    }

    public void execute(VoidSpawn plugin, Consumer<SupportAPIResponse> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            HttpClient httpClient = HttpClientBuilder.create().build();

            Gson gson = new Gson();
            JsonObject dataObject = new JsonObject();
            JsonArray filesObject = new JsonArray();
            dataObject.add("files", filesObject);
            dataObject.addProperty("plugin", "VoidSpawn");
            dataObject.addProperty("version", plugin.getDescription().getVersion());
            dataObject.addProperty("email", email);
            dataObject.addProperty("message", message);

            for(File file: files){
                JsonObject fileObject = new JsonObject();
                fileObject.addProperty("filename", file.getName());
                fileObject.addProperty("content", FileUtil.readLineByLine(file.toPath()));
                filesObject.add(fileObject);
            }

            boolean success = true;
            String message = "Successfully sent support request";

            try     {
                HttpPost request = new HttpPost("http://localhost:4040/plugin/VoidSpawn");
                StringEntity params = new StringEntity(gson.toJson(dataObject));
                request.addHeader("content-type", "application/json");
                request.setEntity(params);

                httpClient.execute(request);
            } catch (HttpResponseException e){
                success = false;
                message = String.format("Support Request Rejected: %s", e.getMessage());
            }catch(IOException e){
                success = false;
                message = "Sorry, can't connect to support services";
            }

            final String finalMessage = message;
            final boolean finalSuccess = success;
            Bukkit.getScheduler().runTask(plugin, () ->
                    callback.accept(new SupportAPIResponse(finalSuccess, finalMessage)));
        });
    }
}
