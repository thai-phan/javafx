package main.java.Models;

import main.java.Models.ModelObject.ResultListView;
import main.java.Models.ModelObject.Resultinfo;

public class ViewListModel {
    private Resultinfo resultinfo;
    private ResultListView resultList;

    public void setResultList(ResultListView resultList) {
        this.resultList = resultList;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public ResultListView getResultList() {
        return resultList;
    }
}
