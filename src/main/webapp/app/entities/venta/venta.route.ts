import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVenta, Venta } from 'app/shared/model/venta.model';
import { VentaService } from './venta.service';
import { VentaComponent } from './venta.component';
import { VentaDetailComponent } from './venta-detail.component';
import { VentaUpdateComponent } from './venta-update.component';

@Injectable({ providedIn: 'root' })
export class VentaResolve implements Resolve<IVenta> {
  constructor(private service: VentaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVenta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((venta: HttpResponse<Venta>) => {
          if (venta.body) {
            return of(venta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Venta());
  }
}

export const ventaRoute: Routes = [
  {
    path: '',
    component: VentaComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'concesionario3App.venta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: VentaDetailComponent,
    resolve: {
      venta: VentaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionario3App.venta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: VentaUpdateComponent,
    resolve: {
      venta: VentaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionario3App.venta.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: VentaUpdateComponent,
    resolve: {
      venta: VentaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionario3App.venta.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
