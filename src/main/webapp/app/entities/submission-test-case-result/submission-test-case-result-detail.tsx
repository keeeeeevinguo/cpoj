import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './submission-test-case-result.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const SubmissionTestCaseResultDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const submissionTestCaseResultEntity = useAppSelector(state => state.submissionTestCaseResult.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="submissionTestCaseResultDetailsHeading">
          <Translate contentKey="cpojApp.submissionTestCaseResult.detail.title">SubmissionTestCaseResult</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{submissionTestCaseResultEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpojApp.submissionTestCaseResult.name">Name</Translate>
            </span>
          </dt>
          <dd>{submissionTestCaseResultEntity.name}</dd>
          <dt>
            <span id="resultMessage">
              <Translate contentKey="cpojApp.submissionTestCaseResult.resultMessage">Result Message</Translate>
            </span>
          </dt>
          <dd>{submissionTestCaseResultEntity.resultMessage}</dd>
          <dt>
            <span id="resultMessageDetail">
              <Translate contentKey="cpojApp.submissionTestCaseResult.resultMessageDetail">Result Message Detail</Translate>
            </span>
          </dt>
          <dd>{submissionTestCaseResultEntity.resultMessageDetail}</dd>
          <dt>
            <span id="elapsedTimeInMS">
              <Translate contentKey="cpojApp.submissionTestCaseResult.elapsedTimeInMS">Elapsed Time In MS</Translate>
            </span>
          </dt>
          <dd>{submissionTestCaseResultEntity.elapsedTimeInMS}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="cpojApp.submissionTestCaseResult.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{submissionTestCaseResultEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="cpojApp.submissionTestCaseResult.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {submissionTestCaseResultEntity.createdDate ? (
              <TextFormat value={submissionTestCaseResultEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="cpojApp.submissionTestCaseResult.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{submissionTestCaseResultEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="cpojApp.submissionTestCaseResult.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {submissionTestCaseResultEntity.lastModifiedDate ? (
              <TextFormat value={submissionTestCaseResultEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="cpojApp.submissionTestCaseResult.submission">Submission</Translate>
          </dt>
          <dd>{submissionTestCaseResultEntity.submission ? submissionTestCaseResultEntity.submission.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/submission-test-case-result" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/submission-test-case-result/${submissionTestCaseResultEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubmissionTestCaseResultDetail;
