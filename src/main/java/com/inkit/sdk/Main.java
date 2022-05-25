package com.inkit.sdk;

import com.inkit.sdk.models.Render;
import java.util.Map;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        

        Map<String, Object> params = new HashMap<>();
        params.put("name", "java sdk test");
        params.put("html", "<html>hello there {{fname}}</html>");
        params.put("height", 11);
        params.put("width", 8.5);
        params.put("unit", "in");

        Map<String, Object> mergeParams = new HashMap<>();
        mergeParams.put("fname", "Bob");
        params.put("merge_parameters", mergeParams);

        try {

            Inkit.apiKey = "xxx";
            Render render = Render.create(params);
            Render.retrievePdfAndSaveToFile(render.id, "your_pdf.pdf");
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
}
