package com.yr.www.web;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yr.www.entity.*;
import com.yr.www.entity.dto.ManNoticeDto;
import com.yr.www.entity.dto.ManOrgDto;
import com.yr.www.enums.EnumOrgType;
import com.yr.www.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yr123
 * @since 2018-05-23
 */
@Controller
//@RequestMapping("/manOrg")
public class ManOrgController {

    @Autowired
    private ManOrgMapper manOrgMapper;
    @Autowired
    private ManUserMapper manUserMapper;
    @Autowired
    private ManUserOrgMapper manUserOrgMapper;
    @Autowired
    private ManApplyMapper manApplyMapper;
    @Autowired
    private ManNoticeMapper manNoticeMapper;

    @RequestMapping(value = {"/orgList"})
    @ResponseBody
    public Object userList(ManOrg org){
        List<ManOrgDto> orgs = manOrgMapper.selectOrgDtoList();
        orgs.forEach(dto->dto.setOrgTypeName(EnumOrgType.toMap().get(dto.getOrgType())));
        if (!ObjectUtils.isEmpty(org.getAuditStatus())){
            orgs=orgs.stream().filter(dto -> dto.getAuditStatus().equals(org.getAuditStatus())).collect(Collectors.toList());
        }
        if (!ObjectUtils.isEmpty(org.getOrgFounder())){
            orgs=orgs.stream().filter(dto -> dto.getOrgFounder().equals(org.getOrgFounder())).collect(Collectors.toList());
        }
        return JSONObject.toJSON(orgs);
    }

    /**
     * 跳转到社团成员页
     * @param modelAndView
     * @param id
     * @return
     */
    @RequestMapping(value = {"/orgUserPage"})
    public ModelAndView orgUserPage(ModelAndView modelAndView, Integer id){

        modelAndView.addObject("id",id);
        modelAndView.setViewName("user/orgUserList");
        return modelAndView;
    }

    /***
     * 获取社团成员列表
     * @param id
     * @return
     */
    @RequestMapping(value = {"/orgUser"})
    @ResponseBody
    public Object orgUser(Integer id){
        List<ManUserOrg> manUserOrgs = manUserOrgMapper.selectListByOid(id);
        List<Integer> userIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(manUserOrgs)){
            manUserOrgs.forEach(manUserOrg -> userIds.add(manUserOrg.getuId()));
        }
//        List<ManUser> users = manUserMapper.selectList(new EntityWrapper<ManUser>().in("id",userIds));

        List<ManUser> users = manUserMapper.selectListByIds(userIds);
        return JSONObject.toJSON(users);
    }

    /***
     * 社团添加
     * @param org
     * @return
     */
    @RequestMapping(value = {"/orgAdd"})
    @ResponseBody
    public Object orgAdd(ManOrg org){
        org.setGmtCreate(new Date());
        manOrgMapper.insert(org);
        return JSONObject.toJSON("OK");
    }


    /**
     * 社团启用禁用
     * @param org
     * @return
     */
    @RequestMapping(value = {"/orgAble"})
    @ResponseBody
    public Object orgAble(ManOrg org){
        manOrgMapper.updateById(org);
        return JSONObject.toJSON("OK");
    }

    /***
     * 删除社团
     * @param id
     * @return
     */
    @RequestMapping(value = {"/orgDel"})
    @ResponseBody
    @Transactional
    public Object orgDel(Integer id){
        //删除成员关系
        ManUserOrg manUserOrg = new ManUserOrg();
        manUserOrg.setoId(id);
        List<ManUserOrg> manUserOrgs = manUserOrgMapper.selectList(new EntityWrapper<>(manUserOrg));
        List<Integer> ids = new ArrayList<>();
        manUserOrgs.forEach(manUserOrg1 -> ids.add(manUserOrg1.getId()));
        if (!CollectionUtils.isEmpty(ids)){
            manUserOrgMapper.deleteBatchIds(ids);
        }

        //删除社团
        manOrgMapper.deleteById(id);

        return JSONObject.toJSON("OK");
    }

    /***
     * 社团批量删除
     * @param delIds
     * @return
     */
    @RequestMapping(value = {"/orgDelAll"})
    @ResponseBody
    @Transactional
    public Object orgDelAll(Integer[] delIds){
        for (Integer id:delIds
             ) {
            //删除成员关系
            ManUserOrg manUserOrg = new ManUserOrg();
            manUserOrg.setoId(id);
            List<ManUserOrg> manUserOrgs = manUserOrgMapper.selectList(new EntityWrapper<>(manUserOrg));
            List<Integer> ids = new ArrayList<>();
            manUserOrgs.forEach(manUserOrg1 -> ids.add(manUserOrg1.getId()));
            if (!CollectionUtils.isEmpty(ids)){
                manUserOrgMapper.deleteBatchIds(ids);
            }
        }
        manOrgMapper.deleteBatchIds(Arrays.asList(delIds));
        return JSONObject.toJSON("OK");
    }

    /**
     * 社团审核
     * @param org
     * @return
     */
    @RequestMapping(value = {"/orgAudit"})
    @ResponseBody
    @Transactional
    public Object orgAudit(ManOrg org){
        manOrgMapper.updateById(org);
        //创建人加入社团
        if (org.getAuditStatus()==2){
            ManOrg manOrg = manOrgMapper.selectById(org.getId());
            ManUserOrg manUserOrg = new ManUserOrg();
            manUserOrg.setoId(manOrg.getId());
            manUserOrg.setuId(manOrg.getOrgFounder());
            manUserOrg.setGmtCreate(new Date());
            manUserOrgMapper.insert(manUserOrg);
        }

        return JSONObject.toJSON("OK");
    }

    /***
     * 社团修改初始化页面
     * @param modelAndView
     * @param id
     * @return
     */
    @RequestMapping(value = {"/orgInitUpdate"})
    public ModelAndView orgInitUpdate(ModelAndView modelAndView, Integer id){

        ManOrg manOrg = manOrgMapper.selectById(id);
        modelAndView.addObject("org",manOrg);
        modelAndView.setViewName("org/orgInfo");
        return modelAndView;
    }

    /**
     * 社团首页(社团信息+社团公告)
     * @param modelAndView
     * @param id
     * @return
     */
    @RequestMapping(value = {"/orgHome"})
    public ModelAndView orgHome(ModelAndView modelAndView, Integer id){

        ManOrg manOrg = manOrgMapper.selectById(id);
        modelAndView.addObject("org",manOrg);
        ManNotice manNotice = new ManNotice();
        manNotice.setOrgId(id);
        List<ManNoticeDto> notices = manNoticeMapper.selectDtoList(manNotice);
        modelAndView.addObject("notices",notices);
        modelAndView.setViewName("org/orgHome");
        return modelAndView;
    }


    /***
     * 我加入的社团列表
     * @param session
     * @return
     */
    @RequestMapping(value = {"/myOrgList"})
    @ResponseBody
    public Object myOrgList(HttpSession session){
        ManUser manUser = (ManUser) session.getAttribute("sessionUser");
        List<ManOrgDto> manOrgDtos = new ArrayList<>();
        List<Integer> orgIds = new ArrayList<>();
        List<ManUserOrg> manUserOrgs = manUserOrgMapper.selectListByUid(manUser.getId());
        if (!ObjectUtils.isEmpty(manUserOrgs)){
            manUserOrgs.forEach(manUserOrg -> orgIds.add(manUserOrg.getoId()));
            manOrgDtos= manOrgMapper.selectOrgDtoListByIds(orgIds);
            manOrgDtos.forEach(dto->dto.setOrgTypeName(EnumOrgType.toMap().get(dto.getOrgType())));
        }
        return JSONObject.toJSON(manOrgDtos);
    }


    @RequestMapping(value = {"/orgApply"})
    @ResponseBody
    public Object orgApply(ManApply apply){
        ManUserOrg manUserOrg = new ManUserOrg();
        manUserOrg.setoId(apply.getoId());
        manUserOrg.setuId(apply.getuId());
        ManUserOrg userOrg = manUserOrgMapper.selectOne(manUserOrg);
        if (!ObjectUtils.isEmpty(userOrg)){
            return JSONObject.toJSON("您已加入该社团，请勿重复申请！");
        }
        ManApply manApply = new ManApply();
        manApply.setoId(apply.getoId());
        manApply.setuId(apply.getuId());
        if (!ObjectUtils.isEmpty(manApplyMapper.selectOne(manApply))){
            return JSONObject.toJSON("您已提交申请，请等待审核！");
        }
        apply.setGmtCreate(new Date());
        manApplyMapper.insert(apply);
        return JSONObject.toJSON("OK");
    }







}
