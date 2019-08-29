import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './vm.reducer';
import { IVm } from 'app/shared/model/vm.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVmUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IVmUpdateState {
  isNew: boolean;
}

export class VmUpdate extends React.Component<IVmUpdateProps, IVmUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { vmEntity } = this.props;
      const entity = {
        ...vmEntity,
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
    this.props.history.push('/entity/vm');
  };

  render() {
    const { vmEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="piserveSelfServiceApp.vm.home.createOrEditLabel">Create or edit a Vm</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : vmEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="vm-id">ID</Label>
                    <AvInput id="vm-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="vmnameLabel" for="vm-vmname">
                    Vmname
                  </Label>
                  <AvField id="vm-vmname" type="text" name="vmname" />
                </AvGroup>
                <AvGroup>
                  <Label id="numCPULabel" for="vm-numCPU">
                    Num CPU
                  </Label>
                  <AvField id="vm-numCPU" type="string" className="form-control" name="numCPU" />
                </AvGroup>
                <AvGroup>
                  <Label id="memoryLabel" for="vm-memory">
                    Memory
                  </Label>
                  <AvField id="vm-memory" type="text" name="memory" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/vm" replace color="info">
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
  vmEntity: storeState.vm.entity,
  loading: storeState.vm.loading,
  updating: storeState.vm.updating,
  updateSuccess: storeState.vm.updateSuccess
});

const mapDispatchToProps = {
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
)(VmUpdate);
