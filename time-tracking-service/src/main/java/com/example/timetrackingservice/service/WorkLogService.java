package com.example.timetrackingservice.service;


import com.example.timetrackingservice.dto.WorkLogCreateDTO;
import com.example.timetrackingservice.dto.WorkLogDetailDTO;
import org.springframework.stereotype.Service;

@Service
public interface WorkLogService {
    WorkLogDetailDTO startWorkLog(WorkLogCreateDTO createDTO);

    WorkLogDetailDTO endWorkLog(WorkLogCreateDTO createDTO);

}
