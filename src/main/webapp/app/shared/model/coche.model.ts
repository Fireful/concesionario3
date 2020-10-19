import { IVenta } from 'app/shared/model/venta.model';
export interface ICoche {
  id?: number;
  marca?: string;
  anio?: number;
  electrico?: boolean;
  precio?: number;
  venta?: IVenta;
  color?: string;
}

export class Coche implements ICoche {
  constructor(
    public id?: number,
    public marca?: string,
    public anio?: number,
    public electrico?: boolean,
    public precio?: number,
    public venta?: IVenta,
    public color?: string
  ) {
    this.electrico = this.electrico || false;
  }
}
