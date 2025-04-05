package com.example.timetrackingservice.mapper;

import com.example.timetrackingservice.dto.WorkLogCreateDTO;
import com.example.timetrackingservice.dto.WorkLogDetailDTO;
import com.example.timetrackingservice.entity.WorkLog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkLogMapper {

    WorkLog toEntity(WorkLogCreateDTO dto);

    WorkLogDetailDTO toDetailDto(WorkLog workLog);

    List<WorkLogDetailDTO> toDetailDtoList(List<WorkLog> workLogs);
}
