import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IVmRequest, defaultValue } from 'app/shared/model/vm-request.model';

export const ACTION_TYPES = {
  FETCH_VMREQUEST_LIST: 'vmRequest/FETCH_VMREQUEST_LIST',
  FETCH_VMREQUEST: 'vmRequest/FETCH_VMREQUEST',
  CREATE_VMREQUEST: 'vmRequest/CREATE_VMREQUEST',
  UPDATE_VMREQUEST: 'vmRequest/UPDATE_VMREQUEST',
  DELETE_VMREQUEST: 'vmRequest/DELETE_VMREQUEST',
  RESET: 'vmRequest/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVmRequest>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type VmRequestState = Readonly<typeof initialState>;

// Reducer

export default (state: VmRequestState = initialState, action): VmRequestState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_VMREQUEST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VMREQUEST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_VMREQUEST):
    case REQUEST(ACTION_TYPES.UPDATE_VMREQUEST):
    case REQUEST(ACTION_TYPES.DELETE_VMREQUEST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_VMREQUEST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VMREQUEST):
    case FAILURE(ACTION_TYPES.CREATE_VMREQUEST):
    case FAILURE(ACTION_TYPES.UPDATE_VMREQUEST):
    case FAILURE(ACTION_TYPES.DELETE_VMREQUEST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_VMREQUEST_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_VMREQUEST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_VMREQUEST):
    case SUCCESS(ACTION_TYPES.UPDATE_VMREQUEST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_VMREQUEST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/vm-requests';

// Actions

export const getEntities: ICrudGetAllAction<IVmRequest> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_VMREQUEST_LIST,
    payload: axios.get<IVmRequest>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IVmRequest> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VMREQUEST,
    payload: axios.get<IVmRequest>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IVmRequest> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VMREQUEST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IVmRequest> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VMREQUEST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVmRequest> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VMREQUEST,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
