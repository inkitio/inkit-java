package com.inkit.sdk.net;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.inkit.sdk.exceptions.InkitException;


public class ApiResource {

    public enum RequestMethod {
        GET,
        POST,
        DELETE
    }

    public static final Gson GSON = createGson();

    public static String request(RequestWrapper rw) throws InkitException {

        HttpClient client = HttpWrapper.getHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder()
            .method(rw.method.name(), rw.exportBody())
            .uri(URI.create(rw.url))
            .headers(rw.getHeaderStrings())
            .build();

        String responseBody = null;
        HttpResponse<String> response = null;

        if (rw.retries > 0) {
            for (int attempts = 0; attempts <= rw.retries; attempts++) {

                try {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    responseBody = response.body();

                    if (response.statusCode() < 200 || response.statusCode() >= 300) {
                        Thread.sleep(1000);
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                // throw exception
            }
        } else {
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                responseBody = response.body();
            } catch (Exception e) {
                System.out.println(e);
            }
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                //throw InkitException(response.toString());
            }
        }

        return responseBody;
  }

  public static <T extends ApiResource> T deserialize (String json, Class<T> classType) {
    
    T resource = null;

    try {
        resource = GSON.fromJson(json, classType);
    } catch (Exception e) {
        System.out.println(e);
    }
        return resource;
    }

    private static Gson createGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }
    
}
