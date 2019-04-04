package com.menu.manger.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menu.manger.dto.NoticeInfo;
import com.menu.manger.mapper.NoticeInfoMapper;
import com.menu.manger.service.INoticeInfoService;
import com.menu.manger.util.Convert;

/**
 * 通知 服务层实现
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
@Service
public class NoticeInfoServiceImpl implements INoticeInfoService 
{
	@Autowired
	private NoticeInfoMapper noticeInfoMapper;

	/**
     * 查询通知信息
     * 
     * @param id 通知ID
     * @return 通知信息
     */
    @Override
	public NoticeInfo selectNoticeInfoById(Integer id)
	{
	    return noticeInfoMapper.selectNoticeInfoById(id);
	}
	
	/**
     * 查询通知列表
     * 
     * @param noticeInfo 通知信息
     * @return 通知集合
     */
	@Override
	public List<NoticeInfo> selectNoticeInfoList(NoticeInfo noticeInfo)
	{
	    return noticeInfoMapper.selectNoticeInfoList(noticeInfo);
	}
	
    /**
     * 新增通知
     * 
     * @param noticeInfo 通知信息
     * @return 结果
     */
	@Override
	public int insertNoticeInfo(NoticeInfo noticeInfo)
	{
	    return noticeInfoMapper.insertNoticeInfo(noticeInfo);
	}
	 /**
     * 新增通知
     * 
     * @param noticeInfo 通知信息
     * @return 结果
     */
	@Override
	public int insertNoticeInfo(String title,int memId,int resourceId,String resourceTable,String picUrl)
	{
		//寫入消息
		NoticeInfo noticeInfo =new NoticeInfo();
		noticeInfo.setTitle(title);
		noticeInfo.setCreateDate(new Date());
		noticeInfo.setIsSee("no");
		noticeInfo.setMemId(memId);
		noticeInfo.setResourceId(resourceId);;
		noticeInfo.setResourceTable(resourceTable);
		noticeInfo.setPicUrl(picUrl);
	    return noticeInfoMapper.insertNoticeInfo(noticeInfo);
	}
	/**
     * 修改通知
     * 
     * @param noticeInfo 通知信息
     * @return 结果
     */
	@Override
	public int updateNoticeInfo(NoticeInfo noticeInfo)
	{
	    return noticeInfoMapper.updateNoticeInfo(noticeInfo);
	}

	/**
     * 删除通知对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteNoticeInfoByIds(String ids)
	{
		return noticeInfoMapper.deleteNoticeInfoByIds(Convert.toStrArray(ids));
	}

	@Override
	public NoticeInfo queryBySouceId(Integer souceId,String tableName) {
		NoticeInfo noticeInfo=new NoticeInfo();
		noticeInfo.setId(souceId);
//		tableName =StringUtils.isEmpty(tableName)? tableName :"my_coupon";
//		noticeInfo.setResourceTable("my_coupon");
		List<NoticeInfo> selectNoticeInfoList = selectNoticeInfoList(noticeInfo);
		if(selectNoticeInfoList !=null && selectNoticeInfoList.size() ==1){
			selectNoticeInfoList.get(0).setIsSee("yes");
			selectNoticeInfoList.get(0).setSeeDate(new Date());
			noticeInfoMapper.updateNoticeInfo(selectNoticeInfoList.get(0));
		}
		return selectNoticeInfoList.get(0);
	}
	
}
