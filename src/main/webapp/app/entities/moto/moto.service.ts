import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMoto } from 'app/shared/model/moto.model';

type EntityResponseType = HttpResponse<IMoto>;
type EntityArrayResponseType = HttpResponse<IMoto[]>;

@Injectable({ providedIn: 'root' })
export class MotoService {
  public resourceUrl = SERVER_API_URL + 'api/motos';

  constructor(protected http: HttpClient) {}

  create(moto: IMoto): Observable<EntityResponseType> {
    return this.http.post<IMoto>(this.resourceUrl, moto, { observe: 'response' });
  }

  update(moto: IMoto): Observable<EntityResponseType> {
    return this.http.put<IMoto>(this.resourceUrl, moto, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMoto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  vendidos(req?: any, venta?: boolean): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoto[]>(`${this.resourceUrl}/${venta}/vendidos`, { params: options, observe: 'response' });
  }

  disponibles(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoto[]>(`${this.resourceUrl}/${req.venta}/disponibles`, { params: options, observe: 'response' });
  }

  electricos(req?: any, tipo?: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoto[]>(`${this.resourceUrl}/${tipo}/electricos`, { params: options, observe: 'response' });
  }
  colores(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoto[]>(`${this.resourceUrl}/color/${encodeURIComponent(req.color)}`, { params: options, observe: 'response' });
  }
}
