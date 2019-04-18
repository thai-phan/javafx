package main.java.Models;

import main.java.Models.ModelObject.CmList;
import main.java.Models.ModelObject.Resultinfo;

public class CampaignListModel {
    private Resultinfo resultinfo;
    private CmList cmList;

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setCmList(CmList cmList) {
        this.cmList = cmList;
    }

    public CmList getCmList() {
        return cmList;
    }
}
