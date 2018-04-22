package com.international.talent.vo;
public class Result
{
    private String link;
    private Integer total;

    public Result(String link, Integer total)
    {
        this.link = link;
        this.total = total;
    }

    /**
     * @return the link
     */
    public String getLink()
    {
        return link;
    }

    /**
     * @return the total
     */
    public Integer getTotal()
    {
        return total;
    }

    @Override
    public String toString()
    {
        return total + ", " + link  + "\n";
    }
}
