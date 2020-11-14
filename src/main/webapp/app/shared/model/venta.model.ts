import { Moment } from 'moment';
import { ICoche } from 'app/shared/model/coche.model';
import { ICliente } from 'app/shared/model/cliente.model';
import { IVendedor } from 'app/shared/model/vendedor.model';
import { IMoto } from './moto.model';

export interface IVenta {
  id?: number;
  fecha?: Moment;
  importeTotal?: number;
  coche?: ICoche;
  moto?: IMoto;
  tipo?: string;
  cliente?: ICliente;
  vendedor?: IVendedor;
  metodoPago?: string;
  numeroVenta?: string;
  estadoVenta?: string;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public fecha?: Moment,
    public importeTotal?: number,
    public coche?: ICoche,
    public moto?: IMoto,
    public tipo?: string,
    public cliente?: ICliente,
    public vendedor?: IVendedor,
    public metodoPago?: string,
    public numeroVenta?: string,
    public estadoVenta?: string
  ) {}
}
