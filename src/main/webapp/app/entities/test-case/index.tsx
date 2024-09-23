import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TestCase from './test-case';
import TestCaseDetail from './test-case-detail';
import TestCaseUpdate from './test-case-update';
import TestCaseDeleteDialog from './test-case-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TestCaseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TestCaseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TestCaseDetail} />
      <ErrorBoundaryRoute path={match.url} component={TestCase} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TestCaseDeleteDialog} />
  </>
);

export default Routes;
