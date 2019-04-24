package com.menu.manger.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.menu.manger.constants.HttpConstants;
import com.menu.manger.dto.AccountFlow;
import com.menu.manger.dto.BranchStore;
import com.menu.manger.dto.IntegralRole;
import com.menu.manger.dto.Members;
import com.menu.manger.dto.MenuFoodExchange;
import com.menu.manger.dto.ScoreHis;
import com.menu.manger.dto.SendMessager;
import com.menu.manger.dto.SysConfig;
import com.menu.manger.mapper.MembersMapper;
import com.menu.manger.mapper.MenuFoodExchangeMapper;
import com.menu.manger.mapper.SendMessagerMapper;
import com.menu.manger.service.IAccountFlowService;
import com.menu.manger.service.IBranchStoreService;
import com.menu.manger.service.IIntegralRoleService;
import com.menu.manger.service.IMembersService;
import com.menu.manger.service.INoticeInfoService;
import com.menu.manger.service.IScoreHisService;
import com.menu.manger.service.ISysConfigService;
import com.menu.manger.util.AESUtil;
import com.menu.manger.util.AjaxResult;
import com.menu.manger.util.BeanUtils;
import com.menu.manger.util.Convert;
import com.menu.manger.util.DateUtils;
import com.menu.manger.util.JsonWebTokenUtil;
import com.menu.manger.util.SMSDto;
import com.menu.manger.util.SendEmailUtil;
import com.menu.manger.util.SendEmaill;
import com.menu.manger.util.ServiceUtil;
import com.menu.manger.util.ThreadLocalUtil;

/**
 * 会员管理 服务层实现
 * 
 * @author liuzhen
 * @date 2019-01-22
 */
@Service
public class MembersServiceImpl implements IMembersService {
	@Autowired
	private MembersMapper membersMapper;

	@Autowired
	private SendMessagerMapper sendMessagerMapper;
	@Autowired
	private SendEmailUtil sendEmailUtil;
	@Autowired
	ISysConfigService configService;
	@Autowired
	private MenuFoodExchangeMapper menuFoodExchangeMapper;;
	@Autowired
	INoticeInfoService noticeInfoService;

	@Autowired
	IIntegralRoleService roleService;
	@Autowired
	IScoreHisService scoreHisService;
	@Autowired
	ISysConfigService sysConfigService;
	@Autowired
	IAccountFlowService accountFlowService;
	@Autowired
	IBranchStoreService branchStoreSerivce;
	private static final Logger log = LoggerFactory
			.getLogger(MembersServiceImpl.class);

	/**
	 * 查询会员管理信息
	 * 
	 * @param id
	 *            会员管理ID
	 * @return 会员管理信息
	 */
	@Override
	public Members selectMembersById(Integer id) {
		return membersMapper.selectMembersById(id);
	}

	/**
	 * 查询会员管理列表
	 * 
	 * @param members
	 *            会员管理信息
	 * @return 会员管理集合
	 */
	@Override
	public List<Members> selectMembersList(Members members) {
		return membersMapper.selectMembersList(members);
	}

	/**
	 * 新增会员管理
	 * 
	 * @param members
	 *            会员管理信息
	 * @return 结果
	 */
	@Override
	public int insertMembers(Members members) {
		// members.setCreateBy(ShiroUtils.getLoginName() + "");
		members.setCreateDate(DateFormatUtils.format(new Date(),
				"yyyy-MM-dd HH-mm-ss"));
		return membersMapper.insertMembers(members);
	}

	/**
	 * 修改会员管理
	 * 
	 * @param members
	 *            会员管理信息
	 * @return 结果
	 */
	@Override
	public int updateMembers(Members members) {
		return membersMapper.updateMembers(members);
	}

	/**
	 * 删除会员管理对象
	 * 
	 * @param ids
	 *            需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteMembersByIds(String ids) {
		return membersMapper.deleteMembersByIds(Convert.toStrArray(ids));
	}

	@Override
	public AjaxResult login(String email, String passWord)
			throws JsonProcessingException, IllegalArgumentException,
			JWTCreationException, UnsupportedEncodingException {
		Members dbMem = membersMapper.selectMembersByEmail(email);

		if (dbMem != null) {
			if (dbMem.getIsVaild().equals("no")) {
				return AjaxResult.error("该該賬號狀態異常!");
			} else if (StringUtils.isEmpty(passWord)
					|| !encryptPassword(dbMem.getEmail(), passWord,
							dbMem.getSalt()).equals(dbMem.getPwd())) {
				return AjaxResult.error("密碼錯誤!");
			}
			Members backMem =dbMem;
			backMem.setPwd(null);
			Date dt =new Date();
			String token =JsonWebTokenUtil.sign(backMem,dt);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("tokent", token);
			data.put("membersInfo", backMem);
			data.put("myscorp", backMem.getScore());
			dbMem.setSaveToken(new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").format(dt));
			membersMapper.updateMembers(dbMem);
			return AjaxResult.success("登陸成功!", data);
		}
		return AjaxResult.error("該賬號不存在!");
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public AjaxResult regist(Members members) throws JsonProcessingException,
			IllegalArgumentException, JWTCreationException,
			UnsupportedEncodingException {
		Lock lock = new ReentrantLock();
		lock.lock();
		Members dbMembers = membersMapper.selectMembersByEmail(members
				.getEmail());

		if (!isEmail(members.getEmail())) {
			return AjaxResult.error("Email格式不正確!");
		}
		if (dbMembers != null) {
			return AjaxResult.error("註冊失敗!該email已經存在!");
		}
		if (StringUtils.isEmpty(members.getPhone())) {
			return AjaxResult.error("手机号不能为空");
		}
		Members phoneMembers = membersMapper.selectMembersByPhone(members
				.getPhone());
		if (phoneMembers != null) {
			return AjaxResult.error("註冊失敗!該電話已經存在!");
		}
		int code = (int) ((Math.random() * 9 + 1) * 100000);

		members.setCreateDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		Map<String, Object> map = new HashMap<>();
		int memCode = (int) ((Math.random() * 11 + 1) * 100000000);
		map.put("key", "salt");
		map.put("value", code);
		Members selectMembersByKey = membersMapper.selectMembersByKey(map);
		while (selectMembersByKey != null) {
			code = (int) ((Math.random() * 9 + 1) * 100000);
			map.put("value", code);
			selectMembersByKey = membersMapper.selectMembersByKey(map);
		}
		members.setSalt(code + "");
		members.setPwd(encryptPassword(members.getEmail(), members.getPwd(),
				members.getSalt()));
		members.setCreateBy("admin");

		map.put("key", "code");
		map.put("value", memCode);
		selectMembersByKey = membersMapper.selectMembersByKey(map);
		while (selectMembersByKey != null) {
			memCode = (int) ((Math.random() * 11 + 1) * 100000000);
			map.put("value", memCode);
			selectMembersByKey = membersMapper.selectMembersByKey(map);
		}
		//查找導入積分
		ScoreHis scoreHis =new ScoreHis();
		scoreHis.setMembersId(-1);
		scoreHis.setMembersName(members.getPhone());
		members.setScore(0);
		List<ScoreHis> selectScoreHisList = scoreHisService.selectScoreHisList(scoreHis);
		if(selectScoreHisList !=null && selectScoreHisList.size() >0){
			members.setScore(selectScoreHisList.get(0).getNewScore());
		}
		members.setCode(memCode + "");
		
		// 查找待領取的優惠券信息
		MenuFoodExchange menuFoodExchange = new MenuFoodExchange();
		menuFoodExchange.setMembersId(Long.valueOf(members.getPhone()
				.replace("+", "").replace("-", "").trim()));
		List<MenuFoodExchange> selectMenuFoodExchangeList = menuFoodExchangeMapper
				.selectMenuFoodExchangeList(menuFoodExchange);
		membersMapper.insertMembers(members);
		if (selectMenuFoodExchangeList != null
				&& selectMenuFoodExchangeList.size() > 0) {
			for (MenuFoodExchange menuFoodExchange2 : selectMenuFoodExchangeList) {
				menuFoodExchange2.setMembersId(members.getId());
				menuFoodExchangeMapper
						.updateMenuFoodExchange(menuFoodExchange2);
				noticeInfoService.insertNoticeInfo("收到好友贈送優惠券通知",
						members.getId(), menuFoodExchange2.getId().intValue(),
						"MenuFoodExchange", menuFoodExchange2.getMenuFoodPic());
			}
		}
		//
		
		
		Members backMem = members;
		backMem.setPwd(null);
		Date dt =new Date();
		String token =JsonWebTokenUtil.sign(backMem,dt);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("tokent", token);
		data.put("membersInfo", backMem);
		data.put("myscorp", backMem.getScore());
		
		Members selectMembersById = membersMapper.selectMembersById(members.getId());
		selectMembersById.setSaveToken(new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").format(dt));
		membersMapper.updateMembers(selectMembersById);
		
		lock.unlock();
		return AjaxResult.success("恭喜您,註冊成功!", data);
	}

	@Override
	public AjaxResult sendEmail(String email) {
		SendEmaill sendEm = new SendEmaill();
		Members byEmail = membersMapper.selectMembersByEmail(email);
		if (byEmail == null) {
			return AjaxResult.error("根據郵箱查詢錯誤,無此用戶!");
		}
		SysConfig selectByKey = configService.selectByKey("sendEmail");
		JSONObject jb = JSONObject.parseObject(selectByKey.getConfigValue());
		sendEm.setSendTo(byEmail.getEmail());
		sendEm.setTitle(jb.getString("send_title"));
		try {
			String token = JsonWebTokenUtil.sign(byEmail, 1000 * 60 * 60 * 2,new Date());
			sendEm.setSendContent(jb.getString("send_content") + "<a href='"
					+ jb.getString("retrieve_url") + "?token=" + token
					+ "'>找回密碼</a>");
			sendEmailUtil.sendEmail(sendEm);
			return AjaxResult.success("郵件發送成功,請注意查收!");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("郵件發送失敗!" + e.getMessage());
		}
	}

	@Override
	public AjaxResult checkTokenEmail(String token) throws JsonParseException,
			JsonMappingException, IllegalArgumentException, IOException {
		Members members = JsonWebTokenUtil.unsign(token, Members.class);
		if (members == null) {
			return AjaxResult.error("郵件已經過期,請重新發送郵件");
		}
		return AjaxResult.success(null,
				JsonWebTokenUtil.sign(members, 60 * 20 * 60,new Date()));
	}

	@Override
	public AjaxResult restPwd(String oepnId) {
		return null;
	}

	@Override
	public AjaxResult updatePwd(String passWord) {
		Members loginMem = (Members) ThreadLocalUtil.getUserInfo();
		if (loginMem == null) {
			return AjaxResult.error(4444, "沒有用戶信息,請確認!");
		}
		Members dbMem = membersMapper.selectMembersByEmail(loginMem.getEmail());
		if (dbMem != null) {
			dbMem.setPwd(encryptPassword(dbMem.getEmail(), passWord,
					dbMem.getSalt()));
			membersMapper.updateMembers(dbMem);

			return AjaxResult.success("修改密碼成功!請牢記新密碼!");
		}
		return AjaxResult.error(4444, "根據郵箱查詢不到數據,請確認操作!");
	}

	@Override
	public AjaxResult forgetEmaill(String phone) {

		Members members = new Members();
		members.setPhone(phone);
		members.setIsVaild("yes");
		List<Members> selectMembersList = membersMapper
				.selectMembersList(members);
		if (selectMembersList != null && selectMembersList.size() > 0) {
			SMSDto sms = new SMSDto();
			// sms.setUserID(userId);
			// sms.setUserPassword(UserPassword);
			double code = ((Math.random() * 9 + 1) * 100000);
			// 6位隨機數
			// sms.setMessageBody(content + code);
			// sms.setMessageType(MessageType);
			sms.setMessageReceiver(phone);
			AjaxResult sendMsg = null;// SMSUtil.sendMsg(sms, smsUrl);
			if (sendMsg.get("code").toString().equals("0")) {
				SMSDto dto = (SMSDto) sendMsg.get("data");
				SendMessager sendMessager = new SendMessager();
				BeanUtils.copyBeanProp(sendMessager, dto);
				// 保存發送歷史
				sendMessager.setCreateDate(new Date());
				sendMessager.setSendCode(code + "");
				;
				sendMessagerMapper.insertSendMessager(sendMessager);
			} else {
				return sendMsg;
			}
		}
		return AjaxResult.error("根據手機號查詢不到用戶信息!請確認手機號");
	}

	@Override
	public AjaxResult vaildCode(String phone, String code) {
		SendMessager sendMessagerByPhone = sendMessagerMapper
				.selectSendMessagerByPhone(phone);
		if (sendMessagerByPhone == null) {
			return AjaxResult.error("該驗證碼不存在或者已經過期,有效期在20分鐘之内 ..");
		}
		if (sendMessagerByPhone.getSendCode().equals(code)) {
			sendMessagerByPhone.setIsVaild("no");
			//sendMessagerMapper.updateSendMessager(sendMessagerByPhone);
			return AjaxResult.success("驗證成功!");
		}
		return AjaxResult.error("驗證碼錯誤");
	}

	public String encryptPassword(String username, String password, String salt) {
		return new Md5Hash(username + password + salt).toHex().toString();
	}

	@Override
	public AjaxResult findEmail(String phone, String code) {
		SendMessager sendMessagerByPhone = sendMessagerMapper
				.selectSendMessagerByPhone(phone);
		if (sendMessagerByPhone == null) {
			return AjaxResult.error("該驗證碼不存在或者已經過期,有效期在20分鐘之内 ..");
		}
		if (sendMessagerByPhone.getSendCode().equals(code)) {
			sendMessagerByPhone.setIsVaild("no");
			sendMessagerMapper.updateSendMessager(sendMessagerByPhone);

			Members members = new Members();
			members.setPhone(phone);
			members.setIsVaild("yes");
			List<Members> selectMembersList = membersMapper
					.selectMembersList(members);
			if (selectMembersList != null && selectMembersList.size() > 0) {
				return AjaxResult.success("驗證成功!", selectMembersList.get(0)
						.getEmail());
			} else {
				return AjaxResult.error("該手機號未曾綁定過郵箱!");
			}
		}
		return AjaxResult.error("驗證碼錯誤");
	}

	public static boolean isEmail(String string) {
		if (string == null)
			return false;
		String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p;
		Matcher m;
		p = Pattern.compile(regEx1);
		m = p.matcher(string);
		if (m.matches())
			return true;
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.menu.manger.service.IMembersService#saveIntegral(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public AjaxResult saveIntegral(String info)
			throws UnsupportedEncodingException, NumberFormatException, ParseException {
		Lock lock =new ReentrantLock();
		lock.lock();
		log.info("request parmat ={}", info);
		info=info.split("a/qrCode/s=")[1].replace("&brandId=100", "").replace("&pos=seito","");
		log.info("request replace head,food ={}", info);
		info = AESUtil.AES_CBC_Decrypt(info);
		log.info("decrypt Info={}", info);
		if(StringUtils.isEmpty(info)){
			return AjaxResult.error("解析二維碼錯誤!");
		}
		String [] result =info.split(";");
		
		AccountFlow accountFlow =new AccountFlow();
		//判斷時間,是否是配置時間
		accountFlow.setInvoiceNo(result[2]);
		List<AccountFlow> selectAccountFlowList2 = accountFlowService.selectAccountFlowList(accountFlow);
		if(selectAccountFlowList2 ==null || selectAccountFlowList2.size() ==0) {
			return AjaxResult.error("很抱歉,该账单没有同步到系统里面");
		}
		if(selectAccountFlowList2.get(0).getMenuId() != null) {
			return AjaxResult.error("该二维码已经积分过了,不允许重复积分");
		}
		SysConfig invaildDay = sysConfigService.selectByKey(HttpConstants.qcSaveDate);
		String transactionDatetime =result[3];
		transactionDatetime =transactionDatetime.substring(0, 8);
		Date tranctionDate= DateUtils.addDays(DateUtils.parseDate(transactionDatetime, "yyyyMMdd"),Integer.parseInt(invaildDay.getConfigValue()));
		if(tranctionDate.before(new Date())){
			return AjaxResult.error("很抱歉，該交易已經過了積分有效期,交易時間:"+transactionDatetime+",有效天數:"+invaildDay.getConfigValue());
		}
		
		Members loginUser =  (Members) ThreadLocalUtil.getUserInfo();

		Members selectMembersById = membersMapper.selectMembersById(loginUser.getId());
		log.info("submitRedemption 3:填写账户流水 ");
		
		
		BranchStore branchStore = new BranchStore();
		branchStore.setStoreNo(Integer.parseInt(result[0] + ""));
		List<BranchStore> selectBranchStoreList = branchStoreSerivce.selectBranchStoreList(branchStore);
		accountFlow.setBranchStoreId(result[0]+"");
		BranchStore selectBranchStoreById = selectBranchStoreList.size() == 0 ? null
				: selectBranchStoreList.get(0);
		if (selectBranchStoreById != null) {
			accountFlow
					.setBranchStoreName(selectBranchStoreById.getStoreName());
		}
		selectAccountFlowList2.get(0).setMenuId(loginUser.getId());;
		accountFlowService.updateAccountFlow(selectAccountFlowList2.get(0));
		ScoreHis scoreHis = new ScoreHis();
		// 查詢積分規則
		log.info("submitRedemption 3:计算积分 ");
		IntegralRole byRole = roleService.selectByRoleByintegralType(loginUser
				.getMembersType(),null);
		if (byRole == null) {
			log.info("submitRedemption 3.1:積分規則爲空,本次交易沒計算積分 ");
		} else {
			//非會員
			if(selectAccountFlowList2.get(0).getNetAmount().intValue() > 2 && loginUser.getMembersType() ==0
			|| selectAccountFlowList2.get(0).getNetAmount().intValue() > 1 && loginUser.getMembersType() ==1){
				log.info("本次交易小於規定金額,不計入積分!");
			}else {
				//非會員
				if(selectAccountFlowList2.get(0).getNetAmount().intValue() < 2 && loginUser.getMembersType() ==0
						|| selectAccountFlowList2.get(0).getNetAmount().intValue() < 1 && loginUser.getMembersType() ==1){
					log.info("本次交易小於規定金額,不計入積分!");
				}else {
					Double score = byRole.getScoreValue()
							* Double.parseDouble(selectAccountFlowList2.get(0).getNetAmount() + "");
					// 記錄積分

					scoreHis.setDescribes("扫描二维码积分:" + score);
					scoreHis.setMembersId(loginUser.getId());
					scoreHis.setOlbScore(selectMembersById.getScore());
					scoreHis.setNewScore(selectMembersById.getScore() + score.intValue());
					scoreHis.setCreatedDate(new Date());
					scoreHis.setBusiId(loginUser.getId() + "");
					scoreHisService.insertScoreHis(scoreHis);
					// 判斷用戶消費，是否滿足自動升級會員
					if (selectMembersById.getMembersType() == 0) {
						IntegralRole selectByRoleByintegralType = roleService.selectByRoleByintegralType(selectMembersById.getMembersType(), 2);
						int money = 0;
						if (selectByRoleByintegralType == null) {
							String queryCigKey = HttpConstants.autoUpgradingMoney;
							if (selectMembersById.getMembersType() == HttpConstants.EmmbersType_1) {
								queryCigKey = HttpConstants.autoUpgradingMoneyVIP;
							}
							SysConfig selectByKey = sysConfigService.selectByKey(queryCigKey);
							money = Integer.parseInt(selectByKey.getConfigValue());
						} else {
							money = (int) selectByRoleByintegralType.getScoreValue();
						}
						int moneyByMemId = accountFlowService.selectAccountMoneyByMemId(selectMembersById.getId());
						if (moneyByMemId >= money) {
							// 如果是會員
							Date vipDateEnd = new Date();
							if (loginUser.getMembersType() == HttpConstants.EmmbersType_1) {
								Date dateTime = DateUtils.dateTime("yyyyMMdd",
										loginUser.getVipDate());
								if (dateTime.after(new Date())) {
									// 如果會員沒有過期，則過期時間 +1年 20191230 + 1 =20201230
									vipDateEnd = DateUtils.addYears(dateTime, 1);
								}
								loginUser.setUpgradeDate(DateUtils.parseDateToStr(
										"yyyyMMdd", new Date()));
								loginUser.setVipDate(DateUtils.parseDateToStr("yyyyMMdd",
										vipDateEnd));
							}
							noticeInfoService.insertNoticeInfo(
									"消費金額滿" + money + " 積分自動升級通知",
									loginUser.getId(),
									0,
									"noticeType",
									"恭喜您：本年度" + DateUtils.dateTime() + ", 消費金額滿"
											+ money
											+ " 積分自動升級,享受VIP優惠,該優惠于："
											+ loginUser.getVipDate() + "失效.");
						}
					}
					// 修改用户积分
					loginUser.setScore(scoreHis.getNewScore());
					membersMapper.updateMembers(loginUser);
				}
			}
		}
		noticeInfoService.insertNoticeInfo("掃碼儲存積分通知", loginUser.getId(), 0,
				"noticeType", "尊敬的會員,掃碼儲存積分:" + scoreHis.getNewScore()
						+ ",纍計積分為:" + loginUser.getScore());
		try {
			return AjaxResult.success(0, "掃碼儲存積分成功!",
					ServiceUtil.tokenByUser(loginUser));
			
		} catch (JsonProcessingException | IllegalArgumentException
				| JWTCreationException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return AjaxResult.success("掃碼儲存積分成功!");
		}finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		String info="http://flqd.majiangyun.com:8899/a/qrCode/s=cMgKCuc7QsZwNTbtGeOmKecLQ+E8CrBb6cCQVkDmmO0=&pos=seito";
		info=info.split("a/qrCode/s=")[1].replace("&brandId=100", "").replace("&pos=seito","");
		System.out.println(info);
	}
}
