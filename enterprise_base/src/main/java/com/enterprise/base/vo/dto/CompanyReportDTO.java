package com.enterprise.base.vo.dto;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/13 下午3:44
 */
public class CompanyReportDTO extends BaseReportDTO{

    private String search;

    private String corpId;

    private String agentStatus;

    private String version;

    private List<Integer> agentStatusList;

    private List<String> versionList;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Integer> getAgentStatusList() {
        return agentStatusList;
    }

    public void setAgentStatusList(List<Integer> agentStatusList) {
        this.agentStatusList = agentStatusList;
    }

    public List<String> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<String> versionList) {
        this.versionList = versionList;
    }
}

