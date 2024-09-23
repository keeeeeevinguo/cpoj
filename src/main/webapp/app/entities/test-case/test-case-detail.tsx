import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './test-case.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TestCaseDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const testCaseEntity = useAppSelector(state => state.testCase.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testCaseDetailsHeading">
          <Translate contentKey="cpojApp.testCase.detail.title">TestCase</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{testCaseEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpojApp.testCase.name">Name</Translate>
            </span>
          </dt>
          <dd>{testCaseEntity.name}</dd>
          <dt>
            <span id="timeLimitInMS">
              <Translate contentKey="cpojApp.testCase.timeLimitInMS">Time Limit In MS</Translate>
            </span>
          </dt>
          <dd>{testCaseEntity.timeLimitInMS}</dd>
          <dt>
            <span id="memoryLimitInMB">
              <Translate contentKey="cpojApp.testCase.memoryLimitInMB">Memory Limit In MB</Translate>
            </span>
          </dt>
          <dd>{testCaseEntity.memoryLimitInMB}</dd>
          <dt>
            <span id="weightPercentage">
              <Translate contentKey="cpojApp.testCase.weightPercentage">Weight Percentage</Translate>
            </span>
            <UncontrolledTooltip target="weightPercentage">
              <Translate contentKey="cpojApp.testCase.help.weightPercentage" />
            </UncontrolledTooltip>
          </dt>
          <dd>{testCaseEntity.weightPercentage}</dd>
          <dt>
            <span id="inputData">
              <Translate contentKey="cpojApp.testCase.inputData">Input Data</Translate>
            </span>
          </dt>
          <dd>{testCaseEntity.inputData}</dd>
          <dt>
            <span id="outputData">
              <Translate contentKey="cpojApp.testCase.outputData">Output Data</Translate>
            </span>
          </dt>
          <dd>{testCaseEntity.outputData}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="cpojApp.testCase.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{testCaseEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="cpojApp.testCase.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {testCaseEntity.createdDate ? <TextFormat value={testCaseEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="cpojApp.testCase.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{testCaseEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="cpojApp.testCase.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {testCaseEntity.lastModifiedDate ? (
              <TextFormat value={testCaseEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="cpojApp.testCase.problem">Problem</Translate>
          </dt>
          <dd>{testCaseEntity.problem ? testCaseEntity.problem.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/test-case" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/test-case/${testCaseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestCaseDetail;
