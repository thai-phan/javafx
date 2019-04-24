package main.java.Models.ModelObject;

import java.util.List;

public class SchInfoList {
    private int cnt;
    private List<SchInfos> schInfos;

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setSchInfoList(List<SchInfos> schInfos) {
        this.schInfos = schInfos;
    }

    public List<SchInfos> getSchInfoList() {
        return schInfos;
    }
}
