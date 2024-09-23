import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './problem.reducer';
import { IProblem } from 'app/shared/model/problem.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import RichTextEditor from 'react-rte';

export const ProblemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const problemEntity = useAppSelector(state => state.problem.entity);
  const loading = useAppSelector(state => state.problem.loading);
  const updating = useAppSelector(state => state.problem.updating);
  const updateSuccess = useAppSelector(state => state.problem.updateSuccess);

  const [textEditorValue, setTextEditorValue] = useState(
    problemEntity && problemEntity.description
      ? RichTextEditor.createValueFromString(problemEntity.description, 'html')
      : RichTextEditor.createEmptyValue()
  );

  const onTextEditorChange = (value) => {
    setTextEditorValue(value);
  }

  const handleClose = () => {
    props.history.push('/problem' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if(!isNew && problemEntity && problemEntity.description) {
      setTextEditorValue(RichTextEditor.createValueFromString(problemEntity.description, 'html'));
    }
  }, [problemEntity]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...problemEntity,
      ...values,
      description: textEditorValue.toString('html')
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
        ...problemEntity,
        createdDate: convertDateTimeFromServer(problemEntity.createdDate),
        lastModifiedDate: convertDateTimeFromServer(problemEntity.lastModifiedDate),
      };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpojApp.problem.home.createOrEditLabel" data-cy="ProblemCreateUpdateHeading">
            <Translate contentKey="cpojApp.problem.home.createOrEditLabel">Create or edit a Problem</Translate>
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
                  id="problem-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpojApp.problem.name')}
                id="problem-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.problem.title')}
                id="problem-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 5, message: translate('entity.validation.minlength', { min: 5 }) },
                  maxLength: { value: 500, message: translate('entity.validation.maxlength', { max: 500 }) },
                }}
              />
              <RichTextEditor
                value={textEditorValue}
                onChange={onTextEditorChange}
              />
              <br></br>
              {/* <ValidatedField
                label={translate('cpojApp.problem.description')}
                id="problem-description"
                name="description"
                data-cy="description"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 10, message: translate('entity.validation.minlength', { min: 10 }) },
                  maxLength: { value: 100000, message: translate('entity.validation.maxlength', { max: 100000 }) },
                }}
              /> */}
              {/* <ValidatedField
                label={translate('cpojApp.problem.createdBy')}
                id="problem-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpojApp.problem.createdDate')}
                id="problem-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              /> */}
              {/* <ValidatedField
                label={translate('cpojApp.problem.lastModifiedBy')}
                id="problem-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              /> */}
              {/* <ValidatedField
                label={translate('cpojApp.problem.lastModifiedDate')}
                id="problem-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              /> */}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/problem" replace color="info">
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

export default ProblemUpdate;
