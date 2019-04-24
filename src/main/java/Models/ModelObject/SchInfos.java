package main.java.Models.ModelObject;

public class SchInfos {
    private String string;
    private String schedule_type_cd;
    private String schedule_start_dttm_ftm;
    private String schedule_end_dttm_ftm;
    private String cron_String_txt;
    private String cron_Dd;
    private String cron_Mm;
    private String status_cd;
    private String execution_schedule_id;
    private String schedule_start_dttm;
    private String schedule_end_dttm;
    private String opt_num;
    private String cron_Week;

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setCron_Dd(String cron_Dd) {
        this.cron_Dd = cron_Dd;
    }

    public String getCron_Dd() {
        return cron_Dd;
    }

    public void setCron_Mm(String cron_Mm) {
        this.cron_Mm = cron_Mm;
    }

    public String getCron_Mm() {
        return cron_Mm;
    }

    public void setCron_String_txt(String cron_String_txt) {
        this.cron_String_txt = cron_String_txt;
    }

    public String getCron_String_txt() {
        return cron_String_txt;
    }

    public void setCron_Week(String cron_Week) {
        this.cron_Week = cron_Week;
    }

    public String getCron_Week() {
        return cron_Week;
    }

    public void setExecution_schedule_id(String execution_schedule_id) {
        this.execution_schedule_id = execution_schedule_id;
    }

    public String getExecution_schedule_id() {
        return execution_schedule_id;
    }

    public void setOpt_num(String opt_num) {
        this.opt_num = opt_num;
    }

    public String getOpt_num() {
        return opt_num;
    }

    public void setSchedule_end_dttm(String schedule_end_dttm) {
        this.schedule_end_dttm = schedule_end_dttm;
    }

    public String getSchedule_end_dttm() {
        return schedule_end_dttm;
    }

    public void setSchedule_end_dttm_ftm(String schedule_end_dttm_ftm) {
        this.schedule_end_dttm_ftm = schedule_end_dttm_ftm;
    }

    public String getSchedule_end_dttm_ftm() {
        return schedule_end_dttm_ftm;
    }

    public void setSchedule_start_dttm(String schedule_start_dttm) {
        this.schedule_start_dttm = schedule_start_dttm;
    }

    public String getSchedule_start_dttm() {
        return schedule_start_dttm;
    }

    public void setSchedule_start_dttm_ftm(String schedule_start_dttm_ftm) {
        this.schedule_start_dttm_ftm = schedule_start_dttm_ftm;
    }

    public String getSchedule_start_dttm_ftm() {
        return schedule_start_dttm_ftm;
    }

    public void setSchedule_type_cd(String schedule_type_cd) {
        this.schedule_type_cd = schedule_type_cd;
    }

    public String getSchedule_type_cd() {
        return schedule_type_cd;
    }

    public void setStatus_cd(String status_cd) {
        this.status_cd = status_cd;
    }

    public String getStatus_cd() {
        return status_cd;
    }
}
