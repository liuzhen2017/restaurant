package com.menu.manger.page;

import com.menu.manger.util.Constants;
import com.menu.manger.util.ServletUtils;

/**
 * 表格数据处理
 * 
 * @author ruoyi
 */
public class TableSupport {
	/**
	 * 封装分页对象
	 */
	public static PageDomain getPageDomain() {
		PageDomain pageDomain = new PageDomain();
		pageDomain.setPageNum(ServletUtils
				.getParameterToInt(Constants.PAGE_NUM));
		pageDomain.setPageSize(ServletUtils
				.getParameterToInt(Constants.PAGE_SIZE));
		pageDomain.setOrderByColumn(ServletUtils
				.getParameter(Constants.ORDER_BY_COLUMN));
		pageDomain.setIsAsc(ServletUtils.getParameter(Constants.IS_ASC));
		return pageDomain;
	}

	public static PageDomain buildPageRequest() {
		return getPageDomain();
	}

	/**
	 * @param pageSize
	 * @param pageNum
	 * @param orderBy
	 * @param asai
	 * @return
	 */
	public static PageDomain buildPageRequest(Integer pageSize,
			Integer pageNum, String orderBy, String asai) {
		PageDomain pageDomain = new PageDomain();
		pageDomain.setPageNum(pageNum);
		pageDomain.setPageSize(pageSize);
		pageDomain.setOrderByColumn(orderBy);
		pageDomain.setIsAsc(asai);
		return pageDomain;
	}
}
