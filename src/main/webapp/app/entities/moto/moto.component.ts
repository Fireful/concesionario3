import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMoto } from 'app/shared/model/moto.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TIPO } from 'app/shared/constants/pagination.constants';

import { MotoService } from './moto.service';
import { MotoDeleteDialogComponent } from './moto-delete-dialog.component';

@Component({
  selector: 'jhi-moto',
  templateUrl: './moto.component.html'
})
export class MotoComponent implements OnInit, OnDestroy {
  motos?: IMoto[];
  todos?: IMoto[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  tipo = TIPO;
  venta: Boolean = true;
  swElec: any;
  swTodos: any;
  swTermic: any;
  color = '';

  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected motoService: MotoService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  public hybrid(tipo: string): void {
    this.motoService
      .electricos(
        {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        },
        tipo
      )
      .subscribe(
        (res: HttpResponse<IMoto[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );
  }

  public all(): void {
    this.loadPage();
  }

  colorFilter(colorFiltro: string): void {
    this.color = colorFiltro;
    this.motoService
      .colores({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        color: colorFiltro
      })
      .subscribe(
        (res: HttpResponse<IMoto[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );
  }

  disponibles(): void {
    this.motoService
      .vendidos(
        {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        },
        false
      )
      .subscribe(
        (res: HttpResponse<IMoto[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );
  }

  vendidos(): void {
    this.motoService
      .vendidos(
        {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        },
        true
      )
      .subscribe(
        (res: HttpResponse<IMoto[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );
  }

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.motoService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IMoto[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  public paginador(valor: number): void {
    this.itemsPerPage = valor;

    this.loadPage();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;

      this.loadPage();
    });
    this.registerChangeInMotos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMoto): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMotos(): void {
    this.eventSubscriber = this.eventManager.subscribe('motoListModification', () => this.loadPage());
  }

  delete(moto: IMoto): void {
    const modalRef = this.modalService.open(MotoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.moto = moto;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IMoto[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/moto'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        color: '#edba32'
      }
    });
    this.motos = data || [];
    this.todos = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
