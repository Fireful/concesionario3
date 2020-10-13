export interface ICliente {
  id?: number;
  nombre?: string;
  dni?: string;
  empresa?: boolean;
}

export class Cliente implements ICliente {
  constructor(public id?: number, public nombre?: string, public dni?: string, public empresa?: boolean) {
    this.empresa = this.empresa || false;
  }
}
