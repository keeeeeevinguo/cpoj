package com.kevin.cpoj.service.mapper;

import com.kevin.cpoj.domain.Problem;
import com.kevin.cpoj.service.dto.ProblemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Problem} and its DTO {@link ProblemDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProblemMapper extends EntityMapper<ProblemDTO, Problem> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProblemDTO toDtoId(Problem problem);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProblemDTO toDtoName(Problem problem);
}
