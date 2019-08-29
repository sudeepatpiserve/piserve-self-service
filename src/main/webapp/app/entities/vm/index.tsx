import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Vm from './vm';
import VmDetail from './vm-detail';
import VmUpdate from './vm-update';
import VmDeleteDialog from './vm-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VmUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VmUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VmDetail} />
      <ErrorBoundaryRoute path={match.url} component={Vm} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={VmDeleteDialog} />
  </>
);

export default Routes;
