package com.international.talent.services;

import java.util.List;

import com.international.talent.dao.LinkDAO;
import com.international.talent.vo.HtmlLink;
import com.international.talent.vo.Result;

public class Service
{
    private LinkDAO dao;

    public Service(String url)
    {
        this.dao = new LinkDAO(url);
    }

    public List<Result> getGroupedResult()
    {
        return this.dao.getGroupedResult();
    }
    public Long totalRegsBase()
    {
        return this.dao.totalRegsBase();
    }

    public void saveLinks(List<HtmlLink> listLinks)
    {
        this.dao.saveLinks(listLinks);
    }
    
    public void prepareDatabase()
    {
        this.dao.prepareDatabase();

    }
}
