package com.kevin.cpoj.service.mapper;

import com.kevin.cpoj.domain.Submission;
import com.kevin.cpoj.service.dto.SubmissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Submission} and its DTO {@link SubmissionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProblemMapper.class })
public interface SubmissionMapper extends EntityMapper<SubmissionDTO, Submission> {
    @Mapping(target = "problem", source = "problem", qualifiedByName = "name")
    SubmissionDTO toDto(Submission s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubmissionDTO toDtoId(Submission submission);
}
