import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VmRequest from './vm-request';
import VmRequestDetail from './vm-request-detail';
import VmRequestUpdate from './vm-request-update';
import VmRequestDeleteDialog from './vm-request-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VmRequestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VmRequestUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VmRequestDetail} />
      <ErrorBoundaryRoute path={match.url} component={VmRequest} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={VmRequestDeleteDialog} />
  </>
);

export default Routes;
