import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ICliente } from 'app/shared/model/cliente.model';
import { ICoche } from 'app/shared/model/coche.model';
import { IMoto } from 'app/shared/model/moto.model';
import { IVendedor } from 'app/shared/model/vendedor.model';
import { IVenta } from 'app/shared/model/venta.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { VendedorService } from '../vendedor/vendedor.service';
import { VentaService } from '../venta/venta.service';

type SelectableEntity = ICoche | ICliente | IVendedor | IVenta;

@Component({
  selector: 'jhi-informes',
  templateUrl: './informes.component.html'
})
export class InformesComponent implements OnInit, OnDestroy {
  clientes?: ICliente[];
  motos?: IMoto[];
  ventas?: IVenta[];
  venta: IVenta | undefined;
  coches?: ICoche[];
  vendedores: IVendedor[] = [];
  eventSubscriber?: Subscription;
  vendedorSeleccionado?: String;
  prueba?: any;
  vendedor?: IVendedor;

  constructor(
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected vendedorService: VendedorService
  ) {}

  terminadasPDF(): void {
    this.ventaService.download();
  }
  vendedoresPDF(valor: number): void {
    // parseInt(valor, 10);

    this.ventaService.pdfVendedores(valor);
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.venta = venta;
      this.vendedorService.query().subscribe((res: HttpResponse<IVendedor[]>) => (this.vendedores = res.body || []));
    });
  }

  trackId(index: number, item: IVendedor): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
