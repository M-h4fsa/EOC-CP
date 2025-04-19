package com.echoesofcommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

public class JsonLoader {
    public List<Leader> loadLeaders(String resourcePath) {
        try (Reader reader = new InputStreamReader(JsonLoader.class.getClassLoader().getResourceAsStream(resourcePath))) {
            if (reader == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            Gson gson = new Gson();
            Type leaderListType = new TypeToken<List<Leader>>(){}.getType();
            List<Leader> leaders = gson.fromJson(reader, leaderListType);
            if (leaders == null || leaders.isEmpty()) {
                throw new IllegalStateException("No leaders found in the JSON file.");
            }
            return leaders;
        } catch (Exception e) {
            System.err.println("Error loading " + resourcePath + ": " + e.getMessage());
            System.exit(1);
            return null;
        }
    }
}