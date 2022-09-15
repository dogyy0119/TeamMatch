package com.gs.service.impl.team;

import com.gs.convert.team.LogoToVoConvert;
import com.gs.model.entity.jpa.db1.team.Logo;
import com.gs.model.vo.team.LogoVo;
import com.gs.repository.jpa.team.LogoRepository;
import com.gs.service.intf.team.LogoService;
import com.gs.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogoServiceLmpl implements LogoService {

    private final LogoRepository logoRepository;
    private final LogoToVoConvert logoToVoConvert;

    @Value("${fileaddress.logoPath}")
    private String logoPath;

    /**
     *  上传logo图片
     * @param file logo文件
     * @param memberId memberId
     * @return Logo实体
     */
    @Override
    public LogoVo uploadImage(MultipartFile file, Long memberId, Integer type){

        String savePath = logoPath + "/" + memberId;
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()) {
            //若不存在该目录，则创建目录
            savePathFile.mkdir();
        }

        //通过UUID生成唯一文件名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());

        String uuid = ServiceUtils.getUUID32();
        String filename = uuid + "." + suffix;
        try {
            //将文件保存指定目录
            //file.transferTo(new File(savePath + filename));
            //File file1 = new File(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(),new File(savePath +"/" + filename));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Logo logo = new Logo();
        logo.setName(filename);
        logo.setMemberId(memberId);
        String url = "/" + memberId + "/" + filename;
        logo.setUrl(url);
        logo.setSize(file.getSize());
        logo.setType(type);
        logo.setCreateTime(new Date());

        logo = logoRepository.save(logo);

        return logoToVoConvert.toVo(logo);
    }

    /**
     * 根据logoId获取Logo实体
     * @param logoId logoId
     * @return Logo实体
     */
    @Override
    public LogoVo getLogoByLogoId(Long logoId){
        return logoToVoConvert.toVo(logoRepository.findLogoById(logoId));
    }

    /**
     * 根据teamId获取Logo实体集
     * @param memberId memberId
     * @return Logo实体集
     */
    @Override
    public List<LogoVo> getLogoList(Long memberId){

        return logoToVoConvert.toVo(logoRepository.findAllByMemberId(memberId));
    }


    /**
     * 删除Logo实体
     * @param logoId logoId
     * @return 是否成功删除
     */
    @Override
    public Boolean deleteLogo(Long logoId){
        logoRepository.deleteById(logoId);
        return true;
    }
}
