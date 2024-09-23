import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ISubmission } from 'app/shared/model/submission.model';
import { getEntities as getSubmissions } from 'app/entities/submission/submission.reducer';
import { getEntity, updateEntity, createEntity, reset } from './submission-test-case-result.reducer';
import { ISubmissionTestCaseResult } from 'app/shared/model/submission-test-case-result.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { TestCaseResultMessage } from 'app/shared/model/enumerations/test-case-result-message.model';

export const SubmissionTestCaseResultUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const submissions = useAppSelector(state => state.submission.entities);
  const submissionTestCaseResultEntity = useAppSelector(state => state.submissionTestCaseResult.entity);
  const loading = useAppSelector(state => state.submissionTestCaseResult.loading);
  const updating = useAppSelector(state => state.submissionTestCaseResult.updating);
  const updateSuccess = useAppSelector(state => state.submissionTestCaseResult.updateSuccess);
  const testCaseResultMessageValues = Object.keys(TestCaseResultMessage);
  const handleClose = () => {
    props.history.push('/submission-test-case-result' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getSubmissions({}));
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
      ...submissionTestCaseResultEntity,
      ...values,
      submission: submissions.find(it => it.id.toString() === values.submission.toString()),
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
          resultMessage: 'NA',
          ...submissionTestCaseResultEntity,
          createdDate: convertDateTimeFromServer(submissionTestCaseResultEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(submissionTestCaseResultEntity.lastModifiedDate),
          submission: submissionTestCaseResultEntity?.submission?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpojApp.submissionTestCaseResult.home.createOrEditLabel" data-cy="SubmissionTestCaseResultCreateUpdateHeading">
            <Translate contentKey="cpojApp.submissionTestCaseResult.home.createOrEditLabel">
              Create or edit a SubmissionTestCaseResult
            </Translate>
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
                  id="submission-test-case-result-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpojApp.submissionTestCaseResult.name')}
                id="submission-test-case-result-name"
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
                label={translate('cpojApp.submissionTestCaseResult.resultMessage')}
                id="submission-test-case-result-resultMessage"
                name="resultMessage"
                data-cy="resultMessage"
                type="select"
              >
                {testCaseResultMessageValues.map(testCaseResultMessage => (
                  <option value={testCaseResultMessage} key={testCaseResultMessage}>
                    {translate('cpojApp.TestCaseResultMessage.' + testCaseResultMessage)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpojApp.submissionTestCaseResult.resultMessageDetail')}
                id="submission-test-case-result-resultMessageDetail"
                name="resultMessageDetail"
                data-cy="resultMessageDetail"
                type="text"
                validate={{
                  minLength: { value: 0, message: translate('entity.validation.minlength', { min: 0 }) },
                  maxLength: { value: 1000, message: translate('entity.validation.maxlength', { max: 1000 }) },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.submissionTestCaseResult.elapsedTimeInMS')}
                id="submission-test-case-result-elapsedTimeInMS"
                name="elapsedTimeInMS"
                data-cy="elapsedTimeInMS"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0, message: translate('entity.validation.min', { min: 0 }) },
                  max: { value: 10000, message: translate('entity.validation.max', { max: 10000 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('cpojApp.submissionTestCaseResult.createdBy')}
                id="submission-test-case-result-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.submissionTestCaseResult.createdDate')}
                id="submission-test-case-result-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('cpojApp.submissionTestCaseResult.lastModifiedBy')}
                id="submission-test-case-result-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.submissionTestCaseResult.lastModifiedDate')}
                id="submission-test-case-result-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="submission-test-case-result-submission"
                name="submission"
                data-cy="submission"
                label={translate('cpojApp.submissionTestCaseResult.submission')}
                type="select"
              >
                <option value="" key="0" />
                {submissions
                  ? submissions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/submission-test-case-result" replace color="info">
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

export default SubmissionTestCaseResultUpdate;
