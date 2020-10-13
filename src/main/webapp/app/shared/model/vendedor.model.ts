export interface IVendedor {
  id?: number;
  nombre?: string;
  dni?: string;
  totalVentas?: number;
}

export class Vendedor implements IVendedor {
  constructor(public id?: number, public nombre?: string, public dni?: string, public totalVentas?: number) {}
}
