package com.international.talent.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections4.ListUtils;

import com.international.talent.vo.HtmlLink;
import com.international.talent.vo.Result;

public class LinkDAO extends BasicDAO
{
    public LinkDAO(String url)
    {
        super(url);
    }

    private static final int MAX_SIZE_LIST = 500;

    public void saveLinks(List<HtmlLink> listLinks)
    {
        if (listLinks == null || listLinks.isEmpty())
        {
            return;
        }
        List<List<HtmlLink>> subSets = ListUtils.partition(listLinks, MAX_SIZE_LIST);
        StringBuffer insertInBatch = null;
        try (Connection con = getConnection();
                        Statement stmt = con.createStatement();)
        {

            System.out.println("Total listLinks[SECOND LEVEL]: " + listLinks.size() + " -> " + " Total de subsets to insert: " + subSets.size());
            for (Iterator<List<HtmlLink>> iterator = subSets.iterator(); iterator.hasNext();)
            {
                insertInBatch = insertInBatch(iterator.next());
                if (validate(insertInBatch))
                {
                    stmt.executeUpdate(insertInBatch.toString());
                }
            }
        }
        catch (SQLException ex)
        {
            Logger lgr = Logger.getLogger(LinkDAO.class.getName());
            lgr.log(Level.SEVERE, insertInBatch.toString());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private boolean validate(StringBuffer strBuffer)
    {
        String string = strBuffer.toString();
        return string.startsWith("INSERT") && string.endsWith("');");
    }

    private StringBuffer insertInBatch(List<HtmlLink> list)
    {
        if (list == null || list.isEmpty())
            return new StringBuffer();

        Iterator<HtmlLink> iterator2 = list.iterator();
        StringBuffer builder = new StringBuffer("INSERT INTO links ( link ) VALUES ");
        HtmlLink htmlLink = null;
        while (iterator2.hasNext())
        {
            htmlLink = (HtmlLink) iterator2.next();
            builder.append(htmlLink).append(", ");
        }
        builder.append(htmlLink).append(";");

        return builder;
    }

    public List<Result> getGroupedResult()
    {
        List<Result> list = new ArrayList<Result>();

        try (Connection con = getConnection();
                        Statement stmt = con.createStatement();)
        {
            StringBuilder sql = new StringBuilder("SELECT COUNT(1) total, link FROM links ");
            sql.append(" GROUP BY link ");
            sql.append(" ORDER BY total DESC ");
            ResultSet rs = stmt.executeQuery(sql.toString());
            while (rs.next())
            {
                list.add(new Result(rs.getString(2), rs.getInt(1)));
            }

            return list;
        }
        catch (SQLException ex)
        {
            Logger lgr = Logger.getLogger(LinkDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    
    public Long totalRegsBase()
    {
        Long total = 0l;
        try (Connection con = getConnection();
                        Statement stmt = con.createStatement();)
        {
            StringBuilder sql = new StringBuilder("SELECT COUNT(1) total FROM links ");
            ResultSet rs = stmt.executeQuery(sql.toString());
            if (rs.next())
            {
               total = rs.getLong(1);
            }

            return total;
        }
        catch (SQLException ex)
        {
            Logger lgr = Logger.getLogger(LinkDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }


    public void prepareDatabase()
    {
        dropDatabase();
        createDatabase();
    }

    private void createDatabase()
    {
        try (Connection con = getConnection();
                        Statement stmt = con.createStatement();)
        {
            stmt.executeUpdate("CREATE TABLE links ( link varchar(512) ) ; ");
        }
        catch (SQLException ex)
        {
            Logger lgr = Logger.getLogger(LinkDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void dropDatabase()
    {
        try (Connection con = getConnection();
                        Statement stmt = con.createStatement();)
        {
            stmt.executeUpdate("DROP TABLE links ;");
        }
        catch (SQLException ex)
        {
            Logger lgr = Logger.getLogger(LinkDAO.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
