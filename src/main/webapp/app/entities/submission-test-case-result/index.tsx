import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SubmissionTestCaseResult from './submission-test-case-result';
import SubmissionTestCaseResultDetail from './submission-test-case-result-detail';
import SubmissionTestCaseResultUpdate from './submission-test-case-result-update';
import SubmissionTestCaseResultDeleteDialog from './submission-test-case-result-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubmissionTestCaseResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubmissionTestCaseResultUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubmissionTestCaseResultDetail} />
      <ErrorBoundaryRoute path={match.url} component={SubmissionTestCaseResult} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubmissionTestCaseResultDeleteDialog} />
  </>
);

export default Routes;
