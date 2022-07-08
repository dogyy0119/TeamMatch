package com.gs.service.impl.league;

import com.gs.convert.league.LeagueMessageDtoConvert;
import com.gs.convert.league.LeagueMessageVoConvert;
import com.gs.convert.team.MessageDtoConvert;
import com.gs.convert.team.MessageVoConvert;
import com.gs.model.dto.league.LeagueMessageDto;
import com.gs.model.dto.team.MessageDto;
import com.gs.model.entity.jpa.db1.league.League;
import com.gs.model.entity.jpa.db1.league.LeagueMessage;
import com.gs.model.entity.jpa.db1.team.Message;
import com.gs.model.vo.league.LeagueMessageVo;
import com.gs.model.vo.team.MessageVo;
import com.gs.repository.jpa.league.LeagueMessageRepository;
import com.gs.repository.jpa.team.MessageRepository;
import com.gs.service.intf.league.LeagueMessageService;
import com.gs.service.intf.team.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LeagueMessageServiceLmpl implements LeagueMessageService {

    private LeagueMessageRepository leagueMessageRepository;
    private LeagueMessageDtoConvert leagueMessageDtoConvert;
    private LeagueMessageVoConvert leagueMessageVoConvert;

    @Override
    public LeagueMessageVo insertLeagueMessage(LeagueMessageDto dto){
//        if (1 == dto.getType() || 2 == dto.getType()){
//            //数据库中已经存在邀请的消息
//            if (messageRepository.existsByTeamIdAndFromIdAndToIdAndType(dto.getTeamId(), dto.getFromId(), dto.getToId(), dto.getType())){
//                return null;
//            }
//        }
        return leagueMessageVoConvert.toVo(leagueMessageRepository.save(leagueMessageDtoConvert.toEntity(dto)));
    }

    @Override
    public List<LeagueMessageVo> getLeagueGroupChatMsgs(Long leagueId, Integer pageNum, Integer pageSize){

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<LeagueMessage> messagePage = leagueMessageRepository.findAll(new Specification<LeagueMessage>() {

            public Predicate toPredicate(Root<LeagueMessage> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> leagueIdPath = root.get("leagueId");
                Path<Integer> typePath = root.get("type");
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(leagueIdPath, leagueId));
                predicates.add(cb.equal(typePath, 4));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<LeagueMessageVo> messageVoList = new ArrayList<>();

        for (LeagueMessage message : messagePage) {
            messageVoList.add(leagueMessageVoConvert.toVo(message));
        }

        return messageVoList;
    }

    @Override
    public void deleteLeagueGroupChatMsgs(Long leagueId){

        List<LeagueMessage> messagePage = leagueMessageRepository.findAll(new Specification<LeagueMessage>() {

            public Predicate toPredicate(Root<LeagueMessage> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> leagueIdPath = root.get("leagueId");
                Path<Integer> typePath = root.get("type");
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(leagueIdPath, leagueId));
                predicates.add(cb.equal(typePath, 4));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }


        });

        leagueMessageRepository.deleteAll(messagePage);
    }

    @Override
    public List<LeagueMessageVo> getLeaguePrivateChatMsgs(Long leagueId, Long fromId, Long toId, Integer pageNum, Integer pageSize){

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<LeagueMessage> messagePage = leagueMessageRepository.findAll(new Specification<LeagueMessage>() {

            public Predicate toPredicate(Root<LeagueMessage> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> leagueIdPath = root.get("leagueId");
                Path<Long> toIdPath = root.get("toId");
                Path<Long> fromIdPath = root.get("fromId");
                Path<Integer> typePath = root.get("type");
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(leagueIdPath, leagueId));
                predicates.add(cb.equal(typePath, 3));
                predicates.add(cb.or(cb.and(cb.equal(toIdPath, fromId), cb.equal(fromIdPath, toId)), cb.and(cb.equal(toIdPath, toId), cb.equal(fromIdPath, fromId))));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<LeagueMessageVo> messageVoList = new ArrayList<>();

        for (LeagueMessage message : messagePage) {
            messageVoList.add(leagueMessageVoConvert.toVo(message));
        }

        return messageVoList;
    }

    @Override
    public void deleteLeaguePrivateChatMsgs(Long leagueId, Long fromId, Long toId){

        List<LeagueMessage> messagePage = leagueMessageRepository.findAll(new Specification<LeagueMessage>() {

            public Predicate toPredicate(Root<LeagueMessage> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> leagueIdPath = root.get("leagueId");
                Path<Long> toIdPath = root.get("toId");
                Path<Long> fromIdPath = root.get("fromId");
                Path<Integer> typePath = root.get("type");
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(leagueIdPath, leagueId));
                predicates.add(cb.equal(typePath, 3));
                predicates.add(cb.or(cb.and(cb.equal(toIdPath, fromId), cb.equal(fromIdPath, toId)), cb.and(cb.equal(toIdPath, toId), cb.equal(fromIdPath, fromId))));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        });


        leagueMessageRepository.deleteAll(messagePage);

        return;
    }

    @Transactional
    public void deleteLeagueChatMsgs(Date date){
        leagueMessageRepository.deleteAllByCreateTimeBefore(date);
    }

    @Override
    @Transactional
    public void deleteLeagueChatMsgs(Long leagueId, Long memberId){
        leagueMessageRepository.deleteAllByLeagueId(leagueId);
    }
}
