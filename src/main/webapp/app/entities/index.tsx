import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Problem from './problem';
import TestCase from './test-case';
import Submission from './submission';
import SubmissionTestCaseResult from './submission-test-case-result';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}problem`} component={Problem} />
      <ErrorBoundaryRoute path={`${match.url}test-case`} component={TestCase} />
      <ErrorBoundaryRoute path={`${match.url}submission`} component={Submission} />
      <ErrorBoundaryRoute path={`${match.url}submission-test-case-result`} component={SubmissionTestCaseResult} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
