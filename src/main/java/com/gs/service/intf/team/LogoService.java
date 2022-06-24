package com.gs.service.intf.team;

import com.gs.model.vo.team.LogoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LogoService {

    /**
     *  上传logo图片
     * @param file logo文件
     * @param memberId memberId
     * @return Logo实体
     */
    LogoVo uploadImage(MultipartFile file, Long memberId, Integer type);

    /**
     * 根据logoId获取Logo实体
     * @param logoId logoId
     * @return Logo实体
     */
    LogoVo getLogoByLogoId(Long logoId);

    /**
     * 根据teamId获取Logo实体集
     * @param memberId 上传者的ID
     * @return Logo实体集
     */
    List<LogoVo> getLogoList(Long memberId);


    /**
     * 删除Logo实体
     * @param logoId logoId
     * @return 是否成功删除
     */
    Boolean deleteLogo(Long logoId);
}
