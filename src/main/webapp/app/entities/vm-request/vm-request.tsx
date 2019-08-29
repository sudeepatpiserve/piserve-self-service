import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './vm-request.reducer';
import { IVmRequest } from 'app/shared/model/vm-request.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IVmRequestProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IVmRequestState = IPaginationBaseState;

export class VmRequest extends React.Component<IVmRequestProps, IVmRequestState> {
  state: IVmRequestState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { vmRequestList, match } = this.props;
    return (
      <div>
        <h2 id="vm-request-heading">
          Vm Requests
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Vm Request
          </Link>
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            {vmRequestList && vmRequestList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th className="hand" onClick={this.sort('id')}>
                      ID <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('dc')}>
                      Dc <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('datastore')}>
                      Datastore <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('cluster')}>
                      Cluster <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('network')}>
                      Network <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('template')}>
                      Template <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      Vm <FontAwesomeIcon icon="sort" />
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {vmRequestList.map((vmRequest, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`${match.url}/${vmRequest.id}`} color="link" size="sm">
                          {vmRequest.id}
                        </Button>
                      </td>
                      <td>{vmRequest.dc}</td>
                      <td>{vmRequest.datastore}</td>
                      <td>{vmRequest.cluster}</td>
                      <td>{vmRequest.network}</td>
                      <td>{vmRequest.template}</td>
                      <td>{vmRequest.vm ? <Link to={`vm/${vmRequest.vm.id}`}>{vmRequest.vm.id}</Link> : ''}</td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`${match.url}/${vmRequest.id}`} color="info" size="sm">
                            <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${vmRequest.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${vmRequest.id}/delete`} color="danger" size="sm">
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="alert alert-warning">No Vm Requests found</div>
            )}
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ vmRequest }: IRootState) => ({
  vmRequestList: vmRequest.entities,
  totalItems: vmRequest.totalItems,
  links: vmRequest.links,
  entity: vmRequest.entity,
  updateSuccess: vmRequest.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VmRequest);
