import { IVmRequest } from 'app/shared/model/vm-request.model';

export interface IVm {
  id?: number;
  vmname?: string;
  numCPU?: number;
  memory?: string;
  vmRequests?: IVmRequest[];
}

export const defaultValue: Readonly<IVm> = {};
