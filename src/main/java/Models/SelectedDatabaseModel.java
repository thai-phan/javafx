package main.java.Models;

import main.java.Models.ModelObject.Datasources;
import main.java.Models.ModelObject.Resultinfo;

public class SelectedDatabaseModel {
    private Resultinfo resultinfo;
    private Datasources datasources;

    public void setResultinfo(Resultinfo resultinfo) {
        this.resultinfo = resultinfo;
    }

    public Resultinfo getResultinfo() {
        return resultinfo;
    }

    public void setDatasources(Datasources datasources) {
        this.datasources = datasources;
    }

    public Datasources getDatasources() {
        return datasources;
    }
}
