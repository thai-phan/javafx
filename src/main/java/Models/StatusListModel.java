package main.java.Models;

import main.java.Models.ModelObject.CdList;
import main.java.Models.ModelObject.Resultinfo;

public class StatusListModel {
    private Resultinfo resultinfo;
    private CdList cdList;

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setCdList(CdList cdList) {
        this.cdList = cdList;
    }

    public CdList getCdList() {
        return cdList;
    }
}
