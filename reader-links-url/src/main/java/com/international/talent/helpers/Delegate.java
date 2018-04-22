package com.international.talent.helpers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.international.talent.vo.HtmlLink;


public class Delegate
{
    public final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
    public final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";

    public List<HtmlLink> processLink(URL url) throws MalformedURLException, IOException
    {
        /*
         * System.out.println(url.getAuthority());// en.wikipedia.org
         * System.out.println(url.getFile());// /wiki/Europe
         * System.out.println(url.getProtocol()); // https
         */
        URLConnection conn = url.openConnection();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())))
        {
            String inputLine;

            List<HtmlLink> extractLinks = new ArrayList<HtmlLink>();
            while ((inputLine = in.readLine()) != null)
            {
                List<HtmlLink> extractLinks2 = extractLinks(inputLine, url);
                if (extractLinks2 == null || extractLinks2.isEmpty())
                    continue;

                extractLinks.addAll(extractLinks2.stream().filter(f -> {
                    return f.getLink().trim().length() > 0 && !f.getLink().contains("File:");
                }).collect(Collectors.toList()));
            }

            return extractLinks;
        }
    }

    private List<HtmlLink> extractLinks(String inputLine, URL url)
    {
        List<HtmlLink> links = new ArrayList<HtmlLink>();
        if (!inputLine.contains("href"))
            return links;

        Pattern patternTag, patternLink;
        Matcher matcherTag, matcherLink;

        patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
        patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);

        matcherTag = patternTag.matcher(inputLine);

        while (matcherTag.find())
        {
            String href = matcherTag.group(1).trim(); // href
            String linkText = matcherTag.group(2); // link text

            matcherLink = patternLink.matcher(href);

            while (matcherLink.find())
            {
                String link = matcherLink.group(1).replaceAll("[\\s|\\u00A0]+", "").trim(); // link
                if (!link.isEmpty())
                {
                    String l2 = formatLink(link, url);
                    HtmlLink obj = new HtmlLink();
                    obj.setLink(l2.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "").replaceAll("[^\\x00-\\x7F]", "").replaceAll("\\p{C}", "")
                                    .replaceAll("[\\s|\\u00A0]+", "").trim());
                    obj.setLinkText(linkText.trim());
                    links.add(obj);
                }
            }
        }
        return links;
    }

    public String formatLink(String link, URL url)
    {
        int type = 0;
        if (link.startsWith("\"//"))
        {
            type = 1;
        }
        else if (link.startsWith("\"/"))
        {
            type = 2;
        }
        else if (link.startsWith("\"#"))
        {
            type = 3;
        }
        String result = "";
        switch (type)
        {
            case 1:
                result = url.getProtocol().replaceAll("^\"|\"$", "").concat(":" + link.replaceAll("^\"|\"$", ""));
                break;
            case 2:
                result = url.getProtocol().replaceAll("^\"|\"$", "")
                                .concat("://" + url.getAuthority().replaceAll("^\"|\"$", "").concat(link.replaceAll("^\"|\"$", "")));
                break;
            case 3:
                result = url.getProtocol()
                                .replaceAll("^\"|\"$", "")
                                .concat("://" + url.getAuthority().replaceAll("^\"|\"$", "").concat(url.getFile().replaceAll("^\"|\"$", ""))
                                                                .concat(link.replaceAll("^\"|\"$", "")));
                break;
            default:
                break;
        }

        return result;
    }
}
