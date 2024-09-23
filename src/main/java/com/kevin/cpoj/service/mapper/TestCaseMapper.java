package com.kevin.cpoj.service.mapper;

import com.kevin.cpoj.domain.TestCase;
import com.kevin.cpoj.service.dto.TestCaseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link TestCase} and its DTO {@link TestCaseDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProblemMapper.class })
public interface TestCaseMapper extends EntityMapper<TestCaseDTO, TestCase> {
    @Mapping(target = "problem", source = "problem", qualifiedByName = "name")
    TestCaseDTO toDto(TestCase s);
}
