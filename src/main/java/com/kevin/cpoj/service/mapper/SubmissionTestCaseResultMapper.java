package com.kevin.cpoj.service.mapper;

import com.kevin.cpoj.domain.SubmissionTestCaseResult;
import com.kevin.cpoj.service.dto.SubmissionTestCaseResultDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link SubmissionTestCaseResult} and its DTO {@link SubmissionTestCaseResultDTO}.
 */
@Mapper(componentModel = "spring", uses = { SubmissionMapper.class })
public interface SubmissionTestCaseResultMapper extends EntityMapper<SubmissionTestCaseResultDTO, SubmissionTestCaseResult> {
    // @Mapping(target = "submission", source = "submission", qualifiedByName = "id")
    SubmissionTestCaseResultDTO toDto(SubmissionTestCaseResult s);
}
