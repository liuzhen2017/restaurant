package com.menu.manger.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.menu.manger.dto.Members;
import com.menu.manger.page.PageDomain;
import com.menu.manger.page.TableDataInfo;
import com.menu.manger.page.TableSupport;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.ThreadLocalUtil;

/**
 * Controller基类
 */
public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(BaseController.class);


	/**
	 * 通过当前线程获取用户信息
	 */
	public Members getUserInfo() {

		Members userInfo = (Members)ThreadLocalUtil.getUserInfo();
		if (userInfo == null) {
			
			return null;
		}
		return userInfo;
	}

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? success() : error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }
    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (null != pageNum && pageSize != null)
        {
            String orderBy = pageDomain.getOrderBy();
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
    /**
     * 设置请求分页数据
     */
    protected void startPage(Integer pageSize,Integer pageNum,String orderBy,String asai)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest(pageSize,pageNum,orderBy,asai);
        if (null != pageNum && pageSize != null)
        {
            orderBy = pageDomain.getOrderBy();
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        rspData.setPageNum(pageNum);
        rspData.setPageSize(pageSize);
        return rspData;
    }
    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回错误码消息
     */
    public AjaxResult error(int code, String message)
    {
        return AjaxResult.error(code, message);
    }

}
