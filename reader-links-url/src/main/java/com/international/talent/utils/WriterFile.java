package com.international.talent.utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.international.talent.vo.Result;

public class WriterFile
{
    
    private String fileName;
    
    public WriterFile(String fileName)
    {
        this.fileName = fileName;
    }
    
    public void writeLog(List<Result> list)
    {
        System.out.println("AGRUP. LINKS : " + list.size());
        Path pathFile = Paths.get(fileName);
        File file = pathFile.toFile();

        try
        {
            Files.deleteIfExists(pathFile);
        }
        catch (IOException e2)
        {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        
        try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
            list.stream().forEach(e -> {
                try
                {
                    fileOutputStream.write((e.toString()).getBytes());
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            });
            fileOutputStream.close();
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}