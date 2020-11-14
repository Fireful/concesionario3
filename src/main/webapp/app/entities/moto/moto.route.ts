import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMoto, Moto } from 'app/shared/model/moto.model';
import { MotoService } from './moto.service';
import { MotoComponent } from './moto.component';
import { MotoVendidosComponent } from './moto-vendidos.component';
import { MotoDetailComponent } from './moto-detail.component';
import { MotoUpdateComponent } from './moto-update.component';

@Injectable({ providedIn: 'root' })
export class MotoResolve implements Resolve<IMoto> {
  constructor(private service: MotoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMoto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((moto: HttpResponse<Moto>) => {
          if (moto.body) {
            return of(moto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Moto());
  }
}

export const motoRoute: Routes = [
  {
    path: '',
    component: MotoComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'concesionario3App.moto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'vendidos',
    component: MotoVendidosComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'concesionario3App.moto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MotoDetailComponent,
    resolve: {
      moto: MotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionario3App.moto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MotoUpdateComponent,
    resolve: {
      moto: MotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionario3App.moto.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MotoUpdateComponent,
    resolve: {
      moto: MotoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'concesionario3App.moto.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
