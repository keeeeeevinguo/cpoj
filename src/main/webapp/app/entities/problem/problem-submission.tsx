import React, { useEffect, useState } from 'react';
import { Link, Redirect, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Card, CardBody, CardTitle, CardSubtitle, CardText, CardHeader, CardFooter, Spinner, CardGroup, Badge } from 'reactstrap';
import { isNumber, TextFormat, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './problem.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { getEntity as getSubmissionEntity, createEntity, reset } from '../submission/submission.reducer';
import { SubmissionResultStatus } from 'app/shared/model/enumerations/submission-result-status.model';
import { SubmissionResultMessage } from 'app/shared/model/enumerations/submission-result-message.model';

export const ProblemSubmission = (props: RouteComponentProps<{ id: string, submissionId: string }>) => {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const submissionEntity = useAppSelector(state => state.submission.entity);
  const loading = useAppSelector(state => state.submission.loading);
  const problemEntity = useAppSelector(state => state.problem.entity);
  const submissionId = props.match.params.submissionId;
  const problemId = props.match.params.id;

  const [hasSubmissionResult, setHasSubmissionResult] = useState(false);

  const cadence = 1000;

  const isWaitingSubmissionResult = () => {
    if (hasSubmissionResult) return false;
    if (submissionId
      && submissionEntity
      && submissionEntity.overallResultStatus
      && submissionEntity.overallResultStatus === SubmissionResultStatus.JUDGED) {
      setHasSubmissionResult(true);
      return false;
    }
    return true;
  }

  const loadWaitingSubmissionEntity = () => {
    if (isWaitingSubmissionResult()) {
      dispatch(getSubmissionEntity(submissionId));
    }
  }

  useEffect(() => {
    dispatch(getEntity(problemId));
    dispatch(getSubmissionEntity(submissionId));

    if (!hasSubmissionResult) {
      const interval = setInterval(() => {
        loadWaitingSubmissionEntity();
      }, cadence);
      return () => clearInterval(interval); // This represents the unmount function, in which you need to clear your interval to prevent memory leaks.
    }
  }, [hasSubmissionResult])

  const renderSubmissionTestCaseResults = () => {
    return (<>
      {
        submissionEntity.submissionTestCaseResults.map((testCaseResult, i) => (
          <Col key={'card' + i}>
            <Card>
              <CardBody>
                <CardTitle tag="h6">
                  Test Case {i + 1}
                </CardTitle>
                <CardSubtitle>
                  {testCaseResult.resultMessage}
                </CardSubtitle>
                <CardText>
                  <FontAwesomeIcon icon="clock" color="primary"/>{' '} {testCaseResult.elapsedTimeInMS}ms
                </CardText>
              </CardBody>
            </Card>
            <br></br>
          </Col>
        ))
      }
    </>
    )
  };

  const getResultMessageColor = () => {
    if (submissionEntity.overallResultMessage === SubmissionResultMessage.NA) {
      return "secondary";
    }
    if (submissionEntity.overallResultMessage === SubmissionResultMessage.PASS) {
      return "success";
    } else if (submissionEntity.overallResultMessage === SubmissionResultMessage.NOT_PASS) {
      return "danger";
    } else {
      return "warning";
    }
  }

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <div>
            <Button tag={Link} to={'/problem/' + problemId} replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="sync" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.retry">Try Again</Translate>
              </span>
            </Button>
          </div>
          <br></br>

          <Card>
            <CardBody>
              <CardTitle tag="h3">
                {problemEntity.title}
                <hr></hr>
              </CardTitle>
              <CardText>
                <div className="content" dangerouslySetInnerHTML={{ __html: problemEntity.description }}></div>
              </CardText>
            </CardBody>
          </Card>
          <br></br>
        </Col>
      </Row>
      <br></br>

      {isAuthenticated && !isAdmin && <div>
        {
          <>
            <Row className="justify-content-center">
              <Col md="8">
                <Card>
                  <CardBody>
                    <CardTitle tag="h3">
                      Submission Result
                      &nbsp;
                      {isWaitingSubmissionResult() &&
                        <Spinner color="primary">
                          Loading...
                        </Spinner>
                      }
                      <hr></hr>
                    </CardTitle>
                    <CardText>
                      {submissionEntity && submissionEntity.overallResultStatus &&
                        <>
                          <Row>
                            <Col>
                              <span id="overallResultStatus">
                                <Translate contentKey="cpojApp.submission.overallResultStatus">Overall Result Status</Translate>
                              </span> &nbsp;
                              <Badge color="info" pill>
                                {submissionEntity.overallResultStatus}
                              </Badge>
                            </Col>

                            {!isWaitingSubmissionResult() &&
                              <>
                                <Col>
                                  <span id="overallResultMessage">
                                    <Translate contentKey="cpojApp.submission.overallResultMessage">Overall Result Message</Translate>
                                  </span> &nbsp;
                                  <Badge color={getResultMessageColor()} pill>
                                    {submissionEntity.overallResultMessage}
                                  </Badge>
                                </Col>
                                <Col>
                                  <span id="overallResultScorePercentage">
                                    <Translate contentKey="cpojApp.submission.overallResultScorePercentage">Overall Result Score Percentage</Translate>
                                  </span>
                                  &nbsp;
                                  <Badge color="primary" pill>
                                    {submissionEntity.overallResultScorePercentage}/100
                                  </Badge>
                                </Col>
                                {/* <Col>
                                  <span id="overallResultMessageDetail">
                                    <Translate contentKey="cpojApp.submission.overallResultMessageDetail">Overall Result Message Detail</Translate>
                                  </span>
                                  :
                                  {submissionEntity.overallResultMessageDetail}
                                </Col> */}
                              </>
                            }
                          </Row>
                          <br></br>
                          <Row>
                            {submissionEntity.submissionTestCaseResults && submissionEntity.submissionTestCaseResults.length > 0 &&
                              <>
                                {renderSubmissionTestCaseResults()}
                              </>
                            }
                          </Row>
                        </>
                      }

                    </CardText>
                  </CardBody>
                </Card>
              </Col>
            </Row>
          </>
        }
      </div>
      }

    </div>
  );
};

export default ProblemSubmission;
