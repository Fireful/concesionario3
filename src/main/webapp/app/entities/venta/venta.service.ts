import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVenta } from 'app/shared/model/venta.model';

type EntityResponseType = HttpResponse<IVenta>;
type EntityArrayResponseType = HttpResponse<IVenta[]>;

@Injectable({ providedIn: 'root' })
export class VentaService {
  public resourceUrl = SERVER_API_URL + 'api/ventas';

  constructor(protected http: HttpClient) {}

  create(venta: IVenta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(venta);
    return this.http
      .post<IVenta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(venta: IVenta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(venta);
    return this.http
      .put<IVenta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVenta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVenta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(venta: IVenta): IVenta {
    const copy: IVenta = Object.assign({}, venta, {
      fecha: venta.fecha && venta.fecha.isValid() ? venta.fecha.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? moment(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((venta: IVenta) => {
        venta.fecha = venta.fecha ? moment(venta.fecha) : undefined;
      });
    }
    return res;
  }

  getNumeroVenta(): Observable<Object> {
    return this.http.get<string>(`${this.resourceUrl}/get-num`, { responseType: 'text' as 'json' });
  }

  finishVenta(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVenta>(`${this.resourceUrl}/terminada/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  terminadas(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVenta[]>(encodeURI(`${this.resourceUrl}/terminadas`), { params: options, observe: 'response' });
  }
}
