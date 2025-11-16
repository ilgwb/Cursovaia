package com.example.cursovaia3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.source.tree.TryTree;
import javafx.print.PageRange;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FileHandler<T extends BaseEntity>{
    private final Path PATH;
    private final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public FileHandler(String path) throws IOException {
        this.PATH = Path.of(path);
        if (Files.notExists(PATH)){
            Files.createFile(PATH);
        }

    }

    public List<T> Read(){
        try(BufferedReader reader = Files.newBufferedReader(PATH)){
            return MAPPER.readValue(reader, new TypeReference<List<T>>() {
            });
        }
        catch(Exception e){
            return new ArrayList<T>();
        }
    }
    public void Write(T toAdd){
        try(BufferedWriter writer = Files.newBufferedWriter(PATH)) {
            List<T> allItems = Read();
            toAdd.setNumber(GetLatestId(allItems) + 1);
            allItems.add(toAdd);
            String json = MAPPER.writeValueAsString(allItems);
            writer.write(json);
        }

        catch (Exception e){
        }
    }
    public int GetLatestId(List<T> allItems) throws IOException {


        if (allItems.isEmpty()) {
            return 0;
        }

        return allItems.stream().mapToInt(BaseEntity::getNumber).max().getAsInt();
    }
}
