package main.java.Models;

import main.java.Models.ModelObject.ResultListCampaign;
import main.java.Models.ModelObject.Resultinfo;

public class CampaignInfoModel {
    private Resultinfo resultinfo;
    private ResultListCampaign resultList;

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setResultList(ResultListCampaign resultList) {
        this.resultList = resultList;
    }

    public ResultListCampaign getResultList() {
        return resultList;
    }
}
