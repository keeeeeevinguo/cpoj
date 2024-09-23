import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './submission.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const SubmissionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const submissionEntity = useAppSelector(state => state.submission.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="submissionDetailsHeading">
          <Translate contentKey="cpojApp.submission.detail.title">Submission</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpojApp.submission.name">Name</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.name}</dd>
          <dt>
            <span id="programmingLanguage">
              <Translate contentKey="cpojApp.submission.programmingLanguage">Programming Language</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.programmingLanguage}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="cpojApp.submission.code">Code</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.code}</dd>
          <dt>
            <span id="overallResultStatus">
              <Translate contentKey="cpojApp.submission.overallResultStatus">Overall Result Status</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.overallResultStatus}</dd>
          <dt>
            <span id="overallResultMessage">
              <Translate contentKey="cpojApp.submission.overallResultMessage">Overall Result Message</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.overallResultMessage}</dd>
          <dt>
            <span id="overallResultMessageDetail">
              <Translate contentKey="cpojApp.submission.overallResultMessageDetail">Overall Result Message Detail</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.overallResultMessageDetail}</dd>
          <dt>
            <span id="overallResultTries">
              <Translate contentKey="cpojApp.submission.overallResultTries">Overall Result Tries</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.overallResultTries}</dd>
          <dt>
            <span id="overallResultScorePercentage">
              <Translate contentKey="cpojApp.submission.overallResultScorePercentage">Overall Result Score Percentage</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.overallResultScorePercentage}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="cpojApp.submission.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.createdBy}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="cpojApp.submission.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {submissionEntity.createdDate ? <TextFormat value={submissionEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="cpojApp.submission.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{submissionEntity.lastModifiedBy}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="cpojApp.submission.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {submissionEntity.lastModifiedDate ? (
              <TextFormat value={submissionEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="cpojApp.submission.problem">Problem</Translate>
          </dt>
          <dd>{submissionEntity.problem ? submissionEntity.problem.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/submission" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/submission/${submissionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubmissionDetail;
