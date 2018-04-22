package com.international.talent.vo;

public class HtmlLink
{
    private String link;
    private String linkText;
    /**
     * @return the link
     */
    public String getLink()
    {
        return link;
    }
    /**
     * @param link the link to set
     */
    public void setLink(String link)
    {
        this.link = link;
    }
    /**
     * @return the linkText
     */
    public String getLinkText()
    {
        return linkText;
    }
    /**
     * @param linkText the linkText to set
     */
    public void setLinkText(String linkText)
    {
        this.linkText = linkText;
    }
    
    @Override
    public String toString()
    {
        return "('" + link + "')";
    }
}
