package com.gs.service.impl.game;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.gs.config.PUBGConfig;
import com.gs.convert.DefMatchConvert;
import com.gs.convert.PUBGMatchesConvert;
import com.gs.model.dto.def.DefMatchDTO;
import com.gs.model.dto.game.PUBGMatchesDTO;
import com.gs.model.dto.vo.PUBGMatchesVO;
import com.gs.model.entity.jpa.db1.def.DefMatchManage;
import com.gs.model.entity.jpa.db1.def.DefMatchOrder;
import com.gs.model.entity.jpa.db1.def.PersonOrder;
import com.gs.model.entity.jpa.db1.def.TeamOrder;
import com.gs.model.entity.jpa.db1.game.PUBGMatches;
import com.gs.model.entity.jpa.db1.game.PUBGPlayer;
import com.gs.model.entity.jpa.db1.game.PUBGSeason;
import com.gs.model.entity.jpa.db1.game.PUBGTeam;
import com.gs.model.entity.jpa.db1.team.Member;
import com.gs.model.entity.jpa.db1.team.Team;
import com.gs.repository.jpa.def.DefMatchManageRepository;
import com.gs.repository.jpa.def.DefMatchOrderRepository;
import com.gs.repository.jpa.def.PersonOrderRepository;
import com.gs.repository.jpa.def.TeamOrderRepository;
import com.gs.repository.jpa.game.PUBGMatchesRepository;
import com.gs.repository.jpa.game.PUBGPlayerRepository;
import com.gs.repository.jpa.game.PUBGSeasonRepository;
import com.gs.repository.jpa.team.MemberRepository;
import com.gs.repository.jpa.team.TeamRepository;
import com.gs.service.intf.def.DefMatchService;
import com.gs.service.intf.game.PUBGMatchesService;
import com.gs.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class PUBGMatchesServiceImpl implements PUBGMatchesService {

    @Autowired
    private PUBGMatchesRepository pubgMatchesRepository;

    @Autowired
    private PUBGMatchesConvert pubgMatchesConvert;

    @Autowired
    private PUBGConfig pubgConfig = new PUBGConfig();

    @Autowired
    private PUBGSeasonRepository pubgSeasonRepository;

    @Autowired
    private PUBGPlayerRepository pubgPlayerRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DefMatchService defMatchService;

    @Autowired
    private DefMatchManageRepository defMatchManageRepository;

    @Autowired
    private DefMatchConvert defMatchConvert;

    @Autowired
    private DefMatchOrderRepository defMatchOrderRepository;

    @Autowired
    private PersonOrderRepository personOrderRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamOrderRepository teamOrderRepository;

    @Override
    public PUBGMatchesDTO create(String pubgMatchesId, Long defMatchId) {

//        getSeasons();
//        perseMatchData("");
        perseGameData(pubgMatchesId, defMatchId);
        //PUBGMatches pubgMatches = pubgMatchesRepository.save(pubgMatchesConvert.toEntity(dto));
        //return pubgMatchesConvert.toDto( pubgMatches );
        return null;
    }

    public void getSeasons() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", pubgConfig.getHead_token());
        headerMap.put("Accept", pubgConfig.getHead_formate());

        String result = HttpUtils.doGet(pubgConfig.getSeasons(), headerMap);

        JSONObject json = new JSONObject(result);
        JSONArray array = json.getJSONArray("data");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject job = array.getJSONObject(i);

                System.out.println("type: " + job.get("type").toString());
                System.out.println("id: " + job.get("id").toString());
                if (job.get("type").toString().equals("season")) {

                    PUBGSeason pubgSeason = pubgSeasonRepository.findPUBGSeasonByName(job.get("id").toString());
                    if (pubgSeason == null) {
                        pubgSeason = new PUBGSeason();
                        pubgSeason.setName(job.get("id").toString());
                        pubgSeasonRepository.save(pubgSeason);
                    }
                }

            }
        }

    }

    public void perseMatchData(String matchData) {

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", pubgConfig.getHead_token());
        headerMap.put("Accept", pubgConfig.getHead_formate());

//        String name = "MDayln";
        String name = "Roronoa--Zoro-_-";
        String result = HttpUtils.doGet(pubgConfig.getUrl_matches() + name, headerMap);

        System.out.println("result: " + result);

        JSONObject json = new JSONObject(result);
//        JSONObject data = json.getJSONObject("data" );

        JSONArray array = json.getJSONArray("data");
        if (array.size() > 0) {
            for (int i = 0; i < array.size(); i++) {
                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                JSONObject job = array.getJSONObject(i);

                System.out.println("type: " + job.get("type").toString());
                System.out.println("id: " + job.get("id").toString());

                JSONObject relationships = job.getJSONObject("relationships");
                JSONObject matches = relationships.getJSONObject("matches");
                JSONArray dataArray = matches.getJSONArray("data");
                if (dataArray.size() > 0) {
                    for (int j = 0; j < dataArray.size(); j++) {
                        JSONObject match = dataArray.getJSONObject(j);

                        System.out.println("match type: " + match.get("type").toString());
                        System.out.println("match id: " + match.get("id").toString());

                        if (match.get("type").toString().equals("match")) {

                            PUBGMatches pubgMatches = pubgMatchesRepository.findPUBGMatchesByPubgMatchesId(match.get("id").toString());
                            if (pubgMatches == null) {
                                pubgMatches = new PUBGMatches();
                                pubgMatches.setPubgMatchesId(match.get("id").toString());
                                pubgMatchesRepository.save(pubgMatches);
                            }
                        }
                    }
                }

            }
        }
    }


    public void perseGameData(String pubgMatchesId, Long defMatchId) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", pubgConfig.getHead_token());
        headerMap.put("Accept", pubgConfig.getHead_formate());

//        String game = "03cd4ce9-f7fd-49ff-8fd0-5485aa72164e";
//        String game = "357cc5ed-418b-4fb4-8b08-632044860c0e";
//        String game = "99f17ea9-48c3-4949-8629-26597a0fde28";
//        String game = "2db0ffd4-1ebb-42b6-b819-ad81094fab16";

//        List<PUBGMatches> pubgMatchesList = pubgMatchesRepository.findAll();
//        int inum=0;
//        for (PUBGMatches entry : pubgMatchesList) {
//            if (inum>2) break;
//            String game = entry.getId();

            PUBGMatches matches = pubgMatchesRepository.findPUBGMatchesByPubgMatchesId(pubgMatchesId);
            if(matches != null) {
                System.out.println(" matches 已经存在！");
                return;
            }

            String result = HttpUtils.doGet(pubgConfig.getUrl_games() + pubgMatchesId, headerMap);

            if(result == null || result.equals("")) {
                System.out.println( "result is null " );
                return;
            }

            System.out.println( result );

            JSONObject json = new JSONObject(result);
            JSONObject data = json.getJSONObject("data");


            PUBGMatches pubgMatches = new PUBGMatches();
            pubgMatches.setPubgMatchesId( pubgMatchesId );
            pubgMatches.setDefMatchId(defMatchId);
            pubgMatches.setData( result );


            JSONObject attributes = data.getJSONObject("attributes");

            System.out.println("gameMode: " + attributes.get("gameMode").toString());
//            entry.setType( attributes.get("gameMode").toString() );
            pubgMatches.setType( attributes.get("gameMode").toString() );

            JSONObject relationships = data.getJSONObject("relationships");
            JSONObject rosters = relationships.getJSONObject("rosters");
            JSONArray array = rosters.getJSONArray("data");

            Map<String, PUBGPlayer> pubgPlayerMap = new HashMap<>();
            Map<String, Map<String, PUBGPlayer> > teamMap = new HashMap<>();
            Map<String, Integer> teamIndexMap = new HashMap<>();

            if (array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                    JSONObject job = array.getJSONObject(i);

                    System.out.println(job.get("type").toString());
                    System.out.println(job.get("id").toString());

                    teamMap.put( job.get("id").toString(), new HashMap<>());
                }
            }
            System.out.println("-------------------------------");

            JSONArray included = json.getJSONArray("included");
            if (included.size() > 0) {
                for (int i = 0; i < included.size(); i++) {
                    // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                    JSONObject team = included.getJSONObject(i);

                    String type = team.get("type").toString();
                    String id = team.get("id").toString();
                    if (type.equals("participant")) {  // 处理队员数据
                        JSONObject participant_attributes = team.getJSONObject("attributes");
                        JSONObject stats = participant_attributes.getJSONObject("stats");
                        System.out.println("participant + id:" + id + "; kills: " + stats.get("kills").toString());

                        String kills = stats.get("kills").toString();
                        String playerId = stats.get("playerId").toString();
                        String name = stats.get("name").toString();

                        PUBGPlayer pubgPlayer = new PUBGPlayer();

                        pubgPlayer.setKills( kills );
                        pubgPlayer.setPubgPlayerName( name );
                        pubgPlayer.setPubgPlayerId( playerId );
                        //pubgPlayer.setMemberId();

                        pubgPlayerMap.put( id, pubgPlayer );

                    } else if (type.equals("roster")) { // 处理队伍数据
                        JSONObject roster_attributes = team.getJSONObject("attributes");
                        JSONObject stats = roster_attributes.getJSONObject("stats");
                        System.out.println("roster + id:" + id + "; teamId: " + stats.get("teamId").toString());

                        Integer index = Integer.parseInt(stats.get("rank").toString());
                        teamIndexMap.put(id, index);
                        Map<String, PUBGPlayer> pubgPlayerMap1 = teamMap.get( id );

                        JSONObject team_relationships = team.getJSONObject("relationships");
                        JSONObject participants = team_relationships.getJSONObject("participants");
                        JSONArray participants_data = participants.getJSONArray("data");
                        if (participants_data.size() > 0) {
                            for (int j = 0; j < participants_data.size(); j++) {
                                // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                                JSONObject job = participants_data.getJSONObject(j);

                                System.out.println(job.get("type").toString());
                                System.out.println(job.get("id").toString());
                                PUBGPlayer pubgPlayer = new PUBGPlayer();
                                pubgPlayerMap1.put(job.get("id").toString(), pubgPlayer);
                            }
                        }

                    }
                }
            }

        List<PUBGTeam>  pubgTeamList = new ArrayList<>();
        Iterator<Map.Entry<String, Map<String, PUBGPlayer> >> iterator = teamMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Map<String, PUBGPlayer> > entry = iterator.next();
            String pubgteam = entry.getKey();

            PUBGTeam pubgTeam = new PUBGTeam();
            pubgTeam.setPubgTeamId( pubgteam );
            pubgTeam.setIndex( teamIndexMap.get(pubgteam) );
            List<PUBGPlayer>  pubgPlayerList = new ArrayList<>();

            Map<String, PUBGPlayer> playerMaps = teamMap.get( pubgteam );
            Iterator<Map.Entry<String, PUBGPlayer >> iteratorplay = playerMaps.entrySet().iterator();
            while (iteratorplay.hasNext()) {
                Map.Entry<String, PUBGPlayer> entryPlayer = iteratorplay.next();
                String player = entryPlayer.getKey();

                PUBGPlayer pubgPlayer = pubgPlayerMap.get(player);
                pubgPlayerList.add(pubgPlayer);
            }
            pubgTeam.setTeamMembers( pubgPlayerList );
            pubgTeamList.add( pubgTeam );
        }
        pubgMatches.setTeamMembers( pubgTeamList );
        pubgMatchesRepository.save( pubgMatches );

    }

    @Override
    public void delete(String id) {
        Optional<PUBGMatches> optionalNews = pubgMatchesRepository.findById(id);
        if (optionalNews.isPresent()) {
            pubgMatchesRepository.deletePUBGMatchesByPubgMatchesIdStartsWith(id);
        }
    }

    @Override
    public PUBGMatchesDTO getPUBGMatchesByDefMatchId(Long memberId, Long defMatchId, Integer pageNum, Integer pageSize) {
        PUBGMatches pubgMatches = pubgMatchesRepository.findPUBGMatchesByDefMatchId(defMatchId);
        if(pubgMatches == null) return null;
        return pubgMatchesConvert.toDto( pubgMatches );
    }

    @Override
    public List<PUBGMatchesDTO> getPUBGMatchesByMatchType(Long memberId, String matchType, Integer pageNum, Integer pageSize) {

        Page<PUBGPlayer> ordersPage = getPUBGPlayerPage(memberId, pageNum, pageSize);

        List<PUBGPlayer> pubgPlayerList = new ArrayList<>();

        for(PUBGPlayer player: ordersPage) {
            pubgPlayerList.add(player);
        }

        System.out.println( "pubgPlayerList size:" + pubgPlayerList.size());
        if(pubgPlayerList.size() == 0) return null;

        return getMatchesByPlayers(pubgPlayerList, matchType, pageNum, pageSize);
    }

    @Override
    public List<PUBGMatchesVO> getPUBGMatchesSortByMatchType(Long memberId, Long teamId, String matchType, Integer pageNum, Integer pageSize) {

        Page<PUBGPlayer> ordersPage = getPUBGPlayerPage(memberId, pageNum, pageSize);

        List<PUBGPlayer> pubgPlayerList = new ArrayList<>();

        for(PUBGPlayer player: ordersPage) {
            pubgPlayerList.add(player);
        }

        if(pubgPlayerList.size() == 0) return null;

        List<PUBGMatchesVO>  pubgMatchesVOS = new ArrayList<>();
        for (PUBGPlayer pubgPlayer : pubgPlayerList) {
            PUBGTeam pubgTeam = pubgPlayer.getPubgTeam();
            PUBGMatches pubgMatches = pubgTeam.getPubgMatchesId();

            PUBGMatchesDTO pubgMatchesDTO = pubgMatchesConvert.toDto( pubgMatches );
            System.out.println( "PUBGMatchesDTO PubgMatchesId:" + pubgMatchesDTO.getPubgMatchesId());
            if(!matchType.equals("") || !matchType.equals(" ")) {
                if(matchType.equals(pubgMatchesDTO.getType())) {
                    DefMatchDTO defMatchDTO = defMatchService.findById( pubgMatches.getDefMatchId() );
                    PUBGMatchesVO pubgMatchesVO = new PUBGMatchesVO();
                    pubgMatchesVO.setDefMatchName( defMatchDTO.getName() );
                    pubgMatchesVO.setGameTime( defMatchDTO.getGameStartTime() );
                    pubgMatchesVO.setDefMatchId( defMatchDTO.getId() );
                    pubgMatchesVO.setIndex(pubgTeam.getIndex());

                    DefMatchDTO defMatch = defMatchService.findById(defMatchDTO.getId());
                    Member member = memberRepository.findMemberById(memberId);
                    DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByDefMatch(defMatchConvert.toEntity(defMatch));
                    if(defMatch.getGameMode() == 0) {
                        DefMatchOrder defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, member.getId());
                        PersonOrder personOrder = personOrderRepository.findPersonOrderByDefMatchOrderAndMember(defMatchOrder, member);
                        if(personOrder != null) pubgMatchesVO.setIsLike(personOrder.getIsLike());
                    } else {
                        DefMatchOrder defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, teamId );
                        System.out.println( "defMatchManage：" + defMatchManage.getId());
                        System.out.println( "teamId：" + teamId);
                        System.out.println( "defMatchOrder.getOrderId() :" + defMatchOrder.getOrderId());

                        Team team = teamRepository.findTeamById(defMatchOrder.getOrderId());
                        if(team != null) {
                            TeamOrder teamOrder = teamOrderRepository.findTeamOrderByDefMatchOrderAndMember(defMatchOrder, member);
                            if (teamOrder != null) pubgMatchesVO.setIsLike(teamOrder.getIsLike());
                        }
                    }
                    pubgMatchesVOS.add(pubgMatchesVO);

                }
            } else {
                DefMatchDTO defMatchDTO = defMatchService.findById( pubgMatches.getDefMatchId() );
                PUBGMatchesVO pubgMatchesVO = new PUBGMatchesVO();
                pubgMatchesVO.setDefMatchName( defMatchDTO.getName() );
                pubgMatchesVO.setGameTime( defMatchDTO.getGameStartTime() );
                pubgMatchesVO.setDefMatchId( defMatchDTO.getId() );
                pubgMatchesVO.setIndex(pubgTeam.getIndex());

                DefMatchDTO defMatch = defMatchService.findById(defMatchDTO.getId());
                Member member = memberRepository.findMemberById(memberId);
                DefMatchManage defMatchManage = defMatchManageRepository.findDefMatchManageByDefMatch(defMatchConvert.toEntity(defMatch));
                if(defMatch.getGameMode() == 0) {
                    DefMatchOrder defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, member.getId());
                    PersonOrder personOrder = personOrderRepository.findPersonOrderByDefMatchOrderAndMember(defMatchOrder, member);
                    if(personOrder != null) pubgMatchesVO.setIsLike(personOrder.getIsLike());
                } else {
                    DefMatchOrder defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, teamId );
                    defMatchOrder = defMatchOrderRepository.findDefMatchOrderByDefMatchManageAndOrderId(defMatchManage, teamId);
                    Team team = teamRepository.findTeamById(defMatchOrder.getOrderId());
                    if(team != null) {
                        TeamOrder teamOrder = teamOrderRepository.findTeamOrderByDefMatchOrderAndMember(defMatchOrder, member);
                        if (teamOrder != null) pubgMatchesVO.setIsLike(teamOrder.getIsLike());
                    }
                }

                pubgMatchesVOS.add(pubgMatchesVO);

                pubgMatchesVOS.add(pubgMatchesVO);
            }
        }
        return pubgMatchesVOS;
    }

    @Override
    public List<PUBGMatchesDTO> findPUBGMatchesByPUBGPlayerNameAndMatchType(String PUBGPlayerName, String matchType, Integer pageNum, Integer pageSize) {

        List<PUBGPlayer> pubgPlayerList = pubgPlayerRepository.findAllPUBGPlayerByPubgPlayerName( PUBGPlayerName );

        if(pubgPlayerList.size() == 0) return null;

        return getMatchesByPlayers(pubgPlayerList, matchType, pageNum, pageSize);
    }

    @Override
    public List<PUBGMatchesDTO> findPUBGMatchesByPUBGPlayerIdAndMatchType(String PUBGPlayerId, String matchType, Integer pageNum, Integer pageSize) {

        List<PUBGPlayer> pubgPlayerList = pubgPlayerRepository.findPUBGPlayerByPubgPlayerId( PUBGPlayerId );

        if(pubgPlayerList.size() == 0) return null;

        return getMatchesByPlayers(pubgPlayerList, matchType, pageNum, pageSize);
    }

    private Page<PUBGPlayer> getPUBGPlayerPage(Long memberId, Integer pageNum, Integer pageSize) {
        String PUBGPlayerName = memberRepository.findMemberById(memberId).getPubgName();
        if(PUBGPlayerName == null || PUBGPlayerName.equals("")) {
            return null;
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<PUBGPlayer> ordersPage = pubgPlayerRepository.findAll(new Specification<PUBGPlayer>() {

            public Predicate toPredicate(Root<PUBGPlayer> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<Long> pubgPlayerNamePath = root.get("pubgPlayerName");

                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(cb.equal( pubgPlayerNamePath, PUBGPlayerName));
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        return ordersPage;
    }

    private  List<PUBGMatchesDTO> getMatchesByPlayers( List<PUBGPlayer> pubgPlayerList, String matchType, Integer pageNum, Integer pageSize){
        List<PUBGMatchesDTO>  pubgMatchesList = new ArrayList<>();
        for (PUBGPlayer pubgPlayer : pubgPlayerList) {
            PUBGTeam pubgTeam = pubgPlayer.getPubgTeam();
            PUBGMatches pubgMatches = pubgTeam.getPubgMatchesId();

            PUBGMatchesDTO pubgMatchesDTO = pubgMatchesConvert.toDto( pubgMatches );
            System.out.println( "PUBGMatchesDTO PubgMatchesId:" + pubgMatchesDTO.getPubgMatchesId());
            if(!matchType.equals("") || !matchType.equals(" ")) {
                if(matchType.equals(pubgMatchesDTO.getType())) {
                    pubgMatchesList.add(pubgMatchesDTO);
                }
            } else {
                pubgMatchesList.add(pubgMatchesDTO);
            }
        }

        return pubgMatchesList;
    }

}
