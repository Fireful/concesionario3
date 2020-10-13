import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IVendedor, Vendedor } from 'app/shared/model/vendedor.model';
import { VendedorService } from './vendedor.service';

@Component({
  selector: 'jhi-vendedor-update',
  templateUrl: './vendedor-update.component.html'
})
export class VendedorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    dni: [],
    totalVentas: []
  });

  constructor(protected vendedorService: VendedorService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendedor }) => {
      this.updateForm(vendedor);
    });
  }

  updateForm(vendedor: IVendedor): void {
    this.editForm.patchValue({
      id: vendedor.id,
      nombre: vendedor.nombre,
      dni: vendedor.dni,
      totalVentas: vendedor.totalVentas
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vendedor = this.createFromForm();
    if (vendedor.id !== undefined) {
      this.subscribeToSaveResponse(this.vendedorService.update(vendedor));
    } else {
      this.subscribeToSaveResponse(this.vendedorService.create(vendedor));
    }
  }

  private createFromForm(): IVendedor {
    return {
      ...new Vendedor(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      totalVentas: this.editForm.get(['totalVentas'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendedor>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
