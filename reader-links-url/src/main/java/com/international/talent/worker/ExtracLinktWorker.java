package com.international.talent.worker;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

import com.international.talent.helpers.Delegate;
import com.international.talent.vo.HtmlLink;

public class ExtracLinktWorker implements Callable<List<HtmlLink>>
{
    private final String link;
    
    public ExtracLinktWorker(String link)
    {
        this.link = link;
    }
    
    @Override
    public List<HtmlLink> call() throws Exception {
        List<HtmlLink> links = new Delegate().processLink(new URL(link));
        System.out.println("The page[" + link + "] has another " + links.size() + " links.");
        return links;
    }
}
