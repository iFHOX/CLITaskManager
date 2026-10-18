package io.github.ifhox.taskmanager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class FileHandler {
    private static final Path FILE = Path.of(System.getProperty("user.home"), "tasks.json");
    private static final Path FILETMP = FILE.resolveSibling("tasks.json.tmp");
    private static final Path FILEBAK = FILE.resolveSibling("tasks.json.bak");

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        @Override
        public JsonElement serialize(LocalDate src, Type typeOfT, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return LocalDate.parse(json.getAsString());
            } catch (DateTimeParseException | IllegalStateException e) {
                throw new JsonParseException("Invalid date: " + json, e);
            }
        }
    }

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    public void saveTasks(ArrayList<Task> tasks) {
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(FILETMP, StandardCharsets.UTF_8)) {
                gson.toJson(tasks, writer);
            }
            Files.move(FILETMP, FILE, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException | JsonParseException e) {
            System.out.println("Sorry, the save failed. " + e.getMessage());
        }
    }

    public ArrayList<Task> loadTasks() {
        ArrayList<Task> list = new ArrayList<Task>();
        try {
            try (BufferedReader reader = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
                Type array = new TypeToken<ArrayList<Task>>() {
                }.getType();
                list = gson.fromJson(reader, array);
                if (list == null) list = new ArrayList<>();
            } catch (NoSuchFileException e) {
                System.out.println("No saved tasks found. Starting fresh.");
            } catch (JsonParseException e) {
                Files.move(FILE, FILEBAK, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("The file got corrupted, but it got backed up before breaking at " + FILEBAK + ". " + e.getMessage() + ". Fix the file and rename it to tasks.json to restore the tasks. Starting fresh");
            } catch (IOException e) {
                System.out.println("Couldn't read tasks. " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Couldn't back up the corrupt file. " + e.getMessage());
        }
        return list;
    }
}

