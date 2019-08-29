import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IVm } from 'app/shared/model/vm.model';
import { getEntities as getVms } from 'app/entities/vm/vm.reducer';
import { getEntity, updateEntity, createEntity, reset } from './vm-request.reducer';
import { IVmRequest } from 'app/shared/model/vm-request.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVmRequestUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IVmRequestUpdateState {
  isNew: boolean;
  vmId: string;
}

export class VmRequestUpdate extends React.Component<IVmRequestUpdateProps, IVmRequestUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      vmId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getVms();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { vmRequestEntity } = this.props;
      const entity = {
        ...vmRequestEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/vm-request');
  };

  render() {
    const { vmRequestEntity, vms, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="piserveSelfServiceApp.vmRequest.home.createOrEditLabel">Create or edit a VmRequest</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : vmRequestEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="vm-request-id">ID</Label>
                    <AvInput id="vm-request-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dcLabel" for="vm-request-dc">
                    Dc
                  </Label>
                  <AvField id="vm-request-dc" type="text" name="dc" />
                </AvGroup>
                <AvGroup>
                  <Label id="datastoreLabel" for="vm-request-datastore">
                    Datastore
                  </Label>
                  <AvField id="vm-request-datastore" type="text" name="datastore" />
                </AvGroup>
                <AvGroup>
                  <Label id="clusterLabel" for="vm-request-cluster">
                    Cluster
                  </Label>
                  <AvField id="vm-request-cluster" type="text" name="cluster" />
                </AvGroup>
                <AvGroup>
                  <Label id="networkLabel" for="vm-request-network">
                    Network
                  </Label>
                  <AvField id="vm-request-network" type="text" name="network" />
                </AvGroup>
                <AvGroup>
                  <Label id="templateLabel" for="vm-request-template">
                    Template
                  </Label>
                  <AvField id="vm-request-template" type="text" name="template" />
                </AvGroup>
                <AvGroup>
                  <Label for="vm-request-vm">Vm</Label>
                  <AvInput id="vm-request-vm" type="select" className="form-control" name="vm.id">
                    <option value="" key="0" />
                    {vms
                      ? vms.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/vm-request" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  vms: storeState.vm.entities,
  vmRequestEntity: storeState.vmRequest.entity,
  loading: storeState.vmRequest.loading,
  updating: storeState.vmRequest.updating,
  updateSuccess: storeState.vmRequest.updateSuccess
});

const mapDispatchToProps = {
  getVms,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VmRequestUpdate);
