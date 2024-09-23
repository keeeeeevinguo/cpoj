import React, { useEffect, useState } from 'react';
import { Link, Redirect, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Card, CardBody, CardTitle, CardSubtitle, CardText, CardHeader, CardFooter, Spinner } from 'reactstrap';
import { isNumber, TextFormat, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './problem.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { getEntity as getSubmissionEntity, createEntity, reset } from '../submission/submission.reducer';
import { ProgrammingLanguage } from 'app/shared/model/enumerations/programming-language.model';
import { SubmissionResultStatus } from 'app/shared/model/enumerations/submission-result-status.model';
import { SubmissionResultMessage } from 'app/shared/model/enumerations/submission-result-message.model';

export const ProblemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const programmingLanguageValues = Object.keys(ProgrammingLanguage);
  const submissionEntity = useAppSelector(state => state.submission.entity);
  const loading = useAppSelector(state => state.submission.loading);
  const updating = useAppSelector(state => state.submission.updating);
  const updateSuccess = useAppSelector(state => state.submission.updateSuccess);
  const problemEntity = useAppSelector(state => state.problem.entity);

  const [hasSubmission, setHasSubmission] = useState(false);

  useEffect(() => {
    dispatch(reset());  // Reset any existing submission
    dispatch(getEntity(props.match.params.id));
  }, [])

  const saveEntity = (values) => {
    let ts = ("" + (new Date()).getTime());
    ts = ts.substring(ts.length - 8, ts.length);

    const entity = {
      ...submissionEntity,
      ...values,
      overallResultStatus: SubmissionResultStatus.WAIT,
      overallResultMessage: SubmissionResultMessage.NA,
      overallResultMessageDetail: "",
      overallResultTries: 0,
      overallResultScorePercentage: 0,
      name: problemEntity.name + "_sb_" + ts,
      problem: problemEntity,
      id: null,
    };

    dispatch(createEntity(entity));
    setHasSubmission(true);
  };

  if (hasSubmission && submissionEntity && submissionEntity.id) {
    const nextRoute = '/problem/' + props.match.params.id + '/submission/' + submissionEntity.id;
    props.history.push(nextRoute);
    return null;
  }

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <div>
            <Button tag={Link} to="/problem" replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.back">Back</Translate>
              </span>
            </Button>
            &nbsp;
            {isAdmin && <Button tag={Link} to={`/problem/${problemEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" />{' '}
              <span className="d-none d-md-inline">
                <Translate contentKey="entity.action.edit">Edit</Translate>
              </span>
            </Button>
            }
          </div>
          <br></br>

          <Card>
            <CardBody>
              <CardTitle tag="h3">
                {problemEntity.title}
                <hr></hr>
              </CardTitle>
              <CardText>
                <div className="content" dangerouslySetInnerHTML={{__html: problemEntity.description}}></div>
              </CardText>
            </CardBody>
          </Card>
          <br></br>
          {isAuthenticated && !isAdmin && <div>
            {
              <>
                <Card>
                  <CardBody>
                    <CardTitle tag="h3">
                      Submit Your Program
                      <hr></hr>
                    </CardTitle>
                    <CardText>
                      <Row className="justify-content-center">
                        <Col>
                          <ValidatedForm defaultValues={{}} onSubmit={saveEntity}>
                            <ValidatedField
                              label={translate('cpojApp.submission.programmingLanguage')}
                              id="submission-programmingLanguage"
                              name="programmingLanguage"
                              data-cy="programmingLanguage"
                              type="select"
                            >
                              {programmingLanguageValues.map(programmingLanguage => (
                                <option value={programmingLanguage} key={programmingLanguage}>
                                  {translate('cpojApp.ProgrammingLanguage.' + programmingLanguage)}
                                </option>
                              ))}
                            </ValidatedField>
                            <ValidatedField
                              label={translate('cpojApp.submission.code')}
                              id="submission-code"
                              name="code"
                              data-cy="code"
                              type="textarea"
                              validate={{
                                required: { value: true, message: translate('entity.validation.required') },
                                minLength: { value: 1, message: translate('entity.validation.minlength', { min: 1 }) },
                                maxLength: { value: 100000, message: translate('entity.validation.maxlength', { max: 100000 }) },
                              }}
                            />
                            <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                              <FontAwesomeIcon icon="save" />
                              &nbsp;
                              Submit
                            </Button>
                          </ValidatedForm>
                        </Col>
                      </Row>
                    </CardText>
                  </CardBody>
                </Card>
                <br></br>
              </>
            }
          </div>
          }
        </Col>
      </Row>

    </div>
  );
};

export default ProblemDetail;
