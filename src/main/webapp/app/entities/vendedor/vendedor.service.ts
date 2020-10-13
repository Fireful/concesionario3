import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVendedor } from 'app/shared/model/vendedor.model';

type EntityResponseType = HttpResponse<IVendedor>;
type EntityArrayResponseType = HttpResponse<IVendedor[]>;

@Injectable({ providedIn: 'root' })
export class VendedorService {
  public resourceUrl = SERVER_API_URL + 'api/vendedors';

  constructor(protected http: HttpClient) {}

  create(vendedor: IVendedor): Observable<EntityResponseType> {
    return this.http.post<IVendedor>(this.resourceUrl, vendedor, { observe: 'response' });
  }

  update(vendedor: IVendedor): Observable<EntityResponseType> {
    return this.http.put<IVendedor>(this.resourceUrl, vendedor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVendedor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVendedor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
