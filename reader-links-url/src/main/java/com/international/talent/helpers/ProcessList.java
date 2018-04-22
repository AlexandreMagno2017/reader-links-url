package com.international.talent.helpers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.international.talent.vo.HtmlLink;
import com.international.talent.worker.ExtracLinktWorker;

/**
 * @author Magno
 *
 */
public class ProcessList
{
    private String link;

    final ExecutorService pool = Executors.newFixedThreadPool(40);
    final ExecutorCompletionService<List<HtmlLink>> completionService = new ExecutorCompletionService<>(pool);

    public ProcessList(String link)
    {
        this.link = link;
    }

    public List<List<HtmlLink>> linksList() throws MalformedURLException, IOException
    {
        Delegate delegate = new Delegate();
        List<HtmlLink> list = delegate.processLink(new URL(link));
        print("The page[" + link + "] has another " + list.size() + " links.");
        List<List<HtmlLink>> collect = executeCallable(list);
        collect.add(list);
        return collect;
    }

    private List<List<HtmlLink>> executeCallable(List<HtmlLink> list)
    {
        List<List<HtmlLink>> collect = new ArrayList<List<HtmlLink>>();

        try
        {
            List<Future<List<HtmlLink>>> contentsFutures = new ArrayList<>(list.size());
            for (final HtmlLink htmlLink : list)
            {
                final Future<List<HtmlLink>> contentFuture = completionService.submit(new ExtracLinktWorker(htmlLink.getLink()));
                contentsFutures.add(contentFuture);
            }

            while (contentsFutures.size() > 0)
            {
                try
                {
                    Future<List<HtmlLink>> ft = completionService.take();
                    List<HtmlLink> links = ft.get();
                    if (links != null)
                    {
                        System.err.println("Has returned : " + links.size());
                        collect.add(links);
                        contentsFutures.remove(ft);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            System.out.println("Has finished");
            pool.shutdownNow();
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return collect;
    }

    private void print(String input)
    {
        System.out.println(input);
    }

}