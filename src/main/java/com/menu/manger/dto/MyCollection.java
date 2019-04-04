package com.menu.manger.dto;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 收藏表 my_collection
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
public class MyCollection extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ID */
	private Integer id;
	/** 類型1為店鋪2為商品 */
	private Integer type;
	/** 來源表 */
	private String resourceTable;
	/** 來源表ID */
	private Integer resourceId;
	/** 會員ID */
	private Integer memuId;
	/** 收藏時間 */
	private Date createDate;
	private String picUrl;
	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setType(Integer type) 
	{
		this.type = type;
	}

	public Integer getType() 
	{
		return type;
	}
	public void setResourceTable(String resourceTable) 
	{
		this.resourceTable = resourceTable;
	}

	public String getResourceTable() 
	{
		return resourceTable;
	}
	public void setResourceId(Integer resourceId) 
	{
		this.resourceId = resourceId;
	}

	public Integer getResourceId() 
	{
		return resourceId;
	}
	public void setMemuId(Integer memuId) 
	{
		this.memuId = memuId;
	}

	public Integer getMemuId() 
	{
		return memuId;
	}
	public void setCreateDate(Date createDate) 
	{
		this.createDate = createDate;
	}

	public Date getCreateDate() 
	{
		return createDate;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("resourceTable", getResourceTable())
            .append("resourceId", getResourceId())
            .append("memuId", getMemuId())
            .append("createDate", getCreateDate())
            .toString();
    }

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
