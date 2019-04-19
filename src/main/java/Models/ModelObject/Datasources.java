package main.java.Models.ModelObject;

import java.util.List;

public class Datasources {
    private int cnt;
    private List<Datasource> datasource;

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getCnt() {
        return cnt;
    }

    public void setDatasource(List<Datasource> datasource) {
        this.datasource = datasource;
    }

    public List<Datasource> getDatasource() {
        return datasource;
    }
}
