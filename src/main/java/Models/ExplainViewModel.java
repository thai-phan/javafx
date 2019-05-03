package main.java.Models;

import main.java.Models.ModelObject.ResultListExplain;
import main.java.Models.ModelObject.Resultinfo;

public class ExplainViewModel {
    private Resultinfo resultinfo;
    private ResultListExplain resultList;

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setResultList(ResultListExplain resultList) {
        this.resultList = resultList;
    }

    public ResultListExplain getResultList() {
        return resultList;
    }
}
