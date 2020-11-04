import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { IVendedor } from 'app/shared/model/vendedor.model';
import { VendedorService } from 'app/entities/vendedor/vendedor.service';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  maxVentasHome = '';
  vendedores?: IVendedor[] = [];
  account: Account | null = null;
  authSubscription?: Subscription;
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  maxVentas: IVendedor[] = [];
  dataMax?: IVendedor | null;
  dataMaxDinero?: IVendedor | null;

  today: Date = new Date();
  dia?: any;

  constructor(
    private accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    private loginModalService: LoginModalService,
    private vendedorService: VendedorService
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
    this.dia = this.today.getDate();
    alert(this.dia);
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
    if (this.predicate !== 'nombre') {
      result.push('nombre');
    }
    return result;
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  protected onSuccess(data: IVendedor[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/vendedor'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.vendedores = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
}
