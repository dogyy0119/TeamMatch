package com.gs.controller;

import com.gs.convert.DefMatchConvert;
import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.dto.def.DefMatchManageDTO;
import com.gs.model.entity.jpa.db1.def.DefMatch;
import com.gs.repository.jpa.def.DefMatchRepository;
import com.gs.service.intf.def.DefMatchManageService;
import com.gs.service.intf.def.DefMatchService;
import com.gs.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Api(tags = "创建自定义比赛数据接口")
@RestController
@RequestMapping("/game/v1.0/app/matches/DefMatch")
@AllArgsConstructor
@CrossOrigin
public class DefMatchController {

    @Autowired
    private DefMatchService defMatchService;

    @Autowired
    private DefMatchManageService defMatchManageService;

    @Autowired
    private DefMatchConvert defMatchConvert;

    @Autowired
    private DefMatchRepository defMatchRepository;

    @ApiOperation(value = "创建自定义比赛")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public R addMatch(@RequestBody DefMatchDTO defMatchDTO) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(0, 100, sort);

        Page<DefMatch> defMatchPage = defMatchService.findMatchesByDate( defMatchDTO.getGameStartTime() ,pageable);
        for (DefMatch entity : defMatchPage) {
            DefMatchDTO dto = defMatchConvert.toDto(entity);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Boolean equels = dto.getGameStartTime().toString().trim().equals(defMatchDTO.getGameStartTime().toString().trim());

            if( dto.getMemberId().equals(defMatchDTO.getMemberId())
                    && dto.getName().equals(defMatchDTO.getName())
                    && sdf.format(dto.getGameStartTime()).trim().equals(sdf.format(defMatchDTO.getGameStartTime()).trim())
            ) {
                System.out.println("DefMatch:" + entity.getId());
                return R.error(" 请勿重复创建！");
            }
        }


        DefMatchDTO dto = defMatchService.create( defMatchDTO );
        if (dto != null) {
            DefMatchManageDTO defMatchManageDTO = new DefMatchManageDTO();

            defMatchManageDTO.setMatchId( dto.getId() );
            defMatchManageDTO.setMemberId( dto.getMemberId() );
            defMatchManageDTO.setMode(dto.getGameMode());
            defMatchManageDTO.setAllOrder( dto.getGameTeamNum() );
            defMatchManageDTO.setCurOrder( 0 );
            defMatchManageService.create(defMatchManageDTO);
        }
        return R.success();
    }

    @ApiOperation(value = "更新自定义比赛")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R updateMatch(@RequestBody DefMatchDTO defMatchDTO) {
        Optional<DefMatch> defMatch = defMatchRepository.findById(defMatchDTO.getId());
        if( !defMatch.isPresent() ) {
            return R.error("主键 id 有误");
        }

        defMatchService.update( defMatchDTO );
        return R.success();
    }

    @ApiOperation(value = "查询自定义比赛")
    @RequestMapping(value = "/getMatchesByID", method = RequestMethod.GET)
    public R getMatchesByID(@RequestParam Long id) {
        DefMatchDTO dto = defMatchService.findById( id );
        return R.success( dto );
    }

    @ApiOperation(value = "删除自定义比赛")
    @RequestMapping(value = "/deleteMatchById", method = RequestMethod.POST)
    public R deleteMatchById(@RequestParam Long id) {
        defMatchService.delete( id );
        return R.success();
    }

    @ApiOperation(value = "按创建者分页查询所有自定义比赛")
    @RequestMapping(value = "/getDefMatchesPageByMember", method = RequestMethod.GET)
    public R getMatchesPageByMember(
            @RequestParam Long memberId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize ){

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<DefMatch> defMatchPage = defMatchService.findMatchesByMember(memberId, pageable);
        List<DefMatchDTO> defMatchDTOS = new ArrayList<>();
        for (DefMatch entity : defMatchPage){
            DefMatchDTO dto =  defMatchConvert.toDto(entity);
            defMatchDTOS.add(dto);
        }

        return R.success(defMatchDTOS);
    }

    @ApiOperation(value = "按日期分页查询自定义比赛")
    @RequestMapping(value = "/getDefMatchesPageByDate", method = RequestMethod.GET)
    public R getDefMatchesPageByDate(
            @RequestParam Date date,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize ) {

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<DefMatch> defMatchPage = defMatchService.findMatchesByDate(date, pageable);
        List<DefMatchDTO> defMatchDTOS = new ArrayList<>();
        for (DefMatch entity : defMatchPage) {
            DefMatchDTO dto = defMatchConvert.toDto(entity);
            defMatchDTOS.add(dto);
        }
        return R.success(defMatchDTOS);
    }

    @ApiOperation(value = "按类型分页查询自定义比赛")
    @RequestMapping(value = "/getDefMatchesPageByMatchType", method = RequestMethod.GET)
    public R getMatchesPageByType(
            @RequestParam String matchType,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize ){

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<DefMatch> defMatchPage = defMatchService.findMatchesByMatchType( matchType, pageable);
        List<DefMatchDTO> defMatchDTOS = new ArrayList<>();
        for (DefMatch entity : defMatchPage){
            DefMatchDTO dto =  defMatchConvert.toDto(entity);
            defMatchDTOS.add(dto);
        }

        return R.success(defMatchDTOS);
    }

    @ApiOperation(value = "按类型分页查询自定义比赛")
    @RequestMapping(value = "/getDefMatchesPageByGameMode", method = RequestMethod.GET)
    public R getMatchesPageByType(
            @RequestParam int gameMode,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize ){

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<DefMatch> defMatchPage = defMatchService.findMatchesByGameMode( gameMode, pageable);
        List<DefMatchDTO> defMatchDTOS = new ArrayList<>();
        for (DefMatch entity : defMatchPage){
            DefMatchDTO dto =  defMatchConvert.toDto(entity);
            defMatchDTOS.add(dto);
        }

        return R.success(defMatchDTOS);
    }

    @ApiOperation(value = "查询所有自己创建比赛")
    @RequestMapping(value = "/getManageMatchesPage", method = RequestMethod.GET)
    public R getManageMatchesPageByMemberId(
            @RequestParam Long memberId,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( defMatchService.getManageMatchesPage( memberId, pageNum, pageSize) );
    }

    @ApiOperation(value = "按名字查询比赛")
    @RequestMapping(value = "/getManageMatchesPageByKey", method = RequestMethod.GET)
    public R getManageMatchesPageByKey(
            @RequestParam String key,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) {

        return R.success( defMatchService.getMatchByKey( key, pageNum, pageSize) );
    }

}
