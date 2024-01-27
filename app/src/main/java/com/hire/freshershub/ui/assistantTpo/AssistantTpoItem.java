package com.hire.freshershub.ui.assistantTpo;

public class AssistantTpoItem {
    private String assistantDeptName;
    private String assistantTpoName;
    private String assistantTpoDesignation;
    private String assistantTpoWorkMail;
    private String assistantTpoPersonalMail;
    private String assistantTpoContactNo;

    public AssistantTpoItem(){}

    public AssistantTpoItem(String assistantDeptName, String assistantTpoName, String assistantTpoDesignation, String assistantTpoWorkMail, String assistantTpoPersonalMail, String assistantTpoContactNo) {
        this.assistantDeptName = assistantDeptName;
        this.assistantTpoName = assistantTpoName;
        this.assistantTpoDesignation = assistantTpoDesignation;
        this.assistantTpoWorkMail = assistantTpoWorkMail;
        this.assistantTpoPersonalMail = assistantTpoPersonalMail;
        this.assistantTpoContactNo = assistantTpoContactNo;
    }

    public String getAssistantDeptName() {
        return assistantDeptName;
    }

    public String getAssistantTpoName() {
        return assistantTpoName;
    }

    public String getAssistantTpoDesignation() {
        return assistantTpoDesignation;
    }

    public String getAssistantTpoWorkMail() {
        return assistantTpoWorkMail;
    }

    public String getAssistantTpoPersonalMail() {
        return assistantTpoPersonalMail;
    }

    public String getAssistantTpoContactNo() {
        return assistantTpoContactNo;
    }
}
