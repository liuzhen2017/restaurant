package com.menu.manger.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 会员管理表 members
 * 
 * @author liuzhen
 * @date 2019-04-03
 */
public class Members extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Integer id;
	/** 邮箱 */
	private String email;
	/** 密码 */
	private String pwd;
	/** 姓名 */
	private String name;
	/** 地区 */
	private String region;
	/** 性别 */
	private Integer sex;
	/** 生日 */
	private String birthday;
	/** 积分 */
	private Integer score;
	/** 是否有效 */
	private String isVaild;
	/** 创建日期 */
	private String createDate;
	/** 會員類型 */
	private Integer membersType;
	/** 升級時間 */
	private String upgradeDate;
	/** 會員有效期 */
	private String vipDate;
	/**  */
	private String salt;
	/**  */
	private String phone;
	/** 会员编号 */
	private String code;
	/** 用户是否勾选了推送 */
	private String isSend;
	/** 是否推送了消息 */
	private String isTis;
	/** 登陆token,通过这个来判断是否登陆,如果不匹配这个字段,则踢出去 */
	private String saveToken;
	/** 备用字段 */
	private String spareField1;
	/** 备用字段 */
	private String spareField2;
	/** 备用字段 */
	private String spareField3;
	/** 备用字段 */
	private String spareField4;
	/** 备用字段 */
	private String spareField5;
	/** 备用字段 */
	private String spareField6;
	/** 备用字段 */
	private String spareField7;
	/** 备用字段 */
	private String spareField8;

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}
	public void setEmail(String email) 
	{
		this.email = email;
	}

	public String getEmail() 
	{
		return email;
	}
	public void setPwd(String pwd) 
	{
		this.pwd = pwd;
	}

	public String getPwd() 
	{
		return pwd;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	public void setRegion(String region) 
	{
		this.region = region;
	}

	public String getRegion() 
	{
		return region;
	}
	public void setSex(Integer sex) 
	{
		this.sex = sex;
	}

	public Integer getSex() 
	{
		return sex;
	}
	public void setBirthday(String birthday) 
	{
		this.birthday = birthday;
	}

	public String getBirthday() 
	{
		return birthday;
	}
	public void setScore(Integer score) 
	{
		this.score = score;
	}

	public Integer getScore() 
	{
		return score;
	}
	public void setIsVaild(String isVaild) 
	{
		this.isVaild = isVaild;
	}

	public String getIsVaild() 
	{
		return isVaild;
	}
	public void setCreateDate(String createDate) 
	{
		this.createDate = createDate;
	}

	public String getCreateDate() 
	{
		return createDate;
	}
	public void setMembersType(Integer membersType) 
	{
		this.membersType = membersType;
	}

	public Integer getMembersType() 
	{
		return membersType;
	}
	public void setUpgradeDate(String upgradeDate) 
	{
		this.upgradeDate = upgradeDate;
	}

	public String getUpgradeDate() 
	{
		return upgradeDate;
	}
	public void setVipDate(String vipDate) 
	{
		this.vipDate = vipDate;
	}

	public String getVipDate() 
	{
		return vipDate;
	}
	public void setSalt(String salt) 
	{
		this.salt = salt;
	}

	public String getSalt() 
	{
		return salt;
	}
	public void setPhone(String phone) 
	{
		this.phone = phone;
	}

	public String getPhone() 
	{
		return phone;
	}
	public void setCode(String code) 
	{
		this.code = code;
	}

	public String getCode() 
	{
		return code;
	}
	public void setIsSend(String isSend) 
	{
		this.isSend = isSend;
	}

	public String getIsSend() 
	{
		return isSend;
	}
	public void setIsTis(String isTis) 
	{
		this.isTis = isTis;
	}

	public String getIsTis() 
	{
		return isTis;
	}
	public void setSaveToken(String saveToken) 
	{
		this.saveToken = saveToken;
	}

	public String getSaveToken() 
	{
		return saveToken;
	}
	public void setSpareField1(String spareField1) 
	{
		this.spareField1 = spareField1;
	}

	public String getSpareField1() 
	{
		return spareField1;
	}
	public void setSpareField2(String spareField2) 
	{
		this.spareField2 = spareField2;
	}

	public String getSpareField2() 
	{
		return spareField2;
	}
	public void setSpareField3(String spareField3) 
	{
		this.spareField3 = spareField3;
	}

	public String getSpareField3() 
	{
		return spareField3;
	}
	public void setSpareField4(String spareField4) 
	{
		this.spareField4 = spareField4;
	}

	public String getSpareField4() 
	{
		return spareField4;
	}
	public void setSpareField5(String spareField5) 
	{
		this.spareField5 = spareField5;
	}

	public String getSpareField5() 
	{
		return spareField5;
	}
	public void setSpareField6(String spareField6) 
	{
		this.spareField6 = spareField6;
	}

	public String getSpareField6() 
	{
		return spareField6;
	}
	public void setSpareField7(String spareField7) 
	{
		this.spareField7 = spareField7;
	}

	public String getSpareField7() 
	{
		return spareField7;
	}
	public void setSpareField8(String spareField8) 
	{
		this.spareField8 = spareField8;
	}

	public String getSpareField8() 
	{
		return spareField8;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("email", getEmail())
            .append("pwd", getPwd())
            .append("name", getName())
            .append("region", getRegion())
            .append("sex", getSex())
            .append("birthday", getBirthday())
            .append("score", getScore())
            .append("isVaild", getIsVaild())
            .append("createDate", getCreateDate())
            .append("membersType", getMembersType())
            .append("upgradeDate", getUpgradeDate())
            .append("vipDate", getVipDate())
            .append("salt", getSalt())
            .append("phone", getPhone())
            .append("code", getCode())
            .append("isSend", getIsSend())
            .append("isTis", getIsTis())
            .append("saveToken", getSaveToken())
            .append("spareField1", getSpareField1())
            .append("spareField2", getSpareField2())
            .append("spareField3", getSpareField3())
            .append("spareField4", getSpareField4())
            .append("spareField5", getSpareField5())
            .append("spareField6", getSpareField6())
            .append("spareField7", getSpareField7())
            .append("spareField8", getSpareField8())
            .toString();
    }
}
