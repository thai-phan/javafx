package main.java.Models;

import main.java.Models.ModelObject.Resultinfo;
import main.java.Models.ModelObject.SchInfoList;

public class CampaignScheduleModel {
    private Resultinfo resultinfo;
    private SchInfoList schInfoList;

    public void setSchInfoList(SchInfoList schInfoList) {
        this.schInfoList = schInfoList;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public SchInfoList getSchInfoList() {
        return schInfoList;
    }
}

