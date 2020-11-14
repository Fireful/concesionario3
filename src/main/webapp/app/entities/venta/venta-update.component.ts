import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, RequiredValidator, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVenta, Venta } from 'app/shared/model/venta.model';
import { VentaService } from './venta.service';
import { ICoche } from 'app/shared/model/coche.model';
import { CocheService } from 'app/entities/coche/coche.service';
import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from 'app/entities/cliente/cliente.service';
import { IVendedor } from 'app/shared/model/vendedor.model';
import { VendedorService } from 'app/entities/vendedor/vendedor.service';
import { MetodoPago } from 'app/entities/venta/metodo-pago.enum';
import { EstadoVenta } from './estado-venta.enum';
import { IMoto } from 'app/shared/model/moto.model';
import { MotoService } from '../moto/moto.service';
import { Tipo } from '../tipo.enum';

type SelectableEntity = ICoche | ICliente | IVendedor | IVenta;

@Component({ selector: 'jhi-venta-update', templateUrl: './venta-update.component.html' })
export class VentaUpdateComponent implements OnInit {
  isSaving = false;
  ventas: IVenta[] = [];
  venta?: IVenta;
  coches: ICoche[] = [];
  motos: IMoto[] = [];
  clientes: ICliente[] = [];
  vendedors: IVendedor[] = [];
  metodoPago: MetodoPago[] = [];
  predicate!: string;
  ascending!: boolean;
  estadoVenta: EstadoVenta[] = [];
  botonTerminar = true;

  metodo = Object.entries(MetodoPago).map(([key, value]) => ({ number: key, word: value }));
  estado = Object.entries(EstadoVenta).map(([key, value]) => ({ number: key, word: value }));
  tipo = Object.entries(Tipo).map(([key, value]) => ({ number: key, word: value }));

  dataAux = '';
  seleccion = 'Importe Total';
  editForm = this.fb.group({
    id: [],
    fecha: [],
    importeTotal: [],
    coche: [],
    moto: [],
    tipo: [],
    cliente: [],
    vendedor: [],
    metodoPago: [],
    numeroVenta: [],
    estadoVenta: []
  });
  cocheSeleccionado: any;
  tipoSeleccionado?: string;
  seleccionado: any;
  numero: any;
  vehiculoSelec: any;
  vehiculoSeleccionado = 'coche';

  constructor(
    protected ventaService: VentaService,
    protected router: Router,
    protected cocheService: CocheService,
    protected motoService: MotoService,
    protected clienteService: ClienteService,
    protected vendedorService: VendedorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  vehiculo(valor: string): void {
    this.vehiculoSeleccionado = valor;
    alert(valor);
    this.editForm.patchValue({
      tipo: valor
    });
  }

  cambioTipo(): void {
    alert(this.tipoSeleccionado);
  }

  cambioCoche(): void {
    this.seleccionado = this.cocheSeleccionado.precio;
    this.editForm.patchValue({
      importeTotal: this.seleccionado
    });
    /*  this.ventaService.subscribe(data =>{
        this.dataAux = data

      }); */
  }

  ngOnInit(): void {
    this.vehiculoSeleccionado = 'coche';
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.venta = venta;
      if (!venta.id) {
        const today = moment();
        venta.fecha = today;
      }

      if (venta.tipo === 'moto') {
        this.vehiculoSelec = 'moto';
        alert('tipo: ' + this.vehiculoSelec);
      }
      if (venta.tipo === 'coche') {
        this.vehiculoSelec = 'coche';
        alert('tipo: ' + this.vehiculoSelec);
      }
      if (!venta.numeroVenta) {
        this.botonTerminar = false;
        if (venta.id) {
          const today = moment().startOf('day');
          venta.numeroVenta = '00' + venta.id + today.year();
        } else {
          this.ventaService.getNumeroVenta().subscribe(data => {
            this.dataAux = data.toString();
            this.editForm.patchValue({
              numeroVenta: this.dataAux
            });
          });
        }
      } else {
        this.botonTerminar = true;
      }

      this.updateForm(venta);

      this.motoService
        .disponibles({ venta: false })
        .pipe(
          map((res: HttpResponse<IMoto[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IMoto[]) => {
          if (!venta.moto || !venta.moto.id) {
            this.motos = resBody;
          } else {
            this.motoService
              .find(venta.moto.id)
              .pipe(
                map((subRes: HttpResponse<ICoche>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICoche[]) => (this.motos = concatRes));
          }
        });

      this.cocheService
        .disponibles({ venta: false })
        .pipe(
          map((res: HttpResponse<ICoche[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICoche[]) => {
          if (!venta.coche || !venta.coche.id) {
            this.coches = resBody;
          } else {
            this.cocheService
              .find(venta.coche.id)
              .pipe(
                map((subRes: HttpResponse<ICoche>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICoche[]) => (this.coches = concatRes));
          }
        });

      this.clienteService.query().subscribe((res: HttpResponse<ICliente[]>) => (this.clientes = res.body || []));

      this.vendedorService.query().subscribe((res: HttpResponse<IVendedor[]>) => (this.vendedors = res.body || []));
    });

    if (null === this.editForm.get('numeroVenta')!.value) {
      this.ventaService.getNumeroVenta().subscribe(data => {
        this.dataAux = data.toString();
        this.editForm.patchValue({
          numeroVenta: this.dataAux
        });
      });
    }
  }

  updateForm(venta: IVenta): void {
    this.editForm.patchValue({
      id: venta.id,
      fecha: venta.fecha ? venta.fecha.format(DATE_TIME_FORMAT) : null,
      importeTotal: venta.importeTotal,
      coche: venta.id,
      moto: venta.id,
      tipo: venta.tipo,
      vehiculoSeleccionado: venta.moto,
      cliente: venta.cliente,
      vendedor: venta.vendedor,
      metodoPago: venta.metodoPago,
      numeroVenta: venta.numeroVenta,
      estadoVenta: venta.estadoVenta
    });
  }

  previousState(): void {
    window.history.back();
  }

  trackId(index: number, item: IVenta): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  save(): void {
    this.isSaving = true;
    const venta = this.createFromForm();
    if (venta.id !== undefined) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }
  terminaVenta(): void {
    const venta = this.createFromForm();
    if (venta.id) {
      this.subscribeToSaveResponse(this.ventaService.finishVenta(venta.id));
    }
  }

  private createFromForm(): IVenta {
    return {
      ...new Venta(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? moment(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      importeTotal: this.editForm.get(['importeTotal'])!.value,
      coche: this.editForm.get(['coche'])!.value,
      moto: this.editForm.get(['moto'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
      vendedor: this.editForm.get(['vendedor'])!.value,
      metodoPago: this.editForm.get(['metodoPago'])!.value,
      numeroVenta: this.editForm.get(['numeroVenta'])!.value,
      estadoVenta: this.editForm.get(['estadoVenta'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getNumVenta(vehiculoSeleccionado: string): void {
    this.ventaService.getNumeroVenta();
  }
}
