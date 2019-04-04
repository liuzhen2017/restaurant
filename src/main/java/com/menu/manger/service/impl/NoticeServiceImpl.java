package com.menu.manger.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.menu.manger.dto.Notice;
import com.menu.manger.mapper.NoticeMapper;
import com.menu.manger.service.INoticeService;
import com.menu.manger.util.Convert;

/**
 * 通知公告 服务层实现
 * 
 * @author liuzhen
 * @date 2019-02-15
 */
@Service
public class NoticeServiceImpl implements INoticeService 
{
	@Autowired
	private NoticeMapper noticeMapper;

	/**
     * 查询通知公告信息
     * 
     * @param noticeId 通知公告ID
     * @return 通知公告信息
     */
    @Override
	public Notice selectNoticeById(Integer noticeId)
	{
	    return noticeMapper.selectNoticeById(noticeId);
	}
	
	/**
     * 查询通知公告列表
     * 
     * @param notice 通知公告信息
     * @return 通知公告集合
     */
	@Override
	public List<Notice> selectNoticeList(Notice notice)
	{
	    return noticeMapper.selectNoticeList(notice);
	}
	
    /**
     * 新增通知公告
     * 
     * @param notice 通知公告信息
     * @return 结果
     */
	@Override
	public int insertNotice(Notice notice)
	{
	    return noticeMapper.insertNotice(notice);
	}
	
	/**
     * 修改通知公告
     * 
     * @param notice 通知公告信息
     * @return 结果
     */
	@Override
	public int updateNotice(Notice notice)
	{
	    return noticeMapper.updateNotice(notice);
	}

	/**
     * 删除通知公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteNoticeByIds(String ids)
	{
		return noticeMapper.deleteNoticeByIds(Convert.toStrArray(ids));
	}
	
}
