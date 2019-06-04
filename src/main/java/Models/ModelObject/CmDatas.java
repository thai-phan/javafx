package main.java.Models.ModelObject;

public class CmDatas {
    private String name;
    private String path;
    private String string;
    private String description;
    private String status_Cd;
    private String status_Cd_name;
    private String folder_Id;
    private String create_User;
    private String create_Dttm;
    private String update_User;
    private String update_Dttm;
    private String folder_Entry_Id;
    private String entity_Id;
    private String component_Type_Cd;
    private String finder_custom_column1;
    private String finder_custom_column2;
    private String comm_class_id;
    private String comm_class_name;

    public CmDatas(String name, String description, String status_Cd_name,
                   String path, String create_User, String create_Dttm,
                   String update_User, String update_Dttm, String entity_Id, String status_Cd,
                   String comm_class_id, String comm_class_name) {
        this.name = name;
        this.description = description;
        this.status_Cd_name = status_Cd_name;
        this.path = path;
        this.create_User = create_User;
        this.create_Dttm= create_Dttm;
        this.update_User = update_User;
        this.update_Dttm = update_Dttm;
        this.entity_Id = entity_Id;
        this.status_Cd = status_Cd;
        this.comm_class_id = comm_class_id;
        this.comm_class_name = comm_class_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus_Cd(String status_Cd) {
        this.status_Cd = status_Cd;
    }

    public String getStatus_Cd() {
        return status_Cd;
    }

    public void setStatus_Cd_name(String status_Cd_name) {
        this.status_Cd_name = status_Cd_name;
    }

    public String getStatus_Cd_name() {
        return status_Cd_name;
    }

    public void setFolder_Id(String folder_Id) {
        this.folder_Id = folder_Id;
    }

    public String getFolder_Id() {
        return folder_Id;
    }

    public void setCreate_User(String create_User) {
        this.create_User = create_User;
    }

    public String getCreate_User() {
        return create_User;
    }

    public void setCreate_Dttm(String create_Dttm) {
        this.create_Dttm = create_Dttm;
    }

    public String getCreate_Dttm() {
        return create_Dttm;
    }

    public void setUpdate_User(String update_User) {
        this.update_User = update_User;
    }

    public String getUpdate_User() {
        return update_User;
    }

    public void setUpdate_Dttm(String update_Dttm) {
        this.update_Dttm = update_Dttm;
    }

    public String getUpdate_Dttm() {
        return update_Dttm;
    }

    public void setEntity_Id(String entity_Id) {
        this.entity_Id = entity_Id;
    }

    public String getFolder_Entry_Id() {
        return folder_Entry_Id;
    }

    public void setComponent_Type_Cd(String component_Type_Cd) {
        this.component_Type_Cd = component_Type_Cd;
    }

    public String getComponent_Type_Cd() {
        return component_Type_Cd;
    }

    public void setFinder_custom_column1(String finder_custom_column1) {
        this.finder_custom_column1 = finder_custom_column1;
    }

    public String getEntity_Id() {
        return entity_Id;
    }

    public void setFinder_custom_column2(String finder_custom_column2) {
        this.finder_custom_column2 = finder_custom_column2;
    }

    public String getFinder_custom_column1() {
        return finder_custom_column1;
    }

    public void setFolder_Entry_Id(String folder_Entry_Id) {
        this.folder_Entry_Id = folder_Entry_Id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFinder_custom_column2() {
        return finder_custom_column2;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getPath() {
        return path;
    }

    public String getString() {
        return string;
    }

    public void setComm_class_id(String comm_class_id) {
        this.comm_class_id = comm_class_id;
    }

    public String getComm_class_id() {
        return comm_class_id;
    }

    public void setComm_class_name(String comm_class_name) {
        this.comm_class_name = comm_class_name;
    }

    public String getComm_class_name() {
        return comm_class_name;
    }
}

