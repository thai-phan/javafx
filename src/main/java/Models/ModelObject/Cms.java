package main.java.Models.ModelObject;

public class Cms {
    private String name;
    private String string;
    private String description;
    private String create_User;
    private String create_Dttm;
    private String update_User;
    private String update_Dttm;
    private String status_Cd;
    private String communication_Id;
    private String local_Copy_Ind;
    private String campaign_Collection_Id;
    private String schema_Id;
    private String schema_Element_Id;
    private String segment_Plan_Id;
    private String cost_Rollup_Ind;
    private String locked_Definition_Ind;
    private String planned_Communication_Start_Dt;
    private String planned_Communication_End_Dt;
    private String planned_Communication_End_Ind;
    private String planned_Generation_Cycles_Num;
    private String planned_Generation_Cycle_Ind;
    private String planned_Comm_Result_Start_Dt;
    private String planned_Comm_Result_End_Dt;
    private String actual_Communication_Start_Dt;
    private String actual_Communication_End_Dt;
    private String actual_Comm_Result_Start_Dt;
    private String actual_Comm_Result_End_Dt;
    private String custom1_Start_Dt;
    private String custom1_End_Dt;
    private String custom2_Start_Dt;
    private String custom2_End_Dt;
    private String custom3_Start_Dt;
    private String custom3_End_Dt;
    private String max_Leads_Per_Cycle_Num;
    private String max_Leads_Per_Cycle_Ind;
    private String max_Leads_Per_Lifetime_Num;
    private String max_Leads_Per_Lifetime_Ind;
    private String max_Variable_Cost_Amt;
    private String max_Variable_Cost_Ind;
    private String frozen_Ind;
    private String reference_Txt;
    private String source_Entity_Id;
    private String comm_Class_Id;
    private String comm_Subclass_Id;
    private String recycle_Ind;
    private String recycle_Num;
    private String contact_Date_Origin_Cd;
    private String concurrent_Contacts_Ind;
    private String count_Leads_As_Contacted_Ind;
    private String consider_Related_Contacts_Ind;
    private String bundle_Leads_Ind;
    private String deduplication_Type_Cd;
    private String deduplication_Days_Num;
    private String deduplication_Minutes_Num;
    private String deduplication_Last_Run_Num;
    private String deduplication_Dttm;
    private String apply_Channel_Recency_Ind;
    private String custom_Attribute_Ind;
    private String execution_Schedule_Id;
    private String control_Method_Cd;
    private String control_Percentage_Num;
    private String control_Minimum_Num;
    private String activate_User;
    private String activate_Dttm;
    private String apply_Contact_Rules_Ind;
    private String channel_INSTANCE_ID;

    public void setChannel_INSTANCE_ID(String channel_INSTANCE_ID) {
        this.channel_INSTANCE_ID = channel_INSTANCE_ID;
    }

    public String getChannel_INSTANCE_ID() {
        return channel_INSTANCE_ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActual_Communication_Start_Dt(String actual_Communication_Start_Dt) {
        this.actual_Communication_Start_Dt = actual_Communication_Start_Dt;
    }

    public String getActual_Communication_Start_Dt() {
        return actual_Communication_Start_Dt;
    }

    public void setActual_Communication_End_Dt(String actual_Communication_End_Dt) {
        this.actual_Communication_End_Dt = actual_Communication_End_Dt;
    }

    public String getActual_Communication_End_Dt() {
        return actual_Communication_End_Dt;
    }

    public String getPlanned_Communication_Start_Dt() {
        return planned_Communication_Start_Dt;
    }

    public void setPlanned_Communication_End_Dt(String planned_Communication_End_Dt) {
        this.planned_Communication_End_Dt = planned_Communication_End_Dt;
    }

    public String getPlanned_Communication_End_Dt() {
        return planned_Communication_End_Dt;
    }

    public void setPlanned_Communication_Start_Dt(String planned_Communication_Start_Dt) {
        this.planned_Communication_Start_Dt = planned_Communication_Start_Dt;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setUpdate_Dttm(String update_Dttm) {
        this.update_Dttm = update_Dttm;
    }

    public void setUpdate_User(String update_User) {
        this.update_User = update_User;
    }

    public void setCreate_Dttm(String create_Dttm) {
        this.create_Dttm = create_Dttm;
    }

    public void setCreate_User(String create_User) {
        this.create_User = create_User;
    }

    public void setStatus_Cd(String status_Cd) {
        this.status_Cd = status_Cd;
    }

    public void setActual_Comm_Result_End_Dt(String actual_Comm_Result_End_Dt) {
        this.actual_Comm_Result_End_Dt = actual_Comm_Result_End_Dt;
    }

    public void setActual_Comm_Result_Start_Dt(String actual_Comm_Result_Start_Dt) {
        this.actual_Comm_Result_Start_Dt = actual_Comm_Result_Start_Dt;
    }

    public void setCampaign_Collection_Id(String campaign_Collection_Id) {
        this.campaign_Collection_Id = campaign_Collection_Id;
    }

    public void setCommunication_Id(String communication_Id) {
        this.communication_Id = communication_Id;
    }

    public void setCost_Rollup_Ind(String cost_Rollup_Ind) {
        this.cost_Rollup_Ind = cost_Rollup_Ind;
    }

    public void setLocal_Copy_Ind(String local_Copy_Ind) {
        this.local_Copy_Ind = local_Copy_Ind;
    }

    public void setCustom1_End_Dt(String custom1_End_Dt) {
        this.custom1_End_Dt = custom1_End_Dt;
    }

    public void setCustom1_Start_Dt(String custom1_Start_Dt) {
        this.custom1_Start_Dt = custom1_Start_Dt;
    }

    public void setCustom2_End_Dt(String custom2_End_Dt) {
        this.custom2_End_Dt = custom2_End_Dt;
    }

    public void setCustom2_Start_Dt(String custom2_Start_Dt) {
        this.custom2_Start_Dt = custom2_Start_Dt;
    }

    public void setLocked_Definition_Ind(String locked_Definition_Ind) {
        this.locked_Definition_Ind = locked_Definition_Ind;
    }

    public void setPlanned_Comm_Result_End_Dt(String planned_Comm_Result_End_Dt) {
        this.planned_Comm_Result_End_Dt = planned_Comm_Result_End_Dt;
    }

    public void setCustom3_Start_Dt(String custom3_Start_Dt) {
        this.custom3_Start_Dt = custom3_Start_Dt;
    }

    public void setPlanned_Comm_Result_Start_Dt(String planned_Comm_Result_Start_Dt) {
        this.planned_Comm_Result_Start_Dt = planned_Comm_Result_Start_Dt;
    }

    public void setPlanned_Communication_End_Ind(String planned_Communication_End_Ind) {
        this.planned_Communication_End_Ind = planned_Communication_End_Ind;
    }

    public void setPlanned_Generation_Cycle_Ind(String planned_Generation_Cycle_Ind) {
        this.planned_Generation_Cycle_Ind = planned_Generation_Cycle_Ind;
    }

    public void setPlanned_Generation_Cycles_Num(String planned_Generation_Cycles_Num) {
        this.planned_Generation_Cycles_Num = planned_Generation_Cycles_Num;
    }

    public void setSchema_Element_Id(String schema_Element_Id) {
        this.schema_Element_Id = schema_Element_Id;
    }

    public void setSchema_Id(String schema_Id) {
        this.schema_Id = schema_Id;
    }

    public void setSegment_Plan_Id(String segment_Plan_Id) {
        this.segment_Plan_Id = segment_Plan_Id;
    }

    public void setCustom3_End_Dt(String custom3_End_Dt) {
        this.custom3_End_Dt = custom3_End_Dt;
    }

    public void setActivate_Dttm(String activate_Dttm) {
        this.activate_Dttm = activate_Dttm;
    }

    public void setActivate_User(String activate_User) {
        this.activate_User = activate_User;
    }

    public void setApply_Channel_Recency_Ind(String apply_Channel_Recency_Ind) {
        this.apply_Channel_Recency_Ind = apply_Channel_Recency_Ind;
    }

    public void setApply_Contact_Rules_Ind(String apply_Contact_Rules_Ind) {
        this.apply_Contact_Rules_Ind = apply_Contact_Rules_Ind;
    }

    public void setBundle_Leads_Ind(String bundle_Leads_Ind) {
        this.bundle_Leads_Ind = bundle_Leads_Ind;
    }

    public void setComm_Class_Id(String comm_Class_Id) {
        this.comm_Class_Id = comm_Class_Id;
    }

    public void setComm_Subclass_Id(String comm_Subclass_Id) {
        this.comm_Subclass_Id = comm_Subclass_Id;
    }

    public void setConcurrent_Contacts_Ind(String concurrent_Contacts_Ind) {
        this.concurrent_Contacts_Ind = concurrent_Contacts_Ind;
    }

    public void setConsider_Related_Contacts_Ind(String consider_Related_Contacts_Ind) {
        this.consider_Related_Contacts_Ind = consider_Related_Contacts_Ind;
    }

    public void setContact_Date_Origin_Cd(String contact_Date_Origin_Cd) {
        this.contact_Date_Origin_Cd = contact_Date_Origin_Cd;
    }

    public void setControl_Method_Cd(String control_Method_Cd) {
        this.control_Method_Cd = control_Method_Cd;
    }

    public void setControl_Minimum_Num(String control_Minimum_Num) {
        this.control_Minimum_Num = control_Minimum_Num;
    }

    public void setControl_Percentage_Num(String control_Percentage_Num) {
        this.control_Percentage_Num = control_Percentage_Num;
    }

    public void setCount_Leads_As_Contacted_Ind(String count_Leads_As_Contacted_Ind) {
        this.count_Leads_As_Contacted_Ind = count_Leads_As_Contacted_Ind;
    }

    public void setCustom_Attribute_Ind(String custom_Attribute_Ind) {
        this.custom_Attribute_Ind = custom_Attribute_Ind;
    }

    public void setDeduplication_Days_Num(String deduplication_Days_Num) {
        this.deduplication_Days_Num = deduplication_Days_Num;
    }

    public void setDeduplication_Dttm(String deduplication_Dttm) {
        this.deduplication_Dttm = deduplication_Dttm;
    }

    public void setDeduplication_Last_Run_Num(String deduplication_Last_Run_Num) {
        this.deduplication_Last_Run_Num = deduplication_Last_Run_Num;
    }

    public void setDeduplication_Minutes_Num(String deduplication_Minutes_Num) {
        this.deduplication_Minutes_Num = deduplication_Minutes_Num;
    }

    public void setDeduplication_Type_Cd(String deduplication_Type_Cd) {
        this.deduplication_Type_Cd = deduplication_Type_Cd;
    }

    public void setExecution_Schedule_Id(String execution_Schedule_Id) {
        this.execution_Schedule_Id = execution_Schedule_Id;
    }

    public void setFrozen_Ind(String frozen_Ind) {
        this.frozen_Ind = frozen_Ind;
    }

    public void setMax_Leads_Per_Cycle_Ind(String max_Leads_Per_Cycle_Ind) {
        this.max_Leads_Per_Cycle_Ind = max_Leads_Per_Cycle_Ind;
    }

    public void setMax_Leads_Per_Cycle_Num(String max_Leads_Per_Cycle_Num) {
        this.max_Leads_Per_Cycle_Num = max_Leads_Per_Cycle_Num;
    }

    public void setMax_Leads_Per_Lifetime_Ind(String max_Leads_Per_Lifetime_Ind) {
        this.max_Leads_Per_Lifetime_Ind = max_Leads_Per_Lifetime_Ind;
    }

    public void setMax_Leads_Per_Lifetime_Num(String max_Leads_Per_Lifetime_Num) {
        this.max_Leads_Per_Lifetime_Num = max_Leads_Per_Lifetime_Num;
    }

    public void setMax_Variable_Cost_Amt(String max_Variable_Cost_Amt) {
        this.max_Variable_Cost_Amt = max_Variable_Cost_Amt;
    }

    public void setMax_Variable_Cost_Ind(String max_Variable_Cost_Ind) {
        this.max_Variable_Cost_Ind = max_Variable_Cost_Ind;
    }

    public void setRecycle_Ind(String recycle_Ind) {
        this.recycle_Ind = recycle_Ind;
    }

    public void setRecycle_Num(String recycle_Num) {
        this.recycle_Num = recycle_Num;
    }

    public void setReference_Txt(String reference_Txt) {
        this.reference_Txt = reference_Txt;
    }

    public void setSource_Entity_Id(String source_Entity_Id) {
        this.source_Entity_Id = source_Entity_Id;
    }

    public String getString() {
        return string;
    }

    public String getUpdate_Dttm() {
        return update_Dttm;
    }

    public String getUpdate_User() {
        return update_User;
    }

    public String getCreate_Dttm() {
        return create_Dttm;
    }

    public String getCreate_User() {
        return create_User;
    }

    public String getStatus_Cd() {
        return status_Cd;
    }

    public String getActual_Comm_Result_End_Dt() {
        return actual_Comm_Result_End_Dt;
    }

    public String getActual_Comm_Result_Start_Dt() {
        return actual_Comm_Result_Start_Dt;
    }

    public String getCampaign_Collection_Id() {
        return campaign_Collection_Id;
    }

    public String getCommunication_Id() {
        return communication_Id;
    }

    public String getCost_Rollup_Ind() {
        return cost_Rollup_Ind;
    }

    public String getLocal_Copy_Ind() {
        return local_Copy_Ind;
    }

    public String getCustom1_End_Dt() {
        return custom1_End_Dt;
    }

    public String getCustom1_Start_Dt() {
        return custom1_Start_Dt;
    }

    public String getActivate_Dttm() {
        return activate_Dttm;
    }

    public String getCustom2_End_Dt() {
        return custom2_End_Dt;
    }

    public String getCustom2_Start_Dt() {
        return custom2_Start_Dt;
    }

    public String getLocked_Definition_Ind() {
        return locked_Definition_Ind;
    }

    public String getPlanned_Comm_Result_End_Dt() {
        return planned_Comm_Result_End_Dt;
    }

    public String getCustom3_End_Dt() {
        return custom3_End_Dt;
    }

    public String getPlanned_Comm_Result_Start_Dt() {
        return planned_Comm_Result_Start_Dt;
    }

    public String getPlanned_Communication_End_Ind() {
        return planned_Communication_End_Ind;
    }

    public String getPlanned_Generation_Cycle_Ind() {
        return planned_Generation_Cycle_Ind;
    }

    public String getPlanned_Generation_Cycles_Num() {
        return planned_Generation_Cycles_Num;
    }

    public String getSchema_Element_Id() {
        return schema_Element_Id;
    }

    public String getCustom3_Start_Dt() {
        return custom3_Start_Dt;
    }

    public String getSchema_Id() {
        return schema_Id;
    }

    public String getSegment_Plan_Id() {
        return segment_Plan_Id;
    }

    public String getActivate_User() {
        return activate_User;
    }

    public String getApply_Channel_Recency_Ind() {
        return apply_Channel_Recency_Ind;
    }

    public String getApply_Contact_Rules_Ind() {
        return apply_Contact_Rules_Ind;
    }

    public String getBundle_Leads_Ind() {
        return bundle_Leads_Ind;
    }

    public String getMax_Leads_Per_Cycle_Ind() {
        return max_Leads_Per_Cycle_Ind;
    }

    public String getComm_Class_Id() {
        return comm_Class_Id;
    }

    public String getComm_Subclass_Id() {
        return comm_Subclass_Id;
    }

    public String getConcurrent_Contacts_Ind() {
        return concurrent_Contacts_Ind;
    }

    public String getConsider_Related_Contacts_Ind() {
        return consider_Related_Contacts_Ind;
    }

    public String getContact_Date_Origin_Cd() {
        return contact_Date_Origin_Cd;
    }

    public String getControl_Method_Cd() {
        return control_Method_Cd;
    }

    public String getControl_Minimum_Num() {
        return control_Minimum_Num;
    }

    public String getMax_Leads_Per_Cycle_Num() {
        return max_Leads_Per_Cycle_Num;
    }

    public String getControl_Percentage_Num() {
        return control_Percentage_Num;
    }

    public String getCount_Leads_As_Contacted_Ind() {
        return count_Leads_As_Contacted_Ind;
    }

    public String getCustom_Attribute_Ind() {
        return custom_Attribute_Ind;
    }

    public String getDeduplication_Days_Num() {
        return deduplication_Days_Num;
    }

    public String getDeduplication_Dttm() {
        return deduplication_Dttm;
    }

    public String getDeduplication_Last_Run_Num() {
        return deduplication_Last_Run_Num;
    }

    public String getDeduplication_Minutes_Num() {
        return deduplication_Minutes_Num;
    }

    public String getDeduplication_Type_Cd() {
        return deduplication_Type_Cd;
    }

    public String getExecution_Schedule_Id() {
        return execution_Schedule_Id;
    }

    public String getFrozen_Ind() {
        return frozen_Ind;
    }

    public String getMax_Leads_Per_Lifetime_Ind() {
        return max_Leads_Per_Lifetime_Ind;
    }

    public String getMax_Leads_Per_Lifetime_Num() {
        return max_Leads_Per_Lifetime_Num;
    }

    public String getMax_Variable_Cost_Amt() {
        return max_Variable_Cost_Amt;
    }

    public String getMax_Variable_Cost_Ind() {
        return max_Variable_Cost_Ind;
    }

    public String getRecycle_Ind() {
        return recycle_Ind;
    }

    public String getRecycle_Num() {
        return recycle_Num;
    }

    public String getReference_Txt() {
        return reference_Txt;
    }

    public String getSource_Entity_Id() {
        return source_Entity_Id;
    }
}

