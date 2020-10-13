import { Moment } from 'moment';
import { ICoche } from 'app/shared/model/coche.model';
import { ICliente } from 'app/shared/model/cliente.model';
import { IVendedor } from 'app/shared/model/vendedor.model';

export interface IVenta {
  id?: number;
  fecha?: Moment;
  importeTotal?: number;
  coche?: ICoche;
  cliente?: ICliente;
  vendedor?: IVendedor;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public fecha?: Moment,
    public importeTotal?: number,
    public coche?: ICoche,
    public cliente?: ICliente,
    public vendedor?: IVendedor
  ) {}
}
