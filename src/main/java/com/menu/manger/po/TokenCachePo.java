//package com.menu.manger.po;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import com.menu.manger.util.CacheBase;
//import com.ruoyi.system.domain.Members;
//import com.ruoyi.system.mapper.MembersMapper;
//
//@Service
//public class TokenCachePo implements CacheBase<Members, Integer>{
//
//	@Autowired
//	private MembersMapper membersMapper;
//	@Override
//	@Cacheable(value="members")
//	public Members get(Integer id) {
//		return membersMapper.selectMembersById(id);
//	}
//
//	@Override
//	@CacheEvict(value="members")
//	public Members delete(Integer id) {
//		return null;
//	}
//
//	@Override
//	@CachePut(value="members")
//	public Members save(Integer id, Members value) {
//		return value;
//	}
//
//	@Override
//	@CacheEvict(value="members")
//	public Members update(Integer id, Members value) {
//		return value;
//	}
//	
//	
//}
