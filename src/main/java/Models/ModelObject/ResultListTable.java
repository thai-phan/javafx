package main.java.Models.ModelObject;

import java.util.List;

public class ResultListTable {
    private int cnt;
    private List<DbNames> dbNames;

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setDbNames(List<DbNames> dbNames) {
        this.dbNames = dbNames;
    }

    public List<DbNames> getDbNames() {
        return dbNames;
    }
}
