package com.menu.manger.service;

import java.util.List;
import java.util.Map;

import com.menu.manger.dto.NoticeInfo;

/**
 * 通知 服务层
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
public interface INoticeInfoService 
{
	/**
     * 查询通知信息
     * 
     * @param id 通知ID
     * @return 通知信息
     */
	public NoticeInfo selectNoticeInfoById(Integer id);
	
	/**
     * 查询通知列表
     * 
     * @param noticeInfo 通知信息
     * @return 通知集合
     */
	public List<NoticeInfo> selectNoticeInfoList(NoticeInfo noticeInfo);
	
	/**
     * 新增通知
     * 
     * @param noticeInfo 通知信息
     * @return 结果
     */
	public int insertNoticeInfo(NoticeInfo noticeInfo);
	
	/**
     * 修改通知
     * 
     * @param noticeInfo 通知信息
     * @return 结果
     */
	public int updateNoticeInfo(NoticeInfo noticeInfo);
		
	/**
     * 删除通知信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteNoticeInfoByIds(String ids);

	public NoticeInfo queryBySouceId(Integer souceId,String tableName);

	/**
	 * 新增通知消息
	 * @param title
	 * @param memId
	 * @param resourceId
	 * @param resourceTable
	 * @param picUrl
	 * @return
	 */
	int insertNoticeInfo(String title, int memId, int resourceId,
			String resourceTable, String picUrl);
	
}