import { IVm } from 'app/shared/model/vm.model';

export interface IVmRequest {
  id?: number;
  dc?: string;
  datastore?: string;
  cluster?: string;
  network?: string;
  template?: string;
  vm?: IVm;
}

export const defaultValue: Readonly<IVmRequest> = {};
