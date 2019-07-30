package com.enterprise.base.vo;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/23 下午5:27
 */
public class PaperBallVO {

    private Integer totalCount;

    private List<PaperBallDetailVO> paperBallList;

    public class PaperBallDetailVO{

        private Integer id;

        private Integer type;

        private Date createTime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public PaperBallDetailVO() {
        }

        public PaperBallDetailVO(Integer id, Date createTime) {
            this.id = id;
            this.createTime = createTime;
        }

        public PaperBallDetailVO(Integer id, Integer type, Date createTime) {
            this.id = id;
            this.type = type;
            this.createTime = createTime;
        }
    }


    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<PaperBallDetailVO> getPaperBallList() {
        return paperBallList;
    }

    public void setPaperBallList(List<PaperBallDetailVO> paperBallList) {
        this.paperBallList = paperBallList;
    }
}
