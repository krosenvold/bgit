package com.atlassian.labs.bamboo.git;

import java.util.List;
import java.util.Map;

import org.jfree.data.general.Dataset;

import com.atlassian.bamboo.reports.collector.ReportCollector;

/**
 * A basic report.
 */
public class ExampleReport implements ReportCollector
{


    public Dataset getDataset()
    {
        return null;
    }

    public void setResultsList(List list)
    {
    }

    public void setParams(Map map)
    {
    }

    public String getPeriodRange()
    {
        return null;
    }
    
    
}