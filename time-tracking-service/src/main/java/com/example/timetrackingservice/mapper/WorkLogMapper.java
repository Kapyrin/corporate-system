package com.example.timetrackingservice.mapper;
import com.example.timetrackingservice.dto.WorkLogResponseDto;
import com.example.timetrackingservice.entity.WorkLog;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface WorkLogMapper {
    WorkLogResponseDto toDto(WorkLog entity);
    WorkLog toEntity(WorkLogResponseDto dto);
    List<WorkLogResponseDto> toDto(List<WorkLog> entities);
}
