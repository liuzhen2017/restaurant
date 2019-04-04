/**
 * 
 */
package com.menu.manger.constants.enmu;

/**
 * @author liuzhen
 *
 */
public class SysEnmu {
	/**
	 * 會員類型
	 * @author liuzhen
	 *
	 */
	public enum  EmmbersType{
		EMMBERS_1(1,"普通會員"),
		EMMBERS_2(2,"VIP會員");
		
		
		public static String getName(int index) {
	        for (EmmbersType c : EmmbersType.values()) {
	        if (c.key == index) {
	            return c.name;
	        }
	        }
	        return null;
	    }
		
		EmmbersType(int key,String name){
			this.name=name;
			this.key =key;
		}
		private int key;
		private String name;
		public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
	/**
	 * 優惠券狀態
	 * 		<option value="1">已发放</option>
									<option value="2">未领取</option>
									<option value="3">已领取</option>
									<option value="4">已使用</option>
									<option value="5">已过期</option>
	 * @author liuzhen
	 *
	 */
	public enum  COUPON_STATUS{
		/**
		 * 未發放
		 */
		COUPON_STATUS_0(0,"未發放"),
		/**
		 * 已发放
		 */
		COUPON_STATUS_1(1,"已发放"),
		/**
		 * 未领取
		 */
		COUPON_STATUS_2(2,"未领取"),
		/**
		 * 已领取
		 */
		COUPON_STATUS_3(3,"已领取"),
		/**
		 * 已使用
		 */
		COUPON_STATUS_4(4,"已使用"),
		/**
		 * 已过期
		 */
		COUPON_STATUS_5(5,"已过期");
		
		
		public static String getName(int index) {
	        for (EmmbersType c : EmmbersType.values()) {
	        if (c.key == index) {
	            return c.name;
	        }
	        }
	        return null;
	    }
		
		COUPON_STATUS(int key,String name){
			this.name=name;
			this.key =key;
		}
		private int key;
		private String name;
		public int getKey() {
			return key;
		}
		public void setKey(int key) {
			this.key = key;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	}
}
