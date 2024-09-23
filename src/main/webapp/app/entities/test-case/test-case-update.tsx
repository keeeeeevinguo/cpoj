import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IProblem } from 'app/shared/model/problem.model';
import { getEntities as getProblems } from 'app/entities/problem/problem.reducer';
import { getEntity, updateEntity, createEntity, reset } from './test-case.reducer';
import { ITestCase } from 'app/shared/model/test-case.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TestCaseUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const problems = useAppSelector(state => state.problem.entities);
  const testCaseEntity = useAppSelector(state => state.testCase.entity);
  const loading = useAppSelector(state => state.testCase.loading);
  const updating = useAppSelector(state => state.testCase.updating);
  const updateSuccess = useAppSelector(state => state.testCase.updateSuccess);
  const handleClose = () => {
    props.history.push('/test-case' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getProblems({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...testCaseEntity,
      ...values,
      problem: problems.find(it => it.id.toString() === values.problem.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...testCaseEntity,
          createdDate: convertDateTimeFromServer(testCaseEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(testCaseEntity.lastModifiedDate),
          problem: testCaseEntity?.problem?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpojApp.testCase.home.createOrEditLabel" data-cy="TestCaseCreateUpdateHeading">
            <Translate contentKey="cpojApp.testCase.home.createOrEditLabel">Create or edit a TestCase</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="test-case-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpojApp.testCase.name')}
                id="test-case-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 1, message: translate('entity.validation.minlength', { min: 1 }) },
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                id="test-case-problem"
                name="problem"
                data-cy="problem"
                label={translate('cpojApp.testCase.problem')}
                type="select"
              >
                <option value="" key="0" />
                {problems
                  ? problems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('cpojApp.testCase.timeLimitInMS')}
                id="test-case-timeLimitInMS"
                name="timeLimitInMS"
                data-cy="timeLimitInMS"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 10, message: translate('entity.validation.min', { min: 10 }) },
                  max: { value: 10000, message: translate('entity.validation.max', { max: 10000 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('cpojApp.testCase.memoryLimitInMB')}
                id="test-case-memoryLimitInMB"
                name="memoryLimitInMB"
                data-cy="memoryLimitInMB"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 100, message: translate('entity.validation.min', { min: 100 }) },
                  max: { value: 512, message: translate('entity.validation.max', { max: 512 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('cpojApp.testCase.weightPercentage')}
                id="test-case-weightPercentage"
                name="weightPercentage"
                data-cy="weightPercentage"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="weightPercentageLabel">
                <Translate contentKey="cpojApp.testCase.help.weightPercentage" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpojApp.testCase.inputData')}
                id="test-case-inputData"
                name="inputData"
                data-cy="inputData"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.testCase.outputData')}
                id="test-case-outputData"
                name="outputData"
                data-cy="outputData"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              {/* <ValidatedField
                label={translate('cpojApp.testCase.createdBy')}
                id="test-case-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.testCase.createdDate')}
                id="test-case-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('cpojApp.testCase.lastModifiedBy')}
                id="test-case-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.testCase.lastModifiedDate')}
                id="test-case-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              /> */}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/test-case" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TestCaseUpdate;
