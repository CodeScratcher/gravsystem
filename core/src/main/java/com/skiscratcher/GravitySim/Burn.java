/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.skiscratcher.GravitySim;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author afisher
 */

@AllArgsConstructor
@Getter
@Setter
public class Burn {
    private int id = 0;
    private Vector2D velocity;
    private double time;
    
    public static List<Burn> fromCSV(String path, List<Object> objects) throws FileNotFoundException, IOException {
        Reader in = new FileReader(path);
        List<CSVRecord> records = CSVFormat.DEFAULT.parse(in).getRecords();
        List<Burn> burns = new ArrayList<>();
        for (CSVRecord record : records) {
            String idText = record.get(0);
            String timeText = record.get(1);
            String tangent = record.get(2);
            String normal = record.get(3);
            
            burns.add(new Burn(Integer.parseInt(idText), new Vector2D(Double.parseDouble(tangent), Double.parseDouble(normal)), Double.parseDouble(timeText)));
        }
        
        return burns;
    }
   
}
