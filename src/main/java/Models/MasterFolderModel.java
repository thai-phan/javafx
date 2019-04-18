package main.java.Models;

import main.java.Models.ModelObject.CmFolderList;
import main.java.Models.ModelObject.Resultinfo;

public class MasterFolderModel {
    private Resultinfo resultinfo;
    private CmFolderList cmFolderList;

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setCmFolderList(CmFolderList cmFolderList) {
        this.cmFolderList = cmFolderList;
    }

    public CmFolderList getCmFolderList() {
        return cmFolderList;
    }
}

