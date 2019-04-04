package com.menu.manger.dto;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 通知公告表 sys_notice
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
public class Notice extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 公告ID */
	private Integer noticeId;
	/** 公告标题 */
	private String noticeTitle;
	/** 公告类型（1通知 2公告） */
	private String noticeType;
	/** 公告内容 */
	private String noticeContent;
	/** 公告状态（0正常 1关闭） */
	private String status;
	/** 创建者 */
	private String createBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新者 */
	private String updateBy;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String remark;

	public void setNoticeId(Integer noticeId) 
	{
		this.noticeId = noticeId;
	}

	public Integer getNoticeId() 
	{
		return noticeId;
	}
	public void setNoticeTitle(String noticeTitle) 
	{
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeTitle() 
	{
		return noticeTitle;
	}
	public void setNoticeType(String noticeType) 
	{
		this.noticeType = noticeType;
	}

	public String getNoticeType() 
	{
		return noticeType;
	}
	public void setNoticeContent(String noticeContent) 
	{
		this.noticeContent = noticeContent;
	}

	public String getNoticeContent() 
	{
		return noticeContent;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}

	public String getStatus() 
	{
		return status;
	}
	public void setCreateBy(String createBy) 
	{
		this.createBy = createBy;
	}

	public String getCreateBy() 
	{
		return createBy;
	}
	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public Date getCreateTime() 
	{
		return createTime;
	}
	public void setUpdateBy(String updateBy) 
	{
		this.updateBy = updateBy;
	}

	public String getUpdateBy() 
	{
		return updateBy;
	}
	public void setUpdateTime(Date updateTime) 
	{
		this.updateTime = updateTime;
	}

	public Date getUpdateTime() 
	{
		return updateTime;
	}
	public void setRemark(String remark) 
	{
		this.remark = remark;
	}

	public String getRemark() 
	{
		return remark;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("noticeTitle", getNoticeTitle())
            .append("noticeType", getNoticeType())
            .append("noticeContent", getNoticeContent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
