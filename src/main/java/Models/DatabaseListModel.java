package main.java.Models;

import main.java.Models.ModelObject.ResultListTable;
import main.java.Models.ModelObject.Resultinfo;

public class DatabaseListModel {
    private Resultinfo resultinfo;
    private ResultListTable resultList;

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setResultListTable(ResultListTable resultListTable) {
        this.resultList = resultListTable;
    }

    public ResultListTable getResultListTable() {
        return resultList;
    }
}

