import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Submission from './submission';
import SubmissionDetail from './submission-detail';
import SubmissionUpdate from './submission-update';
import SubmissionDeleteDialog from './submission-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SubmissionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SubmissionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SubmissionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Submission} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SubmissionDeleteDialog} />
  </>
);

export default Routes;
