package com.international.talent.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.international.talent.helpers.ProcessList;
import com.international.talent.services.Service;
import com.international.talent.utils.WriterFile;
import com.international.talent.vo.HtmlLink;
import com.international.talent.vo.Result;

public class Application
{

    public static void main(String[] args) throws MalformedURLException, IOException
    {
        try
        {
            /*
             * List<Integer> asList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 1,
             * 2, 3, 4, 5, 6, 7, 8 ); List<List<Integer>> subSets =
             * ListUtils.partition(asList, 3);
             */
            System.out.println("Start Application...");

            System.out.println("Application using H2, the Java SQL database...");

            String fileName = "links.txt";

            System.out.println("File with result is called " + fileName + "...");

            String databaseUrl = "jdbc:h2:~/test";

            System.out.println("Database[" + databaseUrl + "]...");

            Service service = new Service(databaseUrl);
            service.prepareDatabase();

            String primaryLink = "https://en.wikipedia.org/wiki/Europe";
            ProcessList processList = new ProcessList(primaryLink);
            List<List<HtmlLink>> linksList = processList.linksList();
            int totalLinks = linksList.stream().mapToInt(m -> m.size()).sum();
            System.out.println("Total links from subpages : " + totalLinks);
            linksList.parallelStream().forEach(group -> {
                new Service(databaseUrl).saveLinks(group);
            });

            System.out.println("totalRegsBase   " + service.totalRegsBase());
            List<Result> groupedResult = service.getGroupedResult();

            WriterFile writer = new WriterFile(fileName);
            writer.writeLog(groupedResult);

            System.out.println("Start Application...");
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Finished Application...");
        }
    }
}
