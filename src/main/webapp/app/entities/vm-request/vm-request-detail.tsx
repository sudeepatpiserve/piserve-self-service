import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './vm-request.reducer';
import { IVmRequest } from 'app/shared/model/vm-request.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVmRequestDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class VmRequestDetail extends React.Component<IVmRequestDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { vmRequestEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            VmRequest [<b>{vmRequestEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dc">Dc</span>
            </dt>
            <dd>{vmRequestEntity.dc}</dd>
            <dt>
              <span id="datastore">Datastore</span>
            </dt>
            <dd>{vmRequestEntity.datastore}</dd>
            <dt>
              <span id="cluster">Cluster</span>
            </dt>
            <dd>{vmRequestEntity.cluster}</dd>
            <dt>
              <span id="network">Network</span>
            </dt>
            <dd>{vmRequestEntity.network}</dd>
            <dt>
              <span id="template">Template</span>
            </dt>
            <dd>{vmRequestEntity.template}</dd>
            <dt>Vm</dt>
            <dd>{vmRequestEntity.vm ? vmRequestEntity.vm.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/vm-request" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/vm-request/${vmRequestEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ vmRequest }: IRootState) => ({
  vmRequestEntity: vmRequest.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VmRequestDetail);
