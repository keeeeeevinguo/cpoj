import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Problem from './problem';
import ProblemDetail from './problem-detail';
import ProblemSubmission from './problem-submission';
import ProblemUpdate from './problem-update';
import ProblemDeleteDialog from './problem-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProblemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProblemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/submission/:submissionId`} component={ProblemSubmission} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProblemDetail} />
      <ErrorBoundaryRoute path={match.url} component={Problem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProblemDeleteDialog} />
  </>
);

export default Routes;
