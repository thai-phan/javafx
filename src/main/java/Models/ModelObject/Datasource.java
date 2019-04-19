package main.java.Models.ModelObject;

public class Datasource {
    private String string;
    private String database_name_txt;
    private String table_name_txt;
    private String communication_id;

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setCommunication_id(String communication_id) {
        this.communication_id = communication_id;
    }

    public String getCommunication_id() {
        return communication_id;
    }

    public void setDatabase_name_txt(String database_name_txt) {
        this.database_name_txt = database_name_txt;
    }

    public String getDatabase_name_txt() {
        return database_name_txt;
    }

    public void setTable_name_txt(String table_name_txt) {
        this.table_name_txt = table_name_txt;
    }

    public String getTable_name_txt() {
        return table_name_txt;
    }
}

