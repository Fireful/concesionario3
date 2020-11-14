import { IVenta } from 'app/shared/model/venta.model';
export interface IMoto {
  id?: number;
  marca?: string;
  modelo?: string;
  anio?: number;
  electrico?: boolean;
  color?: string;
  cilindrada?: number;
  precio?: number;
  venta?: IVenta;
}

export class Moto implements IMoto {
  constructor(
    public id?: number,
    public marca?: string,
    public modelo?: string,
    public anio?: number,
    public electrico?: boolean,
    public color?: string,
    public cilindrada?: number,
    public precio?: number,
    public venta?: IVenta
  ) {
    this.electrico = this.electrico || false;
  }
}
