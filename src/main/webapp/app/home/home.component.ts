import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { IVendedor } from 'app/shared/model/vendedor.model';
import { VendedorService } from 'app/entities/vendedor/vendedor.service';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { CocheService } from 'app/entities/coche/coche.service';
import { ICoche } from 'app/shared/model/coche.model';
import { HomeResetVentasComponent } from './home-reset-ventas.component';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  maxVentasHome = '';
  disponibles?: ICoche[];
  vendedores?: IVendedor[] = [];
  account: Account | null = null;
  authSubscription?: Subscription;
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate = 'id';
  ascending!: boolean;
  ngbPaginationPage = 1;
  maxVentas: IVendedor[] = [];
  dataMax?: IVendedor | null;
  dataMaxDinero?: IVendedor | null;

  today: Date = new Date();
  dia?: any;

  constructor(
    protected cocheService: CocheService,
    private accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private loginModalService: LoginModalService,
    private vendedorService: VendedorService,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;
    /* this.maxVentas.forEach(item => {
      this.maxVentas[0]=item;
    }); */

    this.vendedorService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IVendedor[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));

    this.vendedorService.getMax().subscribe(data => {
      this.dataMax = data.body;
    });

    this.vendedorService.getMaxDinero().subscribe(data => {
      this.dataMaxDinero = data.body;
    });

    this.cocheService
      .vendidos(
        {
          page: this.page - 1,
          size: this.itemsPerPage,
          sort: ['id,asc']
        },
        false
      )
      .subscribe(
        (res: HttpResponse<ICoche[]>) => this.onSuccess(res.body, res.headers, this.page),
        () => this.onError()
      );

    this.dia = this.today.getDate();

    if (this.dia === 1) {
      this.modalService.open(HomeResetVentasComponent, { size: 'lg', backdrop: 'static' });
    }
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  registerChangeInVendedors(): void {
    this.eventSubscriber = this.eventManager.subscribe('vendedorListModification', () => this.loadPage());
  }

  login(): void {
    this.loginModalService.open();
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  trackId(index: number, item: ICoche): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  protected onSuccess(data: ICoche[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.vendedores = data || [];
    this.disponibles = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
