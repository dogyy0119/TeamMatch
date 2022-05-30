package com.gs.service.impl.team;

import com.gs.convert.team.MessageDtoConvert;
import com.gs.convert.team.MessageVoConvert;
import com.gs.model.dto.team.MessageDto;
import com.gs.model.entity.jpa.db1.team.Message;
import com.gs.model.vo.team.MessageVo;
import com.gs.repository.jpa.team.MessageRepository;
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
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MessageServiceLmpl implements MessageService {

    private MessageRepository messageRepository;
    private MessageDtoConvert messageDtoConvert;
    private MessageVoConvert messageVoConvert;

    @Override
    public MessageVo insertMessage(MessageDto dto){
//        if (1 == dto.getType() || 2 == dto.getType()){
//            //数据库中已经存在邀请的消息
//            if (messageRepository.existsByTeamIdAndFromIdAndToIdAndType(dto.getTeamId(), dto.getFromId(), dto.getToId(), dto.getType())){
//                return null;
//            }
//        }
        return messageVoConvert.toVo(messageRepository.save(messageDtoConvert.toEntity(dto)));
    }

    @Override
    public List<MessageVo> getTeamChatMsgs(String teamId, Long memberId, Integer pageNum, Integer pageSize){

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

        Page<Message> messagePage = messageRepository.findAll(new Specification<Message>() {

            public Predicate toPredicate(Root<Message> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<String> teamIdPath = root.get("teamId");
                Path<Long> toIdPath = root.get("toId");
                Path<Long> fromIdPath = root.get("fromId");
                /**
                 * 连接查询条件, 不定参数，可以连接0..N个查询条件
                 */
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(teamIdPath, teamId));
                predicates.add(cb.or(cb.equal(toIdPath, memberId), cb.equal(fromIdPath, memberId)));

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }

        }, pageable);

        List<MessageVo> messageVoList = new ArrayList<>();

        for (Message message : messagePage) {
            messageVoList.add(messageVoConvert.toVo(message));
        }

        return messageVoList;
    }

    @Override
    @Transactional
    public void deleteTeamChatMsgs(Long teamId, Long memberId){
        messageRepository.deleteAllByTeamId(teamId);
    }
}
