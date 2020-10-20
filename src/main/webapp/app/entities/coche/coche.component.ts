import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICoche } from 'app/shared/model/coche.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TIPO } from 'app/shared/constants/pagination.constants';

import { CocheService } from './coche.service';
import { CocheDeleteDialogComponent } from './coche-delete-dialog.component';

@Component({
  selector: 'jhi-coche',
  templateUrl: './coche.component.html'
})
export class CocheComponent implements OnInit, OnDestroy {
  coches?: ICoche[];
  todos?: ICoche[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  tipo = TIPO;
  venta: Boolean = true;
  swElec: any;
  swTodos: any;
  swTermic: any;
  color: any;

  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected cocheService: CocheService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  public hybrid(tipo: string): void {
    this.cocheService
      .electricos(
        {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        },
        tipo
      )
      .subscribe(
        (res: HttpResponse<ICoche[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );
  }

  public all(): void {
    this.loadPage();
  }

  colorFilter(colorFiltro: string): void {
    this.color = colorFiltro;
    this.cocheService
      .colores({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        color: colorFiltro
      })
      .subscribe(
        (res: HttpResponse<ICoche[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );
  }

  disponibles(): void {
    this.cocheService
      .vendidos(
        {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        },
        false
      )
      .subscribe(
        (res: HttpResponse<ICoche[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );
  }

  vendidos(): void {
    this.cocheService
      .vendidos(
        {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: this.sort()
        },
        true
      )
      .subscribe(
        (res: HttpResponse<ICoche[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );
  }

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    this.cocheService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ICoche[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
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
    this.registerChangeInCoches();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICoche): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCoches(): void {
    this.eventSubscriber = this.eventManager.subscribe('cocheListModification', () => this.loadPage());
  }

  delete(coche: ICoche): void {
    const modalRef = this.modalService.open(CocheDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.coche = coche;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: ICoche[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/coche'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        color: this.color
      }
    });
    this.coches = data || [];
    this.todos = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
