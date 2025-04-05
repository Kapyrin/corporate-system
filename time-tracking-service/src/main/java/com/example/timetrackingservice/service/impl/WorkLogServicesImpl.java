package com.example.timetrackingservice.service.impl;

import com.example.timetrackingservice.dto.WorkLogCreateDTO;
import com.example.timetrackingservice.dto.WorkLogDetailDTO;
import com.example.timetrackingservice.entity.WorkLog;
import com.example.timetrackingservice.mapper.WorkLogMapper;
import com.example.timetrackingservice.repository.WorkLogRepo;
import com.example.timetrackingservice.service.WorkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkLogServicesImpl implements WorkLogService {
    private final WorkLogRepo repo;
    private final WorkLogMapper mapper;


    @Override
    public WorkLogDetailDTO startWorkLog(WorkLogCreateDTO createDTO) {
        //todo добавить уникальные ошибки
        Long lastWorkLogId = repo.findMaxIdByUserId(createDTO.getUserId());
        WorkLog lastWorkLog = repo.getWorkLogByUserIdAndId(createDTO.getUserId(), lastWorkLogId).orElseThrow(RuntimeException::new);
        if (lastWorkLog.getEndTime() == null) {
            throw new RuntimeException("Last work log end time is null");
        }
        return mapper.toDetailDto(repo.save(mapper.toEntity(createDTO)));
    }

    @Override
    public WorkLogDetailDTO endWorkLog(WorkLogCreateDTO createDTO) {
        //todo добавить уникальную ошибку
        Long lastWorkLogId = repo.findMaxIdByUserId(createDTO.getUserId());
        WorkLog lastWorkLog = repo.getWorkLogByUserIdAndId(createDTO.getUserId(), lastWorkLogId).orElseThrow(RuntimeException::new);
        if (lastWorkLog.getEndTime() != null) {
            throw new RuntimeException("Last work log end time is not null");
        }
        lastWorkLog.setEndTime(createDTO.getEndTime());
        return mapper.toDetailDto(repo.save(mapper.toEntity(createDTO)));
    }
}
