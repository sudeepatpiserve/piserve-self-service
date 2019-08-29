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

import { IVm, defaultValue } from 'app/shared/model/vm.model';

export const ACTION_TYPES = {
  FETCH_VM_LIST: 'vm/FETCH_VM_LIST',
  FETCH_VM: 'vm/FETCH_VM',
  CREATE_VM: 'vm/CREATE_VM',
  UPDATE_VM: 'vm/UPDATE_VM',
  DELETE_VM: 'vm/DELETE_VM',
  RESET: 'vm/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVm>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type VmState = Readonly<typeof initialState>;

// Reducer

export default (state: VmState = initialState, action): VmState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_VM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_VM):
    case REQUEST(ACTION_TYPES.UPDATE_VM):
    case REQUEST(ACTION_TYPES.DELETE_VM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_VM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VM):
    case FAILURE(ACTION_TYPES.CREATE_VM):
    case FAILURE(ACTION_TYPES.UPDATE_VM):
    case FAILURE(ACTION_TYPES.DELETE_VM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_VM_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_VM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_VM):
    case SUCCESS(ACTION_TYPES.UPDATE_VM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_VM):
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

const apiUrl = 'api/vms';

// Actions

export const getEntities: ICrudGetAllAction<IVm> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_VM_LIST,
    payload: axios.get<IVm>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IVm> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VM,
    payload: axios.get<IVm>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IVm> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IVm> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVm> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VM,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
