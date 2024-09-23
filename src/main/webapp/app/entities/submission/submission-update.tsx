import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IProblem } from 'app/shared/model/problem.model';
import { getEntities as getProblems } from 'app/entities/problem/problem.reducer';
import { getEntity, updateEntity, createEntity, reset } from './submission.reducer';
import { ISubmission } from 'app/shared/model/submission.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { ProgrammingLanguage } from 'app/shared/model/enumerations/programming-language.model';
import { SubmissionResultStatus } from 'app/shared/model/enumerations/submission-result-status.model';
import { SubmissionResultMessage } from 'app/shared/model/enumerations/submission-result-message.model';

export const SubmissionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const problems = useAppSelector(state => state.problem.entities);
  const submissionEntity = useAppSelector(state => state.submission.entity);
  const loading = useAppSelector(state => state.submission.loading);
  const updating = useAppSelector(state => state.submission.updating);
  const updateSuccess = useAppSelector(state => state.submission.updateSuccess);
  const programmingLanguageValues = Object.keys(ProgrammingLanguage);
  const submissionResultStatusValues = Object.keys(SubmissionResultStatus);
  const submissionResultMessageValues = Object.keys(SubmissionResultMessage);
  const handleClose = () => {
    props.history.push('/submission' + props.location.search);
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
      ...submissionEntity,
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
          programmingLanguage: 'JAVA',
          overallResultStatus: 'WAIT',
          overallResultMessage: 'NA',
          ...submissionEntity,
          createdDate: convertDateTimeFromServer(submissionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(submissionEntity.lastModifiedDate),
          problem: submissionEntity?.problem?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpojApp.submission.home.createOrEditLabel" data-cy="SubmissionCreateUpdateHeading">
            <Translate contentKey="cpojApp.submission.home.createOrEditLabel">Create or edit a Submission</Translate>
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
                  id="submission-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpojApp.submission.name')}
                id="submission-name"
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
              <ValidatedField
                label={translate('cpojApp.submission.overallResultStatus')}
                id="submission-overallResultStatus"
                name="overallResultStatus"
                data-cy="overallResultStatus"
                type="select"
              >
                {submissionResultStatusValues.map(submissionResultStatus => (
                  <option value={submissionResultStatus} key={submissionResultStatus}>
                    {translate('cpojApp.SubmissionResultStatus.' + submissionResultStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpojApp.submission.overallResultMessage')}
                id="submission-overallResultMessage"
                name="overallResultMessage"
                data-cy="overallResultMessage"
                type="select"
              >
                {submissionResultMessageValues.map(submissionResultMessage => (
                  <option value={submissionResultMessage} key={submissionResultMessage}>
                    {translate('cpojApp.SubmissionResultMessage.' + submissionResultMessage)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpojApp.submission.overallResultMessageDetail')}
                id="submission-overallResultMessageDetail"
                name="overallResultMessageDetail"
                data-cy="overallResultMessageDetail"
                type="text"
                validate={{
                  minLength: { value: 0, message: translate('entity.validation.minlength', { min: 0 }) },
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.submission.overallResultTries')}
                id="submission-overallResultTries"
                name="overallResultTries"
                data-cy="overallResultTries"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('cpojApp.submission.overallResultScorePercentage')}
                id="submission-overallResultScorePercentage"
                name="overallResultScorePercentage"
                data-cy="overallResultScorePercentage"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('cpojApp.submission.createdBy')}
                id="submission-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.submission.createdDate')}
                id="submission-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('cpojApp.submission.lastModifiedBy')}
                id="submission-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.submission.lastModifiedDate')}
                id="submission-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="submission-problem"
                name="problem"
                data-cy="problem"
                label={translate('cpojApp.submission.problem')}
                type="select"
              >
                <option value="" key="0" />
                {problems
                  ? problems.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/submission" replace color="info">
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

export default SubmissionUpdate;
