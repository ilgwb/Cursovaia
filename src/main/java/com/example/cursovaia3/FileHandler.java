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

public class FileHandler<T extends BaseEntity> {

    private final Path PATH;
    private final Class<T> type;
    private final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public FileHandler(String path, Class<T> type) throws IOException {
        this.PATH = Path.of(path);
        this.type = type;
        if (Files.notExists(PATH)) Files.createFile(PATH);
    }

    public List<T> Read(){
        try (BufferedReader reader = Files.newBufferedReader(PATH)){
            return MAPPER.readValue(reader, MAPPER.getTypeFactory()
                    .constructCollectionType(List.class, type));
        } catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void Write(T toAdd){
        List<T> allItems = Read();
        try(BufferedWriter writer = Files.newBufferedWriter(PATH)) {
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
    public void Update(T toUpdate){
        List<T> allItems = Read();
        for (int i = 0; i < allItems.size(); i++){
            if (allItems.get(i).getNumber() == toUpdate.getNumber()){
                allItems.set(i,toUpdate);
                break;
            }
        }
        try(BufferedWriter writer = Files.newBufferedWriter(PATH)) {
            String json = MAPPER.writeValueAsString(allItems);
            writer.write(json);
        }
        catch (Exception e){
        }
    }
    public void Delete(int number){
        List<T> allItems = Read();
        for (int i = 0; i < allItems.size(); i++){
            if (allItems.get(i).getNumber() == number){
                allItems.remove(i);
                break;
            }
        }

        try(BufferedWriter writer = Files.newBufferedWriter(PATH)) {
            String json = MAPPER.writeValueAsString(allItems);
            writer.write(json);
        }
        catch (Exception e){
        }
    }
    public void Delete(List<Integer> number){
        System.out.println(number);
        List<T> allItems = Read();
        for (int i = allItems.size() - 1; i >= 0; i--){
            if (number.contains(allItems.get(i).getNumber())){
                allItems.remove(i);
            }
        }


        try(BufferedWriter writer = Files.newBufferedWriter(PATH)) {
            String json = MAPPER.writeValueAsString(allItems);
            writer.write(json);
        }
        catch (Exception e){
        }
    }
    public void Update(List<T> toUpdate){
        List<T> allItems = Read();
        for (int i = 0; i < allItems.size(); i++){
            var updatedEntite = toUpdate.indexOf(allItems.get(i));
            if (updatedEntite != -1){
                allItems.set(i,toUpdate.get(updatedEntite));
            }
        }
        try(BufferedWriter writer = Files.newBufferedWriter(PATH)) {
            String json = MAPPER.writeValueAsString(allItems);
            writer.write(json);
        }
        catch (Exception e){
        }
    }

}
