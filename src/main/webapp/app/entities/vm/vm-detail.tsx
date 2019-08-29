import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './vm.reducer';
import { IVm } from 'app/shared/model/vm.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVmDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class VmDetail extends React.Component<IVmDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { vmEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Vm [<b>{vmEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="vmname">Vmname</span>
            </dt>
            <dd>{vmEntity.vmname}</dd>
            <dt>
              <span id="numCPU">Num CPU</span>
            </dt>
            <dd>{vmEntity.numCPU}</dd>
            <dt>
              <span id="memory">Memory</span>
            </dt>
            <dd>{vmEntity.memory}</dd>
          </dl>
          <Button tag={Link} to="/entity/vm" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/vm/${vmEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ vm }: IRootState) => ({
  vmEntity: vm.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VmDetail);
