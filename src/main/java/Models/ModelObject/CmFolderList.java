package main.java.Models.ModelObject;

import java.util.List;

public class CmFolderList {
    private int cnt;
    private List<CmFolderDatas> cmFolderDatas;

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCmFolderData(List<CmFolderDatas> cmFolderData) {
        this.cmFolderDatas = cmFolderData;
    }

    public List<CmFolderDatas> getCmFolderData() {
        return cmFolderDatas;
    }
}
