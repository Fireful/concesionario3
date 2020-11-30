import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVenta } from 'app/shared/model/venta.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VentaService } from './venta.service';
import { VentaDeleteDialogComponent } from './venta-delete-dialog.component';

@Component({
  selector: 'jhi-venta',
  templateUrl: './venta.component.html'
})
export class VentaComponent implements OnInit, OnDestroy {
  ventas?: IVenta[];
  terminadas?: IVenta[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  booleanoEditar = true;
  numero = '0';

  constructor(
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.ventaService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IVenta[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  terminadasPDF(): void {
    this.ventaService.download();
  }

  /* terminadasPDF(): void {
    this.ventaService
      .terminadas({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IVenta[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );
    alert('Informe generado');
  } */

  ngOnInit(): void {
    /* this.activatedRoute.data.subscribe(({ venta }) => {

      if (!venta.numeroVenta) {
        this.booleanoEditar = false;
      } else {
        this.booleanoEditar = true;
      }
    }); */

    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInVentas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVenta): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVentas(): void {
    this.eventSubscriber = this.eventManager.subscribe('ventaListModification', () => this.loadPage());
  }

  delete(venta: IVenta): void {
    const modalRef = this.modalService.open(VentaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.venta = venta;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IVenta[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/venta'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.ventas = data || [];
    this.terminadas = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
